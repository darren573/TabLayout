package com.darren.tablayout.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.darren.tablayout.R;
import com.darren.tablayout.adapter.JokeAdapter;
import com.darren.tablayout.bean.ResultJoke;
import com.darren.tablayout.utils.TimeUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by lenovo on 2017/4/26.
 */

public class Fragment2 extends Fragment {
    private View rootView;
    private ListView lv_items;
    private JokeAdapter adapter;
    private static int page = 1;
    private List<ResultJoke.ResultBean.Joke> data=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_item1,null);
        initView();
        return rootView;
    }

    private void initView() {
        lv_items= (ListView) rootView.findViewById(R.id.lv_items);
        adapter=new JokeAdapter(data);
        lv_items.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        OkHttpUtils
                .get()
                .url("http://japi.juhe.cn/joke/content/list.from")
                .addParams("sort", "desc")
                .addParams("page", String.valueOf(page))
                .addParams("pagesize", "15")
                .addParams("time", TimeUtils.getTime())
                .addParams("key","a93de61332ac37efee2b4f46fd1a3a7f")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ResultJoke resultJoke= JSON.parseObject(response,ResultJoke.class);
                        data.addAll(resultJoke.getResult().getData());
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
