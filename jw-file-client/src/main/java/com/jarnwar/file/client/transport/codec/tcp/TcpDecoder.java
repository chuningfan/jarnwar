package com.jarnwar.file.client.transport.codec.tcp;

import java.nio.ByteBuffer;
import java.util.List;

import com.jarnwar.file.client.FileRequest;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class TcpDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		ByteBuffer byteBuffer = ByteBuffer.allocate(in.capacity());
		byteBuffer.put(in.array());
		
		byte operationType = byteBuffer.get();
		int fileIdentity = byteBuffer.getInt();
		byte[] dst = new byte[byteBuffer.remaining()];
		ByteBuffer bb = byteBuffer.get(dst, byteBuffer.position(), byteBuffer.capacity());
		
		FileRequest req = new FileRequest();
		req.setFileData(bb.array());
		req.setFileIdentity(fileIdentity);
		req.setOperationType(operationType);
		out.add(req);
	}

}
