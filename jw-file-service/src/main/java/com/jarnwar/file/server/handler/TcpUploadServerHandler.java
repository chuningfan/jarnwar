package com.jarnwar.file.server.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.jarnwar.file.client.FileRequest;
import com.jarnwar.file.client.FileResponse;
import com.jarnwar.file.context.annotation.Autowired;
import com.jarnwar.file.service.OperationService;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.TimeoutException;

public class TcpUploadServerHandler extends SimpleChannelInboundHandler<FileRequest> {

	@Autowired(beanClass = OperationService.class)
	private OperationService service;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FileRequest msg) throws Exception {
		byte type = msg.getOperationType();
		byte[] fileData = msg.getFileData();
		FileResponse resp = new FileResponse();
		resp.setOperationType(type);
		switch (type) {
		case 0:
			int fileIdentity = service.upload(convertToFile(fileData));
			resp.setState((byte) 0);
			resp.setFileIdentity(fileIdentity);
			break;
		case 1:
			File file = service.download(msg.getFileIdentity());
			resp.setState((byte) 0);
			resp.setFileData(convertToByteArray(file));
			break;
		case 2:
			service.remove(msg.getFileIdentity());
			resp.setState((byte) 0);
			break;
		}
		ChannelFuture future = ctx.writeAndFlush(resp, new DefaultChannelPromise(ctx.channel()));
		try {
			future.get(5, TimeUnit.SECONDS);
		} catch(TimeoutException e) {
			future.cancel(true);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
        if (ctx.channel().isActive()) {
            ctx.writeAndFlush("ERR: " +
                    cause.getClass().getSimpleName() + ": " +
                    cause.getMessage() + '\n').addListener(ChannelFutureListener.CLOSE);
        }
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
    	ctx.flush();
    	ctx.close();
	}

	private File convertToFile(byte[] fileData) throws IOException {
		File file = new File("");
		@SuppressWarnings("resource")
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(fileData);
		return file;
	}

	private byte[] convertToByteArray(File file) throws IOException {
		@SuppressWarnings("resource")
		FileInputStream fis = new FileInputStream(file);
		int length = fis.available();
		byte[] b = new byte[length];
		fis.read(b);
		return b;
	}

}
