package edu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.common.R;
import edu.pojo.Category;
import edu.pojo.Employee;
import edu.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        /*1、获取用户密码。md5加密
        * 2、查询数据库中该用户姓名，如没有返回
        * 3、比对数据库中该用户密码，若错误返回
        * 4、查看员工状态，若禁用返回
        * 5、若以上都没问题，返回成功消息
        * */
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername,employee.getUsername());

        Employee one = employeeService.getOne(lqw);
        if(one == null) return R.error("登陆失败");

        if(!password.equals(one.getPassword())) return R.error("登录失败");

        Integer status = one.getStatus();
        if (status == 0) return R.error("该员工已被禁用");

        HttpSession session = request.getSession();
        session.setAttribute("employee",one.getId());
        return R.success(one);
    }
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        /*同一客户端同一浏览器的同一次访问服务器的过程中，session是一致的*/
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
//        Long employee_id = (Long)request.getSession().getAttribute("employee");
//        employee.setCreateUser(employee_id);
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(employee_id);
        //为用户设置统一的密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        log.info(employee.toString());
        employeeService.save(employee);
        return R.success("添加成功");
    }
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //创建page对象，再创建lqw对象实现条件查询
        Page<Employee> employeePage = new Page<>(page,pageSize);

        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        employeeLambdaQueryWrapper.like(!StringUtils.isEmpty(name),Employee::getName,name);
        employeeLambdaQueryWrapper.orderByAsc(Employee::getUpdateTime);

        employeeService.page(employeePage,employeeLambdaQueryWrapper);
        return  R.success(employeePage);

    }
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        //long型数据在JavaScript中会丢失进度，使用扩展的消息转化器将其转为字符串，注意，json数据只要属性名相同，字符串类型也可以赋给Integer类型
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        employeeService.updateById(employee);
        return R.success("修改成功");
    }
    @GetMapping("/{id}")
    public R<Employee> getEmployee(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        if(employee != null)
            return R.success(employee);
        return R.error("回显出错");
    }

}
