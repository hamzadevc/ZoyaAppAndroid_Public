package com.grappetite.zoya.covid;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.erraticsolutions.framework.views.CustomButton;
import com.grappetite.zoya.R;

import java.util.List;

import butterknife.BindView;

public class CovidCentreAdapter extends RecyclerView.Adapter<CovidCentreAdapter.ViewHolder> {

    private List<String> _name;
    private List<String> _city;
    private List<String> _address;
    private List<String> _phonenumber;
    private Context context;

    private List<String> lat;
    private List<String> lng;


    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    CovidCentreAdapter(Context context, List<String> name, List<String> city, List<String> address, List<String> phonenumber, List<String> lat, List<String> lng) {
        this.mInflater = LayoutInflater.from(context);
        this._name = name;

        this._city = city;
        this._address = address;
        this._phonenumber = phonenumber;
        this.lat = lat;
        this.lng = lng;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.eachview_doctor_detail, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String city = _city.get(position);
        String name = _name.get(position);
        String address = _address.get(position);
        String phonenumber = _phonenumber.get(position);
        holder.tv_hospital.setText("Covid Centres");
        holder.tv_city.setText(city);
        holder.tv_name.setText(name);
        holder.tv_address.setText(address);
        holder.tv_phoneNumber.setText(phonenumber);


//Hamza Tahir
        holder.btnDialer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phonenumber));
                context.startActivity(intent);
            }
        });


    }

    //

    // total number of rows
    @Override
    public int getItemCount() {
        return _name.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_specilization)
        TextView tv_city;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_hospital)
        TextView tv_hospital;
        @BindView(R.id.tv_address)
        TextView tv_address;
        @BindView(R.id.tv_phone_number)
        TextView tv_phoneNumber;
        @BindView(R.id.covidCentreClick)
        LinearLayout covidCentreClick;
        //Hamza Tahir
        @BindView(R.id.btn_dialer)
        CustomButton btnDialer;


        @BindView(R.id.iv_icon)
        ImageView iv_icon;

        ViewHolder(View itemView) {
            super(itemView);
            tv_city = itemView.findViewById(R.id.tv_specilization);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_hospital = itemView.findViewById(R.id.tv_hospital);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_phoneNumber = itemView.findViewById(R.id.tv_phone_number);
            covidCentreClick = itemView.findViewById(R.id.covidCentreClick);
            btnDialer = itemView.findViewById(R.id.btn_dialer);
            context = itemView.getContext();
            btnDialer.setOnClickListener(this);
            covidCentreClick.setOnClickListener(this);
            tv_hospital.setText("Covid Centres");
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return _name.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}