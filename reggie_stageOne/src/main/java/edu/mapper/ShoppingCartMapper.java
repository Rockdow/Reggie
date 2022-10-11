package edu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.pojo.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
