package com.jarnwar.file.service;

import java.io.File;

public interface OperationService {
	
	int upload(File file);
	
	File download(int fileIdentity);
	
	void remove(int fileIdentity);
	
}
