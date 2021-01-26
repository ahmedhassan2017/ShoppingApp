package com.example.shoppingapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.shoppingapp.Interfaces.OnItemListener;
import com.example.shoppingapp.Model.Products;
import com.example.shoppingapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {

    ArrayList<Products> ProductsArrayList = new ArrayList<>();
    private OnItemListener onProductListener;

    public ProductsAdapter(OnItemListener onProductListener) {
        this.onProductListener = onProductListener;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.products_item_layout, parent, false),onProductListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {


        holder.pName.setText(ProductsArrayList.get(position).getPname());
        holder.Price.setText(ProductsArrayList.get(position).getPrice());
        Picasso.get().load(ProductsArrayList.get(position).getImage()).into(holder.pImage);


    }

    @Override
    public int getItemCount() {
        return ProductsArrayList.size();
    }

    public void setList(ArrayList<Products> moviesList ) {
        ProductsArrayList = moviesList;
        notifyDataSetChanged();
    }

    public class ProductsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView pName, Price;
        ImageView pImage;
        OnItemListener onProductListener;

        public ProductsViewHolder(@NonNull View itemView, OnItemListener onProductListener) {
            super(itemView);
            pName = itemView.findViewById(R.id.product_name);
            pImage = itemView.findViewById(R.id.product_image);
            Price =itemView.findViewById(R.id.product_price);
            this.onProductListener = onProductListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onProductListener.onItemClicked(getAdapterPosition());
        }
    }

}
