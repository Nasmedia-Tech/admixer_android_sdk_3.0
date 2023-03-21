## 배너(Banner) 광고 시작하기

| 일반 배너 예시 | 하프 배너 예시
|:---:|:---:|
|<img src="https://github.com/Nasmedia-Tech/admixer_aos_sdk_guide/blob/main/images/banner.png"  width="60%" height="60%"/> |<img src="https://github.com/Nasmedia-Tech/admixer_aos_sdk_guide/blob/main/images/banner_half.png"  width="60%" height="60%"/>

## 1. Banner 광고 추가 예제 (광고 뷰 추가)
- 아래 코드는 Banner 광고를 RelativeLayout 에 추가한 예제 입니다.
- Banner 광고 뷰는 2가지 방법으로 사용 할 수 있습니다.    
  (1) 광고 요청 후 받은 즉시 노출   
  (2) 광고 요청 후 받은 뒤, 원하는 시점에 노출   
- (1) 배너 광고 요청 후 받은 즉시 노출을 원할 시, container.addView(banner); 사용
- (2) 배너 광고 요청 후 받은 뒤, 원하는 시점에 노출을 원할 시   
   (2-1) banner.loadAd(); 후   
   (2-2) onReceivedAd 이벤트 에서 if (banner.hasAd) {  // 광고를 성공적으로 받았는지 판단   
   (2-3) container.removeView(banner); // 기존에 있으면 배너 광고 뷰가 있으면 제거   
   (2-4) container.addView(banner);   // 레이아웃에 배너 광고 뷰를 추가   
   (2-5) banner.showAd();  // 호출하여 원하는 시점에 광고 노출   

 - 유의사항
   - 광고 로딩이 성공한 이후 노출하지 않고 지나치게 많은 시간이 지나가면, showAd() 을 호출 했을 때, 제대로 광고가 표시되지 않을 수 있습니다.
   - 레이아웃에 배너 광고뷰를 추가하지 않으면, 광고가 표시되지 않습니다.
   - showAd() 을 호출하지 않으면, 광고가 표시되지 않습니다.
   - loadAd() 을 호출하고 일정 시간이 지나면 광고가 노출되어도 유효 노출로 처리되지 않을 수 있습니다.

- 아래 코드 및 샘플 프로젝트를 참조하여 원하는 방법으로 광고를 추가하십시오.

```java
[BannerActivity.java]
public class BannerActivity extends AppCompatActivity {

    private RelativeLayout container;
    private AdView banner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        container = findViewById(R.id.container_banner);

        AdInfo adInfo = new AdInfo.Builder(Application.ADUNIT_ID_BANNER) // AdMixer 플랫폼에서 발급받은 배너 ADUNIT_ID
                .isRetry(true) // 광고 재요청 설정 (true - 기본값), false 시, 1회 요청 후 바로 Callback
                .build();

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARERNT, ViewGroup.LayoutParams.WRAP_CONTENT);

        banner = new AdView(this);
        banner.setLayoutParams(params);
        // 이 때 설정하신 Banner 의 부모 activity 는 원활한 광고 제공을 위해 hardwareAccelerated 가 true 설정되오니 참고 부탁드립니다.
        banner.setAdInfo(adInfo);
        banner.setAlwaysShowAdView(false); // 광고 로딩 전에도 영역을 차지할 것인지 설정(false – 기본값)
        banner.setAdViewListener(new AdListener() {
            @Override
            public void onReceivedAd(Object o) {
                // 광고 수신 성공
                Toast.makeText(BannerActivity.this, "onReceivedAd", Toast.LENGTH_SHORT).show();

                // [2번 방법] 광고를 받은 뒤, 원하는 시점에 노출
                // if (banner.hasAd) {  // 광고를 성공적으로 받았는지 판단
                //    container.removeView(banner);
                //    container.addView(banner); // 레이아웃에 배너 광고 뷰를 추가
                //    banner.showAd(); // showAd() 를 이용하여, 원하는 시점에 노출 가능
                // }
            }

            @Override
            public void onFailedToReceiveAd(Object o, int i, String s) {
                // 광고 수신 실패
                Toast.makeText(BannerActivity.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEventAd(Object o, AdEvent adEvent) {
                // 기타 이벤트
                Toast.makeText(BannerActivity.this, adEvent.toString(), Toast.LENGTH_SHORT).show();
                switch(adEvent) {
                  case CLICK: // 클릭 시
                  case DISPLAYED: // 노출 시
                      break;                
              }  
            }
        });

        // [1번 방법] 광고 요청 후 받은 즉시 노출
        container.addView(banner);

        // [2번 방법] 광고 요청 후 광고를 받은 뒤, 원하는 시점에 노출
        // banner.loadAd(); // 광고를 미리 로드한다
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

```

## 2. Banner 광고 추가 예제 (Layout 파일 이용 방법)
- 아래 코드는 Banner 광고를 RelativeLayout 에 추가한 예제 입니다.
- Layout 파일 이용 방법 사용시, 광고 요청 후 받은 즉시 노출합니다.

- 아래 코드 및 샘플 프로젝트를 참조하여 원하는 방법으로 광고를 추가하십시오.
```xml
[activit_banner2.xml]
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#f2a8ca">

    <com.nasmedia.admixer.ads.AdView
        android:id="@+id/banner2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentStart="true"
        android:layout_alignParentEnd="true"
        android:layout_alignParentBottom="true" />

</RelativeLayout>
```
```java
[Banner2Activity.java]
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

```

| 전면 배너 예시 | 전면 팝업 배너 예시
|:---:|:---:|
|<img src="https://github.com/Nasmedia-Tech/admixer_aos_sdk_guide/blob/main/images/interstitial_basic.png"  width="60%" height="60%"/>|<img src="https://github.com/Nasmedia-Tech/admixer_aos_sdk_guide/blob/main/images/interstitial_popup.png"  width="60%" height="60%"/>

## 3. Interstitial Banner (전면광고) 추가 예제

- Interstitial 광고 뷰는 2가지 방법으로 사용 할 수 있습니다.     
 (1) 전면 광고 요청 후 받은 즉시 노출   
 (2) 전면 광고 요청 후 받은 뒤, 원하는 시점에 노출   
- (1) 전면 광고 요청 후 받은 즉시 노출을 원할 시, interstitialAd.startInterstitial(); 사용   
- (2) 전면 광고 요청 후 받은 뒤, 원하는 시점에 노출을 원할 시   
  (2-1) interstitialAd.loadInterstitial(); 후    
  (2-2) onReceivedAd 이벤트를 받은 뒤, 원하는 시점에 if(interstitialAd.hasInterstitial) // 전면 광고를 성공적으로 받았는지 판단.   
  (2-3) interstitialAd.showInterstitial(); // 전면 광고 노출    

- Interstitial 광고는 2가지 형태로 제공됩니다. (일반형 Basic, 팝업형 Popup)   
- 팝업형 전면광고 소재 사이즈는 일반형 전면광고 소재 사이즈의 85% 축소된 비율로 노출됩니다.
- 하단의 버튼 영역 사이즈는 팝업형 전면광고 소재 비율 대비 12% 비율로 노출됩니다.
- 팝업형의 경우 아래의 설정을 추가하여 사용 할 수 있습니다.
```java
PopupInterstitialAdOption adConfig = new PopupInterstitialAdOption();
// [팝업형 전면광고] 노출 상태에서 뒤로가기 버튼 방지 (true : 비활성화, false : 활성화)
adConfig.setDisableBackKey(false);
// 디폴트로 제공되며, 광고를 닫는 기능이 적용되는 버튼 (버튼문구, 버튼색상)
adConfig.setButtonLeft("광고종료", "#234234");
// 설정시에만 노출되는 옵션버튼이며, 앱을 종료하는 기능이 적용되는 버튼. 미설정 시 위 광고닫기 버튼만 노출
adConfig.setButtonRight("오른쪽버튼", null);
// 버튼영역 색상지정
adConfig.setButtonFrameColor(null);

...
AdInfo adInfo = new AdInfo.Builder(Application.adUnitId_interstitialBanner) // AdMixer 플랫폼에서 발급받은 전면 배너 ADUNIT_ID
        ...
        .popupAdOption(adConfig) // [팝업형 전면광고] 사용 시, 설정
        .interstitialAdType(AdInfo.InterstitialAdType.Popup)
        ...
        .build();
...

```

- 유의사항
  - 광고 로딩이 성공한 이후 지나치게 많은 시간이 지나가면, showInterstitial() 을 호출 했을 때, 제대로 광고가 표시되지 않을 수 있습니다.
  - showInterstitial() 을 호출하지 않으면, 광고가 표시되지 않습니다.
  - loadInterstitial() 을 호출하고 일정 시간이 지나면 광고가 노출되어도 유효 노출로 처리되지 않을 수 있습니다.

- 아래 코드 및 샘플 프로젝트를 참조하여 원하는 방법으로 광고를 추가하십시오.

```java
[InterstitialActivity.java]
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
		// 전면광고 초기화는 반드시 Activity 또는 Activity Context로 선언 해 주세요
        interstitialAd = new InterstitialAd(this);

        // [아래 설정은 AdInfo.InterstitialAdType.Popup 을 사용 했을 때 원하시는 조건만 추가하시면 됩니다.]
        // [팝업형 전면광고] 추가옵션 (Basic : 일반전면, Popup : 버튼이 있는 팝업형 전면)
        PopupInterstitialAdOption adConfig = new PopupInterstitialAdOption();
        // [팝업형 전면광고] 노출 상태에서 뒤로가기 버튼 방지 (true : 비활성화, false : 활성화)
        adConfig.setDisableBackKey(false);
        // 디폴트로 제공되며, 광고를 닫는 기능이 적용되는 버튼 (버튼문구, 버튼색상)
        adConfig.setButtonLeft("광고종료", "#234234");
        // 설정시에만 노출되는 옵션버튼이며, 앱을 종료하는 기능이 적용되는 버튼. 미설정 시 위 광고닫기 버튼만 노출
        adConfig.setButtonRight("오른쪽버튼", null);
        // 버튼영역 색상지정
        adConfig.setButtonFrameColor(null);

        AdInfo adInfo = new AdInfo.Builder(Application.ADUNIT_ID_INTERSTITIAL_BANNER) // AdMixer 플랫폼에서 발급받은 전면 배너 ADUNIT_ID
                .interstitialTimeout(0)  // 초 단위로 전면 광고 타임아웃 설정 (기본값 : 0, 0 이면 서버지정 시간으로 처리, 서버지정 시간 : 20s)
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



```
