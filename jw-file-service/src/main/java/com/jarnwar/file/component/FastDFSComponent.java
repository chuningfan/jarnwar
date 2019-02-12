package com.jarnwar.file.component;

import com.jarnwar.file.config.FastDFSConfiguration;
import com.jarnwar.file.context.BaseContext;
import com.jarnwar.file.fastdfs.FastDFSConnectionPool;

public class FastDFSComponent implements Component<FastDFSConfiguration, FastDFSConnectionPool> {

	@Override
	public FastDFSConnectionPool getInstance(FastDFSConfiguration config) {
		return BaseContext.getBean(FastDFSConnectionPool.class) == null ? new FastDFSConnectionPool(config) : BaseContext.getBean(FastDFSConnectionPool.class);
	}
	
}
