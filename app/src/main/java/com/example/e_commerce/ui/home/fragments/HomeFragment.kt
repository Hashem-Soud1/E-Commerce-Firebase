package com.example.e_commerce.ui.home.fragments


import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.e_commerce.R
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.databinding.FragmentHomeBinding
import com.example.e_commerce.ui.common.fragments.BaseFragment
import com.example.e_commerce.ui.common.views.loadImage
import com.example.e_commerce.ui.home.adapter.CategoryAdapter
import com.example.e_commerce.ui.home.model.SalesAdUIModel
import com.example.e_commerce.ui.home.adapter.SalesAdAdapter
import com.example.e_commerce.ui.home.model.CategoryUIModel
import com.example.e_commerce.ui.home.model.ProductUIModel
import com.example.e_commerce.ui.home.model.SpecialSectionUIModel
import com.example.e_commerce.ui.home.viewmodel.HomeViewModel
import com.example.e_commerce.ui.product.adapter.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
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

                    }

                    is Resource.Success -> {
                       initCategoryView(resources.data!!)
                    }

                    is Resource.Error -> {

                    }
                }
            }
        }

      lifecycleScope.launch {
            viewModel.flashSalesState.collect { products ->

                initFlashSalesView(products)

            }
      }

        lifecycleScope.launch {
            viewModel.megaSaleState.collect { products ->
                initMegaSaleView(products)
            }
        }

        lifecycleScope.launch {
            viewModel.recommendedSectionDataState.collectLatest { recommendedSectionData ->
                recommendedSectionData?.let {
                    setupRecommendedViewData(it)
                } ?: run {
                 //   binding.recommendedProductLayout.visibility = View.GONE
                }
            }
        }
    }

    private fun setupRecommendedViewData(sectionData: SpecialSectionUIModel) {
Log.d("HomeFragment", "setupRecommendedViewData: $sectionData")
        loadImage(binding.recommendedProductIv, sectionData.imag)
        binding.recommendedProductTitleIv.text = sectionData.title
        binding.recommendedProductDescriptionIv.text = sectionData.description
        binding.recommendedProductLayout.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Recommended Product Clicked, go to ${sectionData.type}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun initMegaSaleView(products: List<ProductUIModel>): List<ProductUIModel> {
        val adapter = ProductAdapter()

        binding.megaSaleRecyclerView.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(
                context, RecyclerView.HORIZONTAL, false
            )
        }

        adapter.submitList(products)
        binding.invalidateAll()
        return products

    }

    private fun initFlashSalesView(products: List<ProductUIModel>) {
        val adapter = ProductAdapter()

        binding.flashSaleRecyclerView.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(
                context, RecyclerView.HORIZONTAL, false
            )
        }

        adapter.submitList(products)
        binding.invalidateAll()

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