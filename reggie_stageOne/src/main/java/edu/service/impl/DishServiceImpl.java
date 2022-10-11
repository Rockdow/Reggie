package edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dto.DishDto;
import edu.mapper.DishMapper;
import edu.pojo.Dish;
import edu.pojo.DishFlavor;
import edu.service.DishFlavorService;
import edu.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    DishFlavorService dishFlavorService;
    @Autowired
    DishMapper dishMapper;
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        super.save(dishDto);//先保存菜品表信息，因为DishDTO类继承了Dish，所以可以用save方法保存
        //调用 BaseMapper 的 insert())或者调用service的save()方法后 ，默认将自增主键封装在 插入对象中批量保存也会
        Long dishId = dishDto.getId();//返回dish的主键值
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (int i = 0; i < flavors.size(); i++) {
            flavors.get(i).setDishId(dishId);
        }
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        super.updateById(dishDto);//只更新了Dish表
        /*对于口味表来说，由于一道菜的口味维度可能是变化的，比如原来只有辣度，现在变成了辣度加温度，所以不能应用简单的更新操作
        * 应该先把口味表中该道菜的相关记录删除，再重写回去*/
        Long id = dishDto.getId();
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,id);
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);

        List<DishFlavor> flavors = dishDto.getFlavors();
        for (int i = 0; i < flavors.size(); i++) {
            flavors.get(i).setDishId(id);
        }
        dishFlavorService.saveBatch(flavors);

    }

    @Override
    public Page<DishDto> joinCategoryPage(Page page, Wrapper<Dish> wrapper) {
        Page<DishDto> dishDtoPage = dishMapper.joinCategoryPage(page, wrapper);
        return dishDtoPage;
    }

}
