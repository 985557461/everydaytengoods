package com.xy.DayTenGoods.ui.article;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoyu on 2016/4/16.
 */
public class ArticleListModel {
    @SerializedName("code")
    public String code;
    @SerializedName("message")
    public String message;
    @SerializedName("articleList")
    public List<ArticleItemModel> articleList;
}
