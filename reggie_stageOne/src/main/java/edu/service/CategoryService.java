package edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.common.R;
import edu.pojo.Category;

public interface CategoryService extends IService<Category> {
    void remove(Long id);

}
