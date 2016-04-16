package com.xy.DayTenGoods.ui.article;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiaoyu on 2016/4/16.
 */
public class ArticleItemModel {
    @SerializedName("id")
    public String id;
    @SerializedName("title")
    public String title;
    @SerializedName("main_image")
    public String main_image;
    @SerializedName("url")
    public String url;
    @SerializedName("content")
    public String content;
}
