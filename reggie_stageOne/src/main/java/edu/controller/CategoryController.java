package edu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.common.R;
import edu.pojo.Category;
import edu.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){
        Page<Category> categoryPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.orderByAsc(Category::getSort);

        categoryService.page(categoryPage,categoryLambdaQueryWrapper);
        return R.success(categoryPage);
    }
    @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        return R.success("添加成功");
    }
    @DeleteMapping
    public R<String> delete(Long ids){
        categoryService.remove(ids);
        return R.success("删除成功");
    }
    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改成功");
    }
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        //参数category接受type参数
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        categoryLambdaQueryWrapper.orderByAsc(Category::getSort,Category::getUpdateTime);
        //因为菜品种类是不能重复的字段，所以不需要去重
        List<Category> list = categoryService.list(categoryLambdaQueryWrapper);
        return R.success(list);
    }
}
