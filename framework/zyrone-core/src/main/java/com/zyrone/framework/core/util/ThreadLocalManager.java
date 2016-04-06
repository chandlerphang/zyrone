package com.zyrone.framework.core.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadLocalManager {
	
	private static final Logger LOG = LoggerFactory.getLogger(ThreadLocalManager.class);
    private static final ThreadLocal<ThreadLocalManager> THREAD_LOCAL_MANAGER = new ThreadLocal<ThreadLocalManager>() {
        @Override
        protected ThreadLocalManager initialValue() {
            return new ThreadLocalManager();
        }
    };

    protected Map<Long, ThreadLocal<?>> threadLocals = new LinkedHashMap<Long, ThreadLocal<?>>();

    public static <T> ThreadLocal<T> createThreadLocal(final Class<T> type) {
        return createThreadLocal(type, true);
    }

    public static <T> ThreadLocal<T> createThreadLocal(final Class<T> type, final boolean createInitialValue) {
        ThreadLocal<T> response = new ThreadLocal<T>() {
            @Override
            protected T initialValue() {
                addThreadLocal(this);
                if (!createInitialValue) {
                    return null;
                }
                try {
                    return type.newInstance();
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void set(T value) {
                super.get();
                super.set(value);
            }
        };
        return response;
    }
    
    public static void addThreadLocal(ThreadLocal<?> threadLocal) {
        Long position;
        synchronized (threadLock) {
            count++;
            position = count;
        }
        THREAD_LOCAL_MANAGER.get().threadLocals.put(position, threadLocal);
    }

    public static void remove() {
        for (Map.Entry<Long, ThreadLocal<?>> entry : THREAD_LOCAL_MANAGER.get().threadLocals.entrySet()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Removing ThreadLocal #" + entry.getKey() + " from request thread.");
            }
            entry.getValue().remove();
        }
        THREAD_LOCAL_MANAGER.get().threadLocals.clear();
        THREAD_LOCAL_MANAGER.remove();
    }

    public static void remove(ThreadLocal<?> threadLocal) {
        Long removePosition = null;
        for (Map.Entry<Long, ThreadLocal<?>> entry : THREAD_LOCAL_MANAGER.get().threadLocals.entrySet()) {
            if (entry.getValue().equals(threadLocal)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Removing ThreadLocal #" + entry.getKey() + " from request thread.");
                }
                entry.getValue().remove();
                removePosition = entry.getKey();
            }
        }
        THREAD_LOCAL_MANAGER.get().threadLocals.remove(removePosition);
    }

    private static Long count = 0L;
    private static final Object threadLock = new Object();

}