package org.rapidoid.config;

import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.data.YAML;
import org.rapidoid.io.Res;
import org.rapidoid.log.Log;
import org.rapidoid.u.U;

import java.util.List;
import java.util.Map;

@Authors("Nikolche Mihajlovski")
@Since("5.1.0")
public class AutoRefreshingConfig {

	private static final ConfigParser YAML_PARSER = new ConfigParser() {
		@SuppressWarnings("unchecked")
		@Override
		public Map<String, Object> parse(byte[] bytes) {
			return YAML.parse(bytes, Map.class);
		}
	};

	private static final Map<List<String>, Res> tracking = U.map();

	public static synchronized void attach(final Config config, final String yamlFilename) {
		attach(config, yamlFilename, YAML_PARSER);
	}

	public static synchronized void attach(final Config config, final String filename, final ConfigParser parser) {
		Log.debug("Initializing auto-refreshing config", "filename", filename);

		final Res res = Res.from(filename);
		tracking.put(config.keys(), res);

		Runnable reload = new Runnable() {
			@Override
			public void run() {
				Map<String, Object> configData = null;

				byte[] bytes = res.getBytesOrNull();
				if (bytes != null) {
					if (bytes.length > 0) {
						configData = parser.parse(bytes);
					}

					Log.info("Loading configuration file", "filename", filename);
					config.assign(U.safe(configData));
				} else {
					Log.debug("Couldn't find configuration file", "filename", filename);
				}
			}
		};

		reload.run();
		res.onChange("config", reload);

		res.trackChanges();
		res.exists(); // trigger loading
	}

	public static synchronized List<List<String>> untrack() {
		List<List<String>> keys = U.list();

		for (Map.Entry<List<String>, Res> e : tracking.entrySet()) {
			keys.add(e.getKey());
			e.getValue().removeChangeListener("config");
		}

		tracking.clear();
		return keys;
	}

}