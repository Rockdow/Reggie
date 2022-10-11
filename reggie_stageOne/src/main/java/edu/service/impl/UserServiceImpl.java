package edu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.mapper.UserMapper;
import edu.pojo.User;
import edu.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
