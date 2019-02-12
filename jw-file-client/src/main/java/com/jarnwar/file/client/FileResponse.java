package com.jarnwar.file.client;

import java.io.Serializable;

public class FileResponse extends FileRequest implements Serializable {
	
	private static final long serialVersionUID = -6417541988280147357L;
	// 0: success, 1: fail
	private byte state = 1;

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}
	
}
