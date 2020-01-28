package com.example.experienceone.adapter.generaladapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.experienceone.R;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.pojo.navmenuitems.MobileRoute;
import com.example.experienceone.pojo.navmenuitems.Result;
import com.example.experienceone.pojo.navmenuitems.RoutesSubcategory;

import java.util.HashMap;
import java.util.List;

public class ExpandableNavListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<Result> listDataHeader;
    private HashMap<Result, List<RoutesSubcategory>> listDataChild;

    public ExpandableNavListAdapter(Context context, List<Result> listDataHeader,
                                    HashMap<Result, List<RoutesSubcategory>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group_child, null);
            }

            TextView txtListChild = convertView
                    .findViewById(R.id.lblListItem);

            txtListChild.setText(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getTitle());
            if (!GlobalClass.hasActiveBooking) {
                txtListChild.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        if (this.listDataChild.get(this.listDataHeader.get(groupPosition)) == null)
            return 0;
        else
            return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                    .size();
    }

    @Override
    public Result getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();

    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        try {
            String headerTitle = getGroup(groupPosition).getTitle();
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group_header, null);
            }
            ImageView ig_view = convertView.findViewById(R.id.img_menu_services);
            ig_view.setVisibility(View.GONE);
            if (listDataHeader.get(groupPosition).getRoutesSubcategory().size()>0) {
                ig_view.setVisibility(View.VISIBLE);
                ig_view.setImageResource(R.drawable.ic_expand_more);
            }
            if (isExpanded) {
                ig_view.setImageResource(R.drawable.ic_expand_less);
            } else {
                ig_view.setImageResource(R.drawable.ic_expand_more);
            }
            TextView lblListHeader = convertView.findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);
        }catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}