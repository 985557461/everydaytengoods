package com.xy.DayTenGoods.ui.article;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xy.DayTenGoods.util.DisplayUtil;

/**
 * Created by xiaoyu on 2016/4/16.
 */
public class ContentLayoutView extends FrameLayout {
    private LinearLayout mContentContainer;
    private int mItemVerticalMargin = 0;
    private int mScreenWidth;
    private int mChildMaxWidth = 0;

    public ContentLayoutView(Context context) {
        super(context);
        init(context);
    }

    public ContentLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ContentLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContentContainer = new LinearLayout(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mContentContainer.setOrientation(LinearLayout.VERTICAL);
        mContentContainer.setGravity(Gravity.CENTER_HORIZONTAL);
        addView(mContentContainer, params);

        mItemVerticalMargin = DisplayUtil.dip2px(context, 25);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        mScreenWidth = wm.getDefaultDisplay().getWidth();
        mChildMaxWidth = mScreenWidth - getPaddingLeft() - getPaddingRight();
    }

    public void setData(String[] arrayString) {
        mContentContainer.removeAllViews();
        int contentCount = arrayString.length;
        for (int i = 0; i < contentCount; i++) {
            if (!TextUtils.isEmpty(arrayString[i])) {
                String text = arrayString[i].trim();
                View view = null;
                if (text.startsWith("http:")) {
                    ContentImageView imageView = new ContentImageView(getContext());
                    imageView.setData(text);
                    view = imageView;
                } else {
                    ContentTextView textView = new ContentTextView(getContext());
                    textView.setData(text);
                    view = textView;
                }
                mContentContainer.addView(view);
            }
        }
    }

    /**
     * 用来展示帖子内容的textview
     */
    private class ContentTextView extends TextView {
        public ContentTextView(Context context) {
            super(context);
            init(context);
        }

        public ContentTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        public ContentTextView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init(context);
        }

        private void init(Context context) {
            setTextSize(16);
            setTextColor(Color.parseColor("#444444"));
            setLineSpacing(DisplayUtil.dip2px(getContext(), 12), 1.0f);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = mItemVerticalMargin;
            setLayoutParams(params);
        }

        public void setData(String contentStr) {
            setText(contentStr);
        }
    }

    /**
     * 用来展示图片的Imageview
     */
    private class ContentImageView extends ImageView {
        public ContentImageView(Context context) {
            super(context);
        }

        public ContentImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public ContentImageView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public void setData(String imageUrl) {
            Glide.with(getContext()).load(imageUrl).asBitmap().into(target);
        }

        private SimpleTarget target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                if(bitmap == null){
                    return;
                }
                int bmpWidth = bitmap.getWidth();
                int bmpHeight = bitmap.getHeight();
                if (bmpWidth > mChildMaxWidth) {//图片的宽度比当前允许的最大宽度大，则动态计算高度,图片充满最大宽度，不能变形
                    int realHeight = (int) (bmpHeight * mChildMaxWidth * 1.0f / bmpWidth);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mChildMaxWidth, realHeight);
                    params.bottomMargin = mItemVerticalMargin;
                    setLayoutParams(params);
                } else {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.bottomMargin = mItemVerticalMargin;
                    setLayoutParams(params);
                }
                ContentImageView.this.setImageBitmap(bitmap);
            }
        };
    }
}
