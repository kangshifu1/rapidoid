package org.rapidoid.http.customize;

/*
 * #%L
 * rapidoid-http-fast
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

import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.commons.MediaType;
import org.rapidoid.http.HttpUtils;
import org.rapidoid.http.Req;
import org.rapidoid.http.Resp;
import org.rapidoid.log.Log;

import java.util.Map;

@Authors("Nikolche Mihajlovski")
@Since("5.1.0")
public class DefaultErrorHandler implements ErrorHandler {

	@Override
	public Object handleError(Req req, Resp resp, Throwable error) {

		if (error instanceof SecurityException) {
			Log.warn("Access denied for request: " + req, "client", req.clientIpAddress());
		} else {
			Log.error("Error occurred when handling request: " + req, error);
		}

		if (resp.contentType() == MediaType.JSON_UTF_8) {
			return HttpUtils.getErrorInfo(resp, error);

		} else if (resp.contentType() == MediaType.PLAIN_TEXT_UTF_8) {
			return HttpUtils.getErrorMessage(resp, error);

		} else {
			return page(resp, error);
		}
	}

	private Object page(Resp resp, Throwable error) {
		if (error instanceof SecurityException) {
			return resp.code(403).view("login").mvc(true);
		} else {
			Map<String, ?> errorInfo = HttpUtils.getErrorInfo(resp, error);
			resp.model().put("error", errorInfo);
			return resp.mvc(true).view("error");
		}
	}

}
