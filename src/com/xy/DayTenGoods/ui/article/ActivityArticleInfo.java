package com.xy.DayTenGoods.ui.article;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.xy.DayTenGoods.R;
import com.xy.DayTenGoods.ui.common.ActivityBaseNoSliding;

/**
 * Created by xiaoyu on 2016/4/16.
 */
public class ActivityArticleInfo extends ActivityBaseNoSliding implements View.OnClickListener {
    private View backView;
    private ImageView mainImage;
    private TextView title;
    private ContentLayoutView contentLayoutView;

    public static ArticleItemModel destItemModel;
    private ArticleItemModel articleItemModel;

    public static void open(Activity activity, ArticleItemModel articleItemModel) {
        destItemModel = articleItemModel;
        Intent intent = new Intent(activity, ActivityArticleInfo.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        articleItemModel = destItemModel;
        destItemModel = null;
        setContentView(R.layout.activity_article_info);
    }

    @Override
    protected void getViews() {
        backView = findViewById(R.id.backView);
        mainImage = (ImageView) findViewById(R.id.mainImage);
        title = (TextView) findViewById(R.id.title);
        contentLayoutView = (ContentLayoutView) findViewById(R.id.contentLayoutView);
    }

    @Override
    protected void initViews() {
        if(articleItemModel != null){
            if(!TextUtils.isEmpty(articleItemModel.title)){
                title.setText(articleItemModel.title);
            }
            if(!TextUtils.isEmpty(articleItemModel.main_image)){
                Glide.with(this).load(articleItemModel.main_image).into(mainImage);
            }
            if(!TextUtils.isEmpty(articleItemModel.content)){
                String[] contents = articleItemModel.content.split("\\$");
                contentLayoutView.setData(contents);
            }
        }
    }

    @Override
    protected void setListeners() {
        backView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backView:
                finish();
                break;
        }
    }
}
