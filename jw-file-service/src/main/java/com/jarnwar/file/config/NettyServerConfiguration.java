package com.jarnwar.file.config;

import com.jarnwar.file.server.Protocol;

public class NettyServerConfiguration extends Configuration {
	
private boolean noDelay = true;
	
	private int bossCount = 1;
	
	private int workerCount = Runtime.getRuntime().availableProcessors();
	
	private int port = 7091;
	
	private boolean keepAlived = true;
	
	private Protocol protocol = Protocol.TCP;
	
	public boolean isNoDelay() {
		return noDelay;
	}

	public void setNoDelay(boolean noDelay) {
		this.noDelay = noDelay;
	}

	public int getBossCount() {
		return bossCount;
	}

	public void setBossCount(int bossCount) {
		this.bossCount = bossCount;
	}

	public int getWorkerCount() {
		return workerCount;
	}

	public void setWorkerCount(int workerCount) {
		this.workerCount = workerCount;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isKeepAlived() {
		return keepAlived;
	}

	public void setKeepAlived(boolean keepAlived) {
		this.keepAlived = keepAlived;
	}

	public Protocol getProtocol() {
		return protocol;
	}

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}
	
}
