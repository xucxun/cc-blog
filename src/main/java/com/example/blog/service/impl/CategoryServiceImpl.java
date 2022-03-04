package com.example.blog.service.impl;

import com.example.blog.dao.CategoryMapper;
import com.example.blog.entity.Category;
import com.example.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> getArticleCategory() {
        return categoryMapper.getArticleCategory();
    }

    @Override
    public int addCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        return categoryMapper.insertCategory(category);
    }

    @Override
    public Category getCategory(Integer id) {
        return categoryMapper.getCategory(id);
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryMapper.getCategoryByName(name);
    }

    @Override
    public Category getOtherCategory(Integer id, String name) {
        return categoryMapper.getOtherCategory(id,name);
    }

    @Override
    public List<Category> getAllCategory(int offset, int limit) {
        return categoryMapper.getAllCategory(offset,limit);
    }

    @Override
    public int countSearchCategory(String name, Integer display) {
        return categoryMapper.searchCategoryCount(name,display);
    }

    @Override
    public List<Category> searchCategoryList(String name, Integer display, int offset, int limit) {
        return categoryMapper.searchCategoryList(name,display,offset,limit);
    }

    @Override
    public int countAllCategory() {
        return categoryMapper.countAllCategory();
    }

    @Override
    public int updateCategory(Integer id, String name,String description) {
        return categoryMapper.updateCategory(id,name,description);
    }

    @Override
    public int updateCategoryRefCount(Integer id, Integer refCount) {
        return categoryMapper.updateCategoryRefCount(id,refCount);
    }

    @Override
    public int updateCategoryStatus(Integer id, Integer status) {
        return categoryMapper.updateCategoryStatus(id,status);
    }

    @Override
    public int updateCategoryDisplay(Integer id, Integer display) {
        return categoryMapper.updateCategoryDisplay(id,display);
    }
}
