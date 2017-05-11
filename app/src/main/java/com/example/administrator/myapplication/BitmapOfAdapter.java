package com.example.administrator.myapplication;

/**
 * Created by Administrator on 2017/4/29.
 */

import android.content.Context;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/27.
 */

public class BitmapOfAdapter extends ArrayAdapter<VideoInfo> {
    private int resourceId;
    VideoInfo videoInfo;
    ImageView videoImage;
    private int FLAG=0;

    public BitmapOfAdapter(Context context, int resource, List<VideoInfo> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        VideoInfo videoInfo=getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        ImageView videoImage=(ImageView)view.findViewById(R.id.video_image);
        TextView videoTitle=(TextView)view.findViewById(R.id.video_text);
        TextView video_ps=(TextView)view.findViewById(R.id.video_ps);

        videoImage.setImageBitmap(videoInfo.getBitmap());
        videoTitle.setText(videoInfo.getVideoname());
        video_ps.setText("播放次数："+videoInfo.getPlaytime());
        notifyDataSetChanged();

//        TextView pc_addtolike= (TextView) view.findViewById(R.id.addtolove);
//        TextView pc_play= (TextView) view.findViewById(R.id.play);
//        pc_addtolike.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(FLAG==0) {
//                    pc_addtolike.setBackgroundResource(R.mipmap.like);
//                    FLAG=1;
//                }else {
//                    pc_addtolike.setBackgroundResource(R.mipmap.unlike);
//                    FLAG=0;
//                }
//            }
//        });


        return view;
    }





}
