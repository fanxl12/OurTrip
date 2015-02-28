package com.fanxl.ourtrip;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.fanxl.ourtrip.bean.Person;
import com.fanxl.ourtrip.bean.TripData;
import com.fanxl.ourtrip.utils.UtilHelper;

public class LoginActivity extends Activity implements OnClickListener{
	
	private EditText login_username, login_password;
	public static final int REGISTER = 1;
	public static final int FORGET_PASSWORD = 2;
	private CheckBox login_save_account;
	private SharedPreferences sp;
	public static final String USERNAME= "userName";
	public static final String PASSWORD = "password";
	public static final String ISSAVEACCOUNT = "isSaveAccount";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		initView();
		initVarible();
	}

	private void initVarible() {
		sp = getSharedPreferences("OutTrip", Activity.MODE_PRIVATE);
		if(sp.getBoolean(ISSAVEACCOUNT, false)){
			login_save_account.setChecked(true);
			login_username.setText(sp.getString(USERNAME, ""));
			login_password.setText(sp.getString(PASSWORD, ""));
		}
	}

	private void initView() {
		findViewById(R.id.login_register).setOnClickListener(this);
		findViewById(R.id.login_forget_pw).setOnClickListener(this);
		findViewById(R.id.login_login).setOnClickListener(this);
		
		login_username = (EditText) findViewById(R.id.login_username);
		login_password = (EditText) findViewById(R.id.login_password);
		
		login_save_account = (CheckBox) findViewById(R.id.login_save_account);
	}

	@Override
	public void onClick(View v) {
		Intent intent; 
		switch (v.getId()) {
		case R.id.login_login:
			logion();
			break;
		case R.id.login_register:
			intent = new Intent(LoginActivity.this, Regisiter.class);
			intent.putExtra("type", REGISTER);
			startActivity(intent);
			break;
		case R.id.login_forget_pw:
			intent = new Intent(LoginActivity.this, Regisiter.class);
			intent.putExtra("type", FORGET_PASSWORD);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	
	private void logion(){
		String username = login_username.getText().toString().trim();
		String password = login_password.getText().toString().trim();
		if(TextUtils.isEmpty(username)){
			UtilHelper.showMsg(LoginActivity.this, "用户名不能为空!");
			return;
		}
		if(TextUtils.isEmpty(password)){
			UtilHelper.showMsg(LoginActivity.this, "密码不能为空!");
			return;
		}
		
		BmobQuery<Person> bmobQuery = new BmobQuery<Person>();
		bmobQuery.addWhereEqualTo("userName", username);
		bmobQuery.addWhereEqualTo("passWord", password.length()==32?password:UtilHelper.jdkMD5(password));
		bmobQuery.findObjects(this, new FindListener<Person>() {
			
			@SuppressLint("CommitPrefEdits") @Override
			public void onSuccess(List<Person> persons) {
				if(persons!=null && persons.size()>0){
					Person person = persons.get(0);
					Editor editor = sp.edit();
					if(login_save_account.isChecked()){
						editor.putBoolean(ISSAVEACCOUNT, true);
						editor.putString(USERNAME, person.getUserName());
						editor.putString(PASSWORD, person.getPassWord());
					}else{
						editor.clear();
					}
					editor.commit();
					TripData.getInstance().setUserId(person.getUserId());
					UtilHelper.showMsg(LoginActivity.this, "登录成功!");
					startActivity(new Intent(LoginActivity.this, MainActivity.class));
					finish();
				}else{
					UtilHelper.showMsg(LoginActivity.this, "用户名或者密码不对，请重新输入!");
				}
			}
			
			@Override
			public void onError(int errorCode, String error) {
				UtilHelper.showMsg(LoginActivity.this, "用户名或者密码不对，请重新输入!");
			}
		});
	}
}
