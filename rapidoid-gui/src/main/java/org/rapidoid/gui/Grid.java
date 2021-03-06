package org.rapidoid.gui;

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
import org.rapidoid.gui.base.AbstractWidget;
import org.rapidoid.html.Tag;
import org.rapidoid.html.tag.TdTag;
import org.rapidoid.model.Item;
import org.rapidoid.model.Items;
import org.rapidoid.model.Property;
import org.rapidoid.u.U;
import org.rapidoid.var.Var;

import java.util.List;

@Authors("Nikolche Mihajlovski")
@Since("2.0.0")
public class Grid extends AbstractWidget {

	private final Items items;

	private String orderBy = "id";
	private int pageSize = 10;
	private Object[] columns = {};
	private String rowCmd;

	public Grid(Items items, String sortOrder, int pageSize, String... columns) {
		this.items = items;
		this.orderBy = sortOrder;
		this.pageSize = pageSize;
		this.columns = columns;
	}

	@Override
	protected Tag render() {
		final List<Property> props = items.properties(columns);

		int total = items.size();
		int pages = (int) Math.ceil(total / (double) pageSize);

		boolean ordered = !U.isEmpty(orderBy);
		Var<String> order = null;

		Items slice = items;

		String currentOrder = orderBy;

		if (ordered) {
			order = local("_order_" + widgetId(), orderBy);
			currentOrder = order.get();
			slice = slice.orderedBy(currentOrder);
		}

		boolean paging = pageSize > 0;
		Var<Integer> pageNumber = null;

		if (paging) {
			pageNumber = local("_page_" + widgetId(), 1, 1, pages);
			slice = getPage(slice, pageNumber.get());
		}

		Tag header = tableHeader(props, order);
		Tag body = tableBody(props, slice);
		Pager pager = paging ? pager(1, pages, pageNumber) : noPager();

		return fullTable(header, body, pager);
	}

	protected Pager noPager() {
		return null;
	}

	protected Tag fullTable(Tag header, Tag body, Pager pager) {
		return row(table_(thead(header), body), pager);
	}

	protected Items getPage(Items items, Integer pageN) {
		Items pageOrAll;
		int pageFrom = Math.max((pageN - 1) * pageSize, 0);
		int pageTo = Math.min((pageN) * pageSize, items.size());

		pageOrAll = items.range(pageFrom, pageTo);
		return pageOrAll;
	}

	protected Tag tableBody(final List<Property> props, Items pageOrAll) {
		Tag body = tbody();

		for (Item item : pageOrAll) {
			Tag row = itemRow(props, item);
			body = body.append(row);
		}
		return body;
	}

	protected Tag tableHeader(final List<Property> props, Var<String> order) {
		Tag header = tr();

		for (Property prop : props) {
			Tag sortIcon = null;

			Object sort;
			if (order != null) {

				String currentOrder = order.get();

				if (currentOrder.equals(prop.name())) {
					sortIcon = fa("sort-amount-asc");
				}

				if (order != null && currentOrder.equals("-" + prop.name())) {
					sortIcon = fa("sort-amount-desc");
				}

				sort = a_void(prop.caption(), " ", sortIcon).cmd("_sort", order, prop.name());
			} else {
				sort = prop.caption();
			}

			header = header.append(th(sort));
		}
		return header;
	}

	protected Tag itemRow(List<Property> properties, Item item) {
		Tag row = tr();

		for (Property prop : properties) {
			Object value = prop.get(item);
			value = U.or(value, "");
			row = row.append(cell(display(value)));
		}

		if (rowCmd != null) {
			row = row.cmd(rowCmd, item.value());
		} else {
			String js = onClickScript(item);
			row = row.onclick(js);
		}

		row = row.class_("pointer");

		return row;
	}

	protected String onClickScript(Item item) {
		String js = U.frmt("Rapidoid.goAt('%s');", urlFor(item.value()));
		return js;
	}

	protected TdTag cell(Object value) {
		return td(value);
	}

	public String orderBy() {
		return orderBy;
	}

	public int pageSize() {
		return pageSize;
	}

	public Object[] columns() {
		return columns;
	}

	public Grid orderBy(String orderBy) {
		this.orderBy = orderBy;
		return this;
	}

	public Grid pageSize(int pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	public Grid columns(Object... columns) {
		this.columns = columns;
		return this;
	}

	public String rowCmd() {
		return rowCmd;
	}

	public Grid rowCmd(String rowCmd) {
		this.rowCmd = rowCmd;
		return this;
	}

}
