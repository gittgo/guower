package com.ourslook.guower.test;

import com.ourslook.guower.SpringBootMyApplication;
import com.ourslook.guower.entity.SysUserEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootMyApplication.class)
public class TestRedis {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test() throws Exception {
        stringRedisTemplate.opsForValue().set("aaa", "111");
        Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));
    }

    @Test
    public void testObj() throws Exception {
        SysUserEntity user = new SysUserEntity("zhangsan");
        ValueOperations<String, SysUserEntity> operations = redisTemplate.opsForValue();
        operations.set("com", user);
        operations.set("com.ourslook.testuser", user, 100, TimeUnit.SECONDS);
        Thread.sleep(10000);
        //redisTemplate.delete("com.ourslook.testuser");
        boolean exists = redisTemplate.hasKey("com.ourslook.testuser");
        boolean exists2 = redisTemplate.hasKey("com");
        if (exists) {
            System.out.println("exists is true");
        } else {
            System.out.println("exists is false");
        }
        Assert.assertEquals("zhangsan", operations.get("com").getUsername());
    }
}