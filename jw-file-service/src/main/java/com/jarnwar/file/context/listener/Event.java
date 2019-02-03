package com.jarnwar.file.context.listener;

public class Event {
	
	private Object source;
	
	private Object[] args;
	
	private String method;

	public Event(Object source, Object[] args, String method) {
		this.source = source;
		this.args = args;
		this.method = method;
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
}
