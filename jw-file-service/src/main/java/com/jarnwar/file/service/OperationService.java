package com.jarnwar.file.service;

import java.util.List;

public interface OperationService {
	
	String add(Object...objects);
	
	String update(Object...objects);
	
	List<Object> getList(Object...objects);
	
	Object delete(Object...objects);
	
}
