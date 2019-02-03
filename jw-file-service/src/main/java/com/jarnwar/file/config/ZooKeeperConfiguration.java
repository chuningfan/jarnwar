package com.jarnwar.file.config;

public class ZooKeeperConfiguration extends Configuration {

	private String url;
	
	private int sessionTimeout;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}
	
}
