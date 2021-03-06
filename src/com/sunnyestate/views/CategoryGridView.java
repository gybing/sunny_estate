package com.sunnyestate.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.GridView;

import com.sunnyestate.R;

public class CategoryGridView extends GridView {
	private Paint paint;

	public CategoryGridView(Context context) {
		super(context);
	}

	public CategoryGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		paint.setColor(context.getResources().getColor(R.color.gridview_line));
		paint.setStrokeWidth((float) 1);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		int count = getChildCount();
		int columns = getNumColumns();
		// 计算行数
		int yu = count % columns;
		int row = count / columns;
		if (yu != 0) {
			row = row + 1;
		}
		int width = getWidth();
		int height = getHeight();
		for (int y = 0; y < height; y += height / row) {
			if (y == 0 || y > height - row)// 第一条和最后一条不画
				continue;
			canvas.drawLine(0, y, width, y, paint);// 划横线
		}
		super.dispatchDraw(canvas);
	}

}
