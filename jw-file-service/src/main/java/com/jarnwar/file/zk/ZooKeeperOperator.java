package com.jarnwar.file.zk;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;

import com.jarnwar.file.config.ZooKeeperConfiguration;

public class ZooKeeperOperator extends AbstractZooKeeper {
	
	public ZooKeeperOperator(ZooKeeperConfiguration config) {
		super(config);
	}
	public void create(String path,byte[] data)throws KeeperException, InterruptedException{
		this.zooKeeper.create(path, data, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
	}
	public List<String> getChild(String path) throws KeeperException, InterruptedException{   
		List<String> list=this.zooKeeper.getChildren(path, true);
		return list;
	}
	
	public byte[] getData(String path) throws KeeperException, InterruptedException {   
        return  this.zooKeeper.getData(path, false,null);   
    }
	
	
}