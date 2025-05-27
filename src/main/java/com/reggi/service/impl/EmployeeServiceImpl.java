package com.reggi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggi.mapper.EmployeeMapper;
import com.reggi.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, com.reggi.entity.Employee> implements EmployeeService {
}
