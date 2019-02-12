package com.jarnwar.file.client.transport.codec.tcp;

import com.jarnwar.file.client.FileRequest;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class TcpEncoder extends MessageToByteEncoder<FileRequest> {

	@Override
	protected void encode(ChannelHandlerContext ctx, FileRequest msg, ByteBuf out) throws Exception {
		byte operationType = msg.getOperationType();
		int fileIdentity = msg.getFileIdentity();
		byte[] fileData = msg.getFileData();
		out.writeByte(operationType);
		out.writeInt(fileIdentity);
		out.writeBytes(fileData);
	}

}
