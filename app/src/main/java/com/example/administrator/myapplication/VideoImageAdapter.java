package com.example.administrator.myapplication;

/**
 * Created by Administrator on 2017/4/29.
 */

import android.content.Context;

import android.content.Intent;
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

public class VideoImageAdapter extends ArrayAdapter<VideoInfo> {
    private int resourceId;
    VideoInfo videoInfo;
    ImageView videoImage;

    Context mContext;

    public VideoImageAdapter(Context context, int resource, List<VideoInfo> objects) {
        super(context, resource, objects);
        resourceId=resource;
        this.mContext=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        VideoInfo listInfo=getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        ImageView videoImage=(ImageView)view.findViewById(R.id.video_image);
        TextView videoTitle=(TextView)view.findViewById(R.id.video_text);
        TextView video_ps=(TextView)view.findViewById(R.id.video_ps);

        videoImage.setImageBitmap(listInfo.getBitmap());
        videoTitle.setText(listInfo.getVideoname());
        video_ps.setText("播放次数："+listInfo.getPlaytime());
        notifyDataSetChanged();

        TextView pc_addtolike= (TextView) view.findViewById(R.id.addtolove);
        TextView pc_play= (TextView) view.findViewById(R.id.play);

        if(listInfo.getLove()) {
            pc_addtolike.setBackgroundResource(R.mipmap.like);

        }else {
            pc_addtolike.setBackgroundResource(R.mipmap.unlike);

        }
        pc_addtolike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listInfo.getLove()) {
                    pc_addtolike.setBackgroundResource(R.mipmap.unlike);
                    listInfo.setLove(false);
                }else {
                    pc_addtolike.setBackgroundResource(R.mipmap.like);
                    listInfo.setLove(true);
                }
            }
        });

        pc_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "http://182.254.151.22:8080/" + listInfo.getAddress();
                Intent intent = new Intent(mContext, VideoPlayer.class);
                intent.putExtra("path", str);// 发送数据
                mContext.startActivity(intent);
            }
        });


        return view;
    }





}
