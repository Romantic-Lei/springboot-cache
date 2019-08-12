package com.luojia.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.luojia.cache.bean.Employee;
import com.luojia.cache.mapper.EmployeeMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	EmployeeMapper employeeMapper;
	
	@Autowired
	StringRedisTemplate stringRedisTemplate;  // 操作键值对都是字符串的
	
	@Autowired
	RedisTemplate redisTemplate;  // 操作键值对都是对象的
	
	@Autowired
	RedisTemplate<Object, Employee> empredisTemplate;
	
	/**
	 * String（字符串）、List（列表）、set（集合）、Hash（散列）、ZSet（有序集合）
	 * tringRedisTemplate.opsForValue()[String（字符串）]
	 * stringRedisTemplate.opsForList()[List（列表）]
	 * stringRedisTemplate.opsForSet()[set（集合）]
	 * stringRedisTemplate.opsForHash()[Hash（散列）]
	 * stringRedisTemplate.opsForZSet()[ZSet（有序集合）]
	 */
	@Test
	public void test01() {
		// 给redis中保存数据
		//stringRedisTemplate.opsForValue().append("msg", "hello");
		// 从redis中获取某一个key的值
		//String msg = stringRedisTemplate.opsForValue().get("msg");
	}
	
	// 测试保存对象
	@Test
	public void test02() {
		Employee empById = employeeMapper.getEmpById(1);
		// 默认如果保存对象，使用jdk序列化机制，序化后的数据保存到redis中
//		redisTemplate.opsForValue().getAndSet("emp-01", empById);
		// 1.将数据以json的方式保存
		// 自己将对象转为json，RedisTemplate默认的序列化规则，改变默认的序列化规则
		empredisTemplate.opsForValue().getAndSet("emp-01", empById);
	}
	
	
	@Test
	public void contextLoads() {
		
		Employee empById = employeeMapper.getEmpById(1);
		System.out.println(empById);
	}

}
