package com.ourslook.guower.test;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

@SuppressWarnings("all")
public class ThreadTest {
    @Test
    public void test() throws Exception {
        //线程方式1 Runable; 不管哪种线程都要使用Thread.start 方法来启动
        new Thread(new MyThread(), "Runnable").start();

        //线程方式2 Callable
        FutureTask futureTask = new FutureTask<String>(new MyCallable());
        new Thread(futureTask, "Callable").start();
        //该方法是阻塞的，一直等待线程执行完毕才会执行
        System.out.println(futureTask.get());
    }

    private class MyCallable implements Callable<String> {
        @Override
        public String call() throws Exception {
            //for (int i = 0; i < 100; ++i) {
            for (int i = 0; i < 5; ++i) {
                System.out.println(Thread.currentThread().getName() + "-->" + i);
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return "执行完毕";
        }
    }

    private class MyThread implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 100; ++i) {
                System.out.println(Thread.currentThread().getName() + "-->" + i);
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
