package com.jarnwar.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.jarnwar.file.config.FastDFSConfiguration;
import com.jarnwar.file.config.NettyServerConfiguration;
import com.jarnwar.file.config.ZooKeeperConfiguration;
import com.jarnwar.file.context.FastDFSContext;
import com.jarnwar.file.context.NettyServerContext;
import com.jarnwar.file.context.ZooKeeperContext;
import com.jarnwar.file.context.annotation.MetaKey;
import com.jarnwar.file.server.NettyServer;

public class Bootstrap {

	private static final Logger LOG = LoggerFactory.getLogger(Bootstrap.class);
	
	private static final String BASE_PATH = System.getProperty("user.dir");

	private static final String SPLITTER = ";";

	private static Map<Class<?>, Object> CONFIGS = null;

	enum Config {
		SERVER("server.properties") {
			@Override
			protected Class<?> getConfigClassMapping() {
				return NettyServerConfiguration.class;
			}
		},
		ZOOKEEPER("zk.properties") {
			@Override
			protected Class<?> getConfigClassMapping() {
				return ZooKeeperConfiguration.class;
			}
		},
		REDIS("reids.properties") {
			@Override
			protected Class<?> getConfigClassMapping() {
				return null;
			}
		},
		FASTDFS("fastdfs.properties") {
			@Override
			protected Class<?> getConfigClassMapping() {
				return FastDFSConfiguration.class;
			}
		};
		private String fileName;

		private Config(String fileName) {
			this.fileName = fileName;
		}

		protected abstract Class<?> getConfigClassMapping();

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
	}

	public static void main(String[] args) throws Exception {
		if (Objects.isNull(CONFIGS)) {
			CONFIGS = loadConfigs();
		}
		NettyServerContext serverContext = new NettyServerContext(
				(NettyServerConfiguration) CONFIGS.get(NettyServerConfiguration.class));
		serverContext.init();
		ZooKeeperContext zkContext = new ZooKeeperContext(
				(ZooKeeperConfiguration) CONFIGS.get(ZooKeeperConfiguration.class));
		zkContext.init();
		FastDFSContext fastDFSContext = new FastDFSContext(
				(FastDFSConfiguration) CONFIGS.get(FastDFSConfiguration.class));
		fastDFSContext.init();

		NettyServer server = serverContext.getBean(NettyServer.class);
		
		String command = "start";
        if (args.length > 0) {
            command = args[args.length - 1];
        }

        if (command.equals("start")) {
            args[args.length - 1] = "start";
    		server.start();
        } else if (command.equals("stop")) {
            args[args.length - 1] = "stop";
            server.stop();
        } else {
        	LOG.warn("Bootstrap: command \"" + command + "\" does not exist.");
        }


	}

	private static Map<Class<?>, Object> loadConfigs()
			throws InstantiationException, IllegalAccessException, FileNotFoundException, IOException {
		String configPath = BASE_PATH + "/config";
		File configDir = new File(configPath);
		Map<Class<?>, Object> returnMap = Maps.newConcurrentMap();
		if (configDir.exists() && configDir.isDirectory()) {
			File[] files = configDir.listFiles();
			if (Objects.nonNull(files) && files.length > 0) {
				String fileName = null;
				Properties prop = null;
				Object instance = null;
				Class<?> configClass = null;
				for (File file : files) {
					if (file.isFile()) {
						fileName = file.getName();
						switch (fileName) {
						case "server.properties":
							configClass = Config.SERVER.getConfigClassMapping();
							instance = configClass.newInstance();
							break;
						case "zk.properties":
							configClass = Config.ZOOKEEPER.getConfigClassMapping();
							break;
						case "redis.properties":
							configClass = Config.REDIS.getConfigClassMapping();
							break;
						case "fastdfs.properties":
							configClass = Config.FASTDFS.getConfigClassMapping();
							break;
						}
						if (Objects.nonNull(configClass)) {
							instance = configClass.newInstance();
							prop = getPropFromFile(file);
							setValuesToBean(instance, prop);
							returnMap.put(configClass, instance);
						}
					}
				}
			} else {
				// throw an exception
			}
		} else {
			// throw an exception
		}
		if (returnMap.isEmpty()) {
			// throw an exception
		}
		return returnMap;
	}

	private static final Properties getPropFromFile(File file) throws FileNotFoundException, IOException {
		try (InputStream in = new FileInputStream(file)) {
			Properties prop = new Properties();
			prop.load(in);
			return prop;
		}
	}

	private static final void setValuesToBean(Object bean, Properties prop)
			throws IllegalArgumentException, IllegalAccessException {
		Class<?> clazz = bean.getClass();
		Field[] fields = clazz.getDeclaredFields();
		String propKey = null;
		for (Field f : fields) {
			propKey = f.getName();
			MetaKey mk = f.getAnnotation(MetaKey.class);
			if (mk != null) {
				String val = mk.value();
				if (Objects.nonNull(val) && !"".equals(val.trim())) {
					propKey = val;
				}
				String valString = prop.getProperty(propKey);
				if (Objects.nonNull(valString)) {
					f.setAccessible(true);
					if (f.getType().isArray()) {
						String[] array = valString.split(SPLITTER);
						f.set(bean, f.getType().cast(array));
					} else {
						f.set(bean, f.getType().cast(valString));
					}
				}
			}
		}
	}

}
