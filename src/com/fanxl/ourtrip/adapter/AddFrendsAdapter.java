package com.fanxl.ourtrip.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanxl.ourtrip.R;
import com.fanxl.ourtrip.bean.Person;
import com.fanxl.ourtrip.utils.ViewHolder;

public class AddFrendsAdapter extends BaseAdapter {

	private List<Person> datas;
	private Context mContext;
	private OnClickListener addClickListener;

	public AddFrendsAdapter() {
	};

	public AddFrendsAdapter(List<Person> datas, Context mContext, OnClickListener addClickListener) {
		this.datas = datas;
		this.mContext = mContext;
		this.addClickListener=addClickListener;
	}

	public void setDatas(List<Person> datas) {
		this.datas = datas;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.item_friends, null);
		}
		
		Person person = datas.get(position);
		TextView item_username = ViewHolder.get(convertView, R.id.item_friend_username);
		ImageView item_sex = ViewHolder.get(convertView, R.id.item_friend_sex);
		TextView item_province = ViewHolder.get(convertView, R.id.item_friend_province);
		Button item_add = ViewHolder.get(convertView, R.id.item_friends_add);
		item_username.setText(person.getUserName());
		item_add.setTag(person);
		item_add.setOnClickListener(addClickListener);
		return convertView;
	}

}
