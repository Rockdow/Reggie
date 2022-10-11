package edu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.common.CustomException;
import edu.common.R;
import edu.dto.SetmealDto;
import edu.pojo.Dish;
import edu.pojo.Setmeal;
import edu.pojo.SetmealDish;
import edu.service.SetmealDishService;
import edu.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithSmDish(setmealDto);
        return R.success("套餐保存成功");
    }
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        QueryWrapper<Setmeal> setmealQueryWrapper = new QueryWrapper<>();
        Page<SetmealDto> DtoPage = new Page<>(page, pageSize);
        setmealQueryWrapper.orderByDesc("update_time");
        Page<SetmealDto> setmealDtoPage = setmealService.joinCategoryPage(DtoPage, setmealQueryWrapper);
        return R.success(setmealDtoPage);
    }
    @DeleteMapping
    @Transactional
    public R<String> delete(Long[] ids){
        //对于起售状态的套餐不能删除，所以先判断
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(Setmeal::getId,ids);
        setmealLambdaQueryWrapper.eq(Setmeal::getStatus,1);
        int count = setmealService.count(setmealLambdaQueryWrapper);
        if(count != 0){
            throw  new CustomException("被删除的套餐中含有启售类型，操作失败");
        }
       List<Long> list = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            list.add(ids[i]);
        }

        setmealService.removeByIds(list);

        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<SetmealDish>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(setmealDishLambdaQueryWrapper);

        return R.success("删除成功");
    }
    @PostMapping("/status/0")
    public R<String> stopSale(Long[] ids){
        for(Long id:ids){
            LambdaUpdateWrapper<Setmeal> setmealLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            setmealLambdaUpdateWrapper.eq(Setmeal::getId,id);
            setmealLambdaUpdateWrapper.set(Setmeal::getStatus,0);
            setmealService.update(setmealLambdaUpdateWrapper);
        }
        return R.success("修改成功");
    }
    @PostMapping("/status/1")
    public R<String> startSale(Long[] ids){
        for(Long id:ids){
            LambdaUpdateWrapper<Setmeal> setmealLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            setmealLambdaUpdateWrapper.eq(Setmeal::getId,id);
            setmealLambdaUpdateWrapper.set(Setmeal::getStatus,1);
            setmealService.update(setmealLambdaUpdateWrapper);
        }
        return R.success("修改成功");
    }
    @GetMapping("/{id}")
    public R<SetmealDto> showSetmealDto(@PathVariable Long id){
        Setmeal setmeal = setmealService.getById(id);
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> list = setmealDishService.list(setmealDishLambdaQueryWrapper);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);
        setmealDto.setSetmealDishes(list);
        return R.success(setmealDto);
    }
    @PutMapping
    @Transactional
    public R<String> update(@RequestBody SetmealDto setmealDto){
        //先删除setmealDish表中的相关该setmeal的记录
        Long id = setmealDto.getId();
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,id);
        setmealDishService.remove(setmealDishLambdaQueryWrapper);

        setmealService.updateById(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (int i = 0; i < setmealDishes.size(); i++) {
            setmealDishes.get(i).setSetmealId(id);
            setmealDishService.save(setmealDishes.get(i));
        }
        return R.success("修改成功");
    }
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        Long categoryId = setmeal.getCategoryId();
        Integer status = setmeal.getStatus();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,categoryId);
        setmealLambdaQueryWrapper.eq(Setmeal::getStatus,status);
        setmealLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(setmealLambdaQueryWrapper);
        return R.success(list);
    }
}
