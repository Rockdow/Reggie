package edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.common.CustomException;
import edu.common.R;
import edu.mapper.CategoryMapper;
import edu.pojo.Category;
import edu.pojo.Dish;
import edu.pojo.Setmeal;
import edu.service.CategoryService;
import edu.service.DishService;
import edu.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    public DishService dishService;
    @Autowired
    public SetmealService setmealService;

    @Override
    public void remove(Long id) {
        /*
        * 分类用于菜品和套餐，这就涉及表和表之间的关联，因此删除的时候需要检查该分类下是否存在菜品或套餐
        * 1、查找菜品表套餐表中的categoryId字段与待删除分类Id相同的记录数
        * 2、如果二者记录数中存在不为0，那就说明不能删除，直接返回自定义异常对象
        * 3、否则，直接删除*/
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();

        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);

        int count1 = dishService.count(dishLambdaQueryWrapper);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);

        if(count1 > 0){
            throw new CustomException("该分类下存在菜品，删除失败");
        }

        if(count2 > 0){
            throw new CustomException("该分类下存在套餐，删除失败");
        }

        super.removeById(id);

    }
}
