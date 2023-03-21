package com.nasmedia.admixer.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nasmedia.admixer.ads.AdEvent;
import com.nasmedia.admixer.ads.AdInfo;
import com.nasmedia.admixer.ads.AdListener;
import com.nasmedia.admixer.ads.InterstitialAd;
import com.nasmedia.admixer.ads.PopupInterstitialAdOption;

public class InterstitialActivity extends AppCompatActivity {

    Button btnInterstitialShow;
    InterstitialAd interstitialAd;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);

        progressBar = findViewById(R.id.loading_bar);
        btnInterstitialShow = findViewById(R.id.btn_interstitial_show);
        interstitialAd = new InterstitialAd(this);

        // [아래 설정은 AdInfo.InterstitialAdType.Popup 을 사용 했을 때 원하시는 조건만 추가하시면 됩니다.]
        // [팝업형 전면광고] 추가옵션 (Basic : 일반전면, Popup : 버튼이 있는 팝업형 전면)
        PopupInterstitialAdOption adConfig = new PopupInterstitialAdOption();
        // [팝업형 전면광고] 노출 상태에서 뒤로가기 버튼 방지 (true : 비활성화, false : 활성화)
        adConfig.setDisableBackKey(false);
        // 디폴트로 제공되며, 광고를 닫는 기능이 적용되는 버튼 (버튼문구, 버튼색상)
        adConfig.setButtonLeft("광고종료", null);
        // 설정시에만 노출되는 옵션버튼이며, 앱을 종료하는 기능이 적용되는 버튼. 미설정 시 위 광고닫기 버튼만 노출
        adConfig.setButtonRight("오른쪽버튼", null);
        // 버튼영역 색상지정
        adConfig.setButtonFrameColor(null);

        AdInfo adInfo = new AdInfo.Builder(Application.ADUNIT_ID_INTERSTITIAL_BANNER) // AdMixer 플랫폼에서 발급받은 전면 배너 ADUNIT_ID
                .interstitialTimeout(0)  // 초단위로 전면 광고 타임아웃 설정 (기본값 : 0, 0 이면 서버지정 시간으로 처리, 서버지정 시간 : 20s)
                .maxRetryCountInSlot(-1) // 리로드 시간 내에 반복 횟수(-1 : 무한, 0 : 반복 없음, n : n번 반복)
                .isUseBackgroundAlpha(true) // 반투명처리 여부 (true: 반투명, false: 처리안함) / 기본값 : true
                .isRetry(true) // 광고 재요청 설정 (true - 기본값), false 시, 1회 요청 후 바로 Callback
                .popupAdOption(adConfig) // [팝업형 전면광고] 사용 시 설정
                .interstitialAdType(AdInfo.InterstitialAdType.Popup) // (default : AdInfo.InterstitialAdType.Basic)
                .build();

        interstitialAd = new InterstitialAd(this);
        // 이 때 설정하신 Interstitial 의 부모 activity 는 원활한 광고 제공을 위해 hardwareAccelerated 가 true 설정되오니 참고 부탁드립니다.
        interstitialAd.setAdInfo(adInfo);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onReceivedAd(Object o) {
                // 광고 수신 성공
                Toast.makeText(InterstitialActivity.this, "onReceivedAd", Toast.LENGTH_SHORT).show();
                btnInterstitialShow.setText("전면광고보기");
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailedToReceiveAd(Object o, int i, String s) {
                // 광고 수신 실패
                Toast.makeText(InterstitialActivity.this, s, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onEventAd(Object o, AdEvent adEvent) {
                // 기타 이벤트
                Toast.makeText(InterstitialActivity.this, "onEventAd :" + adEvent.toString(), Toast.LENGTH_SHORT).show();
                switch (adEvent) {
                    case LEFT_CLICK: // [팝업형 전면, AdInfo.InterstitialAdType.Popup] 왼쪽 버튼 클릭
                    case RIGHT_CLICK: // [팝업형 전면, AdInfo.InterstitialAdType.Popup] 오른쪽 버튼 클릭
                                      // 오른쪽 클릭의 경우, 다양한 환경에서 종료가 가능하기 때문에 퍼블리셔가 직접 설정하게 되어 있습니다.
                                      // 일반적으로 finish() 호출
                    case CLOSE: // 광고 창이 닫혔을 때
                        interstitialAd.closeInterstitial();
                        btnInterstitialShow.setText("전면광고요청");
                        break;
                    case DISPLAYED: // 광고 노출
                        break;
                }
            }
        });

        // [1번 방법] 전면 광고 요청 후 받은 즉시 노출
        // interstitialAd.startInterstitial();

        // [2번 방법] 전면 광고 요청 후 원하는 시점에 노출
        btnInterstitialShow.setOnClickListener(v -> {
            if (interstitialAd.hasInterstitial)
                interstitialAd.showInterstitial();
            else {
                interstitialAd.loadInterstitial();
                progressBar.setVisibility(View.VISIBLE);
            }

        });
    }

    // 생명주기에 따라 아래 설정이 반드시 필요합니다.
    @Override
    protected void onDestroy() {
        if (interstitialAd != null) {
            interstitialAd.stopInterstitial();
            interstitialAd = null;
        }
        super.onDestroy();
    }
}
