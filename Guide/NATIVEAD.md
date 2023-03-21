## 네이티브(Native) 광고 시작하기

| 네이티브 예시 | 네이티브 설명 |
|:---:|:---:|
|<img src="https://github.com/Nasmedia-Tech/admixer_aos_sdk_guide/blob/main/images/native.jpg"  width="60%" height="60%"/>|<img src="https://github.com/Nasmedia-Tech/admixer_aos_sdk_guide/blob/main/images/native_desc.png"  width="50%" height="50%"/>|

## 1. Native 광고 추가 예제
- 아래 코드는 Native 광고를 RelativeLayout 에 추가한 예제 입니다.
- Native 광고는 6가지의 asset 이 있습니다.
- 아이콘(icon) / 제목(title) / 광고주(advertiser) / 설명(description) / 메인(mainView(Image, Video)) / 버튼(cta)
- 제목(title), 아이콘(icon), 메인(mainView) 3개 중 1개는 반드시 필수로 사용하셔야 합니다.
- 유의사항
  - 광고 로딩이 성공한 이후 노출하지 않고 지나치게 많은 시간이 지나가면, 제대로 광고가 표시되지 않을 수 있습니다.
  - 레이아웃에 네이티브 광고뷰를 추가하지 않으면, 광고가 표시되지 않습니다.
  - 광고 로딩이 성공한 이후 노출하지 않고 일정 시간이 지나면 광고가 노출되어도 유효 노출로 처리되지 않을 수 있습니다.

- 아래 코드 및 샘플 프로젝트를 참조하여 원하는 방법으로 광고를 추가하십시오.
```xml
[native_layout_template.xml]
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#f7f7f7">

    <!-- 아이콘(icon) 이미지-->
    <ImageView
        android:id="@+id/iv_icon"
        android:layout_width="90dp"
        android:layout_height="90dp"
        android:layout_alignParentStart="true"
        android:layout_margin="12dp" />

    <!-- 제목(title) 텍스트 -->
    <TextView
        android:id="@+id/tv_title"
        android:layout_width="match_parent"
        android:layout_height="68dp"
        android:layout_marginStart="12dp"
        android:layout_marginTop="19dp"
        android:layout_toEndOf="@id/iv_icon"
        android:lineSpacingExtra="2sp"
        android:singleLine="false"
        android:text="Title .. "
        android:textSize="15sp"
        android:textStyle="bold" />

    <!-- 광고주(advertiser) 텍스트 -->
    <TextView
        android:id="@+id/tv_adv"
        android:layout_width="match_parent"
        android:layout_height="45dp"
        android:layout_below="@id/tv_title"
        android:layout_marginStart="11dp"
        android:layout_toEndOf="@id/iv_icon"
        android:lineSpacingExtra="1sp"
        android:text="Advertiser"
        android:textColor="#c7c7c7"
        android:textSize="11sp" />

    <!-- 설명(description) 텍스트 -->
    <TextView
        android:id="@+id/tv_desc"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_below="@id/iv_icon"
        android:layout_marginStart="11dp"
        android:minHeight="64dp"
        android:singleLine="false"
        android:text="Description"
        android:textColor="#2a2a2a"
        android:textSize="11sp" />

    <!-- 메인(nativeMain Image or Video) View -->
    <com.nasmedia.admixer.common.nativeads.NativeMainAdView
        android:id="@+id/iv_main"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_below="@+id/tv_desc" />

    <!-- 버튼(cta) 버튼 -->
    <Button
        android:id="@+id/btn_cta"
        android:layout_width="match_parent"
        android:layout_height="56dp"
        android:layout_below="@+id/iv_main"
        android:layout_marginTop="11dp"
        android:layout_marginBottom="12dp"
        android:background="#2a2a2a"
        android:textColor="#ffffff" />

</RelativeLayout>
```

```java
[NativeActivity.java]

public class NativeActivity extends AppCompatActivity {

    private NativeAdView nativeAdView;
    private RelativeLayout container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);
        container = findViewById(R.id.container_native);

        AdInfo adInfo = new AdInfo.Builder(Application.ADUNIT_ID_NATIVE) // AdMixer 플랫폼에서 발급받은 배너 ADUNIT_ID;
                .isRetry(true)  // 광고 재요청 설정 (true - 기본값), false 시, 1회 요청 후 바로 Callback
                .build();

        // Native 광고를 노출할, Layout 을 정의한 xml 의 ID 정보를 SDK 에 전달합니다.
        // (icon, title, advertiser, description, main(image,video View), cta ...)
        // 아래 binding 해야 할 asset 중 title or iconImage or mainView 중 1개는 반드시 설정해야 하는 필수값 입니다.
        NativeAdViewBinder viewBinder = new NativeAdViewBinder.Builder(R.layout.item_320x480)
                .setIconImageId(R.id.iv_icon) // 아이콘 ID
                .setTitleId(R.id.tv_title)    // 제목(타이틀) ID
                .setAdvertiserId(R.id.tv_adv) // 광고주 ID
                .setDescriptionId(R.id.tv_desc) // 설명 ID
                .setMainViewId(R.id.iv_main)    // 메인 이미지 또는 동영상 ID
                .setCtaId(R.id.btn_cta) // Call to Action 버튼 (ex 더보기) ID
                .build();

        nativeAdView = new NativeAdView(this);
        // 이 때 설정하신 Native 의 부모 activity 는 원활한 광고 제공을 위해 hardwareAccelerated 가 true 설정되오니 참고 부탁드립니다.
        nativeAdView.setAdInfo(adInfo);
        nativeAdView.setViewBinder(viewBinder); // 네이티브 광고 레이아웃 필수 전달
        nativeAdView.setAdViewListener(new AdListener() {
            @Override
            public void onReceivedAd(Object o) {
                // 광고 수신 성공
                Toast.makeText(NativeActivity.this, "onReceivedAd", Toast.LENGTH_SHORT).show();
                if(nativeAdView.hasAd) { // 광고를 성공적으로 받았는지 판단
                  container.removeAllViews();
                  container.addView(nativeAdView); // 레이아웃에 네이티브 광고를 추가
                }
            }

            @Override
            public void onFailedToReceiveAd(Object o, int i, String s) {
                // 광고 수신 실패
                Toast.makeText(NativeActivity.this, "onFailedToReceiveAd :" + s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEventAd(Object o, AdEvent adEvent) {
                // 기타 이벤트
                Toast.makeText(NativeActivity.this, "onEventAd :" + adEvent.toString(), Toast.LENGTH_SHORT).show();
                switch (adEvent) {
                    case CLICK: // 클릭 시
                    case DISPLAYED: // 노출 시
                        break;
                }
            }
        });

        nativeAdView.loadNativeAd(); // 네이티브 광고 미리 로드
    }

    // 생명주기에 따라 아래 설정이 반드시 필요합니다.
    @Override
    protected void onResume() {
        if (nativeAdView != null) {
            nativeAdView.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (nativeAdView != null) {
            nativeAdView.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (nativeAdView != null) {
            nativeAdView.onDestroy();
            nativeAdView = null;
        }
        super.onDestroy();
    }
}
```
