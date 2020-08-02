package com.erraticsolutions.framework.fragments;


import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class CustomFragment extends Fragment
{
    private Handler handler = new Handler();

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(getContentViewId(),container,false);
    }

    @Override
    public final void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        assign(savedInstanceState);
        repopulate();
    }

    protected abstract int getContentViewId();
    protected abstract void init(View view);
    protected abstract void assign(@Nullable Bundle savedInstanceState);
    protected void populate() {}
    protected void asyncPopulate() {}
    public final void repopulate(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                asyncPopulate();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        populate();
                    }
                });
            }
        }).start();
        populate();
    }

}
