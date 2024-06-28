package com.example.e_commerce.ui.auth.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commerce.data.models.auth.CountryModel
import com.example.e_commerce.databinding.CountryItemLayoutBinding

class CountryAdapter (
    private val countries: List<CountryModel>,
    private val countryClickListener: CountryClickListener
) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    inner class CountryViewHolder(private val binding: CountryItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(country: CountryModel) {
            binding.country = country

            Glide.with(binding.root.context)
                .load(country.imag)
                .into(binding.countryImage)


            binding.root.setOnClickListener {
                countryClickListener.onCountryClicked(country)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding =
            CountryItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countries[position])
    }

    override fun getItemCount(): Int = countries.size

}

interface CountryClickListener {
    fun onCountryClicked(country: CountryModel)
}