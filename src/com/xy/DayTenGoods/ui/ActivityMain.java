package com.xy.DayTenGoods.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.xy.DayTenGoods.R;
import com.xy.DayTenGoods.ui.article.ArticleFragment;
import com.xy.DayTenGoods.ui.common.ActivityBaseNoSliding;
import com.xy.DayTenGoods.ui.hot.HotFragment;
import com.xy.DayTenGoods.util.ActivityManagerUtil;
import com.xy.DayTenGoods.util.ToastUtil;

public class ActivityMain extends ActivityBaseNoSliding implements View.OnClickListener {
    private View hotLL;
    private ImageView hotImageView;
    private TextView hotTextView;
    private View zhiNanLL;
    private ImageView zhiNanImageView;
    private TextView zhiNanTextView;
    private View myCenterLL;
    private ImageView myCenterImageView;
    private TextView myCenterTextView;

    /**
     * fragments*
     */
    private Fragment mFragmentCurrent;

    private HotFragment hotFragment;
    private ArticleFragment articleFragment;
    private MineFragment mineFragment;

    /**
     * back finish*
     */
    private long lastTime;
    private static final long TIME_LONG = 3000000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    protected void getViews() {
        hotLL = findViewById(R.id.hotLL);
        hotImageView = (ImageView) findViewById(R.id.hotImageView);
        hotTextView = (TextView) findViewById(R.id.hotTextView);
        zhiNanLL = findViewById(R.id.zhiNanLL);
        zhiNanImageView = (ImageView) findViewById(R.id.zhiNanImageView);
        zhiNanTextView = (TextView) findViewById(R.id.zhiNanTextView);
        myCenterLL = findViewById(R.id.myCenterLL);
        myCenterImageView = (ImageView) findViewById(R.id.myCenterImageView);
        myCenterTextView = (TextView) findViewById(R.id.myCenterTextView);
    }

    @Override
    protected void initViews() {
        hotTextView.setSelected(true);
        setDefaultFragment();
    }

    @Override
    protected void setListeners() {
        hotLL.setOnClickListener(this);
        zhiNanLL.setOnClickListener(this);
        myCenterLL.setOnClickListener(this);
    }

    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hotFragment = new HotFragment();
        transaction.add(R.id.fragementLayout, hotFragment);
        transaction.commit();
        mFragmentCurrent = hotFragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hotLL:
                changeTab(0);
                break;
            case R.id.zhiNanLL:
                changeTab(1);
                break;
            case R.id.myCenterLL:
                changeTab(2);
                break;
        }
    }

    private void changeTab(int index) {
        switch (index) {
            case 0: {
                hotImageView.setImageResource(R.drawable.ic_tab_select_selected);
                zhiNanImageView.setImageResource(R.drawable.ic_tab_home_normal);
                myCenterImageView.setImageResource(R.drawable.ic_tab_profile_normal);

                hotTextView.setSelected(true);
                zhiNanTextView.setSelected(false);
                myCenterTextView.setSelected(false);

                if (hotFragment == null) {
                    hotFragment = new HotFragment();
                }
                switchContent(mFragmentCurrent, hotFragment);
            }
            break;
            case 1: {
                hotImageView.setImageResource(R.drawable.ic_tab_select_normal);
                zhiNanImageView.setImageResource(R.drawable.ic_tab_home_selected);
                myCenterImageView.setImageResource(R.drawable.ic_tab_profile_normal);

                hotTextView.setSelected(false);
                zhiNanTextView.setSelected(true);
                myCenterTextView.setSelected(false);

                if (articleFragment == null) {
                    articleFragment = new ArticleFragment();
                }
                switchContent(mFragmentCurrent, articleFragment);
            }
            break;
            case 2: {
                hotImageView.setImageResource(R.drawable.ic_tab_select_normal);
                zhiNanImageView.setImageResource(R.drawable.ic_tab_home_normal);
                myCenterImageView.setImageResource(R.drawable.ic_tab_profile_selected);

                hotTextView.setSelected(false);
                zhiNanTextView.setSelected(false);
                myCenterTextView.setSelected(true);

                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                }
                switchContent(mFragmentCurrent, mineFragment);
            }
            break;
        }
    }

    public void switchContent(Fragment from, Fragment to) {
        if (from != to) {
            mFragmentCurrent = to;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            // to.getLoaderManager().hasRunningLoaders();
            // 先判断是否被add过
            if (!to.isAdded()) {
                if (from != null && from.isAdded()) {
                    transaction.hide(from);
                }
                // 隐藏当前的fragment，add下一个到Activity中
                transaction.add(R.id.fragementLayout, to).commitAllowingStateLoss();
            } else {
                // 隐藏当前的fragment，显示下一个
                transaction.hide(from).show(to).commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onBackPressed() {
        long t = System.currentTimeMillis();
        if (t - lastTime < TIME_LONG) {
            ActivityManagerUtil.getInstance().killActivity();
        } else {
            ToastUtil.makeShortText("再按一次退出应用");
            lastTime = t;
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        ActivityManagerUtil.getInstance().killActivity();
        super.onDestroy();
    }
}
