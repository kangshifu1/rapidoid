package org.rapidoid.commons;

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

import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.lambda.Mapper;
import org.rapidoid.u.U;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Authors("Nikolche Mihajlovski")
@Since("5.0.4")
public class Str {

	// regex taken from
	// http://stackoverflow.com/questions/2559759/how-do-i-convert-camelcase-into-human-readable-names-in-java
	private static final Pattern CAMEL_SPLITTER_PATTERN = Pattern
			.compile("(?<=[A-Z])(?=[A-Z][a-z])|(?<=[^A-Z])(?=[A-Z])|(?<=[A-Za-z])(?=[^A-Za-z])");

	private Str() {
	}

	public static String camelSplit(String s) {
		return CAMEL_SPLITTER_PATTERN.matcher(s).replaceAll(" ");
	}

	public static String camelPhrase(String s) {
		return capitalized(camelSplit(s).toLowerCase());
	}

	public static String replace(String s, String[][] repls) {
		for (String[] repl : repls) {
			U.must(repl.length == 2, "Expected pairs of [search, replacement] strings!");
			s = s.replaceAll(Pattern.quote(repl[0]), repl[1]);
		}

		return s;
	}

	public static String replace(String s, String regex, Mapper<String[], String> replacer) {
		StringBuffer output = new StringBuffer();
		Pattern p = Pattern.compile(regex);
		Matcher matcher = p.matcher(s);

		while (matcher.find()) {
			int len = matcher.groupCount() + 1;
			String[] groups = new String[len];

			for (int i = 0; i < groups.length; i++) {
				groups[i] = matcher.group(i);
			}

			Object value;
			try {
				value = replacer.map(groups);
			} catch (Exception e) {
				throw U.rte(e);
			}

			matcher.appendReplacement(output, U.str(value));
		}

		matcher.appendTail(output);
		return output.toString();
	}

	public static String render(Object[] items, String itemFormat, String sep) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < items.length; i++) {
			if (i > 0) {
				sb.append(sep);
			}
			sb.append(U.frmt(itemFormat, items[i]));
		}

		return sb.toString();
	}

	public static String render(Iterable<?> items, String itemFormat, String sep) {
		StringBuilder sb = new StringBuilder();

		int i = 0;
		Iterator<?> it = items.iterator();
		while (it.hasNext()) {
			Object item = it.next();
			if (i > 0) {
				sb.append(sep);
			}

			sb.append(U.frmt(itemFormat, item));
			i++;
		}

		return sb.toString();
	}

	public static String capitalized(String s) {
		return s.isEmpty() ? s : s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	public static String uncapitalized(String s) {
		return s.isEmpty() ? s : s.substring(0, 1).toLowerCase() + s.substring(1);
	}

	public static String mul(String s, int n) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < n; i++) {
			sb.append(s);
		}

		return sb.toString();
	}

	public static String sub(String s, int beginIndex, int endIndex) {
		if (endIndex < 0) {
			endIndex = s.length() + endIndex;
		}
		return s.substring(beginIndex, endIndex);
	}

	public static String trimr(String s, char suffix) {
		return (!s.isEmpty() && s.charAt(s.length() - 1) == suffix) ? sub(s, 0, -1) : s;
	}

	public static String trimr(String s, String suffix) {
		return s.endsWith(suffix) ? sub(s, 0, -suffix.length()) : s;
	}

	public static String triml(String s, char prefix) {
		return (!s.isEmpty() && s.charAt(0) == prefix) ? s.substring(1) : s;
	}

	public static String triml(String s, String prefix) {
		return s.startsWith(prefix) ? s.substring(prefix.length()) : s;
	}

	public static String insert(String target, int atIndex, String insertion) {
		return target.substring(0, atIndex) + insertion + target.substring(atIndex);
	}

	public static String cutToFirst(String s, String delimiter) {
		int pos = s.indexOf(delimiter);
		return pos >= 0 ? s.substring(0, pos) : s;
	}

	public static String cutToLast(String s, String delimiter) {
		int pos = s.lastIndexOf(delimiter);
		return pos >= 0 ? s.substring(0, pos) : s;
	}

	public static String cutFromFirst(String s, String delimiter) {
		int pos = s.indexOf(delimiter);
		return pos >= 0 ? s.substring(pos + delimiter.length()) : s;
	}

	public static String cutFromLast(String s, String delimiter) {
		int pos = s.lastIndexOf(delimiter);
		return pos >= 0 ? s.substring(pos + delimiter.length()) : s;
	}

}
