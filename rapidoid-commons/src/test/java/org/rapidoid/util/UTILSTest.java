package org.rapidoid.util;

/*
 * #%L
 * rapidoid-commons
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
import org.rapidoid.test.TestCommons;

import java.io.File;
import java.util.concurrent.Callable;

public class UTILSTest extends TestCommons {

	@Test
	public void testExists() {
		isFalse(UTILS.exists(null));

		isFalse(UTILS.exists(new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				return null;
			}
		}));

		isFalse(UTILS.exists(new Callable<Object>() {
			@SuppressWarnings("null")
			@Override
			public Object call() throws Exception {
				String s = null;
				return s.length(); // throws NPE!
			}
		}));

		isTrue(UTILS.exists(new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				String s = "abc";
				return s.length();
			}
		}));
	}

	@Test
	public void testUri() {
		eq(UTILS.uri(""), "/");
		eq(UTILS.uri("", "a"), "/a");
		eq(UTILS.uri("b", ""), "/b");
		eq(UTILS.uri("/", "x"), "/x");
		eq(UTILS.uri("/", "/x"), "/x");
		eq(UTILS.uri("/ab\\", "cd\\"), "/ab/cd");
		eq(UTILS.uri("/ab", "/cd/"), "/ab/cd");
		eq(UTILS.uri("/ab/", "/cd/"), "/ab/cd");
		eq(UTILS.uri("x", "123", "w"), "/x/123/w");
	}

	@Test
	public void testPath() {
		eq(UTILS.path(""), "");
		eq(UTILS.path("", "a"), "a");
		eq(UTILS.path("b", ""), "b");
		eq(UTILS.path("x", "y"), "x" + File.separator + "y");

		String abcd = "/ab" + File.separator + "cd";
		eq(UTILS.path("/ab\\", "cd\\"), abcd);
		eq(UTILS.path("/ab/", "cd"), abcd);
	}

}
