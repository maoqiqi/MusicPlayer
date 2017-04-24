package com.software.march.appcommonlibrary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * @author Doc.March
 * @version V 1.0
 * @Description 基类, 公共RecyclerView.Adapter类
 * @date 2017/4/2
 */
public abstract class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    /**
     * 上下文环境
     */
    protected Context mContext;

    /**
     * 数据
     */
    protected List<T> mData;

    /**
     * 每项点击事件
     */
    private OnItemClickListener mOnItemClickListener;

    /**
     * 每项长按事件
     */
    OnItemLongClickListener mOnItemLongClickListener;

    public RecyclerViewAdapter(Context context, List<T> data) {
        this.mContext = context;
        this.mData = data;
    }

    /**
     * 相当于于getView方法中创建View和ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // View.inflate(mContext, getLayoutId(), null);
        View itemView = LayoutInflater.from(mContext).inflate(getLayoutId(), parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    /**
     * 相当于于getView绑定数据部分的代码
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        convert(holder, position, mData.get(position));
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mOnItemLongClickListener != null) {
                    if (!mOnItemLongClickListener.onItemLongClick(v, position)) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(v, position);
                        }
                    }
                } else {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    /**
     * 强制子类重写,返回布局Id
     *
     * @return layout Id
     */
    protected abstract int getLayoutId();

    /**
     * 强制子类重写,得到View或者设置数据
     *
     * @param holder
     * @param position
     * @param item
     */
    public abstract void convert(ViewHolder holder, int position, T item);

    /**
     * RecyclerView通用的ViewHolder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // 布局文件
        private final View mConvertView;

        // 和 Map 类似,但是比 Map 效率高,不过键只能为Integer,用于存储控件.
        private final SparseArray<View> mViews;

        public ViewHolder(View itemView) {
            super(itemView);
            mConvertView = itemView;
            mViews = new SparseArray<>();
        }

        public View getConvertView() {
            return mConvertView;
        }

        /**
         * 通过viewId 获取view,如果没有则加入views
         *
         * @param viewId
         * @return
         */
        public final View findViewById(int viewId) {
            // 根据viewId在mViews中得到View
            View view = mViews.get(viewId);
            if (view == null) {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return view;
        }

        /**
         * 为 TextView 设置字符串
         *
         * @param viewId
         * @param textId
         * @return
         */
        public final ViewHolder setText(int viewId, int textId) {
            TextView view = (TextView) findViewById(viewId);
            view.setText(textId);
            return this;
        }

        /**
         * 为 TextView 设置字符串
         *
         * @param viewId
         * @param text
         * @return
         */
        public final ViewHolder setText(int viewId, String text) {
            TextView view = (TextView) findViewById(viewId);
            view.setText(text);
            return this;
        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemLongClickListener {

        boolean onItemLongClick(View view, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }
}