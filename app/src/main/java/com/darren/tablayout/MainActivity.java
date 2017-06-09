package com.darren.tablayout;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.darren.tablayout.view.fragment.Fragment1;
import com.darren.tablayout.view.fragment.Fragment2;
import com.darren.tablayout.view.fragment.MyFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements
        ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener {
private FragmentTabHost mTabHost;
    private LayoutInflater layoutInflater;
    private Class fragmentArray[] = { Fragment1.class, Fragment2.class};
    private int imageViewArray[] = {R.drawable.tab_home_btn,R.drawable.tab_view_btn};
    private String textViewArray[] = { "新闻","笑话"};
    private List<Fragment> list = new ArrayList<Fragment>();
    private ViewPager vp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();//初始化控件
        initPage();//初始化页面
    }

    private void initPage() {
        Fragment1 fragment1 = new Fragment1();
        Fragment2 fragment2 = new Fragment2();
        list.add(fragment1);
        list.add(fragment2);

        //绑定Fragment适配器
        vp.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(), list));
        mTabHost.getTabWidget().setDividerDrawable(null);
    }

    private void initView() {
        vp = (ViewPager)findViewById(R.id.pager);
        vp.addOnPageChangeListener(this);//设置页面切换时的监听器
        layoutInflater = LayoutInflater.from(this);//加载布局管理器
        /*实例化FragmentTabHost对象并进行绑定*/
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);//绑定tabhost
        mTabHost.setup(this, getSupportFragmentManager(), R.id.pager);//绑定viewpager
        /*简单来说,是为了当点击下面菜单时,上面的ViewPager能滑动到对应的Fragment*/
        mTabHost.setOnTabChangedListener(this);
        int count = textViewArray.length;
        /*新建Tabspec选项卡并设置Tab菜单栏的内容和绑定对应的Fragment*/
        for (int i=0;i<count;i++){
            // 给每个Tab按钮设置标签、图标和文字
            TabHost.TabSpec tabSpec=mTabHost.newTabSpec(textViewArray[i]).setIndicator(getTabItemView(i));
            // 将Tab按钮添加进Tab选项卡中，并绑定Fragment
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            mTabHost.setTag(i);
            mTabHost.getTabWidget().getChildAt(i)
                    .setBackgroundResource(R.drawable.selector_tab_background);//设置Tab被选中的时候颜色改变
        }
    }
    private View getTabItemView(int i){
        //将xml布局转换为view对象
        View view = layoutInflater.inflate(R.layout.tab_content, null);
        //利用view对象，找到布局中的组件,并设置内容，然后返回视图
        ImageView mImageView = (ImageView) view
                .findViewById(R.id.tab_imageView);
        TextView mTextView = (TextView) view.findViewById(R.id.tab_textView);
        mImageView.setBackgroundResource(imageViewArray[i]);
        mTextView.setText(textViewArray[i]);
        return view;
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //表示在前一个页面滑动到后一个页面的时候，在前一个页面滑动前调用的方法
    }

    @Override
    public void onPageSelected(int position) {//这事件是在你页面跳转完毕的时候调用的。
        TabWidget tabWidget=mTabHost.getTabWidget();
        int oldFocusability=tabWidget.getDescendantFocusability();
        //设置View覆盖子类控件而直接获得焦点
        tabWidget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        mTabHost.setCurrentTab(position);//根据位置Postion设置当前的Tab
        tabWidget.setDescendantFocusability(oldFocusability);//设置取消分割线

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state ==1的时候表示正在滑动，state==2的时候表示滑动完毕了，state==0的时候表示什么都没做，就是停在那。
    }

    @Override
    public void onTabChanged(String tabId) {
        int position =mTabHost.getCurrentTab();
        vp.setCurrentItem(position);
    }
}
