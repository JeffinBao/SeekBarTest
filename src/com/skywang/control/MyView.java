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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class MyView extends View {

	private Paint left_line_paint,right_line_paint,round_paint;   
	private PointF point;
	private SeekListener listener;
    private LayoutInflater mLayoutInflater;
    private View mView;
    private TextView mTvProgress;
    
    private int mMax=100;
    private int progress;
    private boolean isSetProgress=false;
    
	interface SeekListener {
		void onSeek(int progress);
	}
	
	public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		
        mLayoutInflater=LayoutInflater.from(context);
        mView=mLayoutInflater.inflate(R.layout.seekbar_hint, null);
        mTvProgress=(TextView)mView.findViewById(R.id.textview_value);
	}

	public MyView(Context context) {
		super(context);
		init();
	}

	private void init() {
		
		point = new PointF();
		
		//default left line paint style
		left_line_paint = new Paint();
		left_line_paint.setColor(Color.WHITE);   
		left_line_paint.setStrokeWidth(1);
		//default right line paint style
		right_line_paint = new Paint();
		right_line_paint.setColor(Color.WHITE);   
		right_line_paint.setStrokeWidth(1);
		
		round_paint = new Paint();
	}
	
	public void setSeekListener(SeekListener listener) {
		this.listener = listener;
	}
	
	public void setTextOverThumb(String str){
		mTvProgress.setText(str);
	}
	
	public String getTextOverThumb(){
		return mTvProgress.getText().toString();
	}
		
	public void setMax(int mMax){
		if(mMax<0)
			return;
		this.mMax=mMax;
	}
	
	public int getMax(){
		return mMax;
	}
	
	public void setProgress(int progress){
		if(progress<0)
			return;
		
		if(progress>mMax){
			progress=mMax;
		}
		
		this.progress=progress;
		isSetProgress=true;
		
	}
	
	public int getProgress(){
		return progress;
	}
	
	public void setLeftLineColor(int color){
		left_line_paint.setColor(color);
	}
	
	public int getLeftLineColor(){
		return left_line_paint.getColor();
	}
	
	public void setRightLineColor(int color){
		right_line_paint.setColor(color);
	}
	
	public int getRightLineColor(){
		return right_line_paint.getColor();
	}
	
	public void setLineWidth(float width){
		left_line_paint.setStrokeWidth(width);
		right_line_paint.setStrokeWidth(width);
	}
	
	public float getLineWidth(){
		return left_line_paint.getStrokeWidth();
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
		
		int h=this.getHeight();
		int w = this.getWidth();
		int y = h / 2;
		int thumb_y=h/2-bitH/2;
		
		float x;
		
		//whether setProgress method is invoked or not
		if(isSetProgress){
			x=(float)progress*(w-bitW)/mMax;
			isSetProgress=false;
		}else{
			x = point.x;
		}
		
		if(x >= w - bitW) {
			x = w - bitW;
		}
		if(x < 0) {
			x = 0;
		}
		
		canvas.drawLine(0, y, x, y, left_line_paint);
		canvas.drawLine(x+bitW, y, w, y, right_line_paint);
		canvas.drawBitmap(bitmap, x, thumb_y, round_paint);
		
		//whether to draw text over thumb or not
		if(mTvProgress!=null){
			Paint textPaint=new Paint();
			textPaint.setColor(Color.WHITE);
			textPaint.setTextSize(36);
			
			float text_posi_x=x+bitW/2;
			int text_posi_y=thumb_y-20;
			
			canvas.drawText(getTextOverThumb(),text_posi_x-20,text_posi_y,textPaint);
		}
		
		progress = (int) ((x / (w-bitW))* mMax);
	
		if(null != listener) {
			//Log.d("DEBUG","progress is "+progress);
			listener.onSeek(progress);
		}
	}
}
