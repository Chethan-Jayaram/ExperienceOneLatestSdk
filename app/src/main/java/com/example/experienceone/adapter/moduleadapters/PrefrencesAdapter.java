package com.example.experienceone.adapter.moduleadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.experienceone.R;
import com.example.experienceone.helper.APIResponse;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.pojo.preference.CreateUpdatePrefrencesPojo;
import com.example.experienceone.pojo.preference.Result;
import com.example.experienceone.retrofit.ClientServiceGenerator;
import com.example.experienceone.services.APIMethods;
import com.example.experienceone.services.ApiListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class PrefrencesAdapter extends RecyclerView.Adapter<PrefrencesAdapter.MyViewHolder> implements ApiListener {

    private List<Result> item_;
    private AdapterClickListner adapterClickListner;
    private Context mContext;

    public PrefrencesAdapter(List<Result> item_, PrefrencesAdapter.AdapterClickListner adapterClickListner, Context context) {
        this.item_ = item_;
        this.adapterClickListner = adapterClickListner;
        mContext = context;
    }


    public interface AdapterClickListner { // create an interface
        void onItemClickListener(Integer position, String task,Boolean bool,Integer id); // create callback function
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prefrences_recycler_content, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
        try {
            if (item_.get(position).getActive()) {
                holder.toggle_switch.setChecked(true);
            } else {
                holder.toggle_switch.setChecked(false);
            }

            holder.toggle_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    putStaus(true,position);
                } else {
                    putStaus(false,position);
                }
            });
            ;
            holder.tv_days.setText(item_.get(position).getRepeatDays());
            holder.tv_pref_title.setText(item_.get(position).getName() );
            holder.iv_options_menu.setOnClickListener(v -> {
                showPopUpMenu(holder, position);
            });
            holder.tv_time.setText(GlobalClass.outputTimeFormat.format(GlobalClass.inputTimeFormat.parse(item_.get(position).getTime())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPopUpMenu(@NotNull MyViewHolder holder, int position) {
        //creating a popup menu
        PopupMenu popup = new PopupMenu(mContext, holder.iv_options_menu);
        //inflating menu from xml resource
        popup.inflate(R.menu.options_menu);
        //adding click listener
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.edit:
                    adapterClickListner.onItemClickListener(position, "edit",holder.toggle_switch.isChecked(), Integer.valueOf(item_.get(position).getId()));
                    break;
                case R.id.delete:
                    adapterClickListner.onItemClickListener(position, "delete",false,0);
                    removeAt(position);
                    break;
            }
            return false;
        });
        //displaying the popup
        popup.show();
    }

    @Override
    public int getItemCount() {
        return item_.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_pref_title, tv_time, tv_days;
        private ImageView iv_options_menu;
        private Switch toggle_switch;

        MyViewHolder(View view) {
            super(view);
            tv_pref_title = view.findViewById(R.id.tv_pref_title);
            tv_time = view.findViewById(R.id.tv_time);
            iv_options_menu = view.findViewById(R.id.iv_options_menu);
            tv_days = view.findViewById(R.id.tv_days);
            toggle_switch = view.findViewById(R.id.toggle_switch);

        }
    }

    private void putStaus(boolean status, int position) {
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Map<String, String> body = new HashMap<>();
        body.put("name", item_.get(position).getName());
        body.put("_time", item_.get(position).getTime());
        body.put("repeat_days", item_.get(position).getRepeatDays());
        body.put("guest", String.valueOf(item_.get(position).getGuest()));
        body.put("is_active", String.valueOf(status));
        Call<CreateUpdatePrefrencesPojo> putStatusPrefrence = api.putStatusPrefrence(String.valueOf(item_.get(position).getId()), headerMap, body);
        APIResponse.postCallRetrofit(putStatusPrefrence, "statusprefrence", mContext, this);
    }

    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {

    }

    @Override
    public void onErrorListner() {

    }

    private void removeAt(int position) {
        item_.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, item_.size());
    }
}
