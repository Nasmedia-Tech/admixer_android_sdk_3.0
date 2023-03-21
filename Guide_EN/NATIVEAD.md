## Native

| Native sample | Native DESC |
|:---:|:---:|
|<img src="https://github.com/Nasmedia-Tech/admixer_aos_sdk_guide/blob/main/images/native.jpg"  width="60%" height="60%"/>|<img src="https://github.com/Nasmedia-Tech/admixer_aos_sdk_guide/blob/main/images/native_desc.png"  width="50%" height="50%"/>|

## 1. Native ad example
- The code below is an example of adding Native ads to RelativeLayout.   
- There are 6 assets for Native ad.   
- Icon / Title / Advertiser / Description / mainView(Image, Video) / CTA   
- One of 3 assets(Title, Icon, mainView) must be used.   
- Precautions   
    - If too much time passes without exposure after successful ad loading, the ad may not be displayed properly   
    - If you do not add a native ad view to the layout, the ad will not be shown.   
    - After a certain amount of time after calling loadAd(), the ad may not be treated as an effective exposure.   

- Refer to the code and sample project below to add ads in any way you like..
```xml
[native_layout_template.xml]
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#f7f7f7">

    <!-- Icon image-->
    <ImageView
        android:id="@+id/iv_icon"
        android:layout_width="90dp"
        android:layout_height="90dp"
        android:layout_alignParentStart="true"
        android:layout_margin="12dp" />

    <!-- Title text -->
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

    <!-- Advertiser text -->
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

    <!-- Description text -->
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

    <!-- nativeMain Image or Video View -->
    <com.nasmedia.admixer.common.nativeads.NativeMainAdView
        android:id="@+id/iv_main"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_below="@+id/tv_desc" />

    <!--CTA button -->
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

        AdInfo adInfo = new AdInfo.Builder(Application.ADUNIT_ID_NATIVE) // adunit id issued by the AdMixer platform
                .isRetry(true)  // ad change setting (true - default). If false, callback immediately after one request
                .build();

        // Pass ID information of xml that defines layout to expose Native ads to SDK.
        // (icon, title, advertiser, description, main(image,video View), cta ...)
        // the asset, one of the titles or iconImages or mainViews, needs to be bound below is a required value must be set.
        NativeAdViewBinder viewBinder = new NativeAdViewBinder.Builder(R.layout.item_320x480)
                .setIconImageId(R.id.iv_icon) // Icon  ID
                .setTitleId(R.id.tv_title)    // Title ID
                .setAdvertiserId(R.id.tv_adv) // Advertiser ID
                .setDescriptionId(R.id.tv_desc) // Description ID
                .setMainViewId(R.id.iv_main)    // main Image or Video ID
                .setCtaId(R.id.btn_cta) // Call to Action button (ex more) ID
                .build();

        nativeAdView = new NativeAdView(this);
        // Please note that hardware Accelerated is set to true for Banner's parent activity to provide a smooth ad serving.
        nativeAdView.setAdInfo(adInfo);
        nativeAdView.setViewBinder(viewBinder); // Native Ad Layout Required Delivery
        nativeAdView.setAdViewListener(new AdListener() {
            @Override
            public void onReceivedAd(Object o) {
                // Success to receive ads
                Toast.makeText(NativeActivity.this, "onReceivedAd", Toast.LENGTH_SHORT).show();
                if(nativeAdView.hasAd) { // Determine if you received an ad successfully
                  container.removeAllViews();
                  container.addView(nativeAdView); // Adding native ad views to the layout
                }
            }

            @Override
            public void onFailedToReceiveAd(Object o, int i, String s) {
                // Fail to receive ads
                Toast.makeText(NativeActivity.this, "onFailedToReceiveAd :" + s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEventAd(Object o, AdEvent adEvent) {
                // Other Events
                Toast.makeText(NativeActivity.this, "onEventAd :" + adEvent.toString(), Toast.LENGTH_SHORT).show();
                switch (adEvent) {
                    case CLICK: // If a click occurs
                    case DISPLAYED: // If an impression occurs
                        break;
                }
            }
        });

        nativeAdView.loadNativeAd(); // Preloading ads
    }

    // Depending on the life cycle, the following settings are essential.
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
