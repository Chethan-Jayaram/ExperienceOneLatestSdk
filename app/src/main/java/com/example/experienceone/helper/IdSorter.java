package com.example.experienceone.helper;

import com.example.experienceone.pojo.HouseKeeping.CategoryItem;

import java.util.Comparator;

public class IdSorter implements Comparator<CategoryItem>
{
    @Override
    public int compare(CategoryItem o1, CategoryItem o2) {
        return o2.getId().compareTo(o1.getId());
    }
}