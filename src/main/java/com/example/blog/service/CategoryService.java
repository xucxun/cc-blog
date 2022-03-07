package com.example.blog.service;

import com.example.blog.entity.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryService {

    /**
     * 前台获取文章类别
     * @return
     */
    List<Category> getArticleCategory();

    /**
     * 新增类别
     * @param category
     * @return
     */
    int addCategory(Category category);

    /**
     * 根据id获取类别
     * @param id 类别ID
     * @return
     */
    Category getCategory(Integer id);

    List<Category> getCategoryByIds(List<Integer> ids);

    /**
     * 根据名称获取类别
     * @param name 类别名称
     * @return
     */
    Category getCategoryByName(String name);


    Category getOtherCategory(Integer id,String name);

    /**
     * 后台分页获取所有类别
     * @param offset
     * @param limit
     * @return
     */
    List<Category> getAllCategory( int offset, int limit);

    /**
     * 搜索类别数
     * @param name
     * @param display
     * @return
     */
    int countSearchCategory(String name,Integer display);

    List<Category> searchCategoryList(String name,Integer display,int offset, int limit);

    /**
     * 所有类别数量
     * @return
     */
    int countAllCategory();

    /**
     * 更新类别
     * @param id
     * @param name
     * @param description
     * @return
     */
    int updateCategory(Integer id,String name,String description);

    /**
     * 更新类别状态
     * @param id
     * @param status
     * @return
     */
    int updateCategoryStatus(Integer id, Integer status);

    /**
     * 设置类别前台显示
     * @param id
     * @param display
     * @return
     */
    int updateCategoryDisplay(Integer id, Integer display);


}
