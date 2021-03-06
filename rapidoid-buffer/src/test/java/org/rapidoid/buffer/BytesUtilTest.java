package org.rapidoid.buffer;

/*
 * #%L
 * rapidoid-buffer
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
import org.rapidoid.bytes.BytesUtil;
import org.rapidoid.data.Range;

@Authors("Nikolche Mihajlovski")
@Since("4.1.0")
public class BytesUtilTest extends BufferTestCommons {

	@Test
	public void testValidURIs() {
		isTrue(isValid("/"));
		isTrue(isValid("/a"));
		isTrue(isValid("/a/"));
		isTrue(isValid("/abcd/"));
		isTrue(isValid("/abc.tar.gz"));
		isTrue(isValid("/abc.js/"));
		isTrue(isValid("/xx%34%64/"));
		isTrue(isValid("/abc-=-=_=+fg/"));
		isTrue(isValid("/-=AaaaA!0!!AZazf=__/_.-=-.=_=.+/fg01.++--2AaaAa34.56789/"));
	}

	@Test
	public void testInalidURIs() {
		isFalse(isValid(""));
		isFalse(isValid("//"));
		isFalse(isValid("/."));
		isFalse(isValid("./"));
		isFalse(isValid(".."));
		isFalse(isValid("f"));
		isFalse(isValid("."));
		isFalse(isValid("/.ff/"));
		isFalse(isValid("/.some-private/"));
		isFalse(isValid("sdfgdfg"));
		isFalse(isValid("/Дфг"));
		isFalse(isValid("/ok/../x"));
		isFalse(isValid("/ok/../../xyz.abc"));
		isFalse(isValid("/ok/./x"));
		isFalse(isValid("/ok/././xyz.abc"));
	}

	private boolean isValid(String uri) {
		Buf buf = buf(uri);
		Range uriRange = Range.fromTo(0, buf.size());
		return BytesUtil.isValidURI(buf.bytes(), uriRange);
	}

}
