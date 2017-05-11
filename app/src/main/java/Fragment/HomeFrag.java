package Fragment;

import android.annotation.TargetApi;
import android.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.administrator.myapplication.BitmapOfAdapter;
import com.example.administrator.myapplication.MainActivity;
import com.example.administrator.myapplication.R;

import com.example.administrator.myapplication.StreamTools;
import com.example.administrator.myapplication.VideoInfo;
import com.example.administrator.myapplication.VideoPlayer;
import com.xys.libzxing.zxing.activity.CaptureActivity;



import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.administrator.myapplication.R.layout.hp_fragment;

/**
 * Created by Administrator on 2017/4/20.
 */

public class HomeFrag extends Fragment implements ViewPager.OnPageChangeListener{
    BitmapOfAdapter adpter;

    private TextView tCode;
    private List<VideoInfo> infolist =new ArrayList<VideoInfo>();

    private ListView lv_left;
    private  Bitmap bitmap_pc;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 2) {
                Toast.makeText(getActivity(), "网络延迟", 0);
            } else {
                MainActivity activity=(MainActivity)getActivity();
                infolist=activity.getInfolist();
                tCode.setText(infolist.size()+"");
                adpter= new BitmapOfAdapter(getActivity(),R.layout.videoinfo,infolist);
                lv_left.setAdapter(adpter);
                lv_left.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        VideoInfo video = infolist.get(position);
                        //传递播放记录
                        MainActivity activity = (MainActivity) getActivity();//获得MainActivity的实例
                        activity.addNameList(video.getVideoname());
                        //点击播放视频
                        String str = "http://182.254.151.22:8080/" + video.getAddress();
                        Intent intent = new Intent(getActivity(), VideoPlayer.class);
                        intent.putExtra("path", str);// 发送数据
                        startActivity(intent);
                    }
                });

                new DownLoadTask().execute();
            }
        }

    };


    /**
     * ViewPager
     */
    private ViewPager viewPager;

    /**
     * 装点点的ImageView数组
     */
    private ImageView[] tips;

    /**
     * 装ImageView数组
     */
    private ImageView[] mImageViews;

    /**
     * 图片资源id
     */
    private int[] imgIdArray ;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(hp_fragment,container,false);
        //扫描二维码按键
        tCode = (TextView)view.findViewById(R.id.viewcode);
        tCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调用ZXIng开源项目源码  扫描二维码
               Intent openCameraIntent=new Intent(getActivity(),CaptureActivity.class);
                startActivityForResult(openCameraIntent,0);


            }
        });

        //图片滑屏
        ViewGroup group = (ViewGroup)view.findViewById(R.id.viewGroup);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        //放置图片资源
        imgIdArray = new int[]{
                R.mipmap.roll1,R.mipmap.roll2,R.mipmap.roll3,R.mipmap.roll4,R.mipmap.roll5
        };
        //设置滑动变化点
        tips = new ImageView[imgIdArray.length];
        for (int i=0;i<tips.length;i++){
            ImageView imageView=new ImageView(getActivity());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(6,6));
            tips[i]=imageView;
            if(i==0){
                tips[i].setBackgroundResource(R.mipmap.dot);
            }else {
                tips[i].setBackgroundResource(R.mipmap.undot);
            }

            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                            , ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin=5;
            layoutParams.rightMargin=5;

            group.addView(imageView,layoutParams);
        }
        //把图片放到数组中
        mImageViews=new ImageView[imgIdArray.length];
        for(int i=0;i<mImageViews.length;i++){
            ImageView imageView=new ImageView(getActivity());
            mImageViews[i] = imageView;
            imageView.setBackgroundResource(imgIdArray[i]);
        }

        viewPager.setAdapter(new MyAdapter());

        viewPager.addOnPageChangeListener(this);

        viewPager.setCurrentItem((mImageViews.length)*100);



        //热门视频ListView
        lv_left= (ListView) view.findViewById(R.id.listview_left);
        getViewInfomation();


        return view;
    }

    //获取排名前四的视频信息
    public void getViewInfomation (){

        new Thread(){
            @Override
            public void run() {

                try {

                    if(infolist!=null) {

                        Message mas= Message.obtain();
                        mas.what = 1;

                        handler.sendMessage(mas);
                    }else {
                        Message mas= Message.obtain();
                        mas.what = 2;
                        handler.sendMessage(mas);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }


            }


        }.start();
    }




    /**
     * 动态设置ListView的高度
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    class DownLoadTask extends AsyncTask<Void,Integer,Boolean> {
        Bitmap fm;


//        public DownLoadTask(String url,String author){
//            this.URL=url;
//            this.AUTHOR=author;
//
//        }

        //获取网络视频第一帧
        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        private Bitmap createVideoThumbnail(String url, int width, int height) {
            Bitmap bitmap = null;
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            int kind = MediaStore.Video.Thumbnails.MINI_KIND;
            try {
                if (Build.VERSION.SDK_INT >= 14) {
                    retriever.setDataSource(url, new HashMap<String, String>());
                } else {
                    retriever.setDataSource(url);
                }
                bitmap = retriever.getFrameAtTime();
            } catch (IllegalArgumentException ex) {
                // Assume this is a corrupt video file
            } catch (RuntimeException ex) {
                // Assume this is a corrupt video file.
            } finally {
                try {
                    retriever.release();
                } catch (RuntimeException ex) {
                    // Ignore failures while cleaning up.
                }
            }
            if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                        ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            }
            return bitmap;
        }


        public  Bitmap loadImageFromUrl(String url, String author) throws IOException {
            //是否SD卡可用
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                //检查是或有保存图片的文件夹，没有就穿件一个
                String FileUrl = Environment.getExternalStorageDirectory()+"/fm_pic/";
                File folder = new File(FileUrl);
                if(!folder.exists()){
                    folder.mkdir();
                }
                File f = new File(FileUrl+author+".jpg");
                //SD卡中是否有该文件，有则直接读取返回
                if(f.exists()){
                    //   FileInputStream fis = new FileInputStream(f);
                    //  Drawable d = Drawable.createFromStream(fis, "src");
                    Bitmap bmap= BitmapFactory.decodeFile(FileUrl+author+".jpg");
                    return bmap;
                }
                //没有的话则去连接下载，并写入到SD卡中
                fm = createVideoThumbnail(url, 30, 30);

                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f));
                fm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                //     bos.flush();
                bos.close();

                //     Drawable d = Drawable.createFromStream(i, "src");
                return loadImageFromUrl(url,author);
            }
            //SD卡不可用则直接加载使用
            else{
                fm = createVideoThumbnail(url, 30, 30);
                return fm;
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          //  image.setImageResource(R.mipmap.roll2);

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            for (int i=0;i<infolist.size();i++){
                try {
                    bitmap_pc= loadImageFromUrl("http://182.254.151.22:8080/"+infolist.get(i).getAddress(),infolist.get(i).getVideoname());
                    infolist.get(i).setBitmap(bitmap_pc);
                    publishProgress();
                }catch (Exception e){
                    Log.e("connERROR","获取失败");
                    return false;
                }
                publishProgress();
            }


            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            adpter.notifyDataSetChanged();

        }
    }







    //滑动图模块
    public class MyAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager)container).removeView(mImageViews[position % mImageViews.length]);

        }

        /**
         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
         */
        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager)container).addView(mImageViews[position % mImageViews.length], 0);
            return mImageViews[position % mImageViews.length];
        }



    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }
    @Override
    public void onPageSelected(int arg0) {
        setImageBackground(arg0 % mImageViews.length);
    }
    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * 设置选中的tip的背景
     * @param selectItems
     */
    private void setImageBackground(int selectItems){
        for(int i=0; i<tips.length; i++){
            if(i == selectItems){
                tips[i].setBackgroundResource(R.mipmap.dot);
            }else{
                tips[i].setBackgroundResource(R.mipmap.undot);
            }
        }
    }


}

