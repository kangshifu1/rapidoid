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
import org.rapidoid.annotation.*;
import org.rapidoid.setup.On;
import org.rapidoid.u.U;

import java.util.Date;

@Authors("Nikolche Mihajlovski")
@Since("5.0.12")
public class HttpPojoApiTest extends HttpTestCommons {

	@Test
	public void test1() {
		On.beans(new Object() {
			public String test1() {
				return "not annotated";
			}
		});

		notFound("/");
		notFound("/test1");
	}

	@Test
	public void test2() {
		On.beans(new Object() {
			@GET
			public Object test2() {
				return 123.456;
			}
		});

		notFound("/");
		onlyGet("/test2");
	}

	@Test
	public void test3() {
		On.beans(new Object() {
			@POST
			public Object test3() {
				return "ABC DE";
			}
		});

		notFound("/");
		onlyPost("/test3");
	}

	@Test
	public void test4() {
		On.beans(new Object() {
			@PUT
			public int test4() {
				return 12345;
			}
		});

		notFound("/");
		onlyPut("/test4");
	}

	@Test
	public void test5() {
		On.beans(new Object() {
			@DELETE
			public boolean test5() {
				return true;
			}
		});

		notFound("/");
		onlyDelete("/test5");
	}

	@Test
	public void test6() {
		On.beans(new Object() {
			@GET
			public Object test6(Req req, Resp resp) {
				return U.map("a", 1, "b", 2);
			}
		});

		notFound("/");
		onlyGet("/test6");
	}

	@Test
	public void test7() {
		On.beans(new Object() {
			@POST
			public Object test7(Req req, Resp resp) {
				return U.list("a", 123, true);
			}
		});

		notFound("/");
		onlyPost("/test7");
	}

	@Test
	public void test8() {
		On.beans(new Object() {
			@PUT
			public Object test8(Req req, Resp resp) {
				return U.set("b", 0, false);
			}
		});

		notFound("/");
		onlyPut("/test8");
	}

	@Test
	public void test9() {
		On.beans(new Object() {
			@DELETE
			public Date test9(Req req, Resp resp) {
				return new Date(50505050);
			}
		});

		notFound("/");
		onlyDelete("/test9");
	}

	static class Ctrl1 {
		@GET
		public Object x(Req req, Resp resp) {
			return reqResp(req, resp);
		}

		@POST
		Object y(Req req, Resp resp) {
			return reqResp(req, resp);
		}

		@Page(raw = true)
		Object p(Req req, Resp resp) {
			return reqResp(req, resp);
		}

		@POST
		protected Object z(Req req, Resp resp) {
			return reqResp(req, resp);
		}

		@POST
		private Object w(Req req, Resp resp) {
			return reqResp(req, resp);
		}
	}

	@Test
	public void test10() {
		On.beans(new Ctrl1());

		notFound("/");

		onlyGet("/x");
		onlyPost("/y");
		getAndPost("/p");

		notFound("/z");
		notFound("/w");
	}

	@Test
	public void test11() {
		On.beans(Ctrl1.class);

		notFound("/");

		onlyGet("/x");
		onlyPost("/y");
		getAndPost("/p");

		notFound("/z");
		notFound("/w");
	}

	class Ctrl2 {
		@GET
		public Object x(Req req, Resp resp) {
			return reqResp(req, resp);
		}

		@POST
		Object y(Req req, Resp resp) {
			return reqResp(req, resp);
		}

		@Page(raw = true)
		Object p(Req req, Resp resp) {
			return reqResp(req, resp);
		}

		@POST
		protected Object z(Req req, Resp resp) {
			return reqResp(req, resp);
		}

		@POST
		private Object w(Req req, Resp resp) {
			return reqResp(req, resp);
		}
	}

	@Test
	public void test12() {
		On.beans(new Ctrl2());

		notFound("/");

		onlyGet("/x");
		onlyPost("/y");
		getAndPost("/p");

		notFound("/z");
		notFound("/w");
	}

	@Test
	public void test13() {
		class Ctrl3 {
			@GET
			public Object x(Req req, Resp resp) {
				return reqResp(req, resp);
			}

			@POST
			Object y(Req req, Resp resp) {
				return reqResp(req, resp);
			}

			@Page(raw = true)
			Object p(Req req, Resp resp) {
				return reqResp(req, resp);
			}

			@POST
			protected Object z(Req req, Resp resp) {
				return reqResp(req, resp);
			}

			@POST
			private Object w(Req req, Resp resp) {
				return reqResp(req, resp);
			}
		}

		On.beans(new Ctrl3());

		notFound("/");

		onlyGet("/x");
		onlyPost("/y");
		getAndPost("/p");

		notFound("/z");
		notFound("/w");
	}

}
