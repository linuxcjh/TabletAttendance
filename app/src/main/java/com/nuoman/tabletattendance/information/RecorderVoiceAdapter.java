package com.nuoman.tabletattendance.information;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nuoman.tabletattendance.R;
import com.nuoman.tabletattendance.model.VoiceReceiveModel;

import java.util.List;

public class RecorderVoiceAdapter extends ArrayAdapter<VoiceReceiveModel.VoiceItemModel> {


	private LayoutInflater inflater;

	private int mMinItemWith;// 设置对话框的最大宽度和最小宽度
	private int mMaxItemWith;

	public RecorderVoiceAdapter(Context context, List<VoiceReceiveModel.VoiceItemModel> dataList) {
		super(context, -1, dataList);
		inflater = LayoutInflater.from(context);

		// 获取系统宽度
		WindowManager wManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wManager.getDefaultDisplay().getMetrics(outMetrics);
		mMaxItemWith = (int) (outMetrics.widthPixels * 0.7f);
		mMinItemWith = (int) (outMetrics.widthPixels * 0.15f);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_layout, parent, false);
			
			viewHolder=new ViewHolder();
			viewHolder.seconds=(TextView) convertView.findViewById(R.id.recorder_time);
			viewHolder.length=convertView.findViewById(R.id.recorder_length);
			viewHolder.secondsLeft=(TextView) convertView.findViewById(R.id.recorder_time_2);
			viewHolder.lengthLeft=convertView.findViewById(R.id.recorder_length_2);
			viewHolder.leftLayout =(RelativeLayout) convertView.findViewById(R.id.left_layout);
			viewHolder.rightLayout =(RelativeLayout) convertView.findViewById(R.id.right_layout);

			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}

		if(getItem(position).getKind().equals("0")){
			viewHolder.rightLayout.setVisibility(View.VISIBLE);
			viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.seconds.setVisibility(View.GONE);//TODO
			viewHolder.seconds.setText(Math.round(getItem(position).getTime())+"\"");
			ViewGroup.LayoutParams lParams=viewHolder.length.getLayoutParams();
			lParams.width=(int) (mMinItemWith+mMaxItemWith/60f*getItem(position).getTime());
			viewHolder.length.setLayoutParams(lParams);
		}else{
			viewHolder.rightLayout.setVisibility(View.GONE);
			viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.secondsLeft.setVisibility(View.GONE);//TODO
			viewHolder.secondsLeft.setText(Math.round(getItem(position).getTime())+"\"");
			ViewGroup.LayoutParams lParams=viewHolder.lengthLeft.getLayoutParams();
			lParams.width=(int) (mMinItemWith+mMaxItemWith/60f*getItem(position).getTime());
			viewHolder.lengthLeft.setLayoutParams(lParams);
		}
		

		return convertView;
	}

	class ViewHolder {
		TextView seconds;// 时间
		View length;// 对话框长度

        TextView secondsLeft;// 时间
		View lengthLeft;// 对话框长度
		RelativeLayout rightLayout;
		RelativeLayout leftLayout;
	}

}
