package org.rapidoid.ioc;

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

import java.util.List;

@Authors("Nikolche Mihajlovski")
@Since("5.1.0")
public class IoCContextChanges {

	private final List<Object> loadedInstances;

	private final List<Object> removedInstances;

	public IoCContextChanges(List<Object> loadedInstances, List<Object> removedInstances) {
		this.loadedInstances = loadedInstances;
		this.removedInstances = removedInstances;
	}

	public List<Object> getLoadedInstances() {
		return loadedInstances;
	}

	public List<Object> getRemovedInstances() {
		return removedInstances;
	}

}
