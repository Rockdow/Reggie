package edu.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.dto.DishDto;
import edu.pojo.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface DishMapper extends BaseMapper<Dish> {
    @Select("select dish.*, category.name as categoryName from dish join category on dish.category_id = category.id ${ew.customSqlSegment} ")
    Page<DishDto> joinCategoryPage(Page page, @Param(Constants.WRAPPER)Wrapper<Dish> wrapper);
}
