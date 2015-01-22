package com.fanxl.ourtrip;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.fanxl.ourtrip.bean.Person;
import com.fanxl.ourtrip.utils.UtilHelper;

/**
 * 注册页面
 * 
 * @author fanxl
 * 
 */
public class Regisiter extends Activity {

	private Button register_send_again, register_register;
	private EditText regisiter_phoneNumber, regisiter_code, regisiter_username, regisiter_passowrd;
	private Person person;
	private final int CODE_SEND = 105;
	private final int CODE_ERROR = 106;
	private final int SAVE_SUCCESS = 107;
	private final int SAVE_FAIL = 108;
	@SuppressLint("HandlerLeak") 
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CODE_SEND:
				UtilHelper.showMsg(Regisiter.this, "验证码已经发送，请注意查收!");
				break;
			case CODE_ERROR:
				UtilHelper.showMsg(Regisiter.this, "验证码输入错误，请重新输入");
				break;
			case SAVE_SUCCESS:
				UtilHelper.showMsg(Regisiter.this, "创建用户成功!");
				startActivity(new Intent(Regisiter.this, MainActivity.class));
				finish();
				break;
			case SAVE_FAIL:
				UtilHelper.showMsg(Regisiter.this, "创建失败，请重试");
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regisiter);
		//初始化短信验证码
		SMSSDK.initSDK(this, "567bfb28fe68", "5f6a592e86271c8383fc777cfd5047cf");
		initView();
		initVarible();
	}

	private void initVarible() {
		EventHandler eh = new EventHandler() {

			@Override
			public void afterEvent(int event, int result, Object data) {

				if (result == SMSSDK.RESULT_COMPLETE) {
					if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
						// 提交验证码成功
						System.out.println("验证码验证成功,可以提交数据到服务器了");
						savePerson();
					} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
						// 获取验证码成功
						handler.sendEmptyMessage(CODE_SEND);
					} else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
						// 返回支持发送验证码的国家列表
						System.out.println("返回支持发送验证码的国家列表");
					}
				} else {
					((Throwable) data).printStackTrace();
					String errorInfo = ((Throwable) data).getMessage();
					if(errorInfo.contains("520")){
						handler.sendEmptyMessage(CODE_ERROR);
					}
				}
			}
		};
		SMSSDK.registerEventHandler(eh);
		person = new Person();
	}
	
	private void checkPerson(final String phoneNumber, final String code){
		BmobQuery<Person> bmobQuery = new BmobQuery<Person>();
		bmobQuery.addWhereEqualTo("phoneNumber", phoneNumber);
		bmobQuery.findObjects(this, new FindListener<Person>() {
			
			@Override
			public void onSuccess(List<Person> persons) {
				UtilHelper.showMsg(Regisiter.this, phoneNumber+"已经注册，请直接登录或者重新输入手机号码");
			}
			
			@Override
			public void onError(int errorCode, String error) {
				System.out.println("用户不存在可以注册"+error);
				SMSSDK.submitVerificationCode("86", phoneNumber, code);
			}
		});
	}
	
	private void savePerson(){
		person.save(this, new SaveListener() {
			
			@Override
			public void onSuccess() {
				handler.sendEmptyMessage(SAVE_SUCCESS);
			}
			
			@Override
			public void onFailure(int code, String error) {
				handler.sendEmptyMessage(SAVE_FAIL);
			}
		});
	}

	private void initView() {
		register_send_again = (Button) findViewById(R.id.register_send_again);
		register_send_again.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String phoneNumber = getInputPhoneNumber();
				if(phoneNumber==null)return;
				SMSSDK.getVerificationCode("86", phoneNumber);
				countDown();
			}
		});
		regisiter_code = (EditText) findViewById(R.id.regisiter_code);
		regisiter_phoneNumber = (EditText) findViewById(R.id.regisiter_phoneNumber);
		register_register = (Button) findViewById(R.id.register_register);
		register_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String phoneNumber = getInputPhoneNumber();
				if(phoneNumber==null)return;
				String code = regisiter_code.getText().toString().trim();
				if(TextUtils.isEmpty(code)){
					UtilHelper.showMsg(Regisiter.this, "请获取到的验证码!");
					return;
				}
				String username = regisiter_username.getText().toString().trim();
				if(TextUtils.isEmpty(username)){
					UtilHelper.showMsg(Regisiter.this, "用户名不能为空!");
					return;
				}
				String password = regisiter_passowrd.getText().toString().trim();
				if(TextUtils.isEmpty(password)){
					UtilHelper.showMsg(Regisiter.this, "密码不能为空!");
					return;
				}
				
				person.setUserName(username);
				person.setPassWord(password);
				person.setPhoneNumber(phoneNumber);
				checkPerson(phoneNumber, code);
			}
		});
		
		regisiter_username = (EditText) findViewById(R.id.regisiter_username);
		regisiter_passowrd = (EditText) findViewById(R.id.regisiter_passowrd);
	}
	
	private String getInputPhoneNumber(){
		String phoneNumber = regisiter_phoneNumber.getText().toString().trim();
		if(TextUtils.isEmpty(phoneNumber)){
			UtilHelper.showMsg(Regisiter.this, "请填写手机号码!");
			return null;
		}
		if(!TextUtils.isDigitsOnly(phoneNumber) || phoneNumber.length()!=11){
			UtilHelper.showMsg(Regisiter.this, "手机号码填写有误!");
			return null;
		}
		return phoneNumber;
	}

	

	private void countDown() {
		register_send_again.setEnabled(false);
		new CountDownTimer(61 * 1000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				register_send_again.setText("重发(" + millisUntilFinished / 1000
						+ "秒)");
			}

			@Override
			public void onFinish() {
				register_send_again.setText("发送短信验证码");
				register_send_again.setEnabled(true);
			}
		}.start();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		SMSSDK.unregisterAllEventHandler();
	}
}