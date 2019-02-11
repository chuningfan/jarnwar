package com.jarnwar.file.component;

public interface Component<Config, T> {
	
	default T getInstance(Config config) {
		return null;
	}
	
}
