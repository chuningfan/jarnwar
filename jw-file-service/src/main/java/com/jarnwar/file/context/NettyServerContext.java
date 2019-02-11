package com.jarnwar.file.context;

import java.util.List;
import java.util.Objects;

import com.jarnwar.file.component.Component;
import com.jarnwar.file.component.NettyServerComponent;
import com.jarnwar.file.config.NettyServerConfiguration;
import com.jarnwar.file.context.listener.Listener;

public class NettyServerContext extends BaseContext<NettyServerConfiguration> {

	public NettyServerContext(NettyServerConfiguration config)
			throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		super(config);
	}

	@Override
	protected void init0() {
		List<Listener> listeners = getConfig().getListeners();
		if (Objects.nonNull(listeners) && !listeners.isEmpty()) {
			for (Listener listener: listeners) {
				addObserver(listener);
			}
		}
	}

	@Override
	public Component<NettyServerConfiguration, ?> getComponent() {
		return new NettyServerComponent();
	}
	
}
