package com.speed.domain.catlifevpn.ads

import android.app.Activity
import android.content.Context
import com.elvishew.xlog.XLog
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

/**
 * @Description:
 * @Author: Alix
 * @Date: 2025/7/8
 */
object AdsManager {

    const val AD_TYPE_INTER = "ad_inter"
    const val AD_TYPE_NATIVE = "ad_native"

    //测试
    val adIntId = "ca-app-pub-3940256099942544/1033173712"
    val adNativeId = "ca-app-pub-3940256099942544/2247696110"
    var mInterstitialAd: InterstitialAd? = null

    var isIntLoading = false
    var isIntShowing = false

    fun loadIntAd(context: Context) {
        if (!isIntLoading){
            XLog.e("预加载插页广告")
            isIntLoading = true
            val adRequest = AdRequest.Builder().build()
            InterstitialAd.load(context, adIntId, adRequest, object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    XLog.e("插页广告加载失败 $adError")
                    mInterstitialAd = null
                    isIntLoading = false
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    XLog.e("插页广告加载成功")
                    mInterstitialAd = interstitialAd
                    isIntLoading = false
                }
            })
        }else {
            XLog.e("插页广告加载中")
        }
    }

    fun showIntAd(activity: Activity) {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(activity)
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdClicked() {
                    // Called when a click is recorded for an ad.
                    XLog.e("Ad was clicked.")
                }

                override fun onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    XLog.e("Ad dismissed fullscreen content.")
                    mInterstitialAd = null
                    isIntShowing = false
                    loadIntAd(activity)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    // Called when ad fails to show.
                    XLog.e("Ad failed to show fullscreen content.")
                    mInterstitialAd = null
                    isIntShowing = false
                    loadIntAd(activity)
                }

                override fun onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    XLog.e("Ad recorded an impression.")
                    isIntShowing = true
                }

                override fun onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    XLog.e("Ad showed fullscreen content.")
                    isIntShowing = true
                }
            }
        } else {
            XLog.e("The interstitial ad wasn't ready yet.")
            loadIntAd(activity)
        }
    }

    fun checkCacheAd(adType: String): Boolean {
        return when (adType) {
            AD_TYPE_INTER -> mInterstitialAd != null
            //AD_TYPE_NATIVE -> atNativeNormalAd != null && atNativeNormalAd?.checkAdStatus()?.isReady == true
            else -> false
        }
    }

//    @SuppressLint("UseCompatLoadingForDrawables")
//    fun loadNativeAd(context: Context, view: TemplateView) {
//        nativeAdLoader = AdLoader.Builder(context, adNativeId)
//            .forNativeAd { nativeAd ->
////                if (isDestroyed) {
////                    nativeAd.destroy()
////                    return@forNativeAd
////                }
//                XLog.e("原生广告加载成功")
//                val styles = NativeTemplateStyle.Builder()
//                    .withMainBackgroundColor(AppHelper.instance.getDrawable(com.fatfat.dev.advertise.R.color.white) as ColorDrawable?)
//                    .withCallToActionBackgroundColor(AppHelper.instance.getDrawable(com.fatfat.dev.advertise.R.color.co_89C4FF) as ColorDrawable?)
//                    .build()
//                view.setStyles(styles)
//                view.setNativeAd(nativeAd)
//                view.visible()
//            }
//            .withAdListener(object : AdListener() {
//                override fun onAdFailedToLoad(adError: LoadAdError) {
//                    XLog.e("原生广告加载失败 $adError")
//                }
//            })
//            .withNativeAdOptions(NativeAdOptions.Builder().build())
//            .build()
//        //adLoader.loadAd(AdRequest.Builder().build())
//    }
//
//    var myNativeAd: NativeAd? = null
//
//    @SuppressLint("UseCompatLoadingForDrawables")
//    fun loadVideoNativeAd(context: Context, view: TemplateView) {
//        myNativeAd?.mediaContent?.let {
//            if (it.hasVideoContent()) {
//                val mediaAspectRatio: Float = it.aspectRatio
//                val duration: Float = it.duration
//            }
//        }
//        val videoOptions = VideoOptions.Builder()
//            .setStartMuted(false)
//            .build()
//
//        val adOptions = NativeAdOptions.Builder()
//            .setVideoOptions(videoOptions)
//            .build()
//        nativeAdLoader = AdLoader.Builder(context, "ca-app-pub-3940256099942544/2247696110")
//            .forNativeAd {
//                val styles = NativeTemplateStyle.Builder()
//                    .withMainBackgroundColor(AppHelper.instance.getDrawable(com.fatfat.dev.advertise.R.color.white) as ColorDrawable?)
//                    .withCallToActionBackgroundColor(AppHelper.instance.getDrawable(com.fatfat.dev.advertise.R.color.co_89C4FF) as ColorDrawable?)
//                    .build()
//                view.setStyles(styles)
//                view.setNativeAd(it)
//                view.visible()
//            }
//            .withNativeAdOptions(adOptions)
//            .build()
//        //adLoader.loadAd(AdRequest.Builder().build())
//    }
//
//    fun checkAdLoader(): Boolean {
//        return nativeAdLoader != null
//    }
//
//    fun showNativeAd() {
//        nativeAdLoader?.loadAd(AdRequest.Builder().build())
//    }
}