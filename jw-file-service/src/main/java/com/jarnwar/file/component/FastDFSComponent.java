package com.jarnwar.file.component;

import com.jarnwar.file.config.FastDFSConfiguration;
import com.jarnwar.file.fastdfs.FastDFSConnectionPool;

public class FastDFSComponent implements Component<FastDFSConfiguration, FastDFSConnectionPool> {

	@Override
	public FastDFSConnectionPool getInstance(FastDFSConfiguration config) {
		return new FastDFSConnectionPool(config);
	}
	
}
