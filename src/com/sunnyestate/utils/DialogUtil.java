package com.sunnyestate.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunnyestate.R;
import com.sunnyestate.task.ConfirmDialog;

import fynn.app.PromptDialog;

public class DialogUtil {
	public static Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
		RelativeLayout layout = (RelativeLayout) v
				.findViewById(R.id.dialog_view);// 加载布局
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
		// 加载动画
		Animation animation = AnimationUtils.loadAnimation(context,
				R.anim.load_animation);
		animation.setDuration(1000);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(animation);
		tipTextView.setText(msg);// 设置加载信息
		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
		// loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setContentView(
				layout,
				new LinearLayout.LayoutParams(
						Utils.getSecreenWidth(context) / 3, Utils
								.getSecreenWidth(context) / 3));// 设置布局

		return loadingDialog;

	}

	public static Dialog createLoadingDialog(Context context) {
		return createLoadingDialog(context, "正在加载");
	}

	/**
	 * 确认对话框
	 * 
	 * @param context
	 * @param title
	 * @param content
	 */
	public static PromptDialog.Builder confirmDialog(Context context,
			String title, String content, String txtOk, String txtCancle,
			final ConfirmDialog callBack) {

		PromptDialog.Builder dialog = new PromptDialog.Builder(context);
		if ("".equals(title)) {
			dialog.setTitle("提示");
		} else {
			dialog.setTitle(title);
		}
		dialog.setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR);
		dialog.setMessage(content);
		dialog.setButton1(txtOk, new PromptDialog.OnClickListener() {

			@Override
			public void onClick(Dialog dialog, int which) {
				dialog.dismiss();
				callBack.onOKClick();

			}
		});
		if (!"".equals(txtCancle)) {
			dialog.setButton2(txtCancle, new PromptDialog.OnClickListener() {

				@Override
				public void onClick(Dialog dialog, int which) {
					dialog.dismiss();
					callBack.onCancleClick();

				}
			});
		}

		return dialog;
	}

	public static PromptDialog.Builder confirmDialog(Context context,
			String content, String txtOk, String txtCancle,
			final ConfirmDialog callBack) {
		return confirmDialog(context, "", content, txtOk, txtCancle, callBack);
	}
}
