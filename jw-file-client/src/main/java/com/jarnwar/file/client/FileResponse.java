package com.jarnwar.file.client;

public class FileResponse extends FileRequest {
	
	// 0: success, 1: fail
	private byte state = 1;

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}
	
}
