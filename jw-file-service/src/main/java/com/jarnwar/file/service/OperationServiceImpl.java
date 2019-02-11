package com.jarnwar.file.service;

import java.util.List;

import com.jarnwar.file.context.BaseContext;
import com.jarnwar.file.fastdfs.FastDFSConnectionPool;

public class OperationServiceImpl implements OperationService {

	private FastDFSConnectionPool pool;
	
	public OperationServiceImpl() {
		pool = BaseContext.getBean(FastDFSConnectionPool.class);
	}
	
	@Override
	public String add(Object... objects) {
		return null;
	}

	@Override
	public String update(Object... objects) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> getList(Object... objects) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object delete(Object... objects) {
		// TODO Auto-generated method stub
		return null;
	}

}
