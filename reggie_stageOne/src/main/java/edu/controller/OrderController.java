package edu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import edu.common.BaseThreadLocal;
import edu.common.R;
import edu.pojo.*;
import edu.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    OrderDetailService orderDetailService;
    @Autowired
    ShoppingCartService shoppingCartService;
    @Autowired
    AddressBookService addressBookService;
    @Autowired
    UserService userService;

    @PostMapping("/submit")
    @Transactional
    public R<String> submit(@RequestBody Orders order){
        //对订单表，订单详细表，购物车表进行操作
        Long userId = BaseThreadLocal.get();
        //查询购物车数据，预备计算订单总金额以及插入订单详细表
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> list = shoppingCartService.list(shoppingCartLambdaQueryWrapper);
        //查询用户地址，用于订单表中的地址描述
        AddressBook address = addressBookService.getById(order.getAddressBookId());
        //生成订单明细表中的订单号
        long orderId = IdWorker.getId();
        //计算订单总金额,顺便设置订单详细表的数据
        AtomicInteger amount = new AtomicInteger(0);
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(list.get(i).getNumber());
            orderDetail.setDishFlavor(list.get(i).getDishFlavor());
            orderDetail.setDishId(list.get(i).getDishId());
            orderDetail.setSetmealId(list.get(i).getSetmealId());
            orderDetail.setName(list.get(i).getName());
            orderDetail.setImage(list.get(i).getImage());
            orderDetail.setAmount(list.get(i).getAmount());
            orderDetails.add(orderDetail);
            amount.addAndGet(list.get(i).getAmount().multiply(new BigDecimal(list.get(i).getNumber())).intValue());
        }
        //获取用户信息
        User user = userService.getById(userId);
        order.setId(orderId);
        order.setOrderTime(LocalDateTime.now());
        order.setCheckoutTime(LocalDateTime.now());
        order.setStatus(2);
        order.setAmount(new BigDecimal(amount.get()));//总金额
        order.setUserId(userId);
        order.setNumber(String.valueOf(orderId));
        order.setUserName(user.getName());
        order.setConsignee(address.getConsignee());
        order.setPhone(address.getPhone());
        order.setAddress((address.getProvinceName() == null ? "" : address.getProvinceName())
                + (address.getCityName() == null ? "" : address.getCityName())
                + (address.getDistrictName() == null ? "" : address.getDistrictName())
                + (address.getDetail() == null ? "" : address.getDetail()));
        //删除购物车
        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);
        //加数据
        orderService.save(order);
        orderDetailService.saveBatch(orderDetails);
        return R.success("提交成功");
    }
}
