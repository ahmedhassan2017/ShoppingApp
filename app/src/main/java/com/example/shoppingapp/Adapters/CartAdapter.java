package com.example.shoppingapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.Interfaces.OnItemListener;
import com.example.shoppingapp.Model.Cart;
import com.example.shoppingapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    ArrayList<Cart> CartArrayList = new ArrayList<>();
    private OnItemListener onCartListener;



    public CartAdapter(OnItemListener onCartListener) {
        this.onCartListener = onCartListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item_layout, parent, false), onCartListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {


        holder.pName.setText(CartArrayList.get(position).getpName());
        holder.Price.setText(CartArrayList.get(position).getPrice()+"  EGP");
        holder.quantity.setText(CartArrayList.get(position).getQuantity());
        Picasso.get().load(CartArrayList.get(position).getImage()).into(holder.pImage);


    }

    @Override
    public int getItemCount() {
        return CartArrayList.size();
    }

    public void setList(ArrayList<Cart> cartsList) {
        CartArrayList = cartsList;
        notifyDataSetChanged();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView pName, Price, quantity;
        CircleImageView pImage;
        ImageView removeImage;
        OnItemListener onCartListener;

        public CartViewHolder(@NonNull View itemView, OnItemListener onCartListener) {
            super(itemView);

            pName = itemView.findViewById(R.id.cart_product_name);
            Price = itemView.findViewById(R.id.cart_product_price);
            pImage = itemView.findViewById(R.id.product_image_cart);
            quantity = itemView.findViewById(R.id.cart_product_quantity);
            removeImage = itemView.findViewById(R.id.cart_remove_image);
            this.onCartListener = onCartListener;
            removeImage.setOnClickListener(this);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (v.equals(removeImage)) {
                onCartListener.onDeleteClicked(getAdapterPosition());
            }else
            onCartListener.onItemClicked(getAdapterPosition());
        }
    }


}
