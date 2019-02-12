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
import com.jarnwar.file.context.BaseContext;
import com.jarnwar.file.context.FastDFSContext;
import com.jarnwar.file.context.NettyServerContext;
import com.jarnwar.file.context.ZooKeeperContext;
import com.jarnwar.file.context.annotation.Autowired;
import com.jarnwar.file.context.annotation.MetaKey;
import com.jarnwar.file.server.NettyServer;
import com.jarnwar.file.server.handler.HttpUploadServerHandler;
import com.jarnwar.file.server.handler.TcpUploadServerHandler;
import com.jarnwar.file.service.OperationServiceImpl;

public class Bootstrap {

	private static final Logger LOG = LoggerFactory.getLogger(Bootstrap.class);

	public static final String BASE_PATH = System.getProperty("user.dir");

	private static final String SPLITTER = ";";

	private static Map<Class<?>, Object> CONFIGS = null;

	private static Map<Class<?>, Object> CONTEXT_BEANS = Maps.newConcurrentMap();

	private static final Class<?>[] SERVICES = { OperationServiceImpl.class};
	
	private static final Class<?>[] SERVER_HANDLERS = { HttpUploadServerHandler.class, TcpUploadServerHandler.class};

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
		NettyServerContext serverContext = null;
		if (Objects.isNull(CONTEXT_BEANS.get(NettyServerContext.class))) {
			serverContext = new NettyServerContext(
					(NettyServerConfiguration) CONFIGS.get(NettyServerConfiguration.class));
			CONTEXT_BEANS.put(NettyServerContext.class, serverContext);
		} else {
			serverContext = (NettyServerContext) CONTEXT_BEANS.get(NettyServerContext.class);
		}
		serverContext.init();
		ZooKeeperContext zkContext = null;
		if (Objects.isNull(CONTEXT_BEANS.get(ZooKeeperContext.class))) {
			zkContext = new ZooKeeperContext(
					(ZooKeeperConfiguration) CONFIGS.get(ZooKeeperConfiguration.class));
			CONTEXT_BEANS.put(ZooKeeperContext.class, zkContext);
		} else {
			zkContext = (ZooKeeperContext) CONTEXT_BEANS.get(ZooKeeperContext.class);
		}
		zkContext.init();
		FastDFSContext fastDFSContext = null;
		if (Objects.isNull(CONTEXT_BEANS.get(FastDFSContext.class))) {
			fastDFSContext = new FastDFSContext(
					(FastDFSConfiguration) CONFIGS.get(FastDFSConfiguration.class));
			CONTEXT_BEANS.put(FastDFSContext.class, fastDFSContext);
		} else {
			fastDFSContext = (FastDFSContext) CONTEXT_BEANS.get(FastDFSContext.class);
		}
		fastDFSContext.init();

		// add service to BEANS
		for (Class<?> clazz : SERVICES) {
			try {
				Object serviceInstance = clazz.newInstance();
				autoSetValue(serviceInstance);
				BaseContext.addBean(serviceInstance);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		// add injections to 
		for (Class<?> clazz: SERVER_HANDLERS) {
			Object handlerInstance = clazz.newInstance();
			autoSetValue(handlerInstance);
		}
		
		NettyServer server = BaseContext.getBean(NettyServer.class);

		if (args.length > 0) {
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
	}

	private static void autoSetValue(Object instance) throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = instance.getClass().getDeclaredFields();
		if (Objects.nonNull(fields) && fields.length > 0) {
			for (Field f: fields) {
				Autowired aw = f.getAnnotation(Autowired.class);
				if (Objects.nonNull(aw)) {
					Class<?> injectedClass = aw.beanClass();
					Object bean = BaseContext.getBean(injectedClass);
					f.setAccessible(true);
					f.set(instance, bean);
				}
			}
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
			}
			String valString = prop.getProperty(propKey);
			if (Objects.nonNull(valString)) {
				f.setAccessible(true);
				if (f.getType().isArray()) {
					String[] array = valString.split(SPLITTER);
					f.set(bean, f.getType().cast(array));
				} else {
					String type = f.getType().toString();
					Object val = null;
					if (type.endsWith("int")) {
						val = Integer.valueOf(valString);
					} else if (type.endsWith("long")) {
						val = Long.valueOf(valString);
					} else if (type.endsWith("boolean")) {
						val = Boolean.valueOf(valString);
					}
					f.set(bean, val);
				}
			}

		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getContext(Class<? extends T> clazz) {
		return (T) CONTEXT_BEANS.get(clazz);
	}

}
