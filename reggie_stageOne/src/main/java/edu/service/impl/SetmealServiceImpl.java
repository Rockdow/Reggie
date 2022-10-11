package edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dto.SetmealDto;
import edu.mapper.SetmealMapper;
import edu.pojo.Setmeal;
import edu.pojo.SetmealDish;
import edu.service.SetmealDishService;
import edu.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    SetmealDishService setmealDishService;
    @Autowired
    SetmealMapper setmealMapper;
    @Override
    @Transactional
    public void saveWithSmDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        Long setmealId = setmealDto.getId();
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for(int i=0;i<setmealDishes.size();i++){
            setmealDishes.get(i).setSetmealId(setmealId);
        }
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public Page<SetmealDto> joinCategoryPage(Page page, Wrapper<Setmeal> wrapper) {
        return   setmealMapper.joinCategoryPage(page,wrapper);
    }
}
