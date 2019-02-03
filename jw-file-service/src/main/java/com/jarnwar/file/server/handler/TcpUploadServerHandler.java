package com.jarnwar.file.server.handler;

import com.jarnwar.file.client.FileRequest;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TcpUploadServerHandler extends SimpleChannelInboundHandler<FileRequest> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FileRequest msg) throws Exception {
		
	}

}
