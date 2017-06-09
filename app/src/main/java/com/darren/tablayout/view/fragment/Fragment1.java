package com.darren.tablayout.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.darren.tablayout.R;
import com.darren.tablayout.activity.NewsActivity;
import com.darren.tablayout.adapter.NewsAdapter;
import com.darren.tablayout.bean.News;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by lenovo on 2017/4/26.
 */

public class Fragment1 extends Fragment {
    private View rootView;
    private ListView lv_items;
    private NewsAdapter adapter;
    private List<News> data = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_item1,null);
        initView();
        return rootView;
    }

    private void initView() {
        lv_items= (ListView) rootView.findViewById(R.id.lv_items);
        adapter=new NewsAdapter(data);
        lv_items.setAdapter(adapter);
        lv_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("news", data.get(position));
                intent.putExtras(bundle);
                Fragment1.this.startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        OkHttpUtils
                .get()
                .url("https://v.juhe.cn/toutiao/index")
                .addParams("key","6a0e786b2c10c570a8e16c035c43ac36")
                .addParams("type","top")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getActivity(), R.string.ask_error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonobject = JSONObject.parseObject(response);
                        JSONArray jsonArray = jsonobject.getJSONObject("result").getJSONArray("data");
                        data.addAll(JSONArray.parseArray(jsonArray.toJSONString(), News.class));
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
