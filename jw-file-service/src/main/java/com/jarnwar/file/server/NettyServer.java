package com.jarnwar.file.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.logging.Logger;

import com.jarnwar.file.config.NettyServerConfiguration;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer implements Server {

	private static String LOCAL_IP = null;
	
	static {
		try {
			LOCAL_IP = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			LOCAL_IP = "127.0.0.1";
		}
	}
	private static final int DEFAULT_WORKER_COUNT = Runtime.getRuntime().availableProcessors();

	private NettyServerConfiguration config;
	
	private EventLoopGroup bossGroup;

	private EventLoopGroup workerGroup;
	
	public NettyServer(NettyServerConfiguration config) {
		this.config = config;
	}
	
	@Override
	public void start() throws Exception {
		bossGroup = new NioEventLoopGroup(config.getBossCount());
		workerGroup = new NioEventLoopGroup(
				config.getWorkerCount() == 0 ? DEFAULT_WORKER_COUNT : config.getWorkerCount());
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.option(ChannelOption.TCP_NODELAY, config.isNoDelay())
				.option(ChannelOption.SO_KEEPALIVE, config.isKeepAlived())
				.childHandler(config.getProtocol().getChannelInitializer());
		try {
			Channel ch = serverBootstrap.bind(LOCAL_IP, config.getPort()).sync().channel();
			printFlag();
			ch.closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			stop();
		}
	}

	private void printFlag() {
		String flag = "☆☆☆☆☆ ☆      ☆ ☆☆☆           ☆☆☆\n" +
					  "	 ☆    ☆ ☆    ☆  ☆             ☆\n" +
					  "         ☆    ☆  ☆   ☆   ☆     ☆     ☆\n" +
					  "         ☆    ☆   ☆  ☆    ☆    ☆    ☆\n" +
					  "         ☆    ☆    ☆ ☆     ☆   ☆   ☆\n" + 
					  "         ☆    ☆     ☆☆      ☆  ☆  ☆\n" +
					  "      ☆ ☆    ☆       ☆       ☆   ☆\n";
		Logger.getGlobal().info(flag);
	}

	@Override
	public void stop() {
		if (Objects.nonNull(workerGroup)) {
			workerGroup.shutdownGracefully();
		}
		if (Objects.nonNull(bossGroup)) {
			bossGroup.shutdownGracefully();
		}
	}

}
