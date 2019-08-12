package com.luojia.cache.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.luojia.cache.bean.Employee;
import com.luojia.cache.service.EmployeeService;

@RestController
public class EmployeeController {
	
	@Autowired
	EmployeeService employeeService;
	
	@GetMapping("/emp/{id}")
	public Employee getEmployee(@PathVariable("id") Integer id) {
		return employeeService.getEmp(id);
	}
	
	@GetMapping("/emp")
	public Employee update(Employee employee) {
		Employee emp = employeeService.updateEmp(employee);
		return emp;
	}
	
	@GetMapping("/delemp")
	public String deleteEmp(Integer id) {
		employeeService.deleteEmp(id);
		return "success";
	}
	
	@GetMapping("/emp/lastname/{lastName}")
	public Employee getEmpByLastName(@PathVariable("lastName") String lastName) {
		return employeeService.getEmpByLastName(lastName);
	}

}
