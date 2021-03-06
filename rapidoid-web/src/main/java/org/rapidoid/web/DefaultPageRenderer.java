package org.rapidoid.web;

/*
 * #%L
 * rapidoid-web
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
import org.rapidoid.config.Config;
import org.rapidoid.gui.GUI;
import org.rapidoid.gui.HtmlPage;
import org.rapidoid.gui.menu.PageMenu;
import org.rapidoid.http.Req;
import org.rapidoid.http.Resp;
import org.rapidoid.http.Screen;
import org.rapidoid.http.customize.Customization;
import org.rapidoid.http.customize.PageRenderer;
import org.rapidoid.u.U;
import org.rapidoid.value.Value;

import java.util.Map;
import java.util.regex.Pattern;

@Authors("Nikolche Mihajlovski")
@Since("5.1.0")
public class DefaultPageRenderer implements PageRenderer {

	private static final Pattern FULL_PAGE_PATTERN = Pattern.compile("(?s)^\\s*<(!DOCTYPE\\s+html|html)>");

	private final Customization customization;

	private final Value<String> title;
	private final Value<String> brand;
	private final Value<Boolean> search;
	private final Value<String> cdn;

	public DefaultPageRenderer(Customization customization) {
		this.customization = customization;
		Config config = customization.config();
		title = config.entry("title").str();
		brand = config.entry("brand").str();
		search = config.entry("search").bool();
		cdn = config.entry("cdn").str();
	}

	@Override
	public Object renderPage(Req req, Resp resp, String content) throws Exception {
		U.notNull(content, "page content");

		if (isFullPage(content)) return content;

		Screen screen = resp.screen();
		HtmlPage page = GUI.page(GUI.hardcoded(content));

		if (screen.title() != null) {
			page.title(screen.title());
		} else {
			page.title(title.get());
		}

		if (screen.brand() != null) {
			page.brand(screen.brand());
		} else {
			page.brand(GUI.hardcoded(brand.get()));
		}

		if (screen.search() != null) {
			page.search(screen.search());
		} else {
			page.search(search.get());
		}

		if (screen.cdn() != null) {
			page.cdn(screen.cdn());
		} else {
			String cdnS = cdn.get();
			if (!"auto".equalsIgnoreCase(cdnS)) {
				page.search(Cls.bool(cdnS));
			}
		}

		Map<String, Object> menu = customization.config().sub("menu").toMap();
		page = page.menu(PageMenu.from(menu));

		return page;
	}

	private boolean isFullPage(String content) {
		return FULL_PAGE_PATTERN.matcher(content).find();
	}

}
