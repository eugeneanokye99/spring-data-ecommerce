package com.shopjoy.graphql.resolver.query;

import com.shopjoy.dto.response.CategoryResponse;
import com.shopjoy.service.CategoryService;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CategoryQueryResolver {

    private final CategoryService categoryService;

    public CategoryQueryResolver(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @QueryMapping
    public List<CategoryResponse> categories() {
        return categoryService.getAllCategories();
    }
}
