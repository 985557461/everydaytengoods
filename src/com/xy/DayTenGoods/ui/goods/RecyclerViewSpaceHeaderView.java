package com.xy.DayTenGoods.ui.goods;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.xy.DayTenGoods.util.DisplayUtil;
import com.xy.DayTenGoods.util.recyclerview.RecyclerViewHeader;

/**
 * Created by xiaoyu on 2016/4/3.
 */
public class RecyclerViewSpaceHeaderView extends RecyclerViewHeader {
    public RecyclerViewSpaceHeaderView(Context context) {
        super(context);
        init(context);
    }

    public RecyclerViewSpaceHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RecyclerViewSpaceHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(context, 9));
        View spaceView = new View(context);
        spaceView.setBackgroundColor(Color.parseColor("#00000000"));
        spaceView.setLayoutParams(params);
        addView(spaceView);
    }
}
