package com.nasmedia.admixerkotlin

import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nasmedia.admixer.ads.AdEvent
import com.nasmedia.admixer.ads.AdInfo
import com.nasmedia.admixer.ads.AdListener
import com.nasmedia.admixer.ads.NativeAdView
import com.nasmedia.admixer.common.nativeads.NativeAdViewBinder

class NativeActivity : AppCompatActivity() {

    var nativeAdView: NativeAdView? = null
    lateinit var container: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native)

        container = findViewById(R.id.container_native)

        val adInfo: AdInfo = AdInfo.Builder(Application.ADUNIT_ID_NATIVE) // AdMixer 플랫폼에서 발급받은 배너 ADUNIT_ID
            .isRetry(true) // 광고 재요청 설정 (true - 기본값), false 시, 1회 요청 후 바로 Callback
            .build()

        // Native 광고를 노출할, Layout 을 정의한 xml 의 ID 정보를 SDK 에 전달합니다.
        // (icon, title, advertiser, description, main(image,video View), cta ...)
        // 아래 binding 해야 할 asset 중 title or iconImage or mainView 중 1개는 반드시 설정해야 하는 필수값 입니다.
        val viewBinder: NativeAdViewBinder = NativeAdViewBinder.Builder(R.layout.item_320x480)
            .setIconImageId(R.id.iv_icon) // 아이콘 ID
            .setTitleId(R.id.tv_title)    // 제목(타이틀) ID
            .setAdvertiserId(R.id.tv_adv) // 광고주 ID
            .setDescriptionId(R.id.tv_desc) // 설명 ID
            .setMainViewId(R.id.iv_main)    // 메인 이미지 또는 동영상 ID
            .setCtaId(R.id.btn_cta) // Call to Action 버튼 (ex 더보기) ID
            .build()

        nativeAdView = NativeAdView(this@NativeActivity)
        // 이 때 설정하신 Native 의 부모 activity 는 원활한 광고 제공을 위해 hardwareAccelerated 가 true 설정되오니 참고 부탁드립니다.
        nativeAdView?.setAdInfo(adInfo, this@NativeActivity)
        nativeAdView?.setViewBinder(viewBinder) // 네이티브 광고 레이아웃 필수 전달
        nativeAdView?.setAdViewListener(object : AdListener {
            override fun onReceivedAd(p0: Any?) {
                // 광고 수신 성공
                Toast.makeText(this@NativeActivity, "onReceivedAd", Toast.LENGTH_SHORT).show()
                if (nativeAdView?.hasAd == true) { // 광고를 성공적으로 받았는지 판단
                    container.removeAllViews()
                    container.addView(nativeAdView) // 레이아웃에 네이티브 광고를 추가
                }
            }

            override fun onFailedToReceiveAd(p0: Any?, p1: Int, p2: String?) {
                // 광고 수신 실패
                Toast.makeText(this@NativeActivity, "onFailedToReceiveAd \n $p2", Toast.LENGTH_SHORT).show()
            }

            override fun onEventAd(p0: Any?, p1: AdEvent?) {
                // 기타 이벤트
                Toast.makeText(this@NativeActivity, "onEventAd : " + p1.toString(), Toast.LENGTH_SHORT).show()
                when (p1) {
                    AdEvent.CLICK -> {} // 클릭 시
                    AdEvent.DISPLAYED -> {} // 노출 시
                    else -> {}
                }
            }
        })

        // 네이티브 광고 미리 로드
        nativeAdView?.loadNativeAd()
    }

    // 생명주기에 따라 아래 설정이 반드시 필요합니다.
    override fun onResume() {
        if (nativeAdView != null) {
            nativeAdView?.onResume()
        }
        super.onResume()
    }

    override fun onPause() {
        if (nativeAdView != null) {
            nativeAdView?.onPause()
        }
        super.onPause()
    }

    override fun onDestroy() {
        if (nativeAdView != null) {
            nativeAdView?.onDestroy()
        }
        super.onDestroy()
    }
}