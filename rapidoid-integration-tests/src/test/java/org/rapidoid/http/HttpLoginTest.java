package org.rapidoid.http;

/*
 * #%L
 * rapidoid-integration-tests
 * %%
 * Copyright (C) 2014 - 2016 Nikolche Mihajlovski and contributors
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.junit.Test;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.commons.Err;
import org.rapidoid.commons.Rnd;
import org.rapidoid.ctx.Current;
import org.rapidoid.security.Roles;
import org.rapidoid.setup.On;
import org.rapidoid.u.U;

import java.util.List;

@Authors("Nikolche Mihajlovski")
@Since("5.1.0")
public class HttpLoginTest extends HttpTestCommons {

	@Test
	public void testLogin() {
		On.get("/user").json(() -> U.list(Current.username(), Current.roles()));

		On.get("/profile").roles(Roles.LOGGED_IN).json(Current::username);

		On.post("/mylogin").json((Resp resp, String user, String pass) -> {
			boolean success = resp.login(user, pass);
			return U.list(success, Current.username(), Current.roles());
		});

		On.post("/mylogout").json((Resp resp, String user, String pass) -> {
			resp.logout();
			return U.list(Current.username(), Current.roles());
		});

		multiThreaded(200, 1000, this::randomUserLogin);
	}

	private void randomUserLogin() {
		switch (Rnd.rnd(4)) {
			case 0:
				loginFlow("foo", "bar", U.list());
				break;
			case 1:
				loginFlow("abc", "abc", U.list("guest"));
				break;
			case 2:
				loginFlow("chuck", "chuck", U.list("moderator", "restarter"));
				break;
			case 3:
				loginFlow("niko", "easy", U.list("owner", "administrator", "moderator"));
				break;
			default:
				throw Err.notExpected();
		}
	}

	private void loginFlow(String user, String pass, List<String> expectedRoles) {
		HttpClient client = HTTP.keepCookies(true).reuseConnections(true).dontClose();

		List<Object> notLoggedIn = U.list(false, null, U.list());
		List<Object> loggedIn = U.list(true, user, expectedRoles);

		eq(client.get(localhost("/user")).parse(), U.list(null, U.list()));

		verifyAccessDenied(client);

		eq(client.post(localhost("/mylogin?user=a1&pass=b")).parse(), notLoggedIn);
		eq(client.post(localhost("/mylogin?user=a2&pass=b")).parse(), notLoggedIn);

		verifyAccessDenied(client);

		eq(client.post(localhost(U.frmt("/mylogin?user=%s&pass=%s", user, pass))).parse(), loggedIn);

		verifyAnonymousAccessDenied();
		verifyAccessGranted(user, client);

		eq(client.get(localhost("/user")).parse(), U.list(user, expectedRoles));

		verifyAnonymousAccessDenied();
		verifyAccessGranted(user, client);

		eq(client.post(localhost("/mylogin?user=a3&pass=b")).parse(), U.list(false, user, expectedRoles));

		verifyAnonymousAccessDenied();
		verifyAccessGranted(user, client);

		eq(client.get(localhost("/user")).parse(), U.list(user, expectedRoles));
		eq(client.post(localhost("/mylogout")).parse(), U.list(null, U.list()));

		verifyAnonymousAccessDenied();
		verifyLoggedOut(client);

		eq(client.get(localhost("/user")).parse(), U.list(null, U.list()));
		eq(client.get(localhost("/user")).parse(), U.list(null, U.list()));

		verifyLoggedOut(client);

		client.close();
	}

	private void verifyAnonymousAccessDenied() {
		onlyGet("/profile");
	}

	private void verifyAccessGranted(String user, HttpClient client) {
		verify("granted-" + user, fetch(client, "get", "/profile"));
	}

	private void verifyAccessDenied(HttpClient client) {
		verify("denied", fetch(client, "get", "/profile"));
	}

	private void verifyLoggedOut(HttpClient client) {
		verify("logout", fetch(client, "get", "/profile"));
	}

}
