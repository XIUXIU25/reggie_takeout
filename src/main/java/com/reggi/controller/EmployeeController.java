package com.reggi.controller;
import com.reggi.common.*;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.reggi.entity.Employee;
import com.reggi.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        String password = employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp=employeeService.getOne(queryWrapper);
        if(emp==null){
            return R.error("login failed");
        }

        if (!emp.getPassword().equals(password)){
            return R.error("login failed");
        }

        if(emp.getStatus()==0){
            return R.error("the account doesn't exist");
        }

        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);

    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //清除session中保存当前员工登录的id
        request.getSession().removeAttribute("employee");
        return R.success("logout success");
    }

    /**
     *
     * add new employee
     * @param employee
     * @return
     */

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){
        log.info("新增员工，员工信息:{}",employee.toString());

        //设置初始密码，进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setCreateTime(new Date());
        employee.setUpdateTime(new Date());

        long empId=(Long)request.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);
        employeeService.save(employee);
        return R.success("新增员工成功");
    }
}
