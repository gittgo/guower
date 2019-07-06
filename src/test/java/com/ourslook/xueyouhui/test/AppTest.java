package com.ourslook.guower.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
    }

    public void testEqual() {
        Integer  one = 1000000;
        Integer  tow = 1000000;
        int      ttt = 1000000;
        Integer  ss1 = 100;
        Integer  ss2 = 100;
        assertTrue(one.equals(tow));
        assertFalse(one == tow);
        assertTrue(ss1 == ss2);
    }

    public void testThread() throws Exception {
        long start = System.currentTimeMillis();
        System.out.println();
        for (int i = 0; i < 1000000; ++i) {
            int aaa = 1 + 1;
            File file1 = ResourceUtils.getFile("classpath:generator_mysql.properties"); //类路径
        }
        System.out.println(Math.abs((start - System.currentTimeMillis()))/1000 + "秒");
    }

    public void testThreadUser() throws Exception {
        ExecutorService cachedThreadPool = null;
        //cachedThreadPool = Executors.newCachedThreadPool();
        cachedThreadPool = Executors.newFixedThreadPool(100);
        long start = System.currentTimeMillis();
        System.out.println();
        for (int i = 0; i < 1000000; ++i) {
            cachedThreadPool.execute(() -> {
                try {
                    int aaa = 1 + 1;
                    File file1 = ResourceUtils.getFile("classpath:generator_mysql.properties"); //类路径
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
        }
        System.out.println(Math.abs((start - System.currentTimeMillis()))/1000 + "秒");
    }
}
