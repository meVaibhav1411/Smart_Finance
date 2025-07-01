package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Category;
import com.example.demo.Repository.CategoryRepository;

@Service
public class CategoryService {
	  @Autowired
	    private CategoryRepository categoryRepository;

	    public Category findByName(String name) {
	        return categoryRepository.findByName(name);
	    }

	    public void save(Category category) {
	        categoryRepository.save(category);
	    }
}
