package com.xy.DayTenGoods.util.okhttp.builder;


import com.xy.DayTenGoods.util.okhttp.OkHttpUtils;
import com.xy.DayTenGoods.util.okhttp.request.OtherRequest;
import com.xy.DayTenGoods.util.okhttp.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers).build();
    }
}
