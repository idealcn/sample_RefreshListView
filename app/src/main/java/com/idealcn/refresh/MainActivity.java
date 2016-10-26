package com.idealcn.refresh;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RefreshListView refreshListView;
    private boolean refresh;
    private List<LoveResult> results = new ArrayList<>();
    private ResultAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshListView = (RefreshListView) findViewById(R.id.refresh);

        adapter = new ResultAdapter(this,results);

        refreshListView.setAdapter(adapter);
        refreshListView.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public boolean onRefreshing() {
                return true;
            }
        });
//        refreshListView.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
//            @Override
//            public boolean onRefreshing() {
//
////               final String url = "http://v.juhe.cn/tour_v2.0/jingqushuju.php?"
////                        +"&area=杭州"
////                        +"&key=91b1e716f91c1aa0aadec812b8556109";
//
//
//              final String  url = "http://japi.juhe.cn/love/list.from?key=c4a484530cca9ad4643d5d7e50fd8bcf&count=5";
//                //"&area=%E8%8B%8F%E5%B7%9E
//                // &name=%E8%8B%8F%E5%B7%9E%E5%9B%AD%E6%9E%97
//                // &key=你申请的key";
//                new Thread(){
//                    @Override
//                    public void run() {
//                        super.run();
//                        try {
//                            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
//
//                            connection.connect();
//
//
//
//                            StringBuilder builder = new StringBuilder();
//                            InputStream inputStream = connection.getInputStream();
//                            BufferedReader bis = new BufferedReader(new InputStreamReader(inputStream));
//                           String line = null;
//                            while ((line =bis.readLine())!=null){
//                                builder.append(line);
//                            }
//                            String msg = builder.toString();
//                            if (!TextUtils.isEmpty(msg)){
//                                JSONObject root = new JSONObject(msg);
//                                String reason = root.getString("reason");
//                                if (reason.equalsIgnoreCase("Success")){
//                                    refresh = true;
//                                    JSONArray array = root.getJSONObject("result").getJSONArray("data");
//                                    for (int i = 0; i < array.length(); i++) {
//                                        LoveResult love = new LoveResult();
//                                        JSONObject object = array.getJSONObject(i);
//                                        love.setBody(object.getString("body"));
//                                        love.setTitle(object.getString("title"));
//                                        love.setId(object.getInt("id"));
//                                        love.setValid(object.getInt("valid"));
//                                        results.add(love);
//                                    }
//                                    Log.d("refresh", "run: "+results.size());
//                                    handler.sendEmptyMessage(0);
//                                }else {
//
//                                }
//                            }
//                            Log.d("refresh", "run: "+msg);
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }.start();
//
//
//                return refresh;
//            }
//        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter.notifyDataSetChanged();
        }
    };
}
