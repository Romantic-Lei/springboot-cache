package com.luojia.cache.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.luojia.cache.bean.Department;
import com.luojia.cache.service.DeptService;

@RestController
public class DeptController {
	
	@Autowired
	DeptService deptService;
	
	/**
	 * 缓存的数据能存入redis
	 * 第二次从缓存中查询就不能反序列化回来
	 * 存在的是dept的json数据，CacheManager默认使用RedisTemplate<Object, Employee>操作redis
	 * @param id
	 * @return
	 */
	@Cacheable(cacheNames = "dept")
	@GetMapping("/dept/{id}")
	public Department getDept(@PathVariable("id") Integer id) {
		Department department = deptService.getDepartmentById(id);
		return department;
		
	}

}
