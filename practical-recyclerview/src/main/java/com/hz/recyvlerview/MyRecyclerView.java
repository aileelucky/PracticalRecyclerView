package com.hz.recyvlerview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hz.recyvlerview.mannger.PRGridLayoutManager;
import com.hz.recyvlerview.mannger.PRLinearLayoutManager;
import com.hz.recyvlerview.mannger.PRStaggeredGridLayoutManager;

import java.util.ArrayList;

import gu.china.com.practical_recyclerview.R;

/**
 * Created by gujiajia on 2015/12/21.
 * Email:aileelucky@gmail.com
 */
public class MyRecyclerView extends RecyclerView implements Runnable {
    private Context mContext;

    private ArrayList<View> mFootViews;
    private Adapter mAdapter;

    private boolean isLoadingData = false; // 是否正在加载数据

    private LoadDataListener mLoadDataListener;

    private LinearLayout no_more_textView_layout;
    private PRProgressWheel pbLoadMore;
    private boolean canLoadMore;
    private Handler mHandler = new MyHandler();
    private String footerText;//底部文案
    private int footerColor = 0xAA333333;//底部文案字体颜色
    private int pageSize;//每页最大数量
    private DataChangeListener dataChangeListener;//数据刷新通知

    private OverScrollListener mOverScrollListener = new OverScrollListener() {
        @Override
        public void overScrollBy(int dy) {
            if (!isLoadingData) {
                mHandler.obtainMessage(0, dy, 0, null).sendToTarget();
                onScrollChanged(0, 0, 0, 0);
            }
        }
    };

    public void setDataChangeListener(DataChangeListener dataChangeListener){
        this.dataChangeListener = dataChangeListener;
    }

    public MyRecyclerView(Context context) {
        this(context, null);
    }

    public MyRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PR_RecyclerView);
        footerText = a.getString(R.styleable.PR_RecyclerView_pr_rrv_footer_text);
        footerColor = a.getColor(R.styleable.PR_RecyclerView_pr_rrv_footer_color, footerColor);
        pageSize = a.getInt(R.styleable.PR_RecyclerView_pr_rrv_page_size, 20);
        mContext = context;
        mFootViews = new ArrayList<>();
        setOverScrollMode(OVER_SCROLL_NEVER);
        canLoadMore = false;
        post(this);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
    }

    /**
     * 自定义Handler刷新数据
     */
    private static class MyHandler extends Handler {
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    updateViewSize(msg);
                    break;
            }
        }

        private void updateViewSize(Message msg) {
            // 重新设置View的宽高
            if (msg.obj != null) {
                View view = ((View) msg.obj);
                view.layout(view.getLeft(), 0, view.getRight(), view.getBottom());
            }
        }
    }

    /**
     * 设置刷新和加载更多数据的监听
     *
     * @param listener {@link LoadDataListener}
     */
    public void setLoadDataListener(LoadDataListener listener) {
        mLoadDataListener = listener;
    }

    /**
     * 加载更多数据完成后调用，必须在UI线程中
     */
    public void loadMoreComplete() {
        isLoadingData = false;
        if (mFootViews.size() > 0) {
            mFootViews.get(0).setVisibility(GONE);
            if(dataChangeListener != null){
                dataChangeListener.dataChange();
            }
        }
    }

    //是否可以上拉加载
    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (mFootViews.isEmpty()) {
            // 新建脚部
            LayoutInflater mInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            RelativeLayout mFooterView = (RelativeLayout) mInflater.inflate(R.layout.pr_recycle_footers_layout, this, false);
            mFootViews.add(mFooterView);
            no_more_textView_layout =  mFooterView.findViewById(R.id.no_more_textView_layout);
            TextView tvLoadMore = (TextView) mFooterView.findViewById(R.id.no_more_textView);
            if (!TextUtils.isEmpty(footerText)) {
                tvLoadMore.setText(footerText);
            }
            //tvLoadMore.setTextColor(footerColor);
            pbLoadMore = (PRProgressWheel) mFooterView
                    .findViewById(R.id.load_more_progressBar);
            pbLoadMore.setVisibility(VISIBLE);

        }
        // 根据是否有头部/脚部视图选择适配器
        if (mFootViews.isEmpty()) {
            super.setAdapter(adapter);
        } else {
            adapter = new WrapAdapter(mFootViews, adapter);
            super.setAdapter(adapter);
        }
        mAdapter = adapter;
    }


    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
    }

    @Override
    public void run() {
        LayoutManager manager = getLayoutManager();
        if (manager instanceof PRLinearLayoutManager) {
            ((PRLinearLayoutManager) manager).setOverScrollListener(mOverScrollListener);
        } else if (manager instanceof PRGridLayoutManager) {
            layoutGridAttach((PRGridLayoutManager) manager);
        } else if (manager instanceof PRStaggeredGridLayoutManager) {
            layoutStaggeredGridHeadAttach((PRStaggeredGridLayoutManager) manager);
        }
        if (mAdapter != null && ((WrapAdapter) mAdapter).getFootersCount() > 0) {
            // 脚部先隐藏
            mFootViews.get(0).setVisibility(GONE);
        }
    }

    /**
     * 给StaggeredGridLayoutManager附加头部和滑动过度监听
     *
     * @param manager {@link PRStaggeredGridLayoutManager}
     */
    private void layoutStaggeredGridHeadAttach(PRStaggeredGridLayoutManager manager) {
        manager.setOverScrollListener(mOverScrollListener);
    }

    /**
     * 给{@link PRGridLayoutManager}附加头部脚部和滑动过度监听
     *
     * @param manager {@link PRGridLayoutManager}
     */
    private void layoutGridAttach(final PRGridLayoutManager manager) {
        // GridView布局
        manager.setOverScrollListener(mOverScrollListener);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return ((WrapAdapter) mAdapter).isFooter(position) ? manager.getSpanCount() : 1;
            }
        });
        requestLayout();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        // 当前不滚动，且不是正在刷新或加载数据
        if (state == RecyclerView.SCROLL_STATE_IDLE && mLoadDataListener != null && !isLoadingData) {
            if (canLoadMore) {
                pbLoadMore.setVisibility(VISIBLE);
            }
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            // 获取最后一个正在显示的Item的位置
            if (layoutManager instanceof PRGridLayoutManager) {
                lastVisibleItemPosition = ((PRGridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof PRStaggeredGridLayoutManager) {
                int[] into = new int[((PRStaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((PRStaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            } else {
                lastVisibleItemPosition = ((PRLinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            if (layoutManager.getChildCount() > 0 && lastVisibleItemPosition >= layoutManager.getItemCount() - 1) {
                if (no_more_textView_layout != null && pbLoadMore != null) {
                    if (canLoadMore) {
                        // 加载更多
                        isLoadingData = true;
                        no_more_textView_layout.setVisibility(GONE);
                        pbLoadMore.setVisibility(VISIBLE);
                        mLoadDataListener.onLoadMore();
                        showFooter();
                    } else {
                        hideFooter();
                        if (lastVisibleItemPosition >= pageSize) {
                            no_more_textView_layout.setVisibility(VISIBLE);//显示没有更多的文案
                            showFooter();
                        }
                        pbLoadMore.setVisibility(GONE);
                    }
                } else {
                    hideFooter();
                }
            }
        }
    }

    //显示底部footer布局
    private void showFooter() {
        if (!mFootViews.isEmpty()) {
            mFootViews.get(0).setVisibility(VISIBLE);
        }
    }

    //隐藏底部布局
    private void hideFooter() {
        if (!mFootViews.isEmpty()) {
            mFootViews.get(0).setVisibility(GONE);
        }
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                break;
        }

        return super.onTouchEvent(ev);
    }

    /**
     * 自定义带有头部/脚部的适配器
     */
    private class WrapAdapter extends RecyclerView.Adapter<ViewHolder> {

        private RecyclerView.Adapter mAdapter;
        private ArrayList<View> mFootViews;

        public WrapAdapter(ArrayList<View> mFootViews, RecyclerView.Adapter mAdapter) {
            this.mAdapter = mAdapter;
            if (mFootViews == null) {
                this.mFootViews = new ArrayList<>();
            } else {
                this.mFootViews = mFootViews;
            }
        }


        /**
         * @param position 位置
         * @return 当前布局是否为Footer
         */
        public boolean isFooter(int position) {
            return position < getItemCount() && position >= getItemCount() - mFootViews.size();
        }

        /**
         * @return Footer的数量
         */
        public int getFootersCount() {
            return mFootViews.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == RecyclerView.INVALID_TYPE - 1) {
                StaggeredGridLayoutManager.LayoutParams params = new StaggeredGridLayoutManager.LayoutParams(
                        StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT, StaggeredGridLayoutManager.LayoutParams.WRAP_CONTENT);
                params.setFullSpan(true);
                mFootViews.get(0).setLayoutParams(params);
                return new HeaderViewHolder(mFootViews.get(0));
            }
            return mAdapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (position < 0) {
                return;
            }
            int adapterCount;
            if (mAdapter != null) {
                adapterCount = mAdapter.getItemCount();
                if (position < adapterCount) {
                    mAdapter.onBindViewHolder(holder, position);
                }
            }
        }

        @Override
        public int getItemCount() {
            if (mAdapter != null) {
                return getFootersCount() + mAdapter.getItemCount();
            } else {
                return getFootersCount();
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position < 0) {
                return RecyclerView.INVALID_TYPE;
            }
            int adapterCount;
            if (mAdapter != null) {
                adapterCount = mAdapter.getItemCount();
                if (position < adapterCount) {
                    return mAdapter.getItemViewType(position);
                }
            }
            return RecyclerView.INVALID_TYPE - 1;
        }

        @Override
        public long getItemId(int position) {
            if (mAdapter != null && position >= 0) {
                int adapterCount = mAdapter.getItemCount();
                if (position < adapterCount) {
                    return mAdapter.getItemId(position);
                }
            }
            return -1;
        }

        private class HeaderViewHolder extends RecyclerView.ViewHolder {
            public HeaderViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    /**
     * 刷新和加载更多数据的监听接口
     */
    public interface LoadDataListener {
        /**
         * 执行加载更多
         */
        void onLoadMore();

    }

    public void notifyData() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }
}
