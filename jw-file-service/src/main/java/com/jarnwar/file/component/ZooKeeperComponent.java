package com.jarnwar.file.component;

import com.jarnwar.file.config.ZooKeeperConfiguration;
import com.jarnwar.file.zk.ZooKeeperOperator;

public class ZooKeeperComponent implements Component<ZooKeeperConfiguration, ZooKeeperOperator> {

	@Override
	public ZooKeeperOperator getInstance(ZooKeeperConfiguration config) {
		return new ZooKeeperOperator(config);
	}


}
