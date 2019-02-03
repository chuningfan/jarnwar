package com.jarnwar.file.server;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLException;

import com.jarnwar.file.server.protocol.HttpChannelInitializer;
import com.jarnwar.file.server.protocol.TcpChannelInitializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public enum Protocol {

	HTTP("http") {
		@Override
		public ChannelInitializer<Channel> getChannelInitializer() throws CertificateException, SSLException {
			return new HttpChannelInitializer();
		}
	}, TCP("tcp") {
		@Override
		public ChannelInitializer<Channel> getChannelInitializer() {
			return new TcpChannelInitializer();
		}
	};
	
	private String protocol;

	public abstract ChannelInitializer<Channel> getChannelInitializer() throws Exception;
	
	private Protocol(String protocol) {
		this.protocol = protocol;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
}
