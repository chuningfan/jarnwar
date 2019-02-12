package com.jarnwar.file.client;

import java.io.File;

public class FileDto {
	
	private String fileIdentity;
	
	private File file;

	public String getFileIdentity() {
		return fileIdentity;
	}

	public void setFileIdentity(String fileIdentity) {
		this.fileIdentity = fileIdentity;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
}
