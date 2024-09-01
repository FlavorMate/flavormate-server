package de.flavormate.aa_interfaces.scripts;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AScript {

	private final String name;

	abstract public void run();

	protected void log(String message, Object... params) {
		var msg = name + ": " + message;
		log.info(msg, params);

	}

	protected void warning(String message, Object... params) {
		var msg = name + ": " + message;
		log.warn(msg, params);
	}
}
