package com.luojia.cache.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.luojia.cache.bean.Employee;
import com.luojia.cache.mapper.EmployeeMapper;

/**
 * 关闭docker之后再重启，我们需要注意：
 * 1.docker的ip是否变化，变了需要在yml或者properties文件中更改ip地址
 * 2.我们在docker中安装了Redis镜像，所以我们关闭在重启不需要在根据镜像启动容器
 * 3.我们只需要在docker中执行 docker ps -a
 * 4.然后根据container id来启动容器   例如： docker start 8823d411ed1b（CONTAINER ID）
 * @author asus
 *
 */
@CacheConfig(cacheNames = "emp"/* , cacheManager = "employeeCacheManager" */)
@Service
public class EmployeeService {

	@Autowired
	EmployeeMapper employeeMapper;
	/**
	 * 将方法的运行结果 写进缓存，以后再要相同的数据，直接从缓存里面取，不用调用方法
	 * CacheManager管理多个Cache组件的，对缓存的真正CRUD操作在Cache组件中，每一个缓存组件有自己唯一一个名字；
	 * 几个属性： 
	 * 	cacheNames/value: 指定缓存的名字
	 *  key: 缓存数据使用的key，可以用它来指定。默认是使用方法参数的值  1-方法的返回值
	 *  		编写SpELl  #id;  参数id的值   等同于  #a0  #p0  #root.args[0]
	 *  keyGenerator：key的生成器；可以自己指定key的生成器的组件id
     *              key/keyGenerator：二选一使用;
     *  cacheManager：指定缓存管理器；或者cacheResolver指定获取解析器
     *
     *  condition：指定符合条件的情况下才缓存；
     *          ,condition = "#id>0"
     *       condition = "#a0>1"：第一个参数的值>1的时候才进行缓存
 	 *
 	 *      unless:否定缓存；当unless指定的条件为true，方法的返回值就不会被缓存；可以获取到结果进行判断
     *           unless = "#result == null"
     *           unless = "#a0==2":如果第一个参数的值是2，结果不缓存；
     *  sync：是否使用异步模式
	 * @param id
	 * @return
	 */
	@Cacheable(cacheNames = "emp")
	public Employee getEmp(Integer id) {
		System.out.println("查询" + id + "员工信息");
		return employeeMapper.getEmpById(id);
	}
	
	/**
	 * @CachePut 即调用方法，又更新缓存；
	   *     修改数据库的某个数据，同时更新缓存
	   *     运行时机：
	   *     1.先调用目标方法
	   *     2.将目标方法的结果缓存起来
	   *     测试步骤
	   *     1.测试1号员工；查到的结果反倒缓存中
	   *     	key： 1 value： lastName： 张三
	   *     2.以后的缓存还是在之前的结果中
	   *     3.更新1号员工【lastName：zhangsan；gender：0】
	   *     	key：传入的employee对象   值：返回的employee对象
	   *     4.查询1号员工？
	   *     	应该是更新后的员工；
	   *     		key = "#employee.id": 使用传入的参数的员工id；
	   *     		key = "result.id": 使用返回后的id
	   *     			@Cacheable的key是不能用#result的
	   *     	为什么是没有更新前的？【1号员工没有在缓存中更新】
	   *     
	 */
	@CachePut(/* value = "emp", */ key = "#result.id")
	public Employee updateEmp(Employee employee) {
		System.out.println("updateEmp:" + employee);
		employeeMapper.updateEmp(employee);
		return employee;
	}
	
	/**
	 * @cacheEvict: 缓存清除
	 * key: 指定消除的数据，不指定的话清除所有的缓存数据
	 * allEntries = true: 指定清除这个缓存中所有的数据
	 * beforeInvocation = false： 缓存的清除是否在方法之前执行
	 * 	默认 false 代表在方法执行之后执行，如果程序出现异常缓存就不会清除
	 * 
	 * beforeInvocation = true：代表清除缓存操作是在方法运行之前执行，无论程序是否异常都会清除
	 * 
	 */
	@CacheEvict(/* value = "emp", */ beforeInvocation = false/*key = "#id"*/)
	public void deleteEmp(Integer id) {
		System.out.println("deleteEmp:" + id);
//		int i = 10/0;   //让程序出现异常
	}
	
	@Caching(
		cacheable = {
					@Cacheable(/* value="emp", */ key = "#lastName")	
		},
		put = {
					@CachePut(/* value="emp", */ key = "#result.id"),
					@CachePut(/* value="emp", */ key = "#result.email")
		}
	)
	public Employee getEmpByLastName(String lastName) {
		return employeeMapper.getEmpByLastName(lastName);
	}
	
}
