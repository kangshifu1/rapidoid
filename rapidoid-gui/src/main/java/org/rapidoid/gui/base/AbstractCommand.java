package org.rapidoid.gui.base;

/*
 * #%L
 * rapidoid-gui
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
import org.rapidoid.gui.reqinfo.ReqInfo;
import org.rapidoid.u.U;

import java.util.Arrays;

@Authors("Nikolche Mihajlovski")
@Since("4.2.0")
public abstract class AbstractCommand<W extends AbstractCommand<?>> extends AbstractWidget {

	private String command;

	private String[] cmdArgs;

	private Runnable handler;

	private boolean handled;

	@SuppressWarnings("unchecked")
	public W command(String cmd, Object... cmdArgs) {
		this.command = cmd;
		this.cmdArgs = strArgs(cmdArgs);

		return (W) this;
	}

	private String[] strArgs(Object[] args) {
		String[] strs = new String[args.length];

		for (int i = 0; i < args.length; i++) {
			strs[i] = U.str(args[i]).replace("'", "`");
		}

		return strs;
	}

	protected void handleEventIfMatching() {
		if (!handled && handler != null && command != null) {
			if (!ReqInfo.get().isGetReq()) {
				String event = (String) ReqInfo.get().posted().get("_cmd");

				if (!U.isEmpty(event) && U.eq(event, command)) {

					Object[] args = new Object[cmdArgs.length];
					for (int i = 0; i < args.length; i++) {
						args[i] = U.or(ReqInfo.get().posted().get("_" + i), "");
					}

					if (Arrays.equals(args, cmdArgs)) {
						handled = true;
						handler.run();
					}
				}
			}
		}
	}

	public String command() {
		return command;
	}

	public Object[] cmdArgs() {
		return cmdArgs;
	}

	protected void setHandler(Runnable handler) {
		this.handler = handler;
	}

}
