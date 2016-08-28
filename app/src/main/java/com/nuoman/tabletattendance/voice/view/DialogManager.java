package com.nuoman.tabletattendance.voice.view;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nuoman.tabletattendance.R;

public class DialogManager {

	private Dialog mDialog;

	private ImageView mIcon;
	private ImageView mVoice;

	private TextView mLable;

	private Context mContext;

	public DialogManager(Context context) {
		mContext = context;
	}

	public void showRecordingDialog() {

		mDialog = new Dialog(mContext, R.style.Theme_audioDialog);
		mDialog.getWindow().getDecorView().setSystemUiVisibility(8);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.dialog_manager, null);
		mDialog.setContentView(view);
		
		
		mIcon = (ImageView) mDialog.findViewById(R.id.dialog_icon);
		mVoice = (ImageView) mDialog.findViewById(R.id.dialog_voice);
		mLable = (TextView) mDialog.findViewById(R.id.recorder_dialogtext);
		mDialog.show();
		
	}

	/**
	 * 设置正在录音时的dialog界面
	 */
	public void recording() {
		if (mDialog != null && mDialog.isShowing()) {
			mIcon.setVisibility(View.VISIBLE);
			mVoice.setVisibility(View.VISIBLE);
			mLable.setVisibility(View.VISIBLE);

			mIcon.setImageResource(R.drawable.recorder);
			mLable.setText(R.string.shouzhishanghua);
			mLable.setBackgroundColor(Color.TRANSPARENT);

		}
	}

	/**
	 * 取消界面
	 */
	public void wantToCancel() {

		if (mDialog != null && mDialog.isShowing()) {
			mIcon.setVisibility(View.VISIBLE);
			mVoice.setVisibility(View.GONE);
			mLable.setVisibility(View.VISIBLE);

			mIcon.setImageResource(R.drawable.cancel);
			mLable.setText(R.string.want_to_cancle);
			mLable.setBackgroundColor(Color.RED);
		}

	}

	// 时间过短
	public void tooShort() {

		if (mDialog != null && mDialog.isShowing()) {
			mIcon.setVisibility(View.VISIBLE);
			mVoice.setVisibility(View.GONE);
			mLable.setVisibility(View.VISIBLE);

			mIcon.setImageResource(R.drawable.voice_to_short);
			mLable.setText(R.string.tooshort);
		}

	}

	// 隐藏dialog
	public void dimissDialog() {

		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
			mDialog = null;
		}

	}

	public void updateVoiceLevel(int level) {
		if (mDialog != null && mDialog.isShowing()) {

			//先不改变它的默认状态
//			mIcon.setVisibility(View.VISIBLE);
//			mVoice.setVisibility(View.VISIBLE);
//			mLable.setVisibility(View.VISIBLE);

			//通过level来找到图片的id，也可以用switch来寻址，但是代码可能会比较长
			int resId = mContext.getResources().getIdentifier("v" + level,
					"drawable", mContext.getPackageName());
			
			mVoice.setImageResource(resId);
		}

	}

}
