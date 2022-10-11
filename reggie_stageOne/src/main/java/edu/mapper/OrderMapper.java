package edu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.pojo.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
