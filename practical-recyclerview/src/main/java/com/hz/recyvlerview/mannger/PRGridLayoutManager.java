package com.hz.recyvlerview.mannger;

/*
 * Created by gujiajia on 2015/12/21.
 * Email:aileelucky@gmail.com
 */
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.hz.recyvlerview.OverScrollListener;


/**
 * Created by shichaohui on 2015/8/3 0003.
 * <br/>
 * 增加了{@link OverScrollListener}的GridLayoutManage
 */
public class PRGridLayoutManager extends GridLayoutManager {

    private OverScrollListener mListener;

    public PRGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public PRGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public PRGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
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
