package com.gt.sokovia.service;

import redis.clients.jedis.Jedis;

public class RedisTest {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("redis");
        jedis.set("tony","anthony");
        System.out.println(jedis.get("tony"));
        System.out.println(jedis.get("not"));
    }

}
