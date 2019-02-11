package com.jarnwar.file.context;

import com.jarnwar.file.component.Component;
import com.jarnwar.file.component.FastDFSComponent;
import com.jarnwar.file.config.FastDFSConfiguration;

public class FastDFSContext extends BaseContext<FastDFSConfiguration> {

	public FastDFSContext(FastDFSConfiguration config)
			throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		super(config);
	}

	@Override
	protected void init0() {
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Component<FastDFSConfiguration, ?> getComponent() {
		return (Component<FastDFSConfiguration, ?>) BEANS.get(FastDFSComponent.class);
	}

}
