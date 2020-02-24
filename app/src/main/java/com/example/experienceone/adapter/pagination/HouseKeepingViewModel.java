package com.example.experienceone.adapter.pagination;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;
import com.example.experienceone.pojo.HouseKeeping.Result;


public class HouseKeepingViewModel extends AndroidViewModel {


    private LiveData<PagedList<Result>> itemPagedList;
    MutableLiveData<PageKeyedDataSource<Integer, Result>> dataSourceMutableLiveData;
    ItemDataSourceFactory itemDataSourceFactory;



    public HouseKeepingViewModel(@NonNull Application application, String url) {
        super(application);

        itemDataSourceFactory = new ItemDataSourceFactory(url);
        dataSourceMutableLiveData = itemDataSourceFactory.getItemLiveDataSource();

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(15)
                        .build();

        itemPagedList = (new LivePagedListBuilder<Integer,Result>(itemDataSourceFactory,config))
                .build();

    }
    public LiveData<PagedList<Result>> getPagedListLiveData() {
        return itemPagedList;
    }
}
