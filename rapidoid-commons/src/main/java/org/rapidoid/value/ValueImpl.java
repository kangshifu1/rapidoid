package org.rapidoid.value;

import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.cls.Cls;
import org.rapidoid.u.U;

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

@Authors("Nikolche Mihajlovski")
@Since("5.1.0")
public class ValueImpl<T> implements Value<T> {

	private final ValueStore<T> store;

	private volatile Class<?> type;

	public ValueImpl(ValueStore<T> store) {
		this.store = store;
	}

	@Override
	public T get() {
		T value = getOrNull();
		U.must(value != null, "The value of %s is mandatory!", desc());
		return value;
	}

	@Override
	public T getOrNull() {
		T value = store.get();
		return type != null ? (T) Cls.convert(value, type) : value;
	}

	@Override
	public <K> K or(K alternative) {
		U.notNull(alternative, "alternative");
		T value = getOrNull();
		return value != null ? (K) Cls.convert(value, alternative.getClass()) : alternative;
	}

	@Override
	public <K> Value<K> to(Class<K> type) {
		U.notNull(type, "type");
		this.type = type;
		return (Value<K>) this; // FIXME use chain of immutable types
	}

	@Override
	public void set(T value) {
		store.set(value);
	}

	@Override
	public String toString() {
		return U.str(getOrNull());
	}

	@Override
	public boolean exists() {
		return getOrNull() != null;
	}

	@Override
	public Value<String> str() {
		return to(String.class);
	}

	@Override
	public Value<Long> num() {
		return to(long.class);
	}

	@Override
	public Value<Boolean> bool() {
		return to(boolean.class);
	}

	private String desc() {
		return U.or(store.desc(), "the variable");
	}

}
