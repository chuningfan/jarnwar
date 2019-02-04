package com.jarnwar.file.config;

import java.util.List;

import com.jarnwar.file.context.listener.Listener;

public abstract class Configuration {
	
	private List<Listener> listeners;

	public List<Listener> getListeners() {
		return listeners;
	}

	public void setListeners(List<Listener> listeners) {
		this.listeners = listeners;
	}
	
}
