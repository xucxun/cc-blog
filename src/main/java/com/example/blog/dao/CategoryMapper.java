package com.example.blog.dao;

import com.example.blog.entity.Article;
import com.example.blog.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {

    List<Category> getArticleCategory();

    int insertCategory(Category category);

    Category getCategory(Integer id);

    Category getCategoryByName(String name);

    Category getOtherCategory(@Param("id")Integer id, @Param("name")String name);

    List<Category> getAllCategory(@Param("offset") int offset, @Param("limit") int limit);

    int countAllCategory();

    int updateCategory(@Param("id")Integer id, @Param("name")String name,@Param("description") String description);

    int updateCategoryRefCount(@Param("id")Integer id, @Param("refCount")Integer refCount);

    int updateCategoryStatus(@Param("id")Integer id,@Param("status") Integer status);

    int updateCategoryDisplay(@Param("id")Integer id,@Param("display") Integer display);

    int searchCategoryCount(@Param("name") String name, @Param("display")Integer display);

    List<Category> searchCategoryList(@Param("name") String name, @Param("display")Integer display,
                                      @Param("offset")int offset, @Param("limit")int limit);

}
