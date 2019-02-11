package com.jarnwar.file.context;

import com.jarnwar.file.component.Component;
import com.jarnwar.file.component.ZooKeeperComponent;
import com.jarnwar.file.config.ZooKeeperConfiguration;

public class ZooKeeperContext extends BaseContext<ZooKeeperConfiguration> {

	public ZooKeeperContext(ZooKeeperConfiguration config)
			throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		super(config);
	}

	@Override
	protected void init0() {
	}

	@Override
	public Component<ZooKeeperConfiguration, ?> getComponent() {
		return new ZooKeeperComponent();
	}

}
