package com.jarnwar.file;

import java.util.Map;
import java.util.Objects;

import com.jarnwar.file.config.FastDFSConfiguration;
import com.jarnwar.file.config.NettyServerConfiguration;
import com.jarnwar.file.config.ZooKeeperConfiguration;
import com.jarnwar.file.context.FastDFSContext;
import com.jarnwar.file.context.NettyServerContext;
import com.jarnwar.file.context.ZooKeeperContext;
import com.jarnwar.file.server.NettyServer;

public class Bootstrap {
	
	private static Map<Class<?>, Object> CONFIGS = null;
	
	public static void main(String[] args) throws Exception {
		if (Objects.isNull(CONFIGS)) {
			CONFIGS = loadConfigs();
		}
		NettyServerContext serverContext = new NettyServerContext((NettyServerConfiguration) CONFIGS.get(NettyServerConfiguration.class));
		serverContext.init();
		ZooKeeperContext zkContext = new ZooKeeperContext((ZooKeeperConfiguration) CONFIGS.get(ZooKeeperConfiguration.class));
		zkContext.init();
		FastDFSContext fastDFSContext = new FastDFSContext((FastDFSConfiguration) CONFIGS.get(FastDFSConfiguration.class));
		fastDFSContext.init();
		
		NettyServer server = serverContext.getBean(NettyServer.class);
		server.start();
		
	}

	private static Map<Class<?>, Object> loadConfigs() {
		
		return null;
	}
	
}
