package com.eac.productlistingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eac.productlistingapp.R;
import com.eac.productlistingapp.model.Product;

import java.util.List;

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Product>contactInfoList;

    public ProductRecyclerViewAdapter(Context context, List<Product> contactInfoList) {
        this.context = context;
        this.contactInfoList = contactInfoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_layout_item,parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //set data
        holder.txtName.setText(contactInfoList.get(position).getName());
        holder.txtDesc.setText(contactInfoList.get(position).getDesc());
        holder.txtPrice.setText(contactInfoList.get(position).getPrice()+"/=");

        Glide.with(context)
                .load(contactInfoList.get(position).getImage())
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return contactInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtName,txtDesc,txtPrice;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDesc = itemView.findViewById(R.id.txtDesc);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
