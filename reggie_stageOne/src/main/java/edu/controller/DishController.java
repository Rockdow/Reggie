package edu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.common.CustomException;
import edu.common.R;
import edu.dto.DishDto;
import edu.pojo.Dish;
import edu.pojo.DishFlavor;
import edu.pojo.Setmeal;
import edu.service.DishFlavorService;
import edu.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private DishService dishService;
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        //使用dto类是因为前端传回的JSON数据封装了菜品表和菜品口味表的消息，使用dishDto来承接
        dishService.saveWithFlavor(dishDto);
        return R.success("菜品添加成功");

    }
    @GetMapping("/page")
    public R<Page<DishDto>> page(int page,int pageSize,String dishName){
        Page<DishDto> dishDtoPage = new Page<>(page,pageSize);
        QueryWrapper<Dish> dishQueryWrapper = new QueryWrapper<Dish>();
        dishQueryWrapper.like(dishName != null,"name",dishName);
        dishQueryWrapper.orderByDesc("update_time");
        dishService.joinCategoryPage(dishDtoPage,dishQueryWrapper);
        return R.success(dishDtoPage);
    }
    @GetMapping("/{id}")
    public R<DishDto> showDishDto(@PathVariable Long id){
        DishDto dishDto = new DishDto();
        Dish dish = dishService.getById(id);
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> list = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
        BeanUtils.copyProperties(dish,dishDto);
        dishDto.setFlavors(list);
        return R.success(dishDto);
    }
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        //使用dto类是因为前端传回的JSON数据封装了菜品表和菜品口味表的消息，使用dishDto来承接
        dishService.updateWithFlavor(dishDto);
        return R.success("菜品添加成功");
    }
    @PostMapping("/status/0")
    public R<String> stopSale(Long[] ids){
        for(Long id:ids){
            LambdaUpdateWrapper<Dish> dishLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            dishLambdaUpdateWrapper.eq(Dish::getId,id);
            dishLambdaUpdateWrapper.set(Dish::getStatus,0);
            dishService.update(dishLambdaUpdateWrapper);
        }
        return R.success("修改成功");
    }
    @PostMapping("/status/1")
    public R<String> startSale(Long[] ids){
        for(Long id:ids){
            LambdaUpdateWrapper<Dish> dishLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            dishLambdaUpdateWrapper.eq(Dish::getId,id);
            dishLambdaUpdateWrapper.set(Dish::getStatus,1);
            dishService.update(dishLambdaUpdateWrapper);
        }
        return R.success("修改成功");
    }
    @DeleteMapping
    @Transactional
    public R<String> delete(Long[] ids){
        //对于起售状态的菜品不能删除，所以先判断
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.in(Dish::getId,ids);
        dishLambdaQueryWrapper.eq(Dish::getStatus,1);
        int count = dishService.count(dishLambdaQueryWrapper);
        if(count != 0){
            throw  new CustomException("被删除的菜品中含有启售类型，操作失败");
        }
        //同时删除菜品表和口味表中的相关信息
        for(Long id:ids){
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,id);
            dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
            dishService.removeById(id);
        }
        return R.success("删除成功");
    }
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//        Long Id = dish.getCategoryId();
//        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        dishLambdaQueryWrapper.eq(Id!=null,Dish::getCategoryId,Id);
//        dishLambdaQueryWrapper.eq(Dish::getStatus,1);
//        List<Dish> list = dishService.list(dishLambdaQueryWrapper);
//        return R.success(list);
//    }
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        Long Id = dish.getCategoryId();
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Id!=null,Dish::getCategoryId,Id);
        dishLambdaQueryWrapper.eq(Dish::getStatus,1);
        List<Dish> list = dishService.list(dishLambdaQueryWrapper);
        List<DishDto> dishDtoList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(list.get(i),dishDto);
            dishDtoList.add(dishDto);
            Long dishId = list.get(i).getId();
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> listFlavor = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            dishDtoList.get(i).setFlavors(listFlavor);
        }
        return R.success(dishDtoList);
    }
}
