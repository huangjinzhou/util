package site.hjz.util.locks;

import org.junit.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class SimpleIdLockTest {

    @Test
    public void test() {
        final SimpleIdLock<String> stringIdLock = new SimpleIdLock<>(4);
        final long start = System.currentTimeMillis();
        final CyclicBarrier barrier = new CyclicBarrier(3, new Runnable() {
            @Override
            public void run() {
                System.out.println("time spend : " + (System.currentTimeMillis() - start));
            }
        });

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    stringIdLock.lock("a");
                    Thread.sleep(100);
                    stringIdLock.lock("b");
                    System.out.println("Thread1 begin");
                    for (int i = 0; i < 10; i++) {
                        System.out.println("Thread 1 running");
                        Thread.sleep(100);
                    }
                    System.out.println("Thread1 end");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stringIdLock.unlock("b");
                stringIdLock.unlock("a");
                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    stringIdLock.lock("b");
                    Thread.sleep(100);
                    stringIdLock.lock("a");
                    System.out.println("Thread2 begin");
                    for (int i = 0; i < 10; i++) {
                        System.out.println("Thread 2 running");
                        Thread.sleep(100);
                    }
                    System.out.println("Thread2 end");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stringIdLock.unlock("a");
                stringIdLock.unlock("b");
                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });

        thread1.start();
        thread2.start();
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}