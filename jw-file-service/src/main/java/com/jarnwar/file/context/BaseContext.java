package com.jarnwar.file.context;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.google.common.collect.Maps;
import com.jarnwar.file.component.Component;
import com.jarnwar.file.config.Configuration;
import com.jarnwar.file.context.listener.Event;
import com.jarnwar.file.context.listener.Listener;
import com.jarnwar.file.exception.ComponentException;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public abstract class BaseContext<Config extends Configuration> extends Observable implements MetaInfo {

	private static ClassLoader LOADER = null;

	static {
		if (Objects.isNull(LOADER)) {
			LOADER = Thread.currentThread().getContextClassLoader();
		}
		if (Objects.isNull(LOADER)) {
			LOADER = BaseContext.class.getClassLoader();
		}
	}

	protected static final Map<Class<?>, Object> BEANS = Maps.newConcurrentMap();

	private volatile Map<String, Object> metaData = Maps.newHashMap();

	private final ReadWriteLock lock = new ReentrantReadWriteLock();

	private final Lock readLock = lock.readLock();

	private final Lock writeLock = lock.writeLock();

	private final Config config;

	private final ThreadPoolExecutor tpx;

	public BaseContext(Config config) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		this.config = config;
		if (Objects.nonNull(config)) {
			List<Listener> listeners = config.getListeners();
			if (Objects.nonNull(listeners) && !listeners.isEmpty()) {
				this.tpx = new ThreadPoolExecutor(listeners.size() * 3 / 4, listeners.size(), 2000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(listeners.size()));
			} else {
				this.tpx = null;
			}
		} else {
			this.tpx = null;
		}
	}

	public void init() throws IllegalArgumentException, IllegalAccessException, InstantiationException,
			ComponentException, NoSuchFieldException, SecurityException {
		if (Objects.isNull(config)) {
			return;
		}
		init0();
		addListeners();
		Component<Config, ?> comp = getComponent();
		Object client = comp.getClient(config);
		if (Objects.nonNull(client)) {
			Type[] types = comp.getClass().getGenericInterfaces();
			if (Objects.isNull(types) || types.length == 0) {
				throw new ComponentException("Maybe you should provide generic types for " + comp.getClass());
			} else {
				Type parameterizedType = types[0];
				Field f = parameterizedType.getClass().getDeclaredField("actualTypeArguments");
				f.setAccessible(true);
				Type[] genericClassArray = (Type[]) f.get(parameterizedType);
				Class<?> clientClass = (Class<?>) genericClassArray[1];
				if (Objects.nonNull(clientClass) && clientClass.isInterface()) {
					if (Objects.isNull(BEANS.get(clientClass))) {
						synchronized (BEANS) {
							if (Objects.isNull(BEANS.get(clientClass)))
								BEANS.put(clientClass, new JdkProxy(LOADER, client, clientClass).getProxyInstance());
						}
					}
				} else {
					if (Objects.isNull(BEANS.get(clientClass))) {
						synchronized (BEANS) {
							if (Objects.isNull(BEANS.get(clientClass)))
								BEANS.put(clientClass, new CglibProxy(client).getProxyInstance());
						}
					}
				}
			}
		} else {
			throw new ComponentException("When collecting bean, occurred an error!");
		}
	}

	// process configuration data.
	protected abstract void init0();

	protected abstract Component<Config, ?> getComponent();

	private void addListeners() {
		List<Listener> listeners = config.getListeners();
		if (Objects.nonNull(listeners) && !listeners.isEmpty()) {
			for (Listener listener : listeners) {
				addObserver(listener);
			}
		}
	}

	@Override
	public Map<String, Object> getMetaData() {
		try {
			readLock.lock();
			return metaData;
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public void operate(String key, Object value, Operation opType) {
		try {
			writeLock.lock();
			switch (opType) {
			case SET:
				metaData.put(key, value);
				break;
			case DELETE:
				metaData.remove(key);
				break;
			}
		} finally {
			writeLock.unlock();
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(Class<? extends T> clazz) {
		return (T) BEANS.get(clazz);
	}

	public void remove(Class<?> clazz) {
		BEANS.remove(clazz);
	}

	private void effectOnListener(Event event) {
		if (countObservers() > 0 && Objects.nonNull(tpx)) {
			tpx.submit(() -> {
				setChanged();
				notifyObservers(event);
			});
		}
	}

	public Config getConfig() {
		return config;
	}
	
	private final class CglibProxy implements MethodInterceptor {
		private Object target;

		private CglibProxy(Object target) {
			this.target = target;
		}

		private Object getProxyInstance() {
			Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(target.getClass());
			enhancer.setClassLoader(LOADER);
			enhancer.setCallback(this);
			return enhancer.create(new Class<?>[] { config.getClass() }, new Object[] { config });
		}

		@Override
		public Object intercept(Object arg0, Method arg1, Object[] arg2, MethodProxy arg3) throws Throwable {
			Object result = arg1.invoke(target, arg2);
			Event event = new Event(target, arg2, arg1.getName());
			effectOnListener(event);
			return result;
		}
	}

	private final class JdkProxy {
		private Object target;
		private Class<?> interfaceClass;
		private ClassLoader classLoader;

		private JdkProxy(ClassLoader classLoader, Object target, Class<?> interfaceClass) {
			this.classLoader = classLoader;
			if (Objects.isNull(classLoader)) {
				classLoader = getClass().getClassLoader();
			}
			this.target = target;
			this.interfaceClass = interfaceClass;
		}

		private Object getProxyInstance() {
			return Proxy.newProxyInstance(classLoader, new Class<?>[] { interfaceClass }, new InvocationHandler() {
				@Override
				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
					Object result = method.invoke(target, args);
					Event event = new Event(target, args, method.getName());
					effectOnListener(event);
					return result;
				}
			});
		}
	}

}
