package com.fanxl.ourtrip;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import com.fanxl.ourtrip.adapter.AddFrendsAdapter;
import com.fanxl.ourtrip.bean.Friend;
import com.fanxl.ourtrip.bean.MyLocation;
import com.fanxl.ourtrip.bean.Person;
import com.fanxl.ourtrip.bean.TripData;
import com.fanxl.ourtrip.utils.UtilHelper;

public class AddFriends extends Activity{
	
	private EditText add_et_username;
	private ListView add_lv_friends;
	private AddFrendsAdapter adapter;
	private OnClickListener addClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Person person = (Person) v.getTag();
			addFriends(person);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_friends);
		initView();
	}

	private void initView() {
		add_et_username = (EditText) findViewById(R.id.add_et_username);
		findViewById(R.id.add_bt_find).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String username = add_et_username.getText().toString().trim();
				if(TextUtils.isEmpty(username)){
					UtilHelper.showMsg(AddFriends.this, "用户名不能为空!");
					return;
				}
				findFriendsByUserName(username);
			}
		});
		add_lv_friends = (ListView) findViewById(R.id.add_lv_friends);
		adapter = new AddFrendsAdapter(new ArrayList<Person>(), this, addClickListener);
		add_lv_friends.setAdapter(adapter);
	}

	/**
	 * 根据用户名查找好友
	 */
	private void findFriendsByUserName(String userName) {
		BmobQuery<Person> bmobQuery = new BmobQuery<Person>();
		bmobQuery.addWhereContains("userName", userName);
		bmobQuery.findObjects(this, new FindListener<Person>() {

			@Override
			public void onSuccess(List<Person> persons) {
				if (persons != null && persons.size() > 0) {
					adapter.setDatas(persons);
				} else {
					UtilHelper.showMsg(AddFriends.this, "未找到此用户名的用户！");
				}
			}

			@Override
			public void onError(int errorCode, String error) {
				UtilHelper.showMsg(AddFriends.this, error);
			}
		});
	}
	
	private void addFriends(final Person person){
		
		BmobQuery<MyLocation> bmobQuery = new BmobQuery<MyLocation>();
		bmobQuery.addWhereEqualTo("userId", person.getUserId());
		bmobQuery.findObjects(this, new FindListener<MyLocation>() {

			@Override
			public void onSuccess(List<MyLocation> locations) {
				if(locations!=null && locations.size()>0){
					Friend friend = new Friend();
					MyLocation location = locations.get(0);
					friend.setmLatitude(location.getmLatitude());
					friend.setmLongtitude(location.getmLongtitude());
					friend.setAddress(location.getAddress());
					TripData tripData = TripData.getInstance();
					friend.setFriendName(person.getUserName());
					friend.setFriendObjectId(person.getObjectId());
					friend.setUserId(tripData.getUserId());
					friend.save(AddFriends.this, new SaveListener() {

						@Override
						public void onSuccess() {
							UtilHelper.showMsg(AddFriends.this, "好友添加成功！");
						}

						@Override
						public void onFailure(int code, String error) {
							
						}
					});
					
//					Friend otherFriend = new Friend();
//					otherFriend.setmLatitude(tripData.getmLatitude());
//					otherFriend.setmLongtitude(tripData.getmLongtitude());
//					otherFriend.setAddress(tripData.getAddress());
//					otherFriend.setFriendName(tripData.getUserName());
//					otherFriend.setFriendObjectId(tripData.getObjectId());
//					otherFriend.setUserId(person.getUserId());
//					otherFriend.save(AddFriends.this, new SaveListener() {
//
//						@Override
//						public void onSuccess() {
//							UtilHelper.showMsg(AddFriends.this, "好友添加成功！");
//						}
//
//						@Override
//						public void onFailure(int code, String error) {
//							
//						}
//					});
					
					
				}
			}

			@Override
			public void onError(int errorCode, String error) {
				
			}
		});
	}

}
