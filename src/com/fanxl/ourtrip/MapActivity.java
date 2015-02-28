package com.fanxl.ourtrip;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.fanxl.ourtrip.MyOrientationListener.OnOrientationListener;
import com.fanxl.ourtrip.bean.Info;
import com.fanxl.ourtrip.bean.MyLocation;
import com.fanxl.ourtrip.bean.TripData;
import com.fanxl.ourtrip.utils.UtilHelper;

public class MapActivity extends Activity {
	
	private MapView mMapView = null;
	private BaiduMap mBaiduMap;
	private boolean isFistIn = true;
	private Context mContext;
	
	//定位相关
	private LocationClient mLocationClient;
	private MyLocationListener myLocationListener;
	
	//当前位置的经纬度
	private double mLatitude;
	private double mLongtitude;
	
	//自定义定位图标
	private BitmapDescriptor mIconLocation;
	private MyOrientationListener orientationListener;
	private float mCurrentX;
	private LocationMode mLocationMode;
	
	//覆盖物相关
	private BitmapDescriptor mMarker;
	private RelativeLayout mMarkerLy;
	
	//保存位置到服务器
	private MyLocation myLocation;
	private final int GET_LOCATION_SUCCESS = 100;
	
	@SuppressLint("HandlerLeak") 
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_LOCATION_SUCCESS:
				updateLocation();
				break;

			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.map_activity);
        this.mContext=this;
        initView();
        //初始化定位
        initLoaction();
        
        initMarker();
	}

	private void initMarker() {
		mMarker = BitmapDescriptorFactory.fromResource(R.drawable.maker);
		
		mMarkerLy = (RelativeLayout) findViewById(R.id.id_maker_ly);
		
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				
				Bundle extraInfo = marker.getExtraInfo();
				Info info = (Info) extraInfo.getSerializable("info");
				ImageView iv = (ImageView) mMarkerLy
						.findViewById(R.id.id_info_img);
				TextView distance = (TextView)findViewById(R.id.id_info_distance);
				TextView name = (TextView) mMarkerLy
						.findViewById(R.id.id_info_name);
				TextView zan = (TextView) mMarkerLy
						.findViewById(R.id.id_info_zan);
				iv.setImageResource(info.getImgId());
				distance.setText(info.getDistance());
				name.setText(info.getName());
				zan.setText(info.getZan() + "");
				
				InfoWindow infoWindow;
				TextView tv = new TextView(mContext);
				tv.setText(info.getName());
				tv.setTextColor(Color.WHITE);
				tv.setPadding(30, 20, 30, 50);
				tv.setBackgroundResource(R.drawable.location_tips);
				
				
				BitmapDescriptor bitView = BitmapDescriptorFactory.fromView(tv);
				
				final LatLng latLng = marker.getPosition();
				Point p = mBaiduMap.getProjection().toScreenLocation(latLng);
				p.y -= 47;
				LatLng ll = mBaiduMap.getProjection().fromScreenLocation(p);
				infoWindow = new InfoWindow(bitView, ll, 0, new OnInfoWindowClickListener() {
					
					@Override
					public void onInfoWindowClick() {
						mBaiduMap.hideInfoWindow();
					}
				});
				mBaiduMap.showInfoWindow(infoWindow);
				mMarkerLy.setVisibility(View.VISIBLE);
				return true;
			}
		});
		
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			
			@Override
			public boolean onMapPoiClick(MapPoi mapPoi) {
				return false;
			}
			
			@Override
			public void onMapClick(LatLng latLing) {
				mMarkerLy.setVisibility(View.GONE);
				mBaiduMap.hideInfoWindow();
			}
		});
	}

	private void initLoaction() {
		
		myLocation = new MyLocation();
		myLocation.setUserId(TripData.getInstance().getUserId());
		
		mLocationMode = LocationMode.NORMAL;
		
		mLocationClient = new LocationClient(this);
		myLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(myLocationListener);
		
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		option.setScanSpan(1000);
		mLocationClient.setLocOption(option);
		//初始化自定义定位图标
		mIconLocation = BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);
		
		orientationListener = new MyOrientationListener(mContext);
		orientationListener.setOnOrientationListener(new OnOrientationListener() {
			
			@Override
			public void onOrientationChanged(float x) {
				mCurrentX = x;
			}
		});
	}

	private void initView() {
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
		mBaiduMap.setMapStatus(msu);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
        mMapView.onResume();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		mBaiduMap.setMyLocationEnabled(true);
		if(!mLocationClient.isStarted())mLocationClient.start();
		//开启方向传感器
		orientationListener.start();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		mBaiduMap.setMyLocationEnabled(false);
		mLocationClient.stop();
		orientationListener.stop();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		//在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
        mMapView.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
        mMapView.onDestroy(); 
	}
	
	private class MyLocationListener implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation location) {
			
			mLatitude = location.getLatitude();
			mLongtitude = location.getLongitude();
			
			myLocation.setmLatitude(mLatitude);
			myLocation.setmLongtitude(mLongtitude);
			
			MyLocationData data = new MyLocationData.Builder()
			.direction(mCurrentX)
			.accuracy(location.getRadius())
			.longitude(mLongtitude)
			.latitude(mLatitude).build();
			
			mBaiduMap.setMyLocationData(data);
			//设置自定义图标
			MyLocationConfiguration config = new MyLocationConfiguration(mLocationMode, true, mIconLocation);
			mBaiduMap.setMyLocationConfigeration(config);
			
			if(isFistIn){
				location();
				UtilHelper.showMsg(mContext, location.getAddrStr());
				isFistIn = false;
			}
			
			getObjectId();
		}

		
	}
	
	/**
	 * 定位到我的位置
	 */
	private void location() {
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(new LatLng(mLatitude, mLongtitude));
		mBaiduMap.animateMapStatus(msu);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.id_map_common:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			break;
		case R.id.id_map_site:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			break;
		case R.id.id_map_traffic:
			if(mBaiduMap.isTrafficEnabled()){
				mBaiduMap.setTrafficEnabled(false);
				item.setTitle("实时交通(on)");
			}else{
				mBaiduMap.setTrafficEnabled(true);
				item.setTitle("实时交通(off)");
			}
			break;
		case R.id.id_map_location:
			location();
			break;
		case R.id.id_map_mode_common:
			mLocationMode = LocationMode.NORMAL;
			break;
		case R.id.id_map_mode_following:
			mLocationMode = LocationMode.FOLLOWING;
			break;
		case R.id.id_map_mode_compass:
			mLocationMode = LocationMode.COMPASS;
			break;
		case R.id.id_add_overlay:
			addOverlay(Info.infos);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 添加覆盖物
	 * @param infos
	 */
	private void addOverlay(List<Info> infos) {
		
		mBaiduMap.clear();
		LatLng latLng = null;
		Marker marker = null;
		OverlayOptions options;
		
		for (Info info : infos) {
			// 经纬度
			latLng = new LatLng(info.getLatitude(), info.getLongitude());
			// 图标
			options = new MarkerOptions().position(latLng).icon(mMarker).zIndex(5);
			marker = (Marker) mBaiduMap.addOverlay(options);
			Bundle bundle = new Bundle();
			bundle.putSerializable("info", info);
			marker.setExtraInfo(bundle);
		}
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.animateMapStatus(msu);
	}
	
	private void getObjectId(){
		if(TripData.getInstance().getLocationObjectId()==null){
			getLocationObjectId();
		}else{
			handler.sendEmptyMessage(GET_LOCATION_SUCCESS);
		}
	}
	
	private void updateLocation(){
		myLocation.update(this, TripData.getInstance().getLocationObjectId(), new UpdateListener() {
			
			@Override
			public void onSuccess() {
				
			}
			
			@Override
			public void onFailure(int errorCode, String error) {
				
			}
		});
	}
	
	private void getLocationObjectId(){
		BmobQuery<MyLocation> bmobQuery = new BmobQuery<MyLocation>();
		bmobQuery.addWhereEqualTo("userId", TripData.getInstance().getUserId());
		bmobQuery.findObjects(this, new FindListener<MyLocation>() {
			
			@Override
			public void onSuccess(List<MyLocation> myLocations) {
				if(myLocations!=null && myLocations.size()>0){
					TripData.getInstance().setUserId(myLocations.get(0).getObjectId());
					handler.sendEmptyMessage(GET_LOCATION_SUCCESS);
				}
			}
			
			@Override
			public void onError(int errorCode, String error) {
				
			}
		});
		
	}

}
