package com.hz.recyvlerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import java.util.List;

import gu.china.com.practical_recyclerview.R;

/**
 * Created by GuJiaJia on 2018/4/19.
 * E-mail 965939858@qq.com
 * Tel: 15050261230
 */
public class PracticalRecyclerView extends FrameLayout {

    public LoadView loadView;
    public MyRecyclerView myRecyclerView;

    public PracticalRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.pr_recyvlerview, this);
        loadView = (LoadView) findViewById(R.id.loadView);
        myRecyclerView = (MyRecyclerView) findViewById(R.id.pr_recycler_view);
        myRecyclerView.setDataChangeListener(new DataChangeListener() {
            @Override
            public void dataChange() {
                setLoadView();
            }
        });
    }

    //设置页面占位图
    private void setLoadView() {
        if (loadView == null || myRecyclerView == null) {
            return;
        }
        int objects = myRecyclerView.getAdapter().getItemCount();
        if (objects > 1) {
            loadView.loadComplete();
        } else {
            loadView.loadCompleteNoDataAttr();
        }
    }
}
