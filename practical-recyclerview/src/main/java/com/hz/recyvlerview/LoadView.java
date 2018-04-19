package com.hz.recyvlerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import gu.china.com.practical_recyclerview.R;


/**
 * Created by Jin on 2016/7/14.
 * Description load View
 */
public class LoadView extends RelativeLayout {

    private LinearLayout noDataLayout;
    private RelativeLayout progressLayout;
    private ImageView imageView;
    private TextView textView;
    public TextView no_data_btn;


    public LoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = View.inflate(context, R.layout.common_load_view, null);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PR_LoadView);
        try {
            String noDataText = a.getString(R.styleable.PR_LoadView_pr_LoadViewNoDataText);
            int noDataImg = a.getResourceId(R.styleable.PR_LoadView_pr_LoadViewNoDataImg, R.drawable.space_icons);
            int defaultColor = a.getColor(R.styleable.PR_LoadView_pr_LoadViewNoDataBackGround, ContextCompat.getColor(context, R.color.pr_common_color_back_white));
            int noneBtnBg = a.getResourceId(R.styleable.PR_LoadView_pr_LoadViewNoDataBtnBackGround, R.drawable.pr_common_bg_oval_theme);
            int defaultNoneBtnColor = a.getColor(R.styleable.PR_LoadView_pr_LoadViewNoDataBtnTextColor, ContextCompat.getColor(context, R.color.pr_common_color_back_white));
            String noDataBtnText = a.getString(R.styleable.PR_LoadView_pr_LoadViewNoDataBtnText);


            noDataLayout = (LinearLayout) view.findViewById(R.id.no_data_layout);
            progressLayout = (RelativeLayout) view.findViewById(R.id.progress_layout);
            no_data_btn = (TextView) view.findViewById(R.id.no_data_btn);
            textView = (TextView) view.findViewById(R.id.no_data_text);
            if (null != noDataText)
                textView.setText(noDataText);

            imageView = (ImageView) view.findViewById(R.id.no_data_img);
            imageView.setBackgroundResource(noDataImg);
            noDataLayout.setBackgroundColor(defaultColor);
            no_data_btn.setText(noDataBtnText);
            no_data_btn.setTextColor(defaultNoneBtnColor);
            no_data_btn.setBackgroundResource(noneBtnBg);
            loadComplete();
        } finally {
            a.recycle();
        }

        this.addView(view);
    }

    public void loadComplete() {
        progressLayout.setVisibility(GONE);
        noDataLayout.setVisibility(GONE);
    }

    public void loadCompleteNoDataDef() {
        loadCompleteNoData(R.drawable.space_icons, R.string.no_data_default_message);
    }


    public void setNoDataTip(String tip){
        if(textView != null){
            textView.setText(tip);
        }
    }
    public void showNoneTextBtn(String text, OnClickListener onClickListener) {
        if (!TextUtils.isEmpty(text)){
            no_data_btn.setText(text);
        }
        no_data_btn.setVisibility(View.VISIBLE);
        no_data_btn.setOnClickListener(onClickListener);
    }

    /**
     * 已经在xml中配置属性
     */
    public void loadCompleteNoDataAttr() {
        imageView.setVisibility(VISIBLE);
        textView.setVisibility(VISIBLE);
        noDataLayout.setVisibility(VISIBLE);
        progressLayout.setVisibility(GONE);
    }

    /**
     * 没数据时候展现的View
     *
     * @param res  图片资源文件
     * @param text 文本
     */
    public void loadCompleteNoData(@DrawableRes int res, String text) {
        textView.setText(text);
        loadCompleteNoData(res);
    }

    public void loadCompleteNoData(@DrawableRes int res, @StringRes int str) {
        textView.setText(str);
        loadCompleteNoData(res);
    }

    public void loadCompleteNoData(@DrawableRes int res) {
        imageView.setBackgroundResource(res);
        imageView.setVisibility(VISIBLE);
        textView.setVisibility(VISIBLE);
        noDataLayout.setVisibility(VISIBLE);
        progressLayout.setVisibility(GONE);
    }

    public void loadCompleteNoData(String text) {
        imageView.setVisibility(VISIBLE);
        textView.setText(text);
        textView.setVisibility(VISIBLE);
        noDataLayout.setVisibility(VISIBLE);
        progressLayout.setVisibility(GONE);
    }

    public void loadCompleteNoDataNoImg(String text) {
        imageView.setVisibility(GONE);
        textView.setText(text);
        textView.setVisibility(VISIBLE);
        noDataLayout.setVisibility(VISIBLE);
        progressLayout.setVisibility(GONE);
    }

    /**
     * 正在加载中...
     */
    public void loading() {
        noDataLayout.setVisibility(GONE);
        progressLayout.setVisibility(VISIBLE);
    }
}
