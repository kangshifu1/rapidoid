package org.rapidoid.db.impl;

/*
 * #%L
 * rapidoid-db-impl
 * %%
 * Copyright (C) 2014 - 2015 Nikolche Mihajlovski
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

import java.util.Map;

import org.rapidoid.beany.SerializableBean;
import org.rapidoid.db.DbColumn;
import org.rapidoid.util.U;

public class DbColumnImpl<E> implements DbColumn<E>, SerializableBean<Object> {

	private static final long serialVersionUID = 5047929644817533060L;

	private final Map<String, Object> map;

	private final String name;

	public DbColumnImpl(Map<String, Object> map, String name) {
		U.notNull(map, "map");
		U.notNull(name, "name");

		this.map = map;
		this.name = name;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E get() {
		return (E) map.get(name);
	}

	@Override
	public void set(E value) {
		if (value != null) {
			map.put(name, value);
		} else {
			map.remove(name);
		}
	}

	@Override
	public Object serializeBean() {
		return get();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deserializeBean(Object serialized) {
		set((E) serialized);
	}

}