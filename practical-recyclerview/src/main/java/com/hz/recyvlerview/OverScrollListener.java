package com.hz.recyvlerview;

/**
 * Created by gujiajia on 2015/12/21.
 * Email:aileelucky@gmail.com
 * 滑动过度的监听接口
 */
public interface OverScrollListener {

    /**
     * 滑动过度时调用的方法
     *
     * @param dy 每毫秒滑动的距离
     */
    void overScrollBy(int dy);

}