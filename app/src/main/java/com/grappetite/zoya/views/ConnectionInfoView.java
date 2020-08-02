package com.grappetite.zoya.views;

import android.content.Context;
import android.os.Build;
import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.grappetite.zoya.R;
import com.pnikosis.materialishprogress.ProgressWheel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConnectionInfoView extends FrameLayout {
    @BindView(R.id.ll_retry)    View ll_retry;
    @BindView(R.id.pw)  ProgressWheel pw;
    @BindView(R.id.tv_nothing_found)    TextView tv_nothingFound;

    private Listener listener;

    public ConnectionInfoView setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    public ConnectionInfoView(@NonNull Context context) {
        super(context);
        init();
    }

    public ConnectionInfoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ConnectionInfoView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ConnectionInfoView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_connection_info,this,false);
        this.addView(view);
        ButterKnife.bind(this);
    }

    public void hideAll() {
        ll_retry.setVisibility(View.INVISIBLE);
        tv_nothingFound.setVisibility(View.INVISIBLE);
        pw.setVisibility(View.INVISIBLE);
    }

    public void showLoader() {
        hideAll();
        pw.setVisibility(View.VISIBLE);
    }

    public void showNothingFound() {
        hideAll();
        tv_nothingFound.setVisibility(View.VISIBLE);
    }

    public void showInternetConnectionFail() {
        hideAll();
        ll_retry.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.btn_retry})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_retry:
                if (listener!=null)
                    listener.onRetry();
                break;
        }
    }

    public interface Listener {
        void onRetry();
    }
}
