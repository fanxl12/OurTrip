package com.fanxl.ourtrip;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.fanxl.ourtrip.utils.FastBlur;

public class MyCenter extends Fragment implements OnClickListener{
	
	private View rootView;
	private LinearLayout center_ll_header;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if(rootView!=null){
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if(parent!=null)parent.removeView(rootView);
		}else{
			rootView = inflater.inflate(R.layout.my_center, container, false);
			initView();
		}
		
		return rootView;
	}

	private void initView() {
		center_ll_header = (LinearLayout) rootView.findViewById(R.id.center_ll_header);
		center_ll_header.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener(){

			@Override
			public boolean onPreDraw() {
				center_ll_header.getViewTreeObserver().removeOnPreDrawListener(this);
				center_ll_header.buildDrawingCache();

                //Bitmap bmp = center_ll_header.getDrawingCache();
				Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.header);
				//bmp.createBitmap(width, height, Bitmap.Config.ARGB_8888);
				System.out.println("原图的宽:"+bmp.getWidth()+"---高:"+bmp.getHeight());
                blur(bmp, center_ll_header);
                return true;
			}
			
		});
		
		rootView.findViewById(R.id.center_friend_location).setOnClickListener(this);
		rootView.findViewById(R.id.center_add_friend).setOnClickListener(this);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN) 
	protected void blur(Bitmap bkg, View view) {
		float scaleFactor = 8;
        float radius = 2;

        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth()/scaleFactor),
                (int) (view.getMeasuredHeight()/scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft()/scaleFactor, -view.getTop()/scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);
        overlay = FastBlur.doBlur(overlay, (int)radius, true);
        view.setBackground(new BitmapDrawable(getResources(), overlay));
        System.out.println("处理之后的宽:"+overlay.getWidth()+"----高:"+overlay.getHeight());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.center_friend_location:
			startActivity(new Intent(getActivity(), MapActivity.class));
			break;
		case R.id.center_add_friend:
			startActivity(new Intent(getActivity(), AddFriends.class));
			break;
		default:
			break;
		}
	}
}
