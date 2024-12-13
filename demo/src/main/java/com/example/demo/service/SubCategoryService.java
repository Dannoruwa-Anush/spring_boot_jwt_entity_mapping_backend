package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.request.SubCategoryRequestDTO;
import com.example.demo.entity.SubCategory;

@Service
public interface SubCategoryService {
    
    List<SubCategory> getAllSubCategories();

    SubCategory getSubCategoryById(long id);

    SubCategory saveSubCategory(SubCategoryRequestDTO subCategorySaveRequestDTO);

    SubCategory updateSubCategory(long id, SubCategoryRequestDTO subCategorySaveRequestDTO);

    void deleteSubCategory(long id);
}
