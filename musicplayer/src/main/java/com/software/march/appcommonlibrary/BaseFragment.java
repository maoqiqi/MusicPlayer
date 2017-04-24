package com.software.march.appcommonlibrary;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Doc.March
 * @version V 1.0
 * @Description Fragment 基类, 继承Fragment
 * @date 2017/4/1
 */
public abstract class BaseFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    /**
     * 上下文
     */
    protected Context mContext;

    /**
     * 根布局
     */
    protected View mRootView;

    /**
     * 控件是否初始化完成
     */
    private boolean isViewCreated;

    /**
     * 数据是否已加载完毕
     */
    private boolean isLoadDataCompleted;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isViewCreated && !isLoadDataCompleted) {
            isLoadDataCompleted = true;
            loadData();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        afterCreate(savedInstanceState);
        isViewCreated = true;
    }

    // 为什么 loadData() 会在两个地方执行? 在 setUserVisibleHint 方法里执行我还能理解,为什么 onActivityCreated 也要执行呢?
    // 因为,ViewPager 默认显示第一页,第一页肯定要先加载数据,而且 setUserVisibleHint 的执行顺序又是在 onCreateView 之前,
    // 同时 onCreateView 需要初始化界面和修改 isViewCreated 的值。所以就需要在 onActivityCreated 里执行一次。
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint()) {
            isLoadDataCompleted = true;
            loadData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext = null;
        mRootView = null;
    }

    /**
     * 强制子类重写,返回布局Id
     *
     * @return layout Id
     */
    protected abstract int getLayoutId();

    /**
     * 强制子类重写
     *
     * @param savedInstanceState
     */
    protected abstract void afterCreate(Bundle savedInstanceState);

    /***
     * 子类实现加载数据的方法
     */
    public void loadData() {

    }

    /**
     * 根据 id 得到 View
     *
     * @param id 视图资源Id
     * @return 当前视图对象
     */
    protected final View findViewById(int id) {
        if (mRootView == null)
            return null;
        return mRootView.findViewById(id);
    }
}