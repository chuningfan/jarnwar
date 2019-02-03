package com.jarnwar.file.component;

import com.jarnwar.file.config.NettyServerConfiguration;
import com.jarnwar.file.server.NettyServer;

public class NettyServerComponent implements Component<NettyServerConfiguration, NettyServer> {

	@Override
	public NettyServer getClient(NettyServerConfiguration config) {
		return new NettyServer(config);
	}

}
