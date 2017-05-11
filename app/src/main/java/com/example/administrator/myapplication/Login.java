package com.example.administrator.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login extends AppCompatActivity {
    protected static final int ERROR = 2;
    protected static final int SUCCESS = 1;

    private Button btnLogin;
    private Button btnJump;
    private EditText edtUsername;
    private EditText edtPsw;
    private TextView textView;

    private SharedPreferences sharedPreferences;



    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String str;
            if(msg.what==SUCCESS){
                str=msg.obj.toString();
                loginRight(str);
            }else{
                Toast.makeText(Login.this,"您的网络不给力啊，请重新登录",0).show();
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences=this.getSharedPreferences("data",MODE_PRIVATE);

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnJump = (Button) findViewById(R.id.btn_jump);
        textView = (TextView) findViewById(R.id.textview);
        edtPsw = (EditText)findViewById(R.id.login_password);
        edtUsername = (EditText)findViewById(R.id.login_username);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginConnect();
            }
        });


    }
    public void loginRight(String str) {
        try {
            //解析JSON数据
            JSONObject jsonObject = new JSONObject(str);
            int id = jsonObject.getInt("id");
            String userName = jsonObject.getString("username");
            String password = jsonObject.getString("password");
            String nickName = jsonObject.getString("nickname");
            //将数据存入SharedPreferences
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putInt("id",id);
            editor.putString("username",userName);
            editor.putString("password",password);
            editor.putString("nickname",nickName);
            editor.commit();
         //   Data data=new Data();
         //   data.setDa_id(id);
         //   data.setDa_username(userName);
         //   data.setDa_pwd(password);
         //  data.setDa_nickname(nickName);
            Intent intent = new Intent();
            intent.setClass(Login.this, MainActivity.class);
          //  Bundle bundle = new Bundle();
         //   bundle.putParcelable("data", data);// 序列化
         //   intent.putExtras(bundle);// 发送数据
            startActivity(intent);// 启动intent
            finish();
        } catch (JSONException e) {
            Toast.makeText(Login.this,"密码错误",0).show();
        }


    }

    public void loginConnect(){
 //       final String username=edtUsername.getText().toString();
 //       final String password=edtPsw.getText().toString();

//        if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
//            Toast.makeText(Login.this,"用户密码不能为空",0).show();
//            return;
//        }

    String    username="admin";
    String    password="12345";

        new Thread(){
            @Override
            public void run() {

                try {
                    String Path="http://182.254.151.22:8080/login.php";
                    URL url =new URL(Path);
                    HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(10000);//连接的超时时间
                    conn.setReadTimeout(5000);//读数据的超时时间
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0(compatible;MSIE 9.0;Windows NT 6.1;Trident/5.0)");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//请求的类型  表单数据

                    String data = "username="+ URLEncoder.encode(username,"utf-8") +"&password="+URLEncoder.encode(password,"utf-8");
                    conn.setRequestProperty("Content-Length", data.length()+"");//数据的长度
                    conn.setDoOutput(true);
                    byte[] bytes=data.getBytes();
                    OutputStream out=conn.getOutputStream();//获取一个输出流
                    out.write(bytes);//把数据以流的方式写给服务器
                    int code=conn.getResponseCode();
                    System.out.println(code);
                    //判断http连接是否正常
                    if(code==200){
                        //访问成功，通过流取的页面的数据信息
                        InputStream is = conn.getInputStream();
                        String  result = StreamTools.readStream(is);
                        Message mas= Message.obtain();
                        mas.what = SUCCESS;
                        mas.obj = result;
                        handler.sendMessage(mas);
                    }else{
                        Message mas = Message.obtain();
                        mas.what = ERROR;
                        handler.sendMessage(mas);
                    }

                } catch (IOException e) {
                    Message mess =Message.obtain();
                    mess.obj="登录失败";
                    handler.sendMessage(mess);
                }


            }
        }.start();
    }




}
