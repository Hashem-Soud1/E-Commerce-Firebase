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


enum class ProductViewType {
    GRID, LIST
}

class ProductAdapter(
    private val viewType: ProductViewType = ProductViewType.GRID,
    private val onProductClick: (ProductUIModel) -> Unit = {}
) : ListAdapter<ProductUIModel, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ProductItemLayoutBinding.inflate(inflater, parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(viewType = viewType, product)
        holder.itemView.setOnClickListener { onProductClick(product) }
    }

    class ProductViewHolder(private val binding: ProductItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewType: ProductViewType, product: ProductUIModel) {
            if (viewType == ProductViewType.GRID) {
                binding.productItemLayout.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            binding.product = product
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