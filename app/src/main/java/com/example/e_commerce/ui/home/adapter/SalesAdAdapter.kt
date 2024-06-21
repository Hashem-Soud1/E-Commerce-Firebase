package com.example.e_commerce.ui.home.adapter

import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.e_commerce.databinding.ItemSalesAdsBinding
import com.example.e_commerce.ui.common.model.SalesAdUIModel
import java.util.Date
import java.util.concurrent.TimeUnit

class SalesAdAdapter(private val salesAds: List<SalesAdUIModel>) :
    RecyclerView.Adapter<SalesAdAdapter.SalesAdViewHolder>() {

    inner class SalesAdViewHolder(val binding: ItemSalesAdsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var countDownTimer: CountDownTimer? = null

        fun bind(salesAd: SalesAdUIModel) {
            binding.titleTextView.text = salesAd.title

            Glide.with(binding.root.context)
                .load(salesAd.imagUrl)
                .transform(CenterCrop(), RoundedCorners(16))
                .into(binding.imageView)

            salesAd.endAt?.let { endDate ->
                startCountDown(endDate)
            }
        }

        fun startCountDown(endDate: Date) {

            val currentTime = System.currentTimeMillis()
            val endTime = endDate.time
            val timeLeft = endTime - currentTime

            countDownTimer?.cancel() // Cancel any existing timer

            if (timeLeft > 0) {
                countDownTimer = object : CountDownTimer(timeLeft, 1000) {
                    override fun onTick(millisUntilFinished: Long) {

                        val totalSeconds = millisUntilFinished / 1000
                        val hours = totalSeconds / 3600
                        val minutes = (totalSeconds % 3600) / 60
                        val seconds = totalSeconds % 60

                        binding.hoursTextView.text = hours.toString()
                        binding.minutesTextView.text = minutes.toString()
                        binding.secondsTextView.text = seconds.toString()
                    }

                    override fun onFinish() {
                        binding.hoursTextView.text = "0"
                        binding.minutesTextView.text = "0"
                        binding.secondsTextView.text = "0"
                    }
                }.start()
            }
        }

        fun stopCountDown() {
            countDownTimer?.cancel()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesAdViewHolder {
        val binding = ItemSalesAdsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SalesAdViewHolder(binding)
    }

    override fun onViewAttachedToWindow(holder: SalesAdViewHolder) {
        super.onViewAttachedToWindow(holder)

        val position = holder.bindingAdapterPosition
        if (position != RecyclerView.NO_POSITION) {
            val salesAd = salesAds[position]
            salesAd.endAt?.let { holder.startCountDown(it) }
        }
    }



    override fun onViewDetachedFromWindow(holder: SalesAdViewHolder) {
        super.onViewDetachedFromWindow(holder)

        holder.stopCountDown()
    }

    override fun onBindViewHolder(holder: SalesAdViewHolder, position: Int) {
        holder.bind(salesAds[position])




    }

    override fun getItemCount(): Int = salesAds.size
}
