package com.jarnwar.file.component;

import com.jarnwar.file.config.NettyServerConfiguration;
import com.jarnwar.file.context.BaseContext;
import com.jarnwar.file.server.NettyServer;

public class NettyServerComponent implements Component<NettyServerConfiguration, NettyServer> {

	@Override
	public NettyServer getInstance(NettyServerConfiguration config) {
		return BaseContext.getBean(NettyServer.class) == null ? new NettyServer(config) : BaseContext.getBean(NettyServer.class);
	}

}
