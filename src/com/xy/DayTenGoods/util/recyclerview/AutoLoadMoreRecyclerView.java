package com.xy.DayTenGoods.util.recyclerview;

import android.content.Context;
import android.support.v7.widget.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import com.xy.DayTenGoods.R;

public class AutoLoadMoreRecyclerView extends LinearLayout {
    private RecyclerView mRecyclerView;
    private LoadMoreInterface loadMoreInterface;
    private boolean hasMore = false;
    private boolean isLoadingMore = false;
    private View mFooterView;
    private Context mContext;

    public AutoLoadMoreRecyclerView(Context context) {
        super(context);
        initView(context);
    }

    public AutoLoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.load_more_recyclerview, null);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setVerticalScrollBarEnabled(true);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setOnScrollListener(new OnRcvScrollListener());
        mRecyclerView.setOnTouchListener(new onTouchRecyclerView());

        mFooterView = view.findViewById(R.id.footerView);
        mFooterView.setVisibility(View.GONE);
        addView(view);
    }


    /**
     * LinearLayoutManager
     */
    public void setLinearLayout(int orientation) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(orientation);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * GridLayoutManager
     */
    public void setGridLayout(int orientation, int spanCount) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, spanCount);
        gridLayoutManager.setOrientation(orientation);
        mRecyclerView.setLayoutManager(gridLayoutManager);
    }


    /**
     * StaggeredGridLayoutManager
     */
    public void setStaggeredGridLayout(int orientation, int spanCount) {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(spanCount, orientation);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mRecyclerView.getLayoutManager();
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void scrollToTop() {
        mRecyclerView.scrollToPosition(0);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter != null) {
            mRecyclerView.setAdapter(adapter);
        }
    }

    /**
     * Solve IndexOutOfBoundsException exception
     */
    public class onTouchRecyclerView implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (isLoadingMore) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void loadMoreCompleted() {
        isLoadingMore = false;
        mFooterView.setVisibility(View.GONE);
    }

    public void setLoadMoreInterface(LoadMoreInterface listener) {
        loadMoreInterface = listener;
    }

    public void hasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    /**
     * recyclerview 滚动监听,这个也可以使用
     */
    class RecyclerViewOnScroll extends RecyclerView.OnScrollListener {

        public RecyclerViewOnScroll() {
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastCompletelyVisibleItem = 0;
            int firstVisibleItem = 0;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int totalItemCount = layoutManager.getItemCount();
            if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = ((GridLayoutManager) layoutManager);
                //Position to find the final item of the current LayoutManager
                lastCompletelyVisibleItem = gridLayoutManager.findLastCompletelyVisibleItemPosition();
                firstVisibleItem = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
            } else if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) layoutManager);
                lastCompletelyVisibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                firstVisibleItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager staggeredGridLayoutManager = ((StaggeredGridLayoutManager) layoutManager);
                // since may lead to the final item has more than one StaggeredGridLayoutManager the particularity of the so here that is an array
                // this array into an array of position and then take the maximum value that is the last show the position value
                int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(lastPositions);
                lastCompletelyVisibleItem = findMax(lastPositions);
                firstVisibleItem = staggeredGridLayoutManager.findFirstVisibleItemPositions(lastPositions)[0];
            }
            if (firstVisibleItem == 0) {
                //todo
            } else {
                //todo
            }
            if (hasMore && (lastCompletelyVisibleItem == totalItemCount - 1) && !isLoadingMore && (dx > 0 || dy > 0)) {
                isLoadingMore = true;
                if (loadMoreInterface != null) {
                    mFooterView.setVisibility(View.VISIBLE);
                    invalidate();
                    loadMoreInterface.loadMore();
                    isLoadingMore = true;
                }
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

        }

        //To find the maximum value in the array
        private int findMax(int[] lastPositions) {
            int max = lastPositions[0];
            for (int value : lastPositions) {
                //       int max    = Math.max(lastPositions,value);
                if (value > max) {
                    max = value;
                }
            }
            return max;
        }
    }

    public static enum LAYOUT_MANAGER_TYPE {
        LINEAR,
        GRID,
        STAGGERED_GRID
    }

    public class OnRcvScrollListener extends RecyclerView.OnScrollListener {
        /**
         * layoutManager的类型（枚举）
         */
        protected LAYOUT_MANAGER_TYPE layoutManagerType;

        /**
         * 最后一个的位置
         */
        private int[] lastPositions;

        /**
         * 最后一个可见的item的位置
         */
        private int lastVisibleItemPosition;

        /**
         * 当前滑动的状态
         */
        private int currentScrollState = 0;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManagerType == null) {
                if (layoutManager instanceof LinearLayoutManager) {
                    layoutManagerType = LAYOUT_MANAGER_TYPE.LINEAR;
                } else if (layoutManager instanceof GridLayoutManager) {
                    layoutManagerType = LAYOUT_MANAGER_TYPE.GRID;
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    layoutManagerType = LAYOUT_MANAGER_TYPE.STAGGERED_GRID;
                } else {
                    throw new RuntimeException(
                            "Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
                }
            }

            switch (layoutManagerType) {
                case LINEAR:
                    lastVisibleItemPosition = ((LinearLayoutManager) layoutManager)
                            .findLastVisibleItemPosition();
                    break;
                case GRID:
                    lastVisibleItemPosition = ((GridLayoutManager) layoutManager)
                            .findLastVisibleItemPosition();
                    break;
                case STAGGERED_GRID:
                    StaggeredGridLayoutManager staggeredGridLayoutManager
                            = (StaggeredGridLayoutManager) layoutManager;
                    if (lastPositions == null) {
                        lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                    }
                    staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                    lastVisibleItemPosition = findMax(lastPositions);
                    break;
            }

        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            currentScrollState = newState;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            if ((visibleItemCount > 0 && hasMore && !isLoadingMore && currentScrollState == RecyclerView.SCROLL_STATE_IDLE && (lastVisibleItemPosition) >= totalItemCount - 1)) {
                isLoadingMore = true;
                if (loadMoreInterface != null) {
                    mFooterView.setVisibility(View.VISIBLE);
                    invalidate();
                    loadMoreInterface.loadMore();
                    isLoadingMore = true;
                }
            }
        }

        private int findMax(int[] lastPositions) {
            int max = lastPositions[0];
            for (int value : lastPositions) {
                if (value > max) {
                    max = value;
                }
            }
            return max;
        }
    }
}
