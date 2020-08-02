package com.grappetite.zoya.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.grappetite.zoya.R;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.interfaces.ZoyaAPI;
import com.grappetite.zoya.postboy.RetrofitInstance;
import com.grappetite.zoya.restapis.parsers.Parser;
import com.grappetite.zoya.utils.SessionUtils;
import com.grappetite.zoya.views.ConnectionInfoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromoCodeActivity extends CustomActivity implements Callback<String> {

    @BindView(R.id.et_promo)
    EditText et_promo;
    private ZoyaAPI zoyaAPI;

    @BindView(R.id.civ)
    ConnectionInfoView civ;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_promo_code;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        zoyaAPI = RetrofitInstance.getRetrofitInstance(getApplicationContext()).create(ZoyaAPI.class);

    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle("PROMO");
        this.setSupportActionbarFont(ContextCustom.getString(this,R.string.font_semiBold));
    }

    @OnClick({
            R.id.btn_promo
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_promo: {

                if(!LocalStoragePreferences.getCheckPromo()){
                    zoyaAPI.setPromo(LocalStoragePreferences.getAuthToken(),et_promo.getText().toString().toUpperCase()).enqueue(this);
                    civ.showLoader();
                }else {
                    Toast.makeText(this,"Referral already added!",Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }

            }
            break;
        }
    }


    @Override
    public void onResponse(Call<String> call, Response<String> response) {

        civ.hideAll();

        Parser parser = new Parser(response.body());

        switch (parser.getResponseCode()){
            case 200:

                if(parser.getResponseStatus().equals("success")){
                    LocalStoragePreferences.setCheckPromo(true);
                    Toast.makeText(this,"Referral added successfully",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this,"Referral invalid!",Toast.LENGTH_SHORT).show();
                }
                onBackPressed();
                break;
            case 401:
                SessionUtils.logout(PromoCodeActivity.this,true);
                break;
        }


    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        Toast.makeText(this,"Error!",Toast.LENGTH_LONG).show();
        onBackPressed();
    }
}
