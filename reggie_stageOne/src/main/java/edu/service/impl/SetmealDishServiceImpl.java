package edu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.mapper.SetmealDishMapper;
import edu.pojo.SetmealDish;
import edu.service.SetmealDishService;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}
