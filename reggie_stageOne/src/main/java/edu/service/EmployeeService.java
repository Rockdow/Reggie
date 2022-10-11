package edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.pojo.Employee;

public interface EmployeeService extends IService<Employee> {
}
/*
*MP中Service接口和对应实现类的标准
public interface UserService extends IService<User> {
}

public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
* */