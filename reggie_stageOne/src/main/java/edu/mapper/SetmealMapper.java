package edu.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.dto.SetmealDto;
import edu.pojo.Setmeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {
    @Select("select setmeal.*, category.name as categoryName from setmeal left join category on setmeal.category_id = category.id ${ew.customSqlSegment} ")
    Page<SetmealDto> joinCategoryPage(Page page, @Param(Constants.WRAPPER) Wrapper<Setmeal> wrapper);
}
