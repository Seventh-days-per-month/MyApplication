package Fragment;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.administrator.myapplication.BitmapOfAdapter;
import com.example.administrator.myapplication.MainActivity;
import com.example.administrator.myapplication.R;

import com.example.administrator.myapplication.VideoImageAdapter;
import com.example.administrator.myapplication.VideoInfo;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/22.
 */

public class HistoryFrag extends Fragment {

//    private TextView mTextView;
    private List<VideoInfo> infolist =new ArrayList<VideoInfo>();
    private List<VideoInfo> historylist =new ArrayList<VideoInfo>();
    private VideoImageAdapter adpter;
    private ListView listView;




    public HistoryFrag() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hs_fragment, container, false);
 //      mTextView = (TextView) view.findViewById(R.id.txt_content);
        listView= (ListView) view.findViewById(R.id.listview);
        MainActivity activity = (MainActivity) getActivity();//获得MainActivity的实例
        infolist= activity.getInfolist();

        adpter= new VideoImageAdapter(getActivity(),R.layout.historyinfo,historylist);
        listView.setAdapter(adpter);

        upDateAdapter();

        return view;
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

//    public void addVideo(String str) {
//        if(findVideoByName(str)!=null){
//            infolist.add(findVideoByName(str));
//            adpter.notifyDataSetChanged();
//        }
//
//    }
//
//    public void setHistorylist(List<VideoInfo> historylist) {
//        this.historylist = historylist;
//    }

    public void upDateAdapter() {
        MainActivity activity = (MainActivity) getActivity();//获得MainActivity的实例
        ArrayList<String> names=activity.getNameList();

        try {
            if(names.size()>0){
   //             mTextView.setText(names.size()+"");
                for (int i=0;i<names.size();i++){
                    for(int j=0;j<historylist.size();j++){
                        if(names.get(i)==historylist.get(j).getVideoname()){
                            historylist.remove(historylist.get(j));
                        }
                    }
                    historylist.add(0,findVideoByName(names.get(i)));
                    adpter.notifyDataSetChanged();

                }
                activity.getNameList().clear();
            }else {
  //              mTextView.setText("没运行啊"+";"+activity.getNameList().size());
            }

        }catch(Exception e) {

        }

    }
}
