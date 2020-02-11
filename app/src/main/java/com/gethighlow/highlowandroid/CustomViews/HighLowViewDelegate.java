package com.gethighlow.highlowandroid.CustomViews;

public interface HighLowViewDelegate {
    void willAddHigh(String highlowid);
    void willAddLow(String highlowid);
    void willEditHigh(String highlowid);
    void willEditLow(String highlowid);
    void willLike(String highlowid);
    void willUnLike(String highlowid);
    void willUnFlag(String highlowid);
    void willFlag(String highlowid);
}
