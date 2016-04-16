package com.xy.DayTenGoods.ui.goods;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.xy.DayTenGoods.R;
import com.xy.DayTenGoods.common_background.ServerConfig;
import com.xy.DayTenGoods.ui.model.GoodsItemModel;
import com.xy.DayTenGoods.ui.model.GoodsListModel;
import com.xy.DayTenGoods.util.GsonUtil;
import com.xy.DayTenGoods.util.ToastUtil;
import com.xy.DayTenGoods.util.okhttp.OkHttpUtils;
import com.xy.DayTenGoods.util.okhttp.callback.StringCallback;
import com.xy.DayTenGoods.util.recyclerview.DividerGridItemDecoration;
import com.xy.DayTenGoods.util.ultra_pull_refresh.PtrClassicFrameLayout;
import com.xy.DayTenGoods.util.ultra_pull_refresh.PtrDefaultHandler;
import com.xy.DayTenGoods.util.ultra_pull_refresh.PtrFrameLayout;
import com.xy.DayTenGoods.util.ultra_pull_refresh.PtrHandler;
import okhttp3.Call;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaoyu on 2016/4/12.
 */
public class GoodsListFragment extends Fragment {
    private PtrClassicFrameLayout refreshContainer;
    private RecyclerView recyclerView;
    private GoodsAdapter goodsAdapter;
    private RecyclerViewSpaceHeaderView headerView;
    private List<GoodsItemModel> goodsItemModelList = new ArrayList<>();
    private String type;

    /**
     * 在这里实现Fragment数据的缓加载.
     *
     * @param isVisibleToUser
     */
    protected boolean isVisible;
    private boolean isLoadData = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.goods_list_fragment, null);
        init(view);
        return view;
    }

    private void init(View view) {
        refreshContainer = (PtrClassicFrameLayout) view.findViewById(R.id.refreshContainer);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(getContext(), R.drawable.goods_list_divider));

        headerView = new RecyclerViewSpaceHeaderView(getContext());
        headerView.attachTo(recyclerView);

        refreshContainer.setLastUpdateTimeRelateObject(this);
        goodsAdapter = new GoodsAdapter(getContext());
        recyclerView.setAdapter(goodsAdapter);

        refreshContainer.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, recyclerView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                loadGoods();
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint() && !isLoadData) {
            isVisible = true;
            loadGoods();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isLoadData = false;
    }

    public void setType(String type) {
        this.type = type;
    }

    private void loadGoods() {
        if (refreshContainer != null) {
            refreshContainer.autoRefresh();
        }
        Map<String, String> params = new HashMap<>();
        params.put("type", type);
        OkHttpUtils.get()
                .params(params)
                .url(ServerConfig.BASE_URL_OFFICAL + ServerConfig.URL_GET_GOODS_LIST)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        if (refreshContainer != null) {
                            refreshContainer.refreshComplete();
                        }
                        ToastUtil.makeShortText("数据获取失败");
                    }

                    @Override
                    public void onResponse(String response) {
                        if (refreshContainer != null) {
                            refreshContainer.refreshComplete();
                        }
                        GoodsListModel goodsListModel = GsonUtil.transModel(response, GoodsListModel.class);
                        if (goodsListModel != null && "1".equals(goodsListModel.code)) {
                            isLoadData = true;
                            if (goodsListModel.goodsList != null) {
                                goodsItemModelList.clear();
                                goodsItemModelList.addAll(goodsListModel.goodsList);
                                goodsAdapter.notifyDataSetChanged();
                            }
                        } else {
                            ToastUtil.makeShortText("网络连接失败了");
                        }
                    }
                });
    }

    private class GoodsAdapter extends RecyclerView.Adapter<GoodsItemViewHolder> {
        private Context context;
        private LayoutInflater inflater;

        public GoodsAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public GoodsItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = inflater.inflate(R.layout.goods_item_view, viewGroup, false);
            GoodsItemViewHolder viewHolder = new GoodsItemViewHolder(context, view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(GoodsItemViewHolder viewHolder, int i) {
            viewHolder.setData(goodsItemModelList.get(i));
        }

        @Override
        public int getItemCount() {
            return goodsItemModelList.size();
        }
    }
}
