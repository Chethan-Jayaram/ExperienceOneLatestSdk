package com.example.experienceone.services;

import com.example.experienceone.model.dinningmodel.DinningSegmentModel;

public interface FragmentCallback {
    void onDataSent(Double price, Integer count, DinningSegmentModel dinningSegmentModel);
}
