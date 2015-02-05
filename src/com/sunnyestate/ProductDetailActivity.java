package com.sunnyestate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sunnyestate.data.CategoryDataDetail;
import com.sunnyestate.data.ShoppingCar;
import com.sunnyestate.db.DBUtils;
import com.sunnyestate.popwindow.ProduceDetailRightPopwindow;
import com.sunnyestate.popwindow.ProduceDetailRightPopwindow.OnlistOnclick;
import com.sunnyestate.task.ConfirmDialog;
import com.sunnyestate.utils.Constants;
import com.sunnyestate.utils.DialogUtil;
import com.sunnyestate.utils.HttpUrlHelper;
import com.sunnyestate.utils.SharedUtils;
import com.sunnyestate.utils.ToastUtil;
import com.sunnyestate.utils.Utils;

import fynn.app.PromptDialog;

public class ProductDetailActivity extends BaseActivity {
	private ImageView img_back;
	private ImageView img_car;
	private ImageView img_more;
	private ImageView img_dec;
	private ImageView img_inc;
	private EditText edit_count;
	private WebView wb;
	private Button btn_add_car;
	private Button btn_buy;

	private Dialog dialog;

	private ProduceDetailRightPopwindow pop;

	private CategoryDataDetail detail = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail);
		detail = (CategoryDataDetail) getIntent().getSerializableExtra(
				"categorydatadetail");
		initView();
	}

	private void initView() {
		btn_buy = (Button) findViewById(R.id.btn_buy);
		btn_add_car = (Button) findViewById(R.id.btn_add_car);
		img_dec = (ImageView) findViewById(R.id.img_dec);
		img_inc = (ImageView) findViewById(R.id.img_inc);
		edit_count = (EditText) findViewById(R.id.edit_count);
		img_back = (ImageView) findViewById(R.id.img_back);
		img_car = (ImageView) findViewById(R.id.img_car);
		img_more = (ImageView) findViewById(R.id.img_more);
		wb = (WebView) findViewById(R.id.webView1);
		wb.setWebChromeClient(new WebViewClient());
		wb.loadUrl("http://www.sunnyestate.cn/Wapi/detail/id/" + detail.getId()
				+ "/showtype/phone" + HttpUrlHelper.getUrlParams());
		dialog = DialogUtil.createLoadingDialog(this);
		dialog.show();
		setListener();
	}

	private void setListener() {
		img_back.setOnClickListener(this);
		img_car.setOnClickListener(this);
		img_more.setOnClickListener(this);
		img_dec.setOnClickListener(this);
		img_inc.setOnClickListener(this);
		btn_add_car.setOnClickListener(this);
		btn_buy.setOnClickListener(this);

	}

	private class WebViewClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				if (dialog != null) {
					dialog.dismiss();
				}
			}
			super.onProgressChanged(view, newProgress);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			finishThisActivity();
			break;
		case R.id.img_more:
			showPop(v);
			break;
		case R.id.img_dec:
			changeCount(v.getId());
			break;
		case R.id.img_inc:
			changeCount(v.getId());
			break;
		case R.id.btn_add_car:
			if (detail == null) {
				return;
			}
			int count = Integer.valueOf(edit_count.getText().toString());
			if (count == 0) {
				ToastUtil.showToast("至少购买换一个产品", Toast.LENGTH_SHORT);
				return;
			}
			ShoppingCar car = new ShoppingCar();
			car.setShopping_id(detail.getId());
			car.setImg_url(detail.getDefaultimg());
			car.setPrice(detail.getOriginalprice());
			car.setTitle(detail.getProducttile());
			car.setCount(count);
			car.setMember_price(detail.getPrice());
			car.write(DBUtils.getDBsa(2));
			sendBroadcast(new Intent(Constants.ADD_SHOPPING_CAR).putExtra(
					"shoppingcar", car));
			ToastUtil.showToast("已加入到购物车", Toast.LENGTH_SHORT);

			break;
		case R.id.btn_buy:
			if ("".equals(SharedUtils.getPasswordKey())) {
				promptLoginDialog();
				return;
			}
			if (detail == null) {
				return;
			}
			int buycount = Integer.valueOf(edit_count.getText().toString());
			if (buycount == 0) {
				ToastUtil.showToast("至少购买换一个产品", Toast.LENGTH_SHORT);
				return;
			}
			ShoppingCar car1 = new ShoppingCar();
			car1.setShopping_id(detail.getId());
			car1.setImg_url(detail.getDefaultimg());
			car1.setPrice(detail.getPrice());
			car1.setTitle(detail.getProducttile());
			car1.setPrice(detail.getPrice());
			car1.setMember_price(detail.getOriginalprice());
			car1.setCount(buycount);
			List<ShoppingCar> lists = new ArrayList<ShoppingCar>();
			lists.add(car1);
			startActivity(new Intent(this, FillOrderActivity.class).putExtra(
					"datadetail", (Serializable) lists));
			Utils.leftOutRightIn(this);
			break;
		default:
			break;
		}
	}

	private void promptLoginDialog() {
		PromptDialog.Builder dialog = DialogUtil.confirmDialog(this,
				"您还没有登录,请先登录在继续操作", "登录", "取消", new ConfirmDialog() {

					@Override
					public void onOKClick() {
						startActivity(new Intent(ProductDetailActivity.this,
								LoginAndRegisterActivity.class));
						Utils.leftOutRightIn(ProductDetailActivity.this);
					}

					@Override
					public void onCancleClick() {

					}
				});
		dialog.show();

	}

	private void changeCount(int id) {
		int count = Integer.valueOf(edit_count.getText().toString());
		if (id == R.id.img_inc) {
			edit_count.setText((count + 1) + "");
		} else if (id == R.id.img_dec) {
			if (count == 0) {
				return;
			}
			edit_count.setText((count - 1) + "");
		}
	}

	private void showPop(View v) {
		pop = new ProduceDetailRightPopwindow(this, v);
		pop.setOnlistOnclick(new OnlistOnclick() {

			@Override
			public void onclick(int position) {
				if (position == 0) {
					sendBroadcast(new Intent(Constants.RETURN_HOME));
					finishThisActivity();
				}
			}
		});
		pop.show();

	}
}
