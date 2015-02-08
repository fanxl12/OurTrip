package com.fanxl.ourtrip.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.fanxl.ourtrip.R;

/**
 * 自定义圆角图片显示控件
 * @author fanxl
 *
 */
public class CornerImageView extends View{
	
	private int type;
	private final int TYPE_CIRCLE = 0;
	private final int TYPE_ROUND = 1;
	
	private Bitmap mSrc;
	private int mRadius;
	/** 
	 * 控件的宽度 
	 */  
	private int mWidth;  
	/** 
	 * 控件的高度 
	 */  
	private int mHeight;  
	
	public CornerImageView(Context context) {
		this(context,null);
	}

	public CornerImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray td = context.obtainStyledAttributes(attrs, R.styleable.CornerImageView);
		int n = td.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = td.getIndex(i);
			switch (attr) {
			case R.styleable.CornerImageView_src:
				mSrc = BitmapFactory.decodeResource(getResources(), td.getResourceId(attr, 0));
				break;
			case R.styleable.CornerImageView_type:
				type = td.getInt(attr, 0);
				break;
			case R.styleable.CornerImageView_borderRadius:
				mRadius = (int) td.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10F, getResources().getDisplayMetrics()));
				break;

			default:
				break;
			}
		}
		td.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int specSize = MeasureSpec.getSize(widthMeasureSpec);
		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		
		if(specMode==MeasureSpec.EXACTLY){
			mWidth = specSize;
		}else{
			int desireByImg = getPaddingLeft()+getPaddingRight()+mSrc.getWidth();
			if(specMode==MeasureSpec.AT_MOST){
				mWidth = Math.min(desireByImg, specSize);
			}else{
				mWidth = desireByImg;
			}
		}
		
		specSize = MeasureSpec.getSize(heightMeasureSpec);
		specMode = MeasureSpec.getMode(heightMeasureSpec);
		if(specMode == MeasureSpec.EXACTLY){
			mHeight = specSize;
		}else{
			int desireByImg = getPaddingBottom()+getPaddingTop()+mSrc.getHeight();
			if(specMode==MeasureSpec.AT_MOST){
				mHeight = Math.min(specSize, desireByImg);
			}else{
				mHeight = desireByImg;
			}
		}
		
		setMeasuredDimension(mWidth, mHeight);
	}
	
	@SuppressLint("DrawAllocation") @Override
	protected void onDraw(Canvas canvas) {
		
		switch (type) {
		case TYPE_CIRCLE:
			int min = Math.min(mWidth, mHeight);
			System.out.println("我画过圆了");
			mSrc = Bitmap.createScaledBitmap(mSrc, min, min, false);
			canvas.drawBitmap(createCircleImage(mSrc, min), 0, 0, null);
			break;
		case TYPE_ROUND:
			canvas.drawBitmap(createRoundConerImage(mSrc), 0, 0, null);
			break;

		default:
			break;
		}
	}

	/**
	 * 根据原图和变长绘制圆形图片
	 * 
	 * @param source
	 * @param min
	 * @return
	 */
	private Bitmap createCircleImage(Bitmap source, int min){
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_8888);
		/**
		 * 产生一个同样大小的画布
		 */
		Canvas canvas = new Canvas(target);
		/**
		 * 首先绘制圆形
		 */
		canvas.drawCircle(min / 2, min / 2, min / 2, paint);
		/**
		 * 使用SRC_IN，参考上面的说明
		 */
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		/**
		 * 绘制图片
		 */
		canvas.drawBitmap(source, 0, 0, paint);
		return target;
	}
	
	
	/**
	 * 根据原图添加圆角
	 * 
	 * @param source
	 * @return
	 */
	private Bitmap createRoundConerImage(Bitmap source){
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(mWidth, mHeight, Config.ARGB_8888);
		Canvas canvas = new Canvas(target);
		RectF rect = new RectF(0, 0, source.getWidth(), source.getHeight());
		canvas.drawRoundRect(rect, mRadius, mRadius, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(source, 0, 0, paint);
		return target;
	}

}
