package com.jarnwar.file.context;

import java.util.Map;

public interface MetaInfo {
	
	enum Operation {
		DELETE, SET;
	}
	
	Map<String, Object> getMetaData();
	
	void operate(String key, Object value, Operation opType);
	
	
}
