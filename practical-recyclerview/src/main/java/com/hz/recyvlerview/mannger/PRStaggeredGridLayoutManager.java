package com.hz.recyvlerview.mannger;

/*
 * Created by gujiajia on 2015/12/21.
 * Email:aileelucky@gmail.com
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import com.hz.recyvlerview.OverScrollListener;


/**
 * Created by shichaohui on 2015/8/3 0003.
 * <br/>
 * 增加了{@link OverScrollListener}的StaggeredGridLayoutManager
 */
public class PRStaggeredGridLayoutManager extends StaggeredGridLayoutManager {

    private OverScrollListener mListener;

    public PRStaggeredGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public PRStaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int scrollRange = super.scrollVerticallyBy(dy, recycler, state);

        if(mListener != null){
            mListener.overScrollBy(dy - scrollRange);
        }

        return scrollRange;
    }

    /**
     * 设置滑动过度监听
     *
     * @param listener
     */
    public void setOverScrollListener(OverScrollListener listener) {
        mListener = listener;
    }

}
