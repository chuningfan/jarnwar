package com.jarnwar.file.zk;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

import com.jarnwar.file.config.ZooKeeperConfiguration;

public class AbstractZooKeeper implements Watcher {

	private static final int SESSION_TIME = 2000;

	protected ZooKeeper zooKeeper;

	protected CountDownLatch countDownLatch = new CountDownLatch(1);

	private final ZooKeeperConfiguration config;
	
	public AbstractZooKeeper(ZooKeeperConfiguration config) {
		this.config = config;
	}
	
	public void connect(String hosts) throws IOException, InterruptedException {
		zooKeeper = new ZooKeeper(hosts, SESSION_TIME, this);
		countDownLatch.await();
	}

	@Override
	public void process(WatchedEvent event) {
		if (event.getState() == KeeperState.SyncConnected) {
			countDownLatch.countDown();
		} else if (event.getState() == KeeperState.Disconnected) {
			
		}
	}

	public void close() throws InterruptedException {
		zooKeeper.close();
	}

}