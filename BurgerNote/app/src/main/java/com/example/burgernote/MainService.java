package com.example.burgernote;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MainService extends Service {

    private FloatingViewManager mFloatingViewManager;
    private final IBinder BINDER = new myBinder();

    ArrayList<Memo> mMemoList = new ArrayList<Memo>();

    public class myBinder extends Binder {     // Binder 상속(Binder 는 IBinder interface 를 구현)
        MainService getService(){
            return MainService.this;      // 현재 Service 객체를 리턴
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // Service 객체와 (화면단 Activity 사이에서) 데이터를 주고받을 때 사용하는 메소드
        // Activity 에서 bindService()를 실행하면 호출됨.
        // data 를 전달할 필요 없으면 null 값 리턴.
        Log.d("myLog", "Service: onBind()");
        return BINDER;     // 리턴할 mBinder 객체는 서비스와 클라이언트 사이의 인터페이스 정의
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("myLog", "Service: onCreate()");
        initMemoViews();
        initFloatingViewManager();
    }

    void initMemoViews(){
        mMemoList.add(new TextMemo(this));
        mMemoList.add(new CalendarMemo(this));
        mMemoList.add(new RecordMemo(this));
        mMemoList.add(new DrawingMemo(this));
    }

    void customizeMemos(boolean[] flags){
        for(int i=0; i<mMemoList.size(); i++){
            Memo memo = mMemoList.get(i);
            if(flags[i]) memo.show();
            else memo.hide();
        }
    }

    void initFloatingViewManager(){
        mFloatingViewManager = new FloatingViewManager(this);

        Memo[] memos = mMemoList.toArray(new Memo[0]);
        mFloatingViewManager.setMemos(memos);
    }

    void showView(){
        mFloatingViewManager.addView();
    }

    void hideView(){
        mFloatingViewManager.removeView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mFloatingViewManager != null) {
            mFloatingViewManager.removeView();
            mFloatingViewManager = null;
            Log.d("myLog", "myService onDestroy()");
        }
    }
}
