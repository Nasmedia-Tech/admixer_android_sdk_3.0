## Banner

| Banner sample | half banner sample
|:---:|:---:|
|<img src="https://github.com/Nasmedia-Tech/admixer_android_sdk_3.0/blob/main/images/banner.png"  width="60%" height="60%"/> |<img src="https://github.com/Nasmedia-Tech/admixer_android_sdk_3.0/blob/main/images/banner_half.png"  width="60%" height="60%"/>

## 1. Banner ad example (Adding Ad view)   
- The code below is an example of adding Banner ads to RelativeLayout.   
- Banner Ad View is available in two ways:.       
  (1) Immediate exposure received after ad request   
  (2) After receiving an ad request, expose it to the desired point in time   
- (1) If you want immediate exposure after requesting banner ads, you can use the code below,   
  container.addView(banner);   
- (2) If you want to be exposed at the desired time after receiving a banner ad request   
  (2-1) banner.loadAd();   
  (2-2) In the onReceivedAd event, if(banner.hasAd) { // Determine if you received an ad successfully   
  (2-3) container.removeView(banner); // Remove existing banner ad views if present   
  (2-4) container.addView(banner);   // Adding banner ad views to the layout   
  (2-5) banner.showAd();  // Call to expose ads at any point in time   

- precautions   
    - If too much time passes without exposure after successful ad loading, the ad may not be displayed properly when showAd() is called.   
    - If you do not add a banner ad view to the layout, the ad will not be shown.   
    - If you do not call showAd(), the ad is not shown.   
    - After a certain amount of time after calling loadAd(), the ad may not be treated as an effective exposure.   
- Refer to the code and sample project below to add ads in any way you like.   

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

        AdInfo adInfo = new AdInfo.Builder(Application.ADUNIT_ID_BANNER)  // adunit id issued by the AdMixer platform
                .isRetry(true) // ad change setting (true - default). If false, callback immediately after one request
                .build();

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARERNT, ViewGroup.LayoutParams.WRAP_CONTENT);

        banner = new AdView(this);
        banner.setLayoutParams(params);
        // Please note that hardware Accelerated is set to true for Banner's parent activity to provide a smooth ad serving.
        banner.setAdInfo(adInfo);
        banner.setAlwaysShowAdView(false); // Set whether to occupy area even before ad loading (false – default)
        banner.setAdViewListener(new AdListener() {
            @Override
            public void onReceivedAd(Object o) {
                // Success to receive ads
                Toast.makeText(BannerActivity.this, "onReceivedAd", Toast.LENGTH_SHORT).show();

                // [2nd method] to be exposed at the desired time after receiving a banner ad request
                // if (banner.hasAd) {  // Determine if you received an ad successfully
                //    container.removeView(banner);
                //    container.addView(banner); // Adding banner ad views to the layout
                //    banner.showAd(); // Use showAd() to expose at any point in time
                // }
            }

            @Override
            public void onFailedToReceiveAd(Object o, int i, String s) {
                // Fail to receive ads
                Toast.makeText(BannerActivity.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEventAd(Object o, AdEvent adEvent) {
                // Other Events
                Toast.makeText(BannerActivity.this, adEvent.toString(), Toast.LENGTH_SHORT).show();
                switch(adEvent) {
                  case CLICK: // If a click occurs
                  case DISPLAYED: // If an impression occurs
                      break;                
              }  
            }
        });

        // [1st method] Immediate exposure received after ad request
        container.addView(banner);

        // [2nd method] to be exposed at the desired time after receiving a banner ad request
        // banner.loadAd(); // Preloading ads
    }


    // Depending on the life cycle, the following settings are essential.
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

## 2. Banner ad example (How to Use Layout Files)
- The code below is an example of adding Banner ads to RelativeLayout.   
- When using the layout file method, it is shown as soon as it is received after the ad.   

- Refer to the code and sample project below to add ads in any way you like..
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

        AdInfo adInfo = new AdInfo.Builder(Application.ADUNIT_ID_BANNER) // adunit id issued by the AdMixer platform
                .isRetry(true) // ad change setting (true - default). If false, callback immediately after one request
                .build();

        banner = findViewById(R.id.banner2);
        // Please note that hardware Accelerated is set to true for Banner's parent activity to provide a smooth ad serving..
        banner.setAdInfo(adInfo);
        banner.setAlwaysShowAdView(false);  // Set whether to occupy area even before ad loading (false – default)
        banner.setAdViewListener(new AdListener() {
            @Override
            public void onReceivedAd(Object o) {
                // Success to receive ads
                Toast.makeText(Banner2Activity.this, "onReceivedAd", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailedToReceiveAd(Object o, int i, String s) {
                // Fail to receive ads
                Toast.makeText(Banner2Activity.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEventAd(Object o, AdEvent adEvent) {
                // Other Events
                Toast.makeText(Banner2Activity.this, adEvent.toString(), Toast.LENGTH_SHORT).show();
                switch(adEvent) {
                  case CLICK: // If a click occurs
                  case DISPLAYED: // If an impression occurs
                      break;                
                }
            }
        });
    }

    // Depending on the life cycle, the following settings are essential.
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

| Interstitial banner sample | Interstitial PopUP banner sample
|:---:|:---:|
|<img src="https://github.com/Nasmedia-Tech/admixer_aos_sdk_guide/blob/main/images/interstitial_basic.png"  width="60%" height="60%"/>|<img src="https://github.com/Nasmedia-Tech/admixer_aos_sdk_guide/blob/main/images/interstitial_popup.png"  width="60%" height="60%"/>

## 3. Interstitial Banner ad example

- Interstitial Banner Ad View is available in two ways:        
  (1) Immediate exposure received after ad request   
  (2) After receiving an ad request, expose it to the desired point in time   
- (1) If you want immediate exposure after requesting banner ads, you can use the code below,   
  interstitialAd.startInterstitial();   
- (2) If you want to be exposed at the desired time after receiving a banner ad request       
  (2-1) interstitialAd.loadInterstitial();       
  (2-2) In the onReceivedAd event, at the desired time, check if you have received a interstitial ad successfully using the code below   
  if(interstitialAd.hasInterstitial)   
  (2-3) interstitialAd.showInterstitial(); // Interstitial ad shown   
     
- Interstitial banner is available in two forms: (Basic, Popup)   
- Popup interstitial banner’s material sizes are exposed at a reduced rate of 85% of the size of the basic interstitial banner’s material.   
- The button area size at the bottom is exposed at a rate of 12% compared to the popup interstitial banner’s material.   
- For popup types, you can add the settings below to use them.   
```java
PopupInterstitialAdOption adConfig = new PopupInterstitialAdOption();
// [Popup interstitial banner] Prevent the back button from being exposed (true: disabled; false: enabled)
adConfig.setDisableBackKey(false);
// A button provided by default and applied with the ability to close the advertisement (Button phrase, button color)
adConfig.setButtonLeft("Close ad", "#234234");
// It is an option button that is exposed only at the time of setting, and the function of shutting down the app is applied. Expose only the Close Above Ad button when not set
adConfig.setButtonRight("right button", null);
// Specify button area color
adConfig.setButtonFrameColor(null);

...
AdInfo adInfo = new AdInfo.Builder(Application.adUnitId_interstitialBanner) // Interstitial adunit id issued by the AdMixer platform
        ...
        .popupAdOption(adConfig) // [Popup interstitial banner] when using, setting up
        .interstitialAdType(AdInfo.InterstitialAdType.Popup)
        ...
        .build();
...

```
- Precautions
    - If too much time passes without exposure after successful ad loading, the ad may not be displayed properly when showInterstitial() is called.
    - If you do not call showInterstitial(), the ad is not shown.
    - After a certain amount of time after calling loadInterstitial(), the ad may not be treated as an effective exposure.
- Refer to the code and sample project below to add ads in any way you like.

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
        interstitialAd = new InterstitialAd(this);

        // [If you use AdInfo.InteractiveAdType.Popup, you can add the conditions you want]
        // [Popup interstitial banner] option (Basic : Interstitial, Popup : Popup with buttons)
        PopupInterstitialAdOption adConfig = new PopupInterstitialAdOption();
        // [Popup interstitial banner] Prevent the back button from being exposed (true: disabled; false: enabled)
        adConfig.setDisableBackKey(false);
        // A button provided by default and applied with the ability to close the advertisement (Button phrase, button color)
        adConfig.setButtonLeft("Close ad", "#234234");
        // It is an option button that is exposed only at the time of setting, and the function of shutting down the app is applied. Expose only the Close Above Ad button when not set
        adConfig.setButtonRight("right button", null);
        // Specify button area color
        adConfig.setButtonFrameColor(null);

        AdInfo adInfo = new AdInfo.Builder(Application.adUnitId_interstitialBanner) // Interstitial adunit id issued by the AdMixer platform
                .interstitialTimeout(0)  // Set the Interstitial ad timeout in seconds (default: 0, 0 is treated as server designated time, server designated time: 20s)
                .maxRetryCountInSlot(-1) // Number of iterations within the reload time (-1 : infinity, 0 : no iterations, n : n iterations)
                .isUseBackgroundAlpha(true) // Translucent treatment (true: translucent, false: not processed) / Default: true
                .isRetry(true) // ad change setting (true - default). If false, callback immediately after one request
                .popupAdOption(adConfig) // [[Popup interstitial banner] when using, setting up
                .interstitialAdType(AdInfo.InterstitialAdType.Popup) // (default : AdInfo.InterstitialAdType.Basic)
                .build();

        interstitialAd = new InterstitialAd(this);
        // Please note that hardware Accelerated is set to true for Banner's parent activity to provide a smooth ad serving  
        interstitialAd.setAdInfo(adInfo);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onReceivedAd(Object o) {
                // Success to receive ads
                Toast.makeText(InterstitialActivity.this, "onReceivedAd", Toast.LENGTH_SHORT).show();
                btnInterstitialShow.setText("show interstitial ad");
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailedToReceiveAd(Object o, int i, String s) {
                // Fail to receive ads
                Toast.makeText(InterstitialActivity.this, s, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onEventAd(Object o, AdEvent adEvent) {
                // Other Events
                Toast.makeText(InterstitialActivity.this, "onEventAd :" + adEvent.toString(), Toast.LENGTH_SHORT).show();
                switch (adEvent) {
                    case LEFT_CLICK: // [Popup, AdInfo.InterstitialAdType.Popup] click left button
                    case RIGHT_CLICK: // [Popup, AdInfo.InterstitialAdType.Popup] click right button    
                                      // For right-clicks, the publisher is supposed to set it up because it can be shut down in various environments.
                                      // Typically call finish()
                    case CLOSE: // When the ad closes
                        interstitialAd.closeInterstitial();
                        btnInterstitialShow.setText("request interstitial ad");
                        break;
                    case DISPLAYED: // ad show
                        break;
                }
            }
        });

        // [1st method] Immediate exposure received after ad request
        // interstitialAd.startInterstitial();

        // [2nd method] to be exposed at the desired time after receiving an ad request
        btnInterstitialShow.setOnClickListener(v -> {
            if (interstitialAd.hasInterstitial)
                interstitialAd.showInterstitial();
            else {
                interstitialAd.loadInterstitial();
                progressBar.setVisibility(View.VISIBLE);
            }

        });
    }

    // Depending on the life cycle, the following settings are essential.
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
