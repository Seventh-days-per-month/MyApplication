package com.example.administrator.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * Created by Administrator on 2017/4/29.
 */

public class VideoPlayer extends Activity {
    private VideoView mvideoView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoplayer);

        mvideoView=(VideoView)findViewById(R.id.videoview);


        String path="";
        Bundle bundle=this.getIntent().getExtras();
        path=bundle.getString("path");


        mvideoView.setVideoPath(path);
        MediaController controller=new MediaController(this);
        mvideoView.setMediaController(controller);
        controller.setMediaPlayer(mvideoView);
    }
}
