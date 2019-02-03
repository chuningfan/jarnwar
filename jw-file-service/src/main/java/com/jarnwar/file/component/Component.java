package com.jarnwar.file.component;

public interface Component<Config, T> {
	
	T getClient(Config config);
	
}
