package com.example.experienceone.adapter.pagination;


import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.experienceone.pojo.HouseKeeping.Result;


public class ItemDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, Result>> itemLiveDataSource = new MutableLiveData<>();


    MutableLiveData<ItemDataSourceFactory> mutableLiveData;
    private String url;


    public ItemDataSourceFactory(String url) {

        this.url=url;
        mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource create() {
        ItemDataSource itemDataSource = new ItemDataSource(url);
        itemLiveDataSource.postValue(itemDataSource);
        return itemDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Result>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
