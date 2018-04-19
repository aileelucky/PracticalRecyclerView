package com.hz.recyvlerview.mannger;

/*
 * Created by gujiajia on 2015/12/21.
 * Email:aileelucky@gmail.com
 */
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.hz.recyvlerview.OverScrollListener;


/**
 * Created by shichaohui on 2015/8/3 0003.
 * <br/>
 * 增加了{@link OverScrollListener}的LinearLayoutManager
 */
public class PRLinearLayoutManager extends LinearLayoutManager {

    private OverScrollListener mListener;

    public PRLinearLayoutManager(Context context) {
        super(context);
    }

    public PRLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public PRLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
        if(listener != null)
        mListener = listener;
    }

}
