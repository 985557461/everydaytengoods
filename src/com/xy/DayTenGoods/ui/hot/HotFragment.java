package com.xy.DayTenGoods.ui.hot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.xy.DayTenGoods.R;
import com.xy.DayTenGoods.common_background.ServerConfig;
import com.xy.DayTenGoods.ui.goods.GoodsListFragment;
import com.xy.DayTenGoods.ui.model.TypeItemModel;
import com.xy.DayTenGoods.ui.model.TypeListModel;
import com.xy.DayTenGoods.util.GsonUtil;
import com.xy.DayTenGoods.util.ToastUtil;
import com.xy.DayTenGoods.util.okhttp.OkHttpUtils;
import com.xy.DayTenGoods.util.okhttp.callback.StringCallback;
import com.xy.DayTenGoods.util.viewpager_indicator.TabPageIndicator;
import okhttp3.Call;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liangyu on 2016/4/15.
 */
public class HotFragment extends Fragment{
    private TabPageIndicator indicator;
    private ViewPager viewPager;
    private GoodsListFragmentAdapter goodsListFragmentAdapter;
    private List<TypeItemModel> typeItemModels = new ArrayList<>();
    private List<GoodsListFragment> fragments = new ArrayList<GoodsListFragment>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hot_fragment,null);
        initViews(view);
        return view;
    }

    private void initViews(View view){
        indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        loadTypes();
    }

    private void loadTypes() {
        Map<String, String> params = new HashMap<>();
        OkHttpUtils.get()
                .params(params)
                .url(ServerConfig.BASE_URL_OFFICAL + ServerConfig.URL_GET_TYPE_LIST)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        ToastUtil.makeShortText("数据获取失败");
                    }

                    @Override
                    public void onResponse(String response) {
                        TypeListModel typeListModel = GsonUtil.transModel(response, TypeListModel.class);
                        if (typeListModel != null && "1".equals(typeListModel.code)) {
                            if (typeListModel.typeList != null) {
                                initData(typeListModel.typeList);
                            }
                        } else {
                            ToastUtil.makeShortText("网络连接失败了");
                        }
                    }
                });
    }

    private void initData(List<TypeItemModel> itemModels) {
        typeItemModels.clear();
        typeItemModels.addAll(itemModels);
        if (typeItemModels.size() <= 0) {
            return;
        }
        for (int i = 0; i < typeItemModels.size(); i++) {
            GoodsListFragment goodsListFragment = new GoodsListFragment();
            goodsListFragment.setType(typeItemModels.get(i).id);
            fragments.add(goodsListFragment);
        }
        goodsListFragmentAdapter = new GoodsListFragmentAdapter(getChildFragmentManager());
        viewPager.setAdapter(goodsListFragmentAdapter);
        viewPager.setOffscreenPageLimit(2);
        indicator.setViewPager(viewPager);
    }

    private class GoodsListFragmentAdapter extends FragmentPagerAdapter {

        public GoodsListFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return typeItemModels.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return typeItemModels.get(position).type_name;
        }
    }
}
