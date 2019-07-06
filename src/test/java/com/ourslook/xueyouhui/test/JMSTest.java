package com.ourslook.guower.test;

import com.ourslook.guower.utils.UrlEncode;
import org.junit.Test;

import java.util.Hashtable;
import java.util.Map;

/**
 * 消息组件：
 * JSM: java标准
 * Apache activemq  http://activemq.apache.org 已经过时，类似一个30岁的狗朝不保夕
 * 代表：activemq: 并发上千条； 上百万级别，处理不了；"古老项目使用"
 *
 *
 * 新时代小组组件：
 * AMQP协议：跨平台、速度快,高级消息协议;两大组件：kafka和rabbitmq
 * kafka: 开源消息组件，性能最高的消息组件；发展比较快；速度快，每秒百万级，免费中最快的; 最早linkin开发； 要结合大数据，各种分析工具进行；
 * Apache kafka：   http://kafka.apache.org/
 *
 * Apache activemq 古老的东西，新时代不用了，并发高了用不了
 * alibaba rabbitmq QM: 使用最广泛，速度也挺快；使用的Erlang语言开发，爱立信最早开发
 * Apache kafka: 明日之星，大数据时代产生；linkin最早开发
 *
 *
 * 掌握：
 *
 * 消息队列使用场景：最大好处，同步变成异步，消息进行缓冲；
 * 商品推荐;
 */
public class JMSTest {
    @Test
    public void test() {
        Map<String,Object> map = new Hashtable<>();
        map.put("username", "dazer");
        map.put("age", "11");
        map.put("pack", "fsadfdsfdsafdsfds");
        System.out.println(UrlEncode.getUrlParamsByMap(map, true));
    }
}
