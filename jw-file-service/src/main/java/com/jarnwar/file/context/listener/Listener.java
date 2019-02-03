package com.jarnwar.file.context.listener;

import java.util.EventListener;
import java.util.Observable;
import java.util.Observer;

public abstract class Listener implements Observer, EventListener {

	@Override
	public void update(Observable o, Object arg) {
		Event event = (Event) arg;
		if (accept(o, event)) perform(o, event);
	}

	protected abstract boolean accept(Observable o, Event event);
	
	protected abstract void perform(Observable o, Event event);
	
}
