package com.example.administrator.myapplication;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Fragment.HistoryFrag;
import Fragment.HomeFrag;
import Fragment.LoveFrag;
import Fragment.UserFrag;


/**
 * Created by Administrator on 2017/4/20.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tabHome;
    private TextView tabHistory;
    private TextView tabLove;
    private TextView tabUser;
    private TextView tvtop;

    private FrameLayout ly_content;

    private HomeFrag f1;
    private HistoryFrag f2;
    private LoveFrag f3;
    private UserFrag f4;


    private String NickName;
    private String video_name;
    private int numOfVideo=0;

    private List<VideoInfo> infolist = new ArrayList<VideoInfo>();
    private List<VideoInfo> historylist =new ArrayList<VideoInfo>();
    private ArrayList<String> nameList=new ArrayList<>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                Toast.makeText(MainActivity.this, "网络延迟", 0);
            } else {
                String str = msg.obj.toString();

                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(str);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        VideoInfo video = new VideoInfo();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        video.setId(jsonObject.getInt("id"));
                        video.setVideoname(jsonObject.getString("videoname"));
                        video.setAddress(jsonObject.getString("address"));
                        video.setPlaytime(jsonObject.getInt("playtime"));
                        video.setBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.roll2));
                        infolist.add(video);
                    }
                    tvtop.setText(infolist.size()+"");
                    bindView();
                    onClick(tabHome);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    };




    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvtop=(TextView)findViewById(R.id.txt_top);
        //使用SharedPreferences读取用户信息
        SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
        NickName=pref.getString("nickname","");
        tvtop.setText("欢迎用户【"+NickName+"】");

        getViewInfomation();

//        bindView();
//        onClick(tabHome);


    }

    //获取排名前四的视频信息
    public void getViewInfomation () {

        new Thread() {
            @Override
            public void run() {

                try {
                    String Path = "http://182.254.151.22:8080/visitvideo.php";
                    URL url = new URL(Path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    if (conn.getResponseCode() == 200) {                //200表示请求成功
                        InputStream is = conn.getInputStream();
                        String result = StreamTools.readStream(is);
                        Message mas = Message.obtain();
                        mas.what = 1;
                        mas.obj = result;
                        handler.sendMessage(mas);
                    } else {
                        Message mas = Message.obtain();
                        mas.what = 2;
                        handler.sendMessage(mas);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }


        }.start();
    }


    public List<VideoInfo> getInfolist() {
        return infolist;
    }

    //UI组件初始化与事件绑定
    private void bindView() {

        tabHome = (TextView) this.findViewById(R.id.tv_hot);
        tabHistory = (TextView) this.findViewById(R.id.tv_history);
        tabUser = (TextView) this.findViewById(R.id.tv_user);
        tabLove = (TextView) this.findViewById(R.id.tv_love);
        ly_content = (FrameLayout) findViewById(R.id.fragment_container);
        tvtop=(TextView)findViewById(R.id.txt_top) ;



        tabHome.setOnClickListener(this);
        tabHistory.setOnClickListener(this);
        tabUser.setOnClickListener(this);
        tabLove.setOnClickListener(this);
    }
    //重置所有文本的选中状态
    public void selected(){
        tabHome.setSelected(false);
        tabHistory.setSelected(false);
        tabLove.setSelected(false);
        tabUser.setSelected(false);
    }

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction){
        if(f1!=null){
            transaction.hide(f1);
        }
        if(f2!=null){
            transaction.hide(f2);
        }
        if(f3!=null){
            transaction.hide(f3);
        }
        if(f4!=null){
            transaction.hide(f4);
        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        switch(v.getId()){
            case R.id.tv_hot:
                selected();
                tabHome.setSelected(true);
                if(f1==null){
//                    f1 = new HomeFrag("第一个Fragment");
                    f1=new HomeFrag();
                    transaction.add(R.id.fragment_container,f1);
                }else{
                    transaction.show(f1);
                }
                break;

            case R.id.tv_history:
                selected();
                tabHistory.setSelected(true);
                if(f2==null){
                    f2 = new HistoryFrag();
                    transaction.add(R.id.fragment_container,f2);
                  //  nameList.clear();
                    } else{
                    if(nameList!=null){
                        if(nameList.size()!=0){
                            f2.upDateAdapter();

                        }
                    }
                    transaction.show(f2);
                }
                break;

            case R.id.tv_love:
                selected();
                tabLove.setSelected(true);
                if(f3==null){
                    f3 = new LoveFrag("第三个Fragment");
                    transaction.add(R.id.fragment_container,f3);
                }else{
                    transaction.show(f3);
                }
                break;

            case R.id.tv_user:
                selected();
                tabUser.setSelected(true);
                if(f4==null){
                    f4 = new UserFrag(NickName);
                    transaction.add(R.id.fragment_container,f4);
                }else{
                    transaction.show(f4);
                }
                break;
        }

        transaction.commit();



    }



    public void setHistorylist(List<VideoInfo> historylist) {
        this.historylist = historylist;
    }

    public List<VideoInfo> getHistorylist() {
        return historylist;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }

    private VideoInfo findVideoByName(String str) {

        if(str==null){
            return null;
        }

        for(int i=0;i<infolist.size();i++){
            if(str==infolist.get(i).getVideoname()){
                return infolist.get(i);
            }
        }
        return null;
    }

    public void addVideo(String str) {
        if(findVideoByName(str)!=null){
            historylist.add(findVideoByName(str));
        }
    }
    public void addNameList(String str){

        nameList.add(str);

    }

    public ArrayList<String> getNameList() {
        return nameList;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    // .setIcon(R.drawable.services)
                    .setTitle("警告")
                    .setMessage("您是否要退出程序吗?")
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub

                                }
//                                @Override
//                                public void onClick(DialogInterface dialog,
//                                        int which) {
//                                }
                            })
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    finish();
                                }
                            }).show();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
    //彻底退出程序
    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
        // 或者下面这种方式
        // Android.os.Process.killProcess(android.os.Process.myPid());
    }
}
