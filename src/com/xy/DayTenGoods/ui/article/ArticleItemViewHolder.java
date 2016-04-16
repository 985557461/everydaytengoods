package com.xy.DayTenGoods.ui.article;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.xy.DayTenGoods.R;
import com.xy.DayTenGoods.util.ToastUtil;

/**
 * Created by xiaoyu on 2016/3/24.
 */
public class ArticleItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Context context;
    private View rootView;
    private ImageView imageView;
    private TextView articleName;

    private ArticleItemModel itemModel;

    public ArticleItemViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        this.rootView = itemView;

        imageView = (ImageView) rootView.findViewById(R.id.imageView);
        articleName = (TextView) rootView.findViewById(R.id.articleName);

        rootView.setOnClickListener(this);
    }

    public void setData(ArticleItemModel itemModel) {
        this.itemModel = itemModel;
        if (itemModel == null) {
            return;
        }
        if (!TextUtils.isEmpty(itemModel.main_image)) {
            Glide.with(context).load(itemModel.main_image).into(imageView);
        } else {
            Glide.with(context).load("").into(imageView);
        }
        if (!TextUtils.isEmpty(itemModel.title)) {
            articleName.setText(itemModel.title);
        } else {
            articleName.setText("");
        }
    }

    @Override
    public void onClick(View view) {
        showItemDetailPage();
    }

    public void showItemDetailPage() {
        if (itemModel == null) {
            ToastUtil.makeShortText("文章暂时找不到了奥");
            return;
        }
        ActivityArticleInfo.open((Activity) context,itemModel);
    }
}
