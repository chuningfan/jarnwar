package com.jarnwar.file.service;

import java.io.File;
import java.util.List;

public interface OperationService {
	
	String upload(List<File> files);
	
	List<File> download(List<String> fileIds);
	
	void remove(List<String> fileIds);
	
}
