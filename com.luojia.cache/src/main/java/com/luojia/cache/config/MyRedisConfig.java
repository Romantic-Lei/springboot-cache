package com.luojia.cache.config;


import java.net.UnknownHostException;
import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.luojia.cache.bean.Department;
import com.luojia.cache.bean.Employee;

@Configuration
public class MyRedisConfig {

    @Bean
    public RedisTemplate<Object, Employee> empRedisTemplate(
            RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        RedisTemplate<Object, Employee> template = new RedisTemplate<Object, Employee>();
        // 使用序列化器
        template.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer<Employee> ser = new Jackson2JsonRedisSerializer<Employee>(Employee.class);
        template.setDefaultSerializer(ser);
        return template;
    }
    @Bean
    public RedisTemplate<Object, Department> deptRedisTemplate(
            RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        RedisTemplate<Object, Department> template = new RedisTemplate<Object, Department>();
        template.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer<Department> ser = new Jackson2JsonRedisSerializer<Department>(Department.class);
        template.setDefaultSerializer(ser);
        return template;
    }



//    //CacheManagerCustomizers可以来定制缓存的一些规则，springboot1.x适用此方法
//    @Primary  //将某个缓存管理器作为默认的
//    @Bean
//    public RedisCacheManager employeeCacheManager(RedisTemplate<Object, Employee> empRedisTemplate){
//        RedisCacheManager cacheManager = new RedisCacheManager(empRedisTemplate);
//        //key多了一个前缀
//
//        //使用前缀，默认会将CacheName作为key的前缀
//        cacheManager.setUsePrefix(true);
//
//        return cacheManager;
//    }
    
    @Primary   //将某个缓存管理器作为默认的
    @Bean
    public RedisCacheManager employeeCacheManager(RedisConnectionFactory redisConnectionFactory) {
    //初始化一个RedisCacheWriter
    RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
    //设置CacheManager的值序列化方式为json序列化
    RedisSerializer<Object> jsonSerializer = new GenericJackson2JsonRedisSerializer();
    RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer);
    RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);

    //设置默认超过时期是1天
    defaultCacheConfig.entryTtl(Duration.ofDays(1));
    //初始化RedisCacheManager
    return new RedisCacheManager(redisCacheWriter, defaultCacheConfig);
    }
    
    @Bean
    public RedisCacheManager deptCacheManager(RedisConnectionFactory redisConnectionFactory) {
    //初始化一个RedisCacheWriter
    RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
    //设置CacheManager的值序列化方式为json序列化
    RedisSerializer<Object> jsonSerializer = new GenericJackson2JsonRedisSerializer();
    RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer);
    RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);

    //设置默认超过时期是1天
    defaultCacheConfig.entryTtl(Duration.ofDays(1));
    //初始化RedisCacheManager
    return new RedisCacheManager(redisCacheWriter, defaultCacheConfig);
    }
    

//    @Bean
//    public RedisCacheManager deptCacheManager(RedisTemplate<Object, Department> deptRedisTemplate){
//        RedisCacheManager cacheManager = new RedisCacheManager(deptRedisTemplate);
//        //key多了一个前缀
//
//        //使用前缀，默认会将CacheName作为key的前缀
//        cacheManager.setUsePrefix(true);
//
//        return cacheManager;
//    }


}
