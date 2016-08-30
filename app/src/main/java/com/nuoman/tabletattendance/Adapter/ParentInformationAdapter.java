package com.nuoman.tabletattendance.Adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nuoman.tabletattendance.R;
import com.nuoman.tabletattendance.common.BaseAdapterHelper;
import com.nuoman.tabletattendance.common.QuickAdapter;
import com.nuoman.tabletattendance.model.ParentInfo;

import java.util.List;

/**
 * Created by Alex on 2015/11/4.
 */
public class ParentInformationAdapter extends QuickAdapter<ParentInfo> {

    public ParentInformationAdapter(Context context, int layoutResId, List<ParentInfo> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, ParentInfo item, int position) {

        ImageView imageView = helper.getView(R.id.item_icon);
        Glide.with(context).load(item.getHeadPicUrl()).error(R.drawable.icon).placeholder(R.drawable.icon).into(imageView);
        helper.setText(R.id.name_tv,item.getDataName());
    }
}
