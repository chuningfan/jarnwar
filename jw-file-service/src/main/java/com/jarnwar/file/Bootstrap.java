package com.jarnwar.file;

import java.util.Map;

import com.google.common.collect.Maps;
import com.jarnwar.file.config.FastDFSConfiguration;
import com.jarnwar.file.config.NettyServerConfiguration;
import com.jarnwar.file.config.ZooKeeperConfiguration;
import com.jarnwar.file.context.FastDFSContext;
import com.jarnwar.file.context.NettyServerContext;
import com.jarnwar.file.context.ZooKeeperContext;
import com.jarnwar.file.server.NettyServer;

public class Bootstrap {
	
	private static final Map<Class<?>, Object> CONFIGS = Maps.newHashMap();
	
	public static void main(String[] args) throws Exception {
		loadConfigs();
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
