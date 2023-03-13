package com.example.mobileyarr

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class KakaoLogin: Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "a8637adec9647993a58fc6787d8ac244")
    }
}