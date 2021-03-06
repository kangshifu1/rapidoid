package org.rapidoid.net.impl;

/*
 * #%L
 * rapidoid-net
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
import org.rapidoid.buffer.BufProvider;
import org.rapidoid.data.Data;
import org.rapidoid.data.Range;
import org.rapidoid.util.UTILS;

@Authors("Nikolche Mihajlovski")
@Since("2.0.0")
public class DecodedData implements Data {

	private final BufProvider src;

	private final Range range;

	private String value;

	public DecodedData(BufProvider src, Range range) {
		this.src = src;
		this.range = range;
	}

	@Override
	public synchronized String get() {
		if (value == null) {
			value = !range.isEmpty() ? UTILS.urlDecode(src.buffer().get(range)) : "";
		}

		return value;
	}

	@Override
	public Range range() {
		return range;
	}

	@Override
	public synchronized void reset() {
		value = null;
	}

}
