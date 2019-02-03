package com.jarnwar.file.fastdfs;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import com.jarnwar.file.config.FastDFSConfiguration;

public class FastDFSConnectionPool {

	private final int size;
	private ArrayBlockingQueue<StorageClient1> idleConnectionPool = null;
	private ConcurrentHashMap<StorageClient1, Object> busyConnectionPool = null;
	private Object obj = new Object();

	public FastDFSConnectionPool(FastDFSConfiguration config) {
		this.size = config.getPoolSize();
		busyConnectionPool = new ConcurrentHashMap<StorageClient1, Object>();
		idleConnectionPool = new ArrayBlockingQueue<StorageClient1>(size);
	}

	public StorageClient1 get(int timeout) {
		StorageClient1 storageClient1 = null;
		try {
			storageClient1 = idleConnectionPool.poll(timeout, TimeUnit.SECONDS);
			if (storageClient1 != null) {
				busyConnectionPool.put(storageClient1, obj);
			}
		} catch (InterruptedException e) {
			storageClient1 = null;
			e.printStackTrace();
		}
		return storageClient1;
	}

	public void recycle(StorageClient1 storageClient1) {
		if (busyConnectionPool.remove(storageClient1) != null) {
			idleConnectionPool.add(storageClient1);
		}
	}

	public void drop(StorageClient1 storageClient1) {
		if (busyConnectionPool.remove(storageClient1) != null) {
			TrackerServer trackerServer = null;
			TrackerClient trackerClient = new TrackerClient();
			try {
				trackerServer = trackerClient.getConnection();
				StorageClient1 newStorageClient1 = new StorageClient1(trackerServer, null);
				idleConnectionPool.add(newStorageClient1);
				System.out.println("------------------------- :connection +1");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (trackerServer != null) {
					try {
						trackerServer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
