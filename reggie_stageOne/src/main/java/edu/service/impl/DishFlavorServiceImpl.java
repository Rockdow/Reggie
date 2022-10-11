package edu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.mapper.DishFlavorMapper;
import edu.pojo.DishFlavor;
import edu.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
