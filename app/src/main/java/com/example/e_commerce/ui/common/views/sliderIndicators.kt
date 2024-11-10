package com.example.e_commerce.ui.common.views

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.e_commerce.R

fun sliderIndicatorsView(
    context: Context,
    viewPager2: ViewPager2,
    indicatorLayout: LinearLayout,
    indicators: MutableList<ImageView>,
    count: Int,
    onCircleClick: (Int) -> Unit = {}
) {
    indicatorLayout.removeAllViews()
    indicators.clear()

    for (i in 0 until count) {
        val circleView = ImageView(context)
        val params = LinearLayout.LayoutParams(
            20, 20
        )
        params.setMargins(8, 0, 8, 0) // Margin between circles
        circleView.setLayoutParams(params)
        // First indicator is red
        circleView.setOnClickListener {
            viewPager2.setCurrentItem(i, true)
            onCircleClick(i)
        }
        indicators.add(circleView)
        indicatorLayout.addView(circleView)
    }
}


fun updateIndicators(
    context: Context,
    indicatorsList: MutableList<ImageView>,
    currentIndex: Int
) {
    for (i in indicatorsList.indices) {
        val imageView = indicatorsList[i]
        val drawableRes = if (i == currentIndex) {
            R.drawable.indicator_selected
        } else {
            R.drawable.indicator_unselected
        }
        imageView.setImageDrawable(
            context.let {
                androidx.core.content.ContextCompat.getDrawable(it, drawableRes)
            }
        )
    }
}