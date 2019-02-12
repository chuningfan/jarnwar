package com.jarnwar.file.server.handler;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.jarnwar.file.client.FileDto;
import com.jarnwar.file.client.FileRequest;
import com.jarnwar.file.client.FileResponse;
import com.jarnwar.file.context.annotation.Autowired;
import com.jarnwar.file.service.OperationService;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TcpUploadServerHandler extends SimpleChannelInboundHandler<FileRequest> {

	@Autowired(beanClass = OperationService.class)
	private OperationService service;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FileRequest msg) throws Exception {
		byte type = msg.getOperationType();
		List<FileDto> fileDtos = msg.getFiles();
		FileResponse resp = new FileResponse();
		resp.setOperationType(type);
		switch (type) {
		case 0:
			String fileIdentity = service.upload(convert2Files(fileDtos));
			resp.setState((byte) 0);
			FileDto up = new FileDto();
			up.setFileIdentity(fileIdentity);
			resp.setFiles(Lists.newArrayList(up));
			break;
		case 1:
			List<File> files = service
					.download(fileDtos.stream().map(FileDto::getFileIdentity).collect(Collectors.toList()));
			resp.setState((byte) 0);
			resp.setFiles(convert2FileDtos(files));
			break;
		case 2:
			service.remove(fileDtos.stream().map(FileDto::getFileIdentity).collect(Collectors.toList()));
			resp.setState((byte) 0);
			break;
		}
		ctx.write(resp);
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

	private List<File> convert2Files(List<FileDto> fileDtos) throws IOException {
		List<File> files = Lists.newArrayList();
		for (FileDto dto : fileDtos) {
			files.add(dto.getFile());
		}
		return files;
	}

	private List<FileDto> convert2FileDtos(List<File> files) {
		List<FileDto> fileDtos = Lists.newArrayList();
		for (File f : files) {
			FileDto dto = new FileDto();
			int hashCode = f.hashCode();
			dto.setFile(f);
			dto.setFileIdentity(hashCode + "");
			fileDtos.add(dto);
		}
		return fileDtos;
	}

}
