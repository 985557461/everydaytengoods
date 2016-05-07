package com.xy.DayTenGoods.ui.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiaoyu on 2016/4/3.
 */
public class GoodsItemModel {
    @SerializedName("goods_name")
    public String goods_name;
    @SerializedName("main_image")
    public String main_image;
    @SerializedName("origin_price")
    public String origin_price;
    @SerializedName("now_price")
    public String now_price;
    @SerializedName("goods_id")
    public String goods_id;
    @SerializedName("goods_url")
    public String goods_url;
    @SerializedName("goods_valume")
    public String goods_valume;
}
