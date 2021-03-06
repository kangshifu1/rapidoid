package org.rapidoid.setup;

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
import org.rapidoid.commons.Env;
import org.rapidoid.log.Log;
import org.rapidoid.util.UTILS;

@Authors("Nikolche Mihajlovski")
@Since("5.1.0")
public class OnChanges {

	static volatile boolean initialized;
	static volatile boolean ignore;

	private OnChanges() {
	}

	public static synchronized void restart() {
		if (!initialized) {
			initialized = true;
			ignore = false;

			if (Env.dev()) {
				if (UTILS.withWatchModule()) {
					WatchForChanges.activate();
				} else {
					Log.warn("Cannot watch for class changes, the rapidoid-watch module is missing!");
				}
			} else {
				Log.warn("Not running in dev mode, hot class reloading is disabled!");
			}
		}
	}

	public static synchronized void byDefaultRestart() {
		if (!ignore) {
			restart();
		}
	}

	public static synchronized void ignore() {
		ignore = true;
	}

	public static boolean isIgnored() {
		return ignore;
	}

	public static boolean isInitialized() {
		return initialized;
	}

}
