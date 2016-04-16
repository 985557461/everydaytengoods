package com.xy.DayTenGoods.ui.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoyu on 2016/4/3.
 */
public class GoodsListModel {
    @SerializedName("code")
    public String code;
    @SerializedName("message")
    public String message;
    @SerializedName("goodsList")
    public List<GoodsItemModel> goodsList;
}
