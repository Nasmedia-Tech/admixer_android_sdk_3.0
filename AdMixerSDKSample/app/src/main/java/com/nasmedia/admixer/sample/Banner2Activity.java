package com.nasmedia.admixer.sample;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nasmedia.admixer.ads.AdEvent;
import com.nasmedia.admixer.ads.AdInfo;
import com.nasmedia.admixer.ads.AdListener;
import com.nasmedia.admixer.ads.AdView;

public class Banner2Activity extends AppCompatActivity {

    private AdView banner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner2);

        AdInfo adInfo = new AdInfo.Builder(Application.ADUNIT_ID_BANNER) // AdMixer 플랫폼에서 발급받은 배너 ADUNIT_ID;
                .isRetry(true) // 광고 재요청 설정 (true - 기본값), false 시, 1회 요청 후 바로 Callback
                .build();

        banner = findViewById(R.id.banner2);
        // 이 때 설정하신 Banner 의 부모 activity 는 원활한 광고 제공을 위해 hardwareAccelerated 가 true 설정되오니 참고 부탁드립니다.
        banner.setAdInfo(adInfo);
        banner.setAlwaysShowAdView(false);   // 광고 로딩 전에도 영역을 차지할 것인지 설정(false – 기본값)
        banner.setAdViewListener(new AdListener() {
            @Override
            public void onReceivedAd(Object o) {
                // 광고 수신 성공
                Toast.makeText(Banner2Activity.this, "onReceivedAd", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailedToReceiveAd(Object o, int i, String s) {
                // 광고 수신 실패
                Toast.makeText(Banner2Activity.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEventAd(Object o, AdEvent adEvent) {
                // 기타 이벤트
                Toast.makeText(Banner2Activity.this, adEvent.toString(), Toast.LENGTH_SHORT).show();
                switch(adEvent) {
                  case CLICK: // 클릭 시
                  case DISPLAYED: // 노출 시
                      break;
                }
            }
        });
    }

    // 생명주기에 따라 아래 설정이 반드시 필요합니다.
    @Override
    protected void onResume() {
        if (banner != null) {
            banner.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (banner != null) {
            banner.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (banner != null) {
            banner.onDestroy();
        }
        super.onDestroy();
    }
}
