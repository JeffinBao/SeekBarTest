package com.skywang.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View {

	private Paint left_line_paint, right_line_paint, round_paint;   
	private PointF point;
	private SeekListener listener;
	
	public MyView(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MyView(Context context) {
		super(context);
		init();
	}
	
	public void setSeekListener(SeekListener listener) {
		this.listener = listener;
	}

	private void init() {
		point = new PointF();
		
		left_line_paint = new Paint();
		left_line_paint.setColor(Color.WHITE);   
		left_line_paint.setStrokeJoin(Paint.Join.ROUND);   
		left_line_paint.setStrokeCap(Paint.Cap.ROUND);   
		left_line_paint.setStrokeWidth(1);
		
//		right_line_paint = new Paint();
//		right_line_paint.setColor(Color.BLUE);   
//		right_line_paint.setStrokeJoin(Paint.Join.ROUND);   
//		right_line_paint.setStrokeCap(Paint.Cap.ROUND);   
//		right_line_paint.setStrokeWidth(1);
		
		round_paint = new Paint();
	}
	
	 @Override   
     public boolean onTouchEvent(MotionEvent event) {   
		 point.x = event.getX();
		 point.y = event.getY();
         invalidate();
         return true;   
     } 
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.seekbar_thumb);
		int bitW = bitmap.getWidth();
		int bitH = bitmap.getHeight();
		int y = bitH / 2;
		
		int w = this.getWidth();
		Log.d("DEBUG","w is "+w);
		Log.d("DEBUG","bitW is "+bitW);
		
		int left_line = 0;
		int right_line = w-bitW;
		float x = point.x;
		
		if(x >= w - bitW) {
			x = w - bitW;
		}
		
		if(x < 0) {
			x = 0;
		}
		
		if(point.x <= 0) {
			left_line = 0;
			right_line = bitW;
		} else {
			right_line = (int) (x + bitW);
		}
		
		canvas.drawLine(left_line, y, x, y, left_line_paint);
		canvas.drawLine(x+bitW, y, w, y, left_line_paint);
		
		canvas.drawBitmap(bitmap, x, 0, round_paint);
		
		Log.e("msg", x + " , " + (x / (w-bitW))* 100);
		if(null != listener) {
			int progress = (int) ((x / (w-bitW))* 100);
			Log.d("DEBUG","progress is "+progress);
			listener.onSeek(progress);
//			listener.onSeek((int) (x == 0 ? x : progress));
		}
	}
	
	interface SeekListener {
		void onSeek(int progress);
	}
	
}
