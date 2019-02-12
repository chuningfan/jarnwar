package com.jarnwar.file.service;

import java.io.File;
import java.util.List;

import com.jarnwar.file.context.annotation.Autowired;
import com.jarnwar.file.fastdfs.FastDFSConnectionPool;

public class OperationServiceImpl implements OperationService {

	@Autowired(beanClass = FastDFSConnectionPool.class)
	private FastDFSConnectionPool pool;

	@Override
	public String upload(List<File> files) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<File> download(List<String> fileIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(List<String> fileIds) {
		// TODO Auto-generated method stub

	}

}
