package com.example.kilogram2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public class KakaoLogin extends AppCompatActivity {
    private static final String TAG = "KakaoLogin";
    private View loginButton, logoutButton, startButton;
    private TextView nickName;
    private ImageView profileImage, kilGram;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_login);

        loginButton = findViewById(R.id.login);
        logoutButton = findViewById(R.id.logout);
        startButton = findViewById(R.id.startBtn);

        nickName = findViewById(R.id.nickname);
        profileImage = findViewById(R.id.profile);
        kilGram = (ImageView) findViewById(R.id.kilGram);

        Function2<OAuthToken,Throwable,Unit> callback =new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            // 콜백 메서드 ,
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                Log.e(TAG,"CallBack Method");
                //oAuthToken != null 이라면 로그인 성공
                if(oAuthToken!=null){
                    // 토큰이 전달된다면 로그인이 성공한 것이고 토큰이 전달되지 않으면 로그인 실패한다.
                    //updateKakaoLoginUi();

                }else {
                    //로그인 실패
                    Log.e(TAG, "invoke: login fail" );
                }
                updateKakaoLoginUi();
                return null;
            }
        };



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 해당 기기에 카카오톡이 설치되어 있는 확인
                if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(KakaoLogin.this)){
                    UserApiClient.getInstance().loginWithKakaoTalk(KakaoLogin.this, callback);
                }else{
                    // 카카오톡이 설치되어 있지 않다면
                    UserApiClient.getInstance().loginWithKakaoAccount(KakaoLogin.this, callback);
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
                        updateKakaoLoginUi();
                        return null;
                    }
                });
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), bmi.class);
                Bundle bundle = new Bundle();
                // bundle.putString("BMI",BMI);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        updateKakaoLoginUi();

    }


    private void updateKakaoLoginUi() {
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                if(user != null) {
                    // 유저의 아이디
                    Log.d(TAG, "invoke: id =" + user.getId());
                    // 유저의 이메일
                    Log.d(TAG, "invoke: email =" + user.getKakaoAccount().getEmail());
                    // 유저의 닉네임
                    Log.d(TAG, "invoke: nickname =" + user.getKakaoAccount().getProfile().getNickname());
                    // 유저의 성별
                    Log.d(TAG, "invoke: gender =" + user.getKakaoAccount().getGender());
                    // 유저의 연령대
                    Log.d(TAG, "invoke: age=" + user.getKakaoAccount().getAgeRange());


                    // 유저 닉네임 세팅해주기
                    nickName.setText(user.getKakaoAccount().getProfile().getNickname());
                    // 유저 프로필 사진 세팅해주기
                    Glide.with(profileImage).load(user.getKakaoAccount().getProfile().getThumbnailImageUrl()).circleCrop().into(profileImage);
                    Log.d(TAG, "invoke: profile = "+user.getKakaoAccount().getProfile().getThumbnailImageUrl());

                    // 로그인이 되어있으면
                    loginButton.setVisibility(View.GONE);
                    logoutButton.setVisibility(View.VISIBLE);
                    startButton.setVisibility(View.VISIBLE);
                    kilGram.setVisibility(View.GONE);
                }
                else {
                    nickName.setText(null);
                    profileImage.setImageBitmap(null);

                    loginButton.setVisibility(View.VISIBLE);
                    logoutButton.setVisibility(View.GONE);
                    startButton.setVisibility(View.GONE);
                    kilGram.setVisibility(View.VISIBLE);
                }
                return null;
            }
        });
    }



}
