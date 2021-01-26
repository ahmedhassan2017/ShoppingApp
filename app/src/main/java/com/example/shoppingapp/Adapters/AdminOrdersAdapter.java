package com.example.shoppingapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.Interfaces.OnItemListener;
import com.example.shoppingapp.Model.AdminOrders;
import com.example.shoppingapp.R;

import java.util.ArrayList;

public class AdminOrdersAdapter extends RecyclerView.Adapter<AdminOrdersAdapter.AdminOrdersVieHolder> {

    ArrayList<AdminOrders> AdminOrdersArrayList = new ArrayList<>();
    private OnItemListener onProductListener;

    public AdminOrdersAdapter(OnItemListener onProductListener) {
        this.onProductListener = onProductListener;
    }


    @NonNull
    @Override
    public AdminOrdersVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdminOrdersVieHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item_layout, parent, false),onProductListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminOrdersVieHolder holder, int position) {

        holder.fn.setText("User Name : " + AdminOrdersArrayList.get(position).getFirstName());
        holder.ln.setText(" " + AdminOrdersArrayList.get(position).getLastName());
        holder.city.setText("City : " + AdminOrdersArrayList.get(position).getCity());
        holder.address.setText("Shipping Address : " + AdminOrdersArrayList.get(position).getAddress());
        holder.phone.setText("Phone number : " + AdminOrdersArrayList.get(position).getPhone());
        holder.date.setText("Date : " + AdminOrdersArrayList.get(position).getDate());
        holder.time.setText("Time : " + AdminOrdersArrayList.get(position).getTime());
        holder.totalAmount.setText(AdminOrdersArrayList.get(position).getTotalAmount());
    }

    @Override
    public int getItemCount() {
        return AdminOrdersArrayList.size();
    }

    public void setList(ArrayList<AdminOrders> ordersList) {
        AdminOrdersArrayList = ordersList;
        notifyDataSetChanged();
    }

    public class AdminOrdersVieHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView fn, ln, city, address, phone, date, time, totalAmount;
        Button shipped_btn;
        OnItemListener onProductListener;

        public AdminOrdersVieHolder(@NonNull View itemView,OnItemListener onProductListener) {
            super(itemView);
            fn = itemView.findViewById(R.id.order_first_name);
            ln = itemView.findViewById(R.id.order_last_name);
            city = itemView.findViewById(R.id.order_city);
            address = itemView.findViewById(R.id.order_address);
            phone = itemView.findViewById(R.id.order_phone);
            date = itemView.findViewById(R.id.order_date);
            time = itemView.findViewById(R.id.order_time);
            totalAmount = itemView.findViewById(R.id.order_price);
            shipped_btn = itemView.findViewById(R.id.shipped_btn);
            this.onProductListener = onProductListener;
            shipped_btn.setOnClickListener(this);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            if (v.equals(shipped_btn)) {
                onProductListener.onDeleteClicked(getAdapterPosition());
            }else
            onProductListener.onItemClicked(getAdapterPosition());
        }
    }

}
