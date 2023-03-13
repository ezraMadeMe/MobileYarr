package com.example.mobileyarr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mobileyarr.databinding.ActivityMainBinding
import com.kakao.sdk.friend.client.PickerClient
import com.kakao.sdk.friend.model.OpenPickerFriendRequestParams
import com.kakao.sdk.friend.model.PickerOrientation
import com.kakao.sdk.friend.model.ViewAppearance
import com.kakao.sdk.talk.TalkApiClient

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnFriend.setOnClickListener { callFriendList() }
    }

    fun callFriendList(){
        TalkApiClient.instance.profile{ profile, error ->
            if(error != null){
                Log.e("@@@프로필 에러",error.message!!)
            }else if (profile != null){
                Log.i("@@@프로필 정보", "닉네임 : ${profile.nickname}")
            }
        }
        TalkApiClient.instance.friends { friends, error ->
            if (error != null) {
                Log.e("@@@친구 가져오기 에러", error.message!!)
            } else if (friends != null) {
                Log.i("@@@친구 가져오기 성공", "친구목록 : ${friends.elements?.joinToString("\n")}")
                Log.i("@@@친구 가져오기 성공", "친구수 : ${friends.totalCount}")
            }
        }

        val openPickerFriendRequestParams = OpenPickerFriendRequestParams(
            title = "야르 뜰 사람", //default "친구 선택"
            viewAppearance = ViewAppearance.AUTO, //default ViewAppearance.AUTO
            orientation = PickerOrientation.AUTO, //default PickerOrientation.AUTO
            enableSearch = true, //default true
            enableIndex = true, //default true
            showMyProfile = true, //default true
            showFavorite = true //default true
        )

        PickerClient.instance.selectFriendPopup(
            context = this,
            params = openPickerFriendRequestParams
        ) { selectedUsers, error ->
            if (error != null) {
                Log.e("@@@친구선택 실패", error.message!!)
            } else {
                Log.d("@@@친구 선택 성공", selectedUsers?.users.toString())
                //선택한 친구와 야르 뜨는 activity로 intent
                //테스트 계정에서 팀원 계정으로 로그인하여 권한을 획득하여야 함
            }
        }
    }
}