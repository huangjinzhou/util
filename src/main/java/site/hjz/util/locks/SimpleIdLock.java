package site.hjz.util.locks;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 简单的不可重入锁
 *
 * @param <T>
 * @author huangjinzhou
 * @date 2018年9月4日14点53分
 */
public class SimpleIdLock<T> {
    private static final long DEFAULT_LOCK_WAIT_TIME = 100;

    private static final Object FLAG = new Object();

    public SimpleIdLock(int capacity) {
        this.cache = new ConcurrentHashMap<>(capacity);
    }

    private final ConcurrentMap<T, Object> cache;

    public void lock(T id) throws InterruptedException {
        while (cache.putIfAbsent(id, FLAG) != null) {
            Thread.sleep(DEFAULT_LOCK_WAIT_TIME);
        }
    }

    public void unlock(T id) {
        cache.remove(id);
    }
}