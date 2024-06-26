package com.example.e_commerce.ui.home.fragments


import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.e_commerce.R
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.databinding.FragmentHomeBinding
import com.example.e_commerce.ui.common.fragments.BaseFragment
import com.example.e_commerce.ui.home.adapter.CategoryAdapter
import com.example.e_commerce.ui.home.model.SalesAdUIModel
import com.example.e_commerce.ui.home.adapter.SalesAdAdapter
import com.example.e_commerce.ui.home.model.CategoryUIModel
import com.example.e_commerce.ui.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {


    override val viewModel: HomeViewModel by viewModels()

    private val indicatorImages = mutableListOf<ImageView>()


    override fun getLayoutResId() : Int = R.layout.fragment_home

    override fun init() {
         initViewModel()

    }






    private fun initViewModel() {

        lifecycleScope.launch {
            viewModel.salesAdsState.collect { resources ->
                when (resources) {
                    is Resource.Loading -> {
                        binding.shimmerSalesAds.root.startShimmer()
                    }

                    is Resource.Success -> {
                        binding.shimmerSalesAds.root.stopShimmer()
                        binding.shimmerSalesAds.root.visibility = View.GONE

                        initSalesAdsView(resources.data!!)

                    }

                    is Resource.Error -> {
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.categoryState.collect { resources ->
                when (resources) {
                    is Resource.Loading -> {
                        Log.d("HASHEML", "initViewModel: categories Success = ${resources.data}")

                    }

                    is Resource.Success -> {
                        Log.d("HASHEM", "initViewModel: categories Success = ${resources.data}")
                       initCategoryView(resources.data!!)
                    }

                    is Resource.Error -> {
                        Log.d("HASHEME", "initViewModel: categories Success = ${resources.data}")

                    }
                }
            }
        }
    }

    private fun initCategoryView(data: List<CategoryUIModel>) {
      val categoryAdapter = CategoryAdapter(data)

        binding.categoriesRecyclerView.apply {
            adapter = categoryAdapter
            setHasFixedSize(true)
            layoutManager= LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false)

            isNestedScrollingEnabled = false
        }

    }


    private fun initSalesAdsView(data: List<SalesAdUIModel>) {

        val salesAdAdapter = SalesAdAdapter(data)
        binding.saleAdsViewPager.adapter = salesAdAdapter



        setupIndicators(data.size)
        setCurrentIndicator(0)


    binding.saleAdsViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            setCurrentIndicator(position)

        }
    })
}

private fun setupIndicators(count: Int) {
    val indicators = arrayOfNulls<ImageView>(count)
    val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    layoutParams.setMargins(8, 0, 8, 0)
    for (i in indicators.indices) {
        indicators[i] = ImageView(context)
        indicators[i]?.setImageDrawable(
            context?.let { it1 ->
                androidx.core.content.ContextCompat.getDrawable(
                    it1,
                    R.drawable.indicator_unselected
                )
            }
        )
        indicators[i]?.layoutParams = layoutParams
        binding.indicatorView.addView(indicators[i])
    }
    indicatorImages.addAll(indicators.filterNotNull())
}

private fun setCurrentIndicator(index: Int) {
    for (i in indicatorImages.indices) {
        val imageView = indicatorImages[i]
        if (i == index) {
            imageView.setImageDrawable(
                context?.let { it1 ->
                    androidx.core.content.ContextCompat.getDrawable(
                        it1,
                        R.drawable.indicator_selected
                    )
                }
            )
        } else {
            imageView.setImageDrawable(
                context?.let { it1 ->
                    androidx.core.content.ContextCompat.getDrawable(
                        it1,
                        R.drawable.indicator_unselected
                    )
                }
            )
        }
    }
}
}