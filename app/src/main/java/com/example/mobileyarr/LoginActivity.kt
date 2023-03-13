package com.example.mobileyarr

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileyarr.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient

class LoginActivity: AppCompatActivity() {
    val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    val pref by lazy { this.getSharedPreferences("user_data", Context.MODE_PRIVATE) }
    val editor by lazy { pref.edit() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val keyhash = Utility.getKeyHash(this)
        Log.d("keyhash", keyhash)
        binding.ivKakao.setOnClickListener { kakaoLogin() }
    }

    fun kakaoLogin(){
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, throwable ->
            Log.i("@@@@token", token.toString())
            if (token != null){
                UserApiClient.instance.me { user, error ->
                    if(user != null){
                        val id = user.id.toString()
                        val date = user.connectedAt.toString()
                        editor.putString("user_id",id)
                            .putString("user_join", date)
                            .commit()
                        Log.d("@@@@카톡 로그인정보 저장 확인","$id + $date")
                    }
                }
                startActivity(Intent(this, MainActivity::class.java))
            } else if(throwable != null){
                AlertDialog.Builder(this).setMessage(throwable.message).show()
            }
        }
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
            UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }
}