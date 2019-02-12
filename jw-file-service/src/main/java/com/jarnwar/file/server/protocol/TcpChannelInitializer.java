package com.jarnwar.file.server.protocol;

import com.jarnwar.file.client.transport.codec.tcp.TcpDecoder;
import com.jarnwar.file.client.transport.codec.tcp.TcpEncoder;
import com.jarnwar.file.server.handler.TcpUploadServerHandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

public class TcpChannelInitializer extends ChannelInitializer<Channel> {

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new TcpDecoder());
		pipeline.addLast(new TcpEncoder());
		pipeline.addLast(new TcpUploadServerHandler());
	}

}
