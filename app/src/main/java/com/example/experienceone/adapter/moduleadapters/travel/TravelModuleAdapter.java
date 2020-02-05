package com.example.experienceone.adapter.moduleadapters.travel;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.experienceone.R;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.model.Details;
import com.example.experienceone.pojo.travel.CarTariff;
import com.example.experienceone.pojo.travel.CategoryItem;
import com.example.experienceone.pojo.travel.Result;
import com.example.experienceone.services.ObjectListner;

import java.util.ArrayList;
import java.util.List;

public class TravelModuleAdapter extends RecyclerView.Adapter {

    private List<ObjectListner> mObjectListenr;
    private CabAdapterlistner menuAdapterlistner;
    private Context context;
    private List<CategoryItem> mItem = new ArrayList<>();
    private TravelRadioAdapter adapter;
    private Animation mAnimSlideDown, mCountIncrementAnima, mCountDescrementAnim;


    public TravelModuleAdapter(List<ObjectListner> mObjectListenr, Context context, CabAdapterlistner menuAdapterlistner) {
        this.mObjectListenr = mObjectListenr;
        this.menuAdapterlistner = menuAdapterlistner;
        this.context = context;
    }


    public interface CabAdapterlistner { // create an interface
        void onItemClickListener(ObjectListner mObjectListenr); // create callback function
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        mCountIncrementAnima = AnimationUtils.loadAnimation(parent.getContext(),
                R.anim.slide_up);

        mAnimSlideDown = AnimationUtils.loadAnimation(parent.getContext(),
                R.anim.slide_down);
        mCountDescrementAnim = AnimationUtils.loadAnimation(parent.getContext(),
                R.anim.slide_down);
        switch (viewType) {
            case ObjectListner.TYPE_CATEGORY_CARD:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.travel_header_content, parent, false);
                return new HeaderCategory(itemView);
            case ObjectListner.TYPE_SUBCATEGORY_CARD:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.travel_tail_content, parent, false);
                return new SubHeaderCategory(itemView);
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.travel_header_content, parent, false);
                return new HeaderCategory(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ObjectListner.TYPE_CATEGORY_CARD:
                ((HeaderCategory) holder).bindView(position);
                break;
            case ObjectListner.TYPE_SUBCATEGORY_CARD:
                ((SubHeaderCategory) holder).bindView(position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (mObjectListenr == null) {
            return 0;
        } else {
            return mObjectListenr.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mObjectListenr.get(position).getType();
    }

    class HeaderCategory extends RecyclerView.ViewHolder {

        private TextView tv_cab_header;

        HeaderCategory(@NonNull View itemView) {
            super(itemView);
            tv_cab_header = itemView.findViewById(R.id.tv_cab_header);

        }

        void bindView(int position) {
            try {
                Result result = (Result) mObjectListenr.get(position);
                CategoryItem item = new CategoryItem();
                item.setCategoryName(result.getCategoryName());
                item.setCategoryId(result.getId());
                mItem.add(item);
                tv_cab_header.setText(result.getCategoryName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class SubHeaderCategory extends RecyclerView.ViewHolder {

        private TextView tv_car_model, tv_count;
        private ImageView img_add, img_mius;
        private RecyclerView radio_group_recycler;

        SubHeaderCategory(@NonNull View itemView) {
            super(itemView);
            tv_car_model = itemView.findViewById(R.id.tv_car_model);

            img_add = itemView.findViewById(R.id.img_add);
            img_mius = itemView.findViewById(R.id.img_mius);
            radio_group_recycler = itemView.findViewById(R.id.radio_group_recycler);
            tv_count = itemView.findViewById(R.id.tv_count);
        }

        void bindView(int position) {
            try {
                CategoryItem item = (CategoryItem) mObjectListenr.get(position);
                tv_car_model.setText(item.getCarModel());
                adapter = new TravelRadioAdapter(item.getCarTariff(), (tarrifPosition -> {
                    item.setTitle(item.getCarTariff().get(tarrifPosition).getLabel());
                    item.setPrice(item.getCarTariff().get(tarrifPosition).getAmount());
                }));
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                radio_group_recycler.setLayoutManager(mLayoutManager);
                radio_group_recycler.setNestedScrollingEnabled(false);
                radio_group_recycler.setItemAnimator(new DefaultItemAnimator());
                radio_group_recycler.setAdapter(adapter);

                img_add.setOnClickListener(v -> {
                    try {
                        if (!(item.getPrice() == null)) {

                            if (Integer.parseInt(tv_count.getText().toString()) < 100) {
                                mCountIncrementAnima.setDuration(100);
                                tv_count.startAnimation(mCountIncrementAnima);

                            }
                            mCountIncrementAnima.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    tv_count.setText(String.valueOf(GlobalClass.numberStepperAdd(Integer.parseInt(tv_count.getText().toString()))));
                                    mAnimSlideDown.setDuration(0);
                                    tv_count.startAnimation(mAnimSlideDown);
                                    item.setQuantity(tv_count.getText().toString());
                                    for (int i = 0; i < mItem.size(); i++) {
                                        if (mItem.get(i).getCategoryId() == item.getCategoryId()) {
                                            item.setTags(mItem.get(i).getCategoryName() + "," + item.getCarModel());
                                        }
                                    }
                                    menuAdapterlistner.onItemClickListener(item);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                        } else {
                            GlobalClass.ShowAlet(context, "Alert", "Please select car tarrif");
                        }



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                img_mius.setOnClickListener(v -> {
                    try {
                        if (Integer.parseInt(tv_count.getText().toString()) >0) {
                            mCountDescrementAnim.setDuration(100);
                            tv_count.startAnimation(mCountDescrementAnim);
                        }
                        mCountDescrementAnim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                tv_count.setText(String.valueOf(GlobalClass.numberStepperSub(Integer.parseInt(tv_count.getText().toString()))));
                                item.setQuantity(tv_count.getText().toString());
                                item.setTags("");
                                menuAdapterlistner.onItemClickListener(item);
                            }
                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });





                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
