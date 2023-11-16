## Reward Interstitial Video

|                                                 Reward Video Sample                                                         | EndCard Sample
|:-------------------------------------------------------------------------------------------------------------------------:|:---:|
| <img src="https://github.com/Nasmedia-Tech/admixer_android_sdk_3.0/blob/main/images/reward.jpg"  width="40%" height="40%"/> |<img src="https://github.com/Nasmedia-Tech/admixer_android_sdk_3.0/blob/main/images/reward_endcard.jpg"  width="40%" height="40%"/>


## Reward Interstitial Video Ad example
- Reward Interstitial Video View is available in two ways:.

  (1) Immediate exposure received after ad request

  (2) After receiving an ad request, expose it to the desired point in time


- After receiving an ad request, expose it to the desired point in time  
  (1) rewardInterstitialVideoAd.loadRewardVideoAd(); Load Request

  (2) After receiving the onReceivedAd event, check with rewardInterstitialVideoAd.hasInterstitial if the reward interstitial video ad was successfully received at the desired time.
  
  (3) rewardInterstitialVideoAd.showRewardVideoAd(); // Reward Interstitial Video show


 - precautions
    - If too much time passes without exposure after successful ad loading, the ad may not be displayed properly when showRewardVideoAd() is called
    - After a certain amount of time after calling loadRewardVideoAd(), the ad may not be treated as an effective exposure.

- Refer to the code and sample project below to add ads in any way you like.
```java
public class RewardInterstitialVideoActivity extends AppCompatActivity {

    private Button btnInterstitialVideoShow;
    private RewardInterstitialVideoAd rewardInterstitialVideoAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitialvideo);

        btnInterstitialVideoShow = findViewById(R.id.btn_interstitial_video_show);
        
        Map<String, String> params = new HashMap<>();
        params.put("use_id", "nas");
        params.put("name", "choi");
        params.put("phone", "010-1111-1111");
		
        AdInfo adInfo = new AdInfo.Builder(Application.adUnitId_interstitialVideo) // adunit id issued by the AdMixer platform
                .interstitialTimeout(0) // // Able to set T-max in seconds (default: 0, 0 is treated as server designated time, server designated time: 20s)
                .maxRetryCountInSlot(-1) // Number of iterations within the reload time (-1 : infinity, 0 : no iterations, n : n iterations)
                .isRetry(true) // ad change setting (true - default). If false, callback immediately after one request
                .setCustomParams(params) // Add Reward Callback as a custom data map (Optional)
                .build();

        rewardInterstitialVideoAd = new RewardInterstitialVideoAd(this);
        // Please note that hardware Accelerated is set to true for Banner's parent activity to provide a smooth ad serving 
        rewardInterstitialVideoAd.setAdInfo(adInfo, this);
        rewardInterstitialVideoAd.setListener(new AdListener() {
            @Override
            public void onReceivedAd(Object adView) {
                // Success to receive ads
                Toast.makeText(getApplicationContext(), "onReceivedAd", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailedToReceiveAd(Object adView, int errorCode, String errorMsg) {
                // Fail to receive ads
                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEventAd(Object adView, AdEvent adEvent) {
                // Other Events
                switch (adEvent) {
                    case CLOSE:   // If the ad close event occurs
                    case SKIPPED: // If a Skip button click event occurs 
                        rewardInterstitialVideoAd.closeRewardVideoAd();
                        break;
                    case COMPLETION: // If the video play end - Rewards payment processing 
                    case CLICK: // If a CTA button click event occurs 
                        break;
                }
            }
        });

        // to be exposed at the desired time after receiving an ad request
        btnInterstitialVideoShow.setOnClickListener(v -> {
            if (rewardInterstitialVideoAd.hasInterstitial) {
                rewardInterstitialVideoAd.showRewardVideoAd(); // ad show
            } else {
                rewardInterstitialVideoAd.loadRewardVideoAd(); // ad load
            }
        });
    }

    // Depending on the life cycle, the following settings are essential.
    @Override
    protected void onDestroy() {
        if (rewardInterstitialVideoAd != null) {
            rewardInterstitialVideoAd.stopRewardVideoAd();
            rewardInterstitialVideoAd = null;
        }
        super.onDestroy();
    }
}
```
<br/>

## Reward
- Please pay a reward to the user when the video is played (COMPLETION) in AdListener's onEventAd.
```java
     @Override
            public void onEventAd(Object adView, AdEvent adEvent) {
                // Other Events
                switch (adEvent) {
                    ...
                    ...
                    ...
                    case COMPLETION: // Please pay a reward to the user when the video play is completed
                    ...
                        break;
                }
            }
```
<br/>

## S2S Reward Callback  (Optional)
- When a user completes viewing a reward video ad, it is a function that informs the external server defined by the publisher that the user has completed viewing the reward video ad.
  Admixer erforms a callback immediately after the advertisement is finished, but it may be delayed for several minutes.
- This feature is available through the detailed settings for the Rewards video adunit on the partner site.

### Setting 1 : Enter callback server url at partner site
- Follow this route to enter the callback server URL of the publisher.
  **Partner site** → **My media** → **adunit setting**
- When a user completes viewing a reward video ad sent through the appropriate ad unit, the reward callback data is sent to the entered callback server URL.

|                                                              Partner site                                                               |
|:----------------------------------------------------------------------------------------------------------------------------------:|
| <img src="https://github.com/Nasmedia-Tech/admixer_android_sdk_3.0/blob/main/images/settings.png"  width="80%" height="80%"/> |

<br/>

### Parameters
|Parameters| Description                            |Example|
|------|--------------------------------|---|
|media_key| media key issued by the AdMixer platform.         |12345678|
|adunit_id| adunit id issued by the AdMixer platform     |87654321|
|adid| Android : google Advertise ID iOS : idfa |860635ea-65bc-eaed-d355-1b5283b30b94|
|user_id| User Identification Value ||
|complete| Event to indicate that the user has completed viewing the ad ||
|timestamp| Time when the complete event occurred |1546300800|
###### The parameters in the following table are sent to the callback URL (see example URL below)

| {Publisher’s callback URLl}?media_key={mediakey}&adunit_id={adunitid}&adid={adid}&user_id={user_id}&complete={complete}&timestamp={timestamp} |
|:-----------------------------------------------------------------------------------------------------------------------------|

<br/>

### Setting 2 : Add SDK CustomData
- The addition of SDK Custom Data allows you to collect additional data from the callback.
- Custom Data should be added in String Map format.
- Please refer to the code below..
```java
   //example
    Map<String, String> params = new HashMap<>();
        params.put("useid", "nas");
        params.put("name", "hdragon");
        params.put("phone", "010-1111-1111");

        AdInfo adInfo = new AdInfo.Builder(Application.adUnitId)
        ...
        ...
        .setCustomParams(params) // Add Rewarded Callbacks CustomData as a String Map
        .build();

```

###### The custom parameters set in Add Custom Data within SDK are included in the callback URL and sent (see example URL below)


| {publishercallbackurl}?media_key={mediakey}&adunit_id={adunitid}&adid={adid}&user_id={user_id}&complete={complete}&timestamp={timestamp}<u>&useid=nas&name=hdragon&phone=010-1111-1111</u> |
|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
