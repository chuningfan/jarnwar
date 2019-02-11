package com.jarnwar.file.service;

import java.util.List;

import org.csource.fastdfs.StorageClient1;

import com.jarnwar.file.Bootstrap;
import com.jarnwar.file.component.FastDFSComponent;
import com.jarnwar.file.context.FastDFSContext;

public class OperationServiceImpl implements OperationService {

	private StorageClient1 client;
	
	{
		FastDFSContext cxt = Bootstrap.getContext(FastDFSContext.class);
		FastDFSComponent dfs = (FastDFSComponent) cxt.getComponent();
		client = (StorageClient1) dfs.getClient();
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
