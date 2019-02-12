package com.jarnwar.file.client;

import java.util.List;

public class FileRequest {

	// 0: upload, 1: download, 2: remove
	private byte operationType;
	
	private List<FileDto> files;

	public byte getOperationType() {
		return operationType;
	}

	public void setOperationType(byte operationType) {
		this.operationType = operationType;
	}

	public List<FileDto> getFiles() {
		return files;
	}

	public void setFiles(List<FileDto> files) {
		this.files = files;
	}
	
}
