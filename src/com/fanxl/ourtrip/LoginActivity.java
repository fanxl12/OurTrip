package com.fanxl.ourtrip;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.fanxl.ourtrip.bean.Person;
import com.fanxl.ourtrip.utils.UtilHelper;

public class LoginActivity extends Activity implements OnClickListener{
	
	private EditText login_username, login_password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		initView();
	}

	private void initView() {
		findViewById(R.id.login_register).setOnClickListener(this);
		findViewById(R.id.login_forget_pw).setOnClickListener(this);
		findViewById(R.id.login_login).setOnClickListener(this);
		
		login_username = (EditText) findViewById(R.id.login_username);
		login_password = (EditText) findViewById(R.id.login_password);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_login:
			logion();
			break;
		case R.id.login_register:
			startActivity(new Intent(LoginActivity.this, Regisiter.class));
			break;
		case R.id.login_forget_pw:
			int num = 0;
			System.out.println(10/num);
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
		bmobQuery.addWhereEqualTo("passWord", password);
		bmobQuery.findObjects(this, new FindListener<Person>() {
			
			@Override
			public void onSuccess(List<Person> persons) {
				if(persons!=null && persons.size()>0){
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
