package com.rixin.cold.fragment.tabs;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.rixin.cold.adapter.TabsRecyclerViewAdapter;
import com.rixin.cold.domain.ColdInfo;
import com.rixin.cold.fragment.BaseFragment;
import com.rixin.cold.global.GlobalConstants;
import com.rixin.cold.utils.CacheUtils;
import com.rixin.cold.utils.NetworkUtils;
import com.rixin.cold.utils.SPUtils;
import com.rixin.cold.utils.UIUtils;
import com.rixin.cold.widget.LoadingPage;
import com.rixin.cold.widget.MyRecyclerView;

import java.util.ArrayList;

/**
 * 历史
 * Created by 飘渺云轩 on 2017/2/9.
 */

public class HistoryFragment extends BaseFragment {

    private ArrayList<ColdInfo> mData;
    private TabsRecyclerViewAdapter mAdapter;
    private int page = 1;
    private int currentSize = 0;

    @Override
    public View onCreateSuccessPage() {
        LinearLayoutManager manager = new LinearLayoutManager(UIUtils.getContext());
        mAdapter = new TabsRecyclerViewAdapter(mData);
        MyRecyclerView myRecyclerView = new MyRecyclerView(manager, mAdapter) {

            @Override
            public void onItemClick(View view, int position) {
                toDetailsPage(mData.get(position - 1).contentUrl);
            }

            @Override
            public void onRefresh() {
                if (NetworkUtils.isNetworkConnected(UIUtils.getContext())) {
                    mData = getServiceData(GlobalConstants.TABS_HISTORY_URL);
                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.setDataChangeListener(mData);
                        }
                    });
                } else {
                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UIUtils.getContext(), "当前网络不可用", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onLoadMore() {
                if (NetworkUtils.isNetworkConnected(UIUtils.getContext())) {
                    page += 1;
                    ArrayList<ColdInfo> moreData = getServiceData(GlobalConstants.TABS_HISTORY_NEXT_URL + page);
                    if (moreData != null) {
                        mData.addAll(moreData);
                    }
                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            if (currentSize == mData.size()) {
                                Toast.makeText(UIUtils.getContext(), "到底了哦，请移步其他分类阅读...", Toast.LENGTH_SHORT).show();
                            }
                            currentSize = mData.size();
                            mAdapter.setDataChangeListener(mData);
                        }
                    });
                } else {
                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UIUtils.getContext(), "当前网络不可用", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        };
        return myRecyclerView.getView();
    }

    @Override
    public LoadingPage.ResultState onLoadData() {
        page = 1;
        String beforeTime = SPUtils.getString(UIUtils.getContext(), GlobalConstants.BEFORE_TIME_KEY, getCurrentDate());
        if (beforeTime.equals(getCurrentDate())) {
            // 如果日期相符则加载缓存
            mData = CacheUtils.getCache(GlobalConstants.TABS_HISTORY_CACHE_KAY);
            if (mData == null) {
                mData = getServiceData(GlobalConstants.TABS_HISTORY_URL);
                // 写缓存
                CacheUtils.setCache(GlobalConstants.TABS_HISTORY_CACHE_KAY, getCacheString(mData));
            }
        } else {
            mData = getServiceData(GlobalConstants.TABS_HISTORY_URL);
            // 写缓存
            CacheUtils.setCache(GlobalConstants.TABS_HISTORY_CACHE_KAY, getCacheString(mData));
        }
        return check(mData);
    }

}
