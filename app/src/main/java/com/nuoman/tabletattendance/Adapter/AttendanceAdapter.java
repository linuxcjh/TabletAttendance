package com.nuoman.tabletattendance.Adapter;

import android.content.Context;
import android.graphics.Color;
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
public class AttendanceAdapter extends QuickAdapter<ReceivedUnreadInforModel> {

    public AttendanceAdapter(Context context, int layoutResId, List<ReceivedUnreadInforModel> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, ReceivedUnreadInforModel item, int position) {

        ImageView imageView = helper.getView(R.id.item_icon);
        Glide.with(context).load(item.getHeadPicUrl()).error(R.drawable.icon).placeholder(R.drawable.icon).into(imageView);
        helper.setText(R.id.name_tv, item.getStudentName());
        switch (item.getAttStatus()) {
            case "缺勤":
                helper.setTextColor(R.id.name_tv, Color.parseColor("#252525"));
                break;
            case "事假":
                helper.setTextColor(R.id.name_tv, Color.parseColor("#F7E011"));

                break;
            case "病假":
                helper.setTextColor(R.id.name_tv, Color.parseColor("#F7E011"));

                break;
            case "公假":
                helper.setTextColor(R.id.name_tv, Color.parseColor("#F7E011"));

                break;

            default:
                helper.setTextColor(R.id.name_tv, Color.parseColor("#85F43E"));

                break;

        }
    }
}
