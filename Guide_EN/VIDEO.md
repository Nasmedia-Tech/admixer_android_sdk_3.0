## Video

|                                                 Video1 sample                                                | Video2 sample
|:------------------------------------------------------------------------------------------------------:|:---:|
| <img src="https://github.com/Nasmedia-Tech/admixer_android_sdk_3.0/blob/main/images/video.jpg"  width="100%" height="100%"/> |<img src="https://github.com/Nasmedia-Tech/admixer_android_sdk_3.0/blob/main/images/video2.jpg"  width="100%" height="100%"/>

- The code below is an example of adding VideoAdView to RelativeLayout after receiving a video ad.
- Refer below, to be exposed a video ad at the desired time after receiving a video ad.   
  (1) videoAdView.loadAd(); // Load a Video Ad
  
  (2) container.removeView(videoAdView); // Remove a existing VideoAdView if it exist   
  
  (3) container.addView(videoAdView, params);   // Add VideoAdView to Layout   
  
  (4) Expose a Video ad
- Precautions
    - If too much time passes without exposure after successful ad loading, the ad may not be displayed properly when addView() is called.
    - After a certain amount of time after calling loadAd(), the ad may not be treated as an effective exposure.

- Refer to the code and sample project below to add ads in any way you like..
```xml
[activity_video.xml]
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#f2a8ca">

    <RelativeLayout
        android:id="@+id/container_video"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="#000000"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent">

        <TextView
            android:id="@+id/tv_complete"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerInParent="true"
            android:gravity="center"
            android:text="Video Play Complete"
            android:textColor="#FFFFFF"
            android:visibility="gone" />

    </RelativeLayout>


</androidx.constraintlayout.widget.ConstraintLayout>
```

```java
public class VideoActivity extends AppCompatActivity {

    private VideoAdView videoAdView;
    private RelativeLayout container;
    private TextView tvComplete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        container = findViewById(R.id.container_video);
        tvComplete = findViewById(R.id.tv_complete);

        AdInfo adInfo = new AdInfo.Builder(Application.adUnitId_video) // Video adunit id issued by the AdMixer platform
                .isRetry(true) // ad change setting (true - default). If false, callback immediately after one request
                .build();

        videoAdView = new VideoAdView(this);
        // Please note that hardware Accelerated is set to true for Video's parent activity to provide a smooth ad serving.
        videoAdView.setAdInfo(adInfo, this);
        videoAdView.setAdViewListener(new AdListener() {
            @Override
            public void onReceivedAd(Object o) {
                // Success to receive ads
                Toast.makeText(VideoActivity.this, "onReceivedAd", Toast.LENGTH_SHORT).show();
                // Expose a Video ad.
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                container.removeView(videoAdView);
                container.addView(videoAdView, params); // Add VideoAdView to Layout [ Expose a Video ad ]
            }

            @Override
            public void onFailedToReceiveAd(Object o, int i, String s) {
                // Fail to receive ads
                Toast.makeText(VideoActivity.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEventAd(Object o, AdEvent adEvent) {
                // Other Events
                Toast.makeText(VideoActivity.this, adEvent.toString(), Toast.LENGTH_SHORT).show();
                switch (adEvent) {
                    case COMPLETION: // If the video play end
                    case SKIPPED:    // If a Skip button click event occurs
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(videoAdView.getWidth(), videoAdView.getHeight());
                        params.addRule(RelativeLayout.CENTER_IN_PARENT);
                        tvComplete.setLayoutParams(params);
                        tvComplete.setVisibility(View.VISIBLE);
                        break;
                    case CLICK: // If a CTA button click event occurs
                        break;
                }
            }
        });

        // to be exposed to a video ad at the desired time after receiving a video ad.  
        videoAdView.loadAd(); // Load a Video Ad
    }

    // Depending on the life cycle, the following settings are essential.
    @Override
    protected void onResume() {
        if (videoAdView != null) {
            videoAdView.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (videoAdView != null) {
            videoAdView.onPause();
        }

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (videoAdView != null) {
            videoAdView.onDestroy();
            videoAdView = null;
        }

        super.onDestroy();
    }
}
```
<br/><br/>

## Interstitial Video
|                                                 Interstitial Video1 Sample                                             | Interstitial Video2 Sample
|:------------------------------------------------------------------------------------------------------:|:---:|
| <img src="https://github.com/Nasmedia-Tech/admixer_android_sdk_3.0/blob/main/images/interstitial_video.jpg"  width="60%" height="60%"/> |<img src="https://github.com/Nasmedia-Tech/admixer_android_sdk_3.0/blob/main/images/interstitial_video_end.jpg"  width="60%" height="60%"/>
- The code below is an example of Interstitial Video Ad View
- Refer below, to be exposed to a Interstitialvideo ad at the desired time after receiving a video ad.
  
  (1) interstitialVideoAdView.loadInterstitialVideoAd(); Load a Interstitial Video Ad 
  
  (2) After receiving the onReceivedAd event, at the desired time if you have received a interstitial ad successfully using the code below
  if(interstitialVideoAdView.hasInterstitial)
  
  (3) interstitialVideoAdView.showInterstitialVideoAd(); // Expose a Interstitial Video ad
- Precautions
    - If too much time passes without exposure after successful ad loading, the ad may not be displayed properly when showInterstitialVideoAd() is called.
    - After a certain amount of time after loadInterstitialVideoAd(), the ad may not be treated as an effective exposure.

- Refer to the code and sample project below to add ads in any way you like.
```java
public class InterstitialVideoActivity extends AppCompatActivity {

    private Button btnInterstitialVideoShow;
    private ProgressBar progressBar;
    private InterstitialVideoAd interstitialVideoAdView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitialvideo);

        progressBar = findViewById(R.id.loading_bar);
        btnInterstitialVideoShow = findViewById(R.id.btn_interstitial_video_show);

        AdInfo adInfo = new AdInfo.Builder(Application.adUnitId_interstitialVideo) // adunit id issued by the AdMixer platform
                .interstitialTimeout(0) // Set the Interstitial ad timeout in seconds (default: 0, 0 is treated as server designated time, server designated time: 20s)
                .maxRetryCountInSlot(-1) // Number of iterations within the reload time (-1 : infinity, 0 : no iterations, n : n iterations)
                .isRetry(true) // ad change setting (true - default). If false, callback immediately after one request
                .build();

        interstitialVideoAdView = new InterstitialVideoAd(this);
        // Please note that hardware Accelerated is set to true for Interstitial Video's parent activity to provide a smooth ad serving
        interstitialVideoAdView.setAdInfo(adInfo, this);
        interstitialVideoAdView.setListener(new AdListener() {
            @Override
            public void onReceivedAd(Object o) {
                // Success to receive ads
                Toast.makeText(InterstitialVideoActivity.this, "onReceivedAd", Toast.LENGTH_SHORT).show();
                btnInterstitialVideoShow.setText("show interstitial ad");
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailedToReceiveAd(Object o, int i, String s) {
                // Fail to receive ads
                Toast.makeText(InterstitialVideoActivity.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEventAd(Object o, AdEvent adEvent) {
                // Other Events
                switch (adEvent) {
                    case CLOSE:   // If the ad close event occurs
                    case SKIPPED: // If a Skip button click event occurs 
                        interstitialVideoAdView.closeInterstitialVideoAd();
                        btnInterstitialVideoShow.setText("request interstitial Video ad");
                        break;
                    case COMPLETION: // If the video play end
                    case CLICK: // If a CTA button click event occurs
                        break;
                }
            }
        });

        // After receiving an ad request, expose it to the desired point in time
        btnInterstitialVideoShow.setOnClickListener(v -> {
            if (interstitialVideoAdView.hasInterstitial)
                interstitialVideoAdView.showInterstitialVideoAd(); // ad show
            else {

                interstitialVideoAdView.loadInterstitialVideoAd(); // ad load
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    // Depending on the life cycle, the following settings are essential.
    @Override
    protected void onDestroy() {
        if (interstitialVideoAdView != null) {
            interstitialVideoAdView.stopInterstitialVideoAd();
            interstitialVideoAdView = null;
        }
        super.onDestroy();
    }
}
```
