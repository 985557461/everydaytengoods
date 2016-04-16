package com.xy.DayTenGoods.ui.goods;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.trade.TradeConstants;
import com.alibaba.sdk.android.trade.TradeService;
import com.alibaba.sdk.android.trade.callback.TradeProcessCallback;
import com.alibaba.sdk.android.trade.model.TradeResult;
import com.alibaba.sdk.android.trade.page.ItemDetailPage;
import com.bumptech.glide.Glide;
import com.xy.DayTenGoods.R;
import com.xy.DayTenGoods.ui.model.GoodsItemModel;
import com.xy.DayTenGoods.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaoyu on 2016/3/24.
 */
public class GoodsItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Context context;
    private View rootView;
    private ImageView imageView;
    private TextView goodsName;
    private TextView nowPrice;

    private GoodsItemModel itemModel;

    public GoodsItemViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        this.rootView = itemView;

        imageView = (ImageView) rootView.findViewById(R.id.imageView);
        goodsName = (TextView) rootView.findViewById(R.id.goodsName);
        nowPrice = (TextView) rootView.findViewById(R.id.nowPrice);

        rootView.setOnClickListener(this);
    }

    public void setData(GoodsItemModel itemModel) {
        this.itemModel = itemModel;
        if (itemModel == null) {
            return;
        }
        if (!TextUtils.isEmpty(itemModel.main_image)) {
            Glide.with(context).load(itemModel.main_image).into(imageView);
        } else {
            Glide.with(context).load("").into(imageView);
        }
        if (!TextUtils.isEmpty(itemModel.goods_name)) {
            goodsName.setText(itemModel.goods_name);
        } else {
            goodsName.setText("");
        }
        if (!TextUtils.isEmpty(itemModel.now_price)) {
            nowPrice.setText(itemModel.now_price);
        } else {
            nowPrice.setText("");
        }
    }

    @Override
    public void onClick(View view) {
        showItemDetailPage();
    }

    public void showItemDetailPage() {
        if (itemModel == null || TextUtils.isEmpty(itemModel.goods_id)) {
            ToastUtil.makeShortText("商品暂时找不到了奥");
            return;
        }
        TradeService tradeService = AlibabaSDK.getService(TradeService.class);
        Map<String, String> exParams = new HashMap<String, String>();
        exParams.put(TradeConstants.ITEM_DETAIL_VIEW_TYPE, TradeConstants.TAOBAO_NATIVE_VIEW);
        ItemDetailPage itemDetailPage = new ItemDetailPage(itemModel.goods_id, exParams);
        tradeService.show(itemDetailPage, null, (Activity) context, null, new TradeProcessCallback() {
            @Override
            public void onFailure(int code, String msg) {
            }

            @Override
            public void onPaySuccess(TradeResult tradeResult) {
            }
        });
    }
}
