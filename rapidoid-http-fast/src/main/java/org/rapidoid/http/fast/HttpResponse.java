package org.rapidoid.http.fast;

/*
 * #%L
 * rapidoid-http-fast
 * %%
 * Copyright (C) 2014 - 2015 Nikolche Mihajlovski and contributors
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.mime.MediaType;
import org.rapidoid.u.U;

@Authors("Nikolche Mihajlovski")
@Since("5.0.2")
public class HttpResponse {

	private final ReqImpl req;

	private Object content = null;

	private int code = 200;

	private MediaType contentType = MediaType.HTML_UTF_8;

	private final Map<String, String> headers = Collections.synchronizedMap(new HashMap<String, String>());

	private final Map<String, String> cookies = Collections.synchronizedMap(new HashMap<String, String>());

	private String redirect = null;

	public HttpResponse(ReqImpl req) {
		this.req = req;
	}

	public synchronized HttpResponse content(Object content) {
		ensureCanChange();
		this.content = content;
		return this;
	}

	public synchronized Object content() {
		return this.content;
	}

	public synchronized HttpResponse code(int code) {
		ensureCanChange();
		this.code = code;
		return this;
	}

	public synchronized int code() {
		return this.code;
	}

	public Map<String, String> headers() {
		ensureCanChange();
		return this.headers;
	}

	public Map<String, String> cookies() {
		ensureCanChange();
		return this.cookies;
	}

	public synchronized HttpResponse contentType(MediaType contentType) {
		ensureCanChange();
		this.contentType = contentType;
		return this;
	}

	public synchronized MediaType contentType() {
		return this.contentType;
	}

	public synchronized HttpResponse redirect(String redirect) {
		ensureCanChange();
		this.redirect = redirect;
		return this;
	}

	public synchronized String redirect() {
		return this.redirect;
	}

	private void ensureCanChange() {
		U.must(!req.isDone(), "The request was already processed, so the response can't be changed now!");
		U.must(!req.isRendering(), "The response rendering has already started, so the response can't be changed now!");
	}

}
