package edu.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.dto.DishDto;
import edu.pojo.Dish;
import org.apache.ibatis.annotations.Param;

public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dishDto);
    void updateWithFlavor(DishDto dishDto);
    Page<DishDto> joinCategoryPage(Page page, Wrapper<Dish> wrapper);
}
