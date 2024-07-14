package com.example.e_commerce.ui.product.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commerce.databinding.ProductItemLayoutBinding
import com.example.e_commerce.ui.home.model.ProductUIModel


class ProductAdapter : ListAdapter<ProductUIModel, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ProductItemLayoutBinding.inflate(inflater, parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }



    class ProductViewHolder(private val binding: ProductItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductUIModel) {

            binding.product = product

            Glide.with(binding.root.context)
                .load(product.images.firstOrNull())
                .into(binding.productImage)

            binding.executePendingBindings() // This ensures that the binding has been executed immediately.
        }
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<ProductUIModel>() {
        override fun areItemsTheSame(oldItem: ProductUIModel, newItem: ProductUIModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductUIModel, newItem: ProductUIModel): Boolean {
            return oldItem == newItem
        }
    }



}