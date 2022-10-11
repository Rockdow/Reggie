package edu.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.dto.DishDto;
import edu.dto.SetmealDto;
import edu.pojo.Setmeal;

public interface SetmealService extends IService<Setmeal> {
    void saveWithSmDish(SetmealDto setmealDto);
    Page<SetmealDto> joinCategoryPage(Page page, Wrapper<Setmeal> wrapper);
}
