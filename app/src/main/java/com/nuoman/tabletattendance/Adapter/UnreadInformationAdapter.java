package com.nuoman.tabletattendance.Adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nuoman.tabletattendance.R;
import com.nuoman.tabletattendance.common.BaseAdapterHelper;
import com.nuoman.tabletattendance.common.QuickAdapter;
import com.nuoman.tabletattendance.model.ReceivedUnreadInforModel;

import java.util.List;

/**
 * Created by Alex on 2015/11/4.
 */
public class UnreadInformationAdapter extends QuickAdapter<ReceivedUnreadInforModel> {

    public UnreadInformationAdapter(Context context, int layoutResId, List<ReceivedUnreadInforModel> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, ReceivedUnreadInforModel item, int position) {

        ImageView imageView = helper.getView(R.id.item_icon);
        Glide.with(context).load(item.getHeadPicUrl()).error(R.drawable.icon).placeholder(R.drawable.icon).into(imageView);
        helper.setText(R.id.name_tv,item.getStudentName());
    }
}
