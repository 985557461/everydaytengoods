package com.xy.DayTenGoods.ui.article;

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
import com.xy.DayTenGoods.util.GsonUtil;
import com.xy.DayTenGoods.util.ToastUtil;
import com.xy.DayTenGoods.util.okhttp.OkHttpUtils;
import com.xy.DayTenGoods.util.okhttp.callback.StringCallback;
import com.xy.DayTenGoods.util.recyclerview.AutoLoadMoreRecyclerView;
import com.xy.DayTenGoods.util.recyclerview.DividerGridItemDecoration;
import com.xy.DayTenGoods.util.recyclerview.LoadMoreInterface;
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
 * Created by liangyu on 2016/4/15.
 */
public class ArticleFragment extends Fragment {
    private PtrClassicFrameLayout refreshContainer;
    private AutoLoadMoreRecyclerView recyclerView;
    private ArticlesAdapter articlesAdapter;
    private List<ArticleItemModel> articleItemModels = new ArrayList<>();

    private static final int limit = 20;
    private int page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_fragment, null);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        refreshContainer = (PtrClassicFrameLayout) view.findViewById(R.id.refreshContainer);
        recyclerView = (AutoLoadMoreRecyclerView) view.findViewById(R.id.recyclerView);

        recyclerView.getRecyclerView().setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        recyclerView.getRecyclerView().addItemDecoration(new DividerGridItemDecoration(getContext(), R.drawable.goods_list_divider));

        refreshContainer.setLastUpdateTimeRelateObject(this);
        articlesAdapter = new ArticlesAdapter(getContext());
        recyclerView.setAdapter(articlesAdapter);

        refreshContainer.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, recyclerView.getRecyclerView(), header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refreshData();
            }
        });

        recyclerView.setLoadMoreInterface(new LoadMoreInterface() {
            @Override
            public void loadMore() {
                loadMoreData();
            }
        });

        refreshContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshContainer.autoRefresh();
            }
        },100);
    }

    private void refreshData() {
        page = 1;
        Map<String, String> params = new HashMap<>();
        params.put("limit", limit + "");
        params.put("page", page + "");
        OkHttpUtils.get()
                .params(params)
                .url(ServerConfig.BASE_URL_OFFICAL + ServerConfig.URL_GET_ARTICLES_LIST)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        refreshContainer.refreshComplete();
                        ToastUtil.makeShortText("网络连接失败了");
                    }

                    @Override
                    public void onResponse(String response) {
                        refreshContainer.refreshComplete();
                        ArticleListModel articleListModel = GsonUtil.transModel(response, ArticleListModel.class);
                        if (articleListModel == null || !"1".equals(articleListModel.code)) {
                            ToastUtil.makeShortText("网络连接失败了");
                            return;
                        }
                        if (articleListModel.articleList != null) {
                            articleItemModels.clear();
                            articleItemModels.addAll(articleListModel.articleList);
                            articlesAdapter.notifyDataSetChanged();
                            if (articleListModel.articleList.size() < limit) {//没有更多了
                                recyclerView.hasMore(false);
                            } else {//也许还有更多
                                recyclerView.hasMore(true);
                            }
                        }
                    }
                });
    }

    private void loadMoreData() {
        page++;
        final Map<String, String> params = new HashMap<>();
        params.put("limit", limit + "");
        params.put("page", page + "");
        OkHttpUtils.get()
                .params(params)
                .url(ServerConfig.BASE_URL_OFFICAL + ServerConfig.URL_GET_ARTICLES_LIST)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        page--;
                        recyclerView.loadMoreCompleted();
                        ToastUtil.makeShortText("网络连接失败了");
                    }

                    @Override
                    public void onResponse(String response) {
                        recyclerView.loadMoreCompleted();
                        ArticleListModel articleListModel = GsonUtil.transModel(response, ArticleListModel.class);
                        if (articleListModel == null || !"1".equals(articleListModel.code)) {
                            page--;
                            ToastUtil.makeShortText("网络连接失败了");
                            return;
                        }
                        if (articleListModel.articleList != null) {
                            articleItemModels.addAll(articleListModel.articleList);
                            articlesAdapter.notifyDataSetChanged();
                            if (articleListModel.articleList.size() < limit) {//没有更多了
                                recyclerView.hasMore(false);
                            } else {//也许还有更多
                                recyclerView.hasMore(true);
                            }
                        }
                    }
                });
    }


    private class ArticlesAdapter extends RecyclerView.Adapter<ArticleItemViewHolder> {
        private Context context;
        private LayoutInflater inflater;

        public ArticlesAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public ArticleItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = inflater.inflate(R.layout.article_item_view, viewGroup, false);
            ArticleItemViewHolder viewHolder = new ArticleItemViewHolder(context, view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ArticleItemViewHolder viewHolder, int i) {
            viewHolder.setData(articleItemModels.get(i));
        }

        @Override
        public int getItemCount() {
            return articleItemModels.size();
        }
    }
}
