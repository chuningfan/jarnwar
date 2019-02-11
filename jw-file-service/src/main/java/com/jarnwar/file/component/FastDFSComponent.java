package com.jarnwar.file.component;

import org.csource.fastdfs.StorageClient1;

import com.jarnwar.file.config.FastDFSConfiguration;
import com.jarnwar.file.context.BaseContext;
import com.jarnwar.file.fastdfs.FastDFSConnectionPool;

public class FastDFSComponent implements Component<FastDFSConfiguration, FastDFSConnectionPool> {

	@Override
	public FastDFSConnectionPool getInstance(FastDFSConfiguration config) {
		return new FastDFSConnectionPool(config);
	}

	@Override
	public Object getClient() {
		FastDFSConnectionPool pool = BaseContext.getBean(FastDFSConnectionPool.class);
		StorageClient1 client = pool.get(3000);
		return client;
	}
	
}
