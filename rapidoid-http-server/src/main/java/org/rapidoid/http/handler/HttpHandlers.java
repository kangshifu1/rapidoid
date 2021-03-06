package org.rapidoid.http.handler;

/*
 * #%L
 * rapidoid-http-server
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
import org.rapidoid.cls.Cls;
import org.rapidoid.commons.Err;
import org.rapidoid.http.*;
import org.rapidoid.http.handler.lambda.*;
import org.rapidoid.http.handler.optimized.DelegatingFastParamsAwareReqHandler;
import org.rapidoid.http.handler.optimized.DelegatingFastParamsAwareReqRespHandler;
import org.rapidoid.http.handler.optimized.DelegatingFastParamsAwareRespHandler;
import org.rapidoid.http.handler.optimized.FastCallableHttpHandler;
import org.rapidoid.lambda.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

@Authors("Nikolche Mihajlovski")
@Since("5.1.0")
public class HttpHandlers {

	public static FastHttpHandler from(FastHttp http, NParamLambda handler, RouteOptions options) {
		if (handler instanceof ReqHandler) {
			return new DelegatingFastParamsAwareReqHandler(http, options, (ReqHandler) handler);

		} else if (handler instanceof ReqRespHandler) {
			return new DelegatingFastParamsAwareReqRespHandler(http, options, (ReqRespHandler) handler);

		} else if (handler instanceof OneParamLambda) {

			OneParamLambda lambda = (OneParamLambda) handler;
			Method method = Cls.getLambdaMethod(lambda);
			Class<?> paramType = method.getParameterTypes()[0];

			if (paramType.equals(Req.class)) {
				return new DelegatingFastParamsAwareReqHandler(http, options, lambda);
			} else if (paramType.equals(Resp.class)) {
				return new DelegatingFastParamsAwareRespHandler(http, options, lambda);
			} else {
				return new OneParamLambdaHandler(http, options, lambda);
			}

		} else if (handler instanceof TwoParamLambda) {

			TwoParamLambda lambda = (TwoParamLambda) handler;
			Method method = Cls.getLambdaMethod(lambda);
			Class<?> param1Type = method.getParameterTypes()[0];
			Class<?> param2Type = method.getParameterTypes()[1];

			if (param1Type.equals(Req.class) && param2Type.equals(Resp.class)) {
				return new DelegatingFastParamsAwareReqRespHandler(http, options, lambda);
			} else {
				return new TwoParamLambdaHandler(http, options, (TwoParamLambda) handler);
			}

		} else if (handler instanceof ThreeParamLambda) {
			return new ThreeParamLambdaHandler(http, options, (ThreeParamLambda) handler);

		} else if (handler instanceof FourParamLambda) {
			return new FourParamLambdaHandler(http, options, (FourParamLambda) handler);

		} else if (handler instanceof FiveParamLambda) {
			return new FiveParamLambdaHandler(http, options, (FiveParamLambda) handler);

		} else if (handler instanceof SixParamLambda) {
			return new SixParamLambdaHandler(http, options, (SixParamLambda) handler);

		} else if (handler instanceof SevenParamLambda) {
			return new SevenParamLambdaHandler(http, options, (SevenParamLambda) handler);

		} else {
			throw Err.notExpected();
		}
	}

	public static void register(FastHttp http, String verb, String path, RouteOptions options, byte[] response) {
		http.on(verb, path, new FastStaticHttpHandler(options, response));
	}

	public static void registerPredefined(FastHttp http, String verb, String path, RouteOptions options, Object response) {
		http.on(verb, path, new PredefinedResponseHandler(http, options, response));
	}

	@SuppressWarnings("unchecked")
	public static void register(FastHttp http, String verb, String path, RouteOptions options, Callable<?> handler) {
		http.on(verb, path, new FastCallableHttpHandler(http, options, (Callable<Object>) handler));
	}

	public static void register(FastHttp http, String verb, String path, RouteOptions options, NParamLambda lambda) {
		FastHttpHandler handler = HttpHandlers.from(http, lambda, options);
		http.on(verb, path, handler);
	}

	public static void register(FastHttp http, String verb, String path, RouteOptions options, Method method, Object instance) {
		http.on(verb, path, new MethodReqHandler(http, options, method, instance));
	}

}
