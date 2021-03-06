package com.tcs.sinanews.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tcs.sinanews.ui.activity.BaseActivity;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/29.
 */

public abstract class BaseFragment extends Fragment {
    protected Context mContext;
    public abstract void init(Bundle savedInstanceState);
    public abstract int getResourceId();
    public abstract void initView(View view);
    //use for lazy load
    protected abstract void onFragmentFirstVisible();

    private boolean mIsFirstVisible = true;
    private boolean mIsPrepared = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        FragmentActivity fragmentActivity = (FragmentActivity) context;
        if (!(fragmentActivity instanceof BaseActivity))
            throw new RuntimeException("所使用的Activity必须依附于BaseActivity");

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onPrepareStates();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onVisible();
        } else {
            onInvisible();
        }
    }


    public void onVisible() {
        if (mIsFirstVisible) {
            mIsFirstVisible = false;
            onPrepareStates();
        }
    }

    public void onInvisible() {

    }

    private synchronized void onPrepareStates() {
        if (mIsPrepared) {
            onFragmentFirstVisible();
        } else {
            mIsPrepared = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getResourceId(), container, false);
        ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
