package com.dnlab.groupbuying;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapLocationSettingActivity extends AppCompatActivity implements OnMapReadyCallback{
    private static MapLocationSettingActivity mapLocationSettingActivity;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private Button btn1;
    private FusedLocationSource locationSource;

    String roadAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location_setting);

        MapFragment mapFragment = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.map_view);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map_view, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        locationSource = new FusedLocationSource(this,LOCATION_PERMISSION_REQUEST_CODE);
        btn1 = findViewById(R.id.ok_btn);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) {
                // 권한 거부됨
                Toast.makeText(this, "위치 권한이 거부되어 현재 위치를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        // 현재 위치 버튼 활성화
        naverMap.setLocationSource(locationSource);
        naverMap.getUiSettings().setLocationButtonEnabled(true);

        // 지도 클릭 이벤트 리스너 설정
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MapLocationSettingActivity.this);
                builder.setIcon(R.drawable.logo);
                builder.setMessage("GPS를 설정 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {


                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                LatLng target = naverMap.getCameraPosition().target;
                                double latitude = target.latitude;
                                double longitude = target.longitude;
                                roadAddress = getAddressFromLatLng(latitude,longitude);
                                // 다른 화면으로 전환하는 코드
                                Intent intent = new Intent();
                                intent.putExtra("road_data", getAddressFromLatLng(latitude,longitude));
                                setResult(RESULT_OK,intent);
                                finish();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 취소 버튼 클릭 시 수행할 코드
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        naverMap.setOnMapClickListener((point, coord) -> {
            getAddressFromLatLng(coord.latitude, coord.longitude);
        });
    }

    public String getAddressFromLatLng(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        String roadAddress = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                roadAddress = address.getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return roadAddress;
    }

    private void getLocation(){

    }


}