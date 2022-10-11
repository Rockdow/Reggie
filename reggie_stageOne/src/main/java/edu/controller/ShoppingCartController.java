package edu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.common.BaseThreadLocal;
import edu.common.R;
import edu.pojo.ShoppingCart;
import edu.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    ShoppingCartService shoppingCartService;
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        /*
        * 1、添加UserID到数据中
        * 2、判断提交的数据是菜品还是套餐，依据该信息查询购物车表中是否存在该用户下的该菜品/套餐
        * 3、如果存在，那么把number字段加一
        * 4、否则直接新增数据，设置number字段为1*/
        shoppingCart.setUserId(BaseThreadLocal.get());
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,shoppingCart.getUserId());

        if(dishId != null){
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        ShoppingCart one = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);
        ShoppingCart returnShopCart = new ShoppingCart();
        if(one != null){
            Integer number = one.getNumber();
            number = number + 1;
            one.setNumber(number);
            returnShopCart = one;
            shoppingCartService.updateById(one);
        } else {
            shoppingCart.setNumber(1);
            returnShopCart = shoppingCart;
            shoppingCartService.save(shoppingCart);
        }
        return R.success(returnShopCart);
    }
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,BaseThreadLocal.get());
        shoppingCartLambdaQueryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(shoppingCartLambdaQueryWrapper);
        return R.success(list);
    }
    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,BaseThreadLocal.get());
        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);
        return R.success("删除成功");
    }
}
