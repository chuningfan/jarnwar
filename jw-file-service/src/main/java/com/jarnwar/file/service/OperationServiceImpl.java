package com.jarnwar.file.service;

import java.io.File;

import com.jarnwar.file.context.annotation.Autowired;
import com.jarnwar.file.fastdfs.FastDFSConnectionPool;

public class OperationServiceImpl implements OperationService {

	@Autowired(beanClass = FastDFSConnectionPool.class)
	private FastDFSConnectionPool pool;

	@Override
	public int upload(File file) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public File download(int fileIdentity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(int fileIdentity) {
		// TODO Auto-generated method stub
		
	}
	
}
