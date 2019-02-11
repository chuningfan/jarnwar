package com.jarnwar.file.config;

import java.util.List;

import com.jarnwar.file.context.listener.Listener;

public abstract class Configuration {
	
	private List<Listener> listeners;
	
	private boolean asyncListening;

	public List<Listener> getListeners() {
		return listeners;
	}

	public void setListeners(List<Listener> listeners) {
		this.listeners = listeners;
	}

	public boolean isAsyncListening() {
		return asyncListening;
	}

	public void setAsyncListening(boolean asyncListening) {
		this.asyncListening = asyncListening;
	}
	
}
