package com.xy.DayTenGoods.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.xy.DayTenGoods.R;

/**
 * Created by xiaoyu on 2016/5/7.
 */
public class MineFragment extends Fragment implements View.OnClickListener{
    private View notLoginView;
    private View cartView;
    private View orderView;
    private View couponView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_fragment, null);
        initViews(view);
        return view;
    }

    private void initViews(View view){
        notLoginView = view.findViewById(R.id.notLoginView);
        cartView = view.findViewById(R.id.cartView);
        orderView = view.findViewById(R.id.orderView);
        couponView = view.findViewById(R.id.couponView);

        notLoginView.setOnClickListener(this);
        cartView.setOnClickListener(this);
        orderView.setOnClickListener(this);
        couponView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.notLoginView:
                break;
            case R.id.cartView:
                break;
            case R.id.orderView:
                break;
            case R.id.couponView:
                break;
        }
    }
}
