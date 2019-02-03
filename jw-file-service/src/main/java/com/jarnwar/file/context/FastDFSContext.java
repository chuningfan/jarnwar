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

	@Override
	protected Component<FastDFSConfiguration, ?> getComponent() {
		return new FastDFSComponent();
	}

}
