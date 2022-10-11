package edu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.common.R;
import edu.pojo.User;
import edu.service.UserService;
import edu.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpServletRequest request){
        String phone = user.getPhone();
        if(phone != null && phone != ""){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("验证码:"+code);
            request.getSession().setAttribute(phone,code);
            return R.success("发送成功");
        }
        return R.error("发送失败");
    }
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpServletRequest request){
        log.info(map.toString());
        String phone = (String)map.get("phone");
        String code = (String)map.get("code");
        Object attribute = request.getSession().getAttribute(phone);
        String s = attribute.toString();
        if(s.equals(code)){
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getPhone,phone);
            User one = userService.getOne(userLambdaQueryWrapper);
            User user = new User();
            if(one == null){
                user.setPhone(phone);
                userService.save(user);
                request.getSession().setAttribute("user",user.getId());
                log.info("user:"+user.getId());
            } else {
                request.getSession().setAttribute("user",one.getId());
                log.info("user:"+one.getId());
            }

            return R.success(user);
        }
        return R.error("登录失败");
    }
    @PostMapping("/loginout")
    public R<String> logout(HttpServletRequest request){
        /*同一客户端同一浏览器的同一次访问服务器的过程中，session是一致的*/
        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }

}
