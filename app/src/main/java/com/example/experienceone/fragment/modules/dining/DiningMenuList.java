package com.example.experienceone.fragment.modules.dining;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.experienceone.R;
import com.example.experienceone.adapter.moduleadapters.dinningadapters.DinningMenuItemAdapter;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.model.dinningmodel.DinningSegmentModel;
import com.example.experienceone.pojo.dinning.CategoryItem;
import com.example.experienceone.services.FragmentCallback;

import java.util.ArrayList;
import java.util.List;

public class DiningMenuList extends Fragment implements FragmentCallback {
    
    private ArrayList<CategoryItem> menuItems=new ArrayList<>();
    private TextView item_count,tv_item_price;
    private DinningSegmentModel dinningSegmentModel;
    private Double prices=0.0;
    private Integer count=0;
    private DiningModuleSegment moduleSegment;
    private Boolean isForward=false;
    private List<CategoryItem> detail;
    private RecyclerView dinning_menu_recycler;
    private Context context;
    private FragmentCallback fragmentCallback;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_menu_list,container,false);
        try {
             context = view.getContext();
             dinning_menu_recycler = view.findViewById(R.id.dinning_menu_recycler);
            TextView menu_name = view.findViewById(R.id.menu_name);
            TextView menu_timing = view.findViewById(R.id.menu_timing);
            TextView menu_desc = view.findViewById(R.id.menu_desc);
            item_count = view.findViewById(R.id.item_count);
            tv_item_price = view.findViewById(R.id.tv_item_price);
            TextView tv_view_order = view.findViewById(R.id.tv_view_order);
            Bundle data = getArguments();
            detail = data.getParcelableArrayList("OutletMenus");
            String menuName = data.getString("SubMenuName");
            dinningSegmentModel=data.getParcelable("subcategory");
            moduleSegment=data.getParcelable("diningModuleSegment");
            moduleSegment.setFragmentCallback(this);
            if(!isForward){
                this.prices = Double.valueOf(data.getString("item_price").replace(" Rs", ""));
                this.count = Integer.valueOf(data.getString("item_count").replace(" items", ""));
            }


            dinningSegmentModel.setTitle("In-Room-Dinning");
            dinningSegmentModel.setBooking(GlobalClass.Booking_id);
            setAdapter();

            menu_name.setText(menuName);
            TextView toolbar_title = getActivity().findViewById(R.id.toolbar_title);
            toolbar_title.setText(menuName);
            String sourceString = "Serving " + menuName + " from <b> 6:30 AM To 11:30 PM </b> ";
            menu_timing.setText(Html.fromHtml(sourceString));
            menu_desc.setText("All " + menuName + " sets are served with the way you liked it cooked");



            tv_view_order.setOnClickListener(v -> {
                if (dinningSegmentModel.getDetails() != null && dinningSegmentModel.getDetails().size() > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("subcategory", dinningSegmentModel);
                    bundle.putParcelableArrayList("category", menuItems);
                    bundle.putString("item_count", item_count.getText().toString());
                    bundle.putString("item_price", tv_item_price.getText().toString());
                    moduleSegment.setArguments(bundle);
                    isForward = true;
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, moduleSegment).addToBackStack(null).commit();
                } else {
                    GlobalClass.ShowAlet(context, "Alert", "please select at least one item");
                }
            });
            item_count.setText(count + " items");
            tv_item_price.setText(prices + " Rs");

        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        try {
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    void setFragmentCallback(FragmentCallback callback) {
        this.fragmentCallback = callback;
    }

    private void setAdapter() {
        DinningMenuItemAdapter adapter = new DinningMenuItemAdapter(detail, count,prices, (categoryItem, price, pos) -> {
            try {
                menuItems.add(categoryItem);
                dinningSegmentModel.setDetails(GlobalClass.removeDuplicateItems(menuItems));
                item_count.setText(dinningSegmentModel.getDetails().size() + " items");
                tv_item_price.setText(GlobalClass.decimalFormat.format(Math.abs(price)) + " Rs");
                this.prices = price;
                this.count = dinningSegmentModel.getDetails().size();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        dinning_menu_recycler.setLayoutManager(mLayoutManager);
        dinning_menu_recycler.setNestedScrollingEnabled(false);
        dinning_menu_recycler.setItemAnimator(new DefaultItemAnimator());
        dinning_menu_recycler.setAdapter(adapter);
    }

    @Override
    public void onDataSent(Double price, Integer count, DinningSegmentModel dinningSegmentModel) {
            prices=price;
            this.count=count;
            this.dinningSegmentModel=new DinningSegmentModel();
            this.dinningSegmentModel.setDetails(dinningSegmentModel.getDetails());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void onDestroy() {
        if (fragmentCallback != null) {
            fragmentCallback.onDataSent(this.prices , this.count , dinningSegmentModel);
        }
        super.onDestroy();
    }
}
