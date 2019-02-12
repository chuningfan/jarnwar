package com.jarnwar.file.client;

import java.io.Serializable;

public class FileRequest implements Serializable {

	private static final long serialVersionUID = -3046549792036100023L;

	// 0: upload, 1: download, 2: remove
	private byte operationType;
	
	private int fileIdentity;
	
	private byte[] fileData;

	public byte getOperationType() {
		return operationType;
	}

	public void setOperationType(byte operationType) {
		this.operationType = operationType;
	}

	public int getFileIdentity() {
		return fileIdentity;
	}

	public void setFileIdentity(int fileIdentity) {
		this.fileIdentity = fileIdentity;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}
	
}
