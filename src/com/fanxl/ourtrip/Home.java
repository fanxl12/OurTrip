package com.fanxl.ourtrip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Home extends Fragment{
	
	private View rootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if(rootView!=null){
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if(parent!=null)parent.removeView(rootView);
		}else{
			rootView = inflater.inflate(R.layout.home, container, false);
			initView();
		}
		return rootView;
	}

	private void initView() {
		TextView title_tv_info = (TextView) rootView.findViewById(R.id.title_tv_info);
		title_tv_info.setText("首页");
	}

}
