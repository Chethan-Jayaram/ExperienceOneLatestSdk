package com.example.experienceone.adapter.pagination;


import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.experienceone.pojo.HouseKeeping.Result;



public class ItemDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, Result>> itemLiveDataSource = new MutableLiveData<>();


    private MutableLiveData<ItemDataSourceFactory> mutableLiveData;
    private String url;


    ItemDataSourceFactory(String url) {
        this.url=url;
        mutableLiveData = new MutableLiveData<>();
    }


    @Override
    public DataSource create() {
        ItemDataSource itemDataSource = new ItemDataSource(url);
        itemLiveDataSource.postValue(itemDataSource);
        return itemDataSource;
    }

    MutableLiveData<PageKeyedDataSource<Integer, Result>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
