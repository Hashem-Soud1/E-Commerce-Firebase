package com.example.e_commerce.ui.home.fragments


import SpaceItemDecoration
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.e_commerce.R
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.databinding.FragmentHomeBinding
import com.example.e_commerce.ui.common.fragments.BaseFragment
import com.example.e_commerce.ui.common.views.loadImage
import com.example.e_commerce.ui.common.views.sliderIndicatorsView
import com.example.e_commerce.ui.common.views.updateIndicators
import com.example.e_commerce.ui.home.adapter.CategoryAdapter
import com.example.e_commerce.ui.home.model.SalesAdUIModel
import com.example.e_commerce.ui.home.adapter.SalesAdAdapter
import com.example.e_commerce.ui.home.model.CategoryUIModel
import com.example.e_commerce.ui.home.model.ProductUIModel
import com.example.e_commerce.ui.home.model.SpecialSectionUIModel
import com.example.e_commerce.ui.home.viewmodel.HomeViewModel
import com.example.e_commerce.ui.product.ProductDetailsActivity
import com.example.e_commerce.ui.product.ProductDetailsActivity.Companion.PRODUCT_UI_MODEL_EXTRA
import com.example.e_commerce.ui.product.adapter.ProductAdapter
import com.example.e_commerce.ui.product.adapter.ProductViewType
import com.example.e_commerce.utils.DepthPageTransformer
import com.example.e_commerce.utils.GridSpacingItemDecoration
import com.example.e_commerce.utils.HorizontalSpaceItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()
    override fun getLayoutResId(): Int = R.layout.fragment_home

    override fun init() {
        initViews()
        iniViewModel()
    }

    private fun iniViewModel() {
        lifecycleScope.launch {
            viewModel.salesAdsState.collect { resources ->
                when (resources) {
                    is Resource.Loading -> {
                    }

                    is Resource.Success -> {
                        binding.shimmerSalesAds.root.stopShimmer()
                        binding.shimmerSalesAds.root.visibility = View.GONE
                        initSalesAdsView(resources.data)
                    }

                    is Resource.Error -> {
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.categoriesState.collect { resources ->
                when (resources) {
                    is Resource.Loading -> {
                    }

                    is Resource.Success -> {
//                        binding.categoriesShimmerView.root.stopShimmer()
//                        binding.categoriesShimmerView.root.visibility = View.GONE
                        initCategoriesView(resources.data)
                    }

                    is Resource.Error -> {
                    }
                }
            }
        }

//        viewModel.getFlashSaleProducts()

        lifecycleScope.launch {
            viewModel.flashSaleState.collect { productsList ->
                flashSaleAdapter.submitList(productsList)
                binding.invalidateAll()
            }
        }
        lifecycleScope.launch {
            viewModel.megaSaleState.collect { productsList ->
                megaSaleAdapter.submitList(productsList)
                binding.invalidateAll()
            }
        }

        lifecycleScope.launch {
            viewModel.recommendedSectionDataState.collectLatest { recommendedSectionData ->
                recommendedSectionData?.let {
                   setupRecommendedViewData(it)
                } ?: run {
//                    binding.recommendedProductLayout.visibility = View.GONE
                }
            }
        }

        viewModel.getNextProducts()
        lifecycleScope.launch {
            viewModel.allProductsState.collectLatest { productsList ->
                allProductsAdapter.submitList(productsList)
                binding.invalidateAll()
            }
        }
    }

    private fun setupRecommendedViewData(sectionData: SpecialSectionUIModel) {
        loadImage(binding.recommendedProductIv, sectionData.imag)
        binding.recommendedProductTitleIv.text = sectionData.title
        binding.recommendedProductDescriptionIv.text = sectionData.description
        binding.recommendedProductLayout.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Recommended Product Clicked, goto ${sectionData.type}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun initCategoriesView(data: List<CategoryUIModel>?) {
        if (data.isNullOrEmpty()) {
            return
        }
        val categoriesAdapter = CategoryAdapter(data)
        binding.categoriesRecyclerView.apply {
            adapter = categoriesAdapter
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )
        }
    }

    private val flashSaleAdapter by lazy {
        ProductAdapter(viewType = ProductViewType.LIST) {
            goToProductDetails(it)
        }
    }
    private val megaSaleAdapter by lazy {
        ProductAdapter(viewType = ProductViewType.LIST) {
            goToProductDetails(it)
        }
    }
    private val allProductsAdapter by lazy {
        ProductAdapter(viewType = ProductViewType.GRID) {
            goToProductDetails(it)
        }

    }

    private fun initViews() {
        binding.flashSaleRecyclerView.apply {
            adapter = flashSaleAdapter
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )
            addItemDecoration(HorizontalSpaceItemDecoration(16))
          //  addItemDecoration(SpaceItemDecoration(spacing = 16, spanCount = 1, includeEdge = true, isHorizontal = true))
        }
        binding.megaSaleRecyclerView.apply {
            adapter = megaSaleAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false
            )

       addItemDecoration(HorizontalSpaceItemDecoration(16))
   //         addItemDecoration(SpaceItemDecoration(spacing = 16, spanCount = 1, includeEdge = true, isHorizontal = true))

        }
        binding.allProductsRv.apply {
            adapter = allProductsAdapter
            layoutManager = GridLayoutManager(
                requireContext(), 2
            )
            addItemDecoration(GridSpacingItemDecoration(2, 16, true))
         //   addItemDecoration(SpaceItemDecoration(spacing = 16, spanCount = 2, includeEdge = true, isHorizontal = false))

        }
    }

    private fun initSalesAdsView(salesAds: List<SalesAdUIModel>?) {
        if (salesAds.isNullOrEmpty()) {
            return
        }

        sliderIndicatorsView(
            requireContext(),
            binding.saleAdsViewPager,
            binding.indicatorView,
            indicators,
            salesAds.size
        )

        val salesAdapter = SalesAdAdapter(lifecycleScope, salesAds)
        binding.saleAdsViewPager.apply {
            adapter = salesAdapter
            setPageTransformer(DepthPageTransformer())
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updateIndicators(requireContext(), indicators, position)
                }
            })
        }

        lifecycleScope.launch(IO) {
            tickerFlow(5000).collect {
                withContext(Main) {
                    binding.saleAdsViewPager.setCurrentItem(
                        (binding.saleAdsViewPager.currentItem + 1) % salesAds.size, true
                    )
                }
            }
        }

        // add animation from top to bottom
        binding.saleAdsViewPager.animate().translationY(0f).alpha(1f).setDuration(500).start()

    }

    private fun tickerFlow(period: Long) = flow {
        while (true) {
            emit(Unit)
            delay(period)
        }
    }

    private var indicators = mutableListOf<ImageView>()

    private fun goToProductDetails(product: ProductUIModel) {
        requireActivity().startActivity(Intent(
            requireActivity(), ProductDetailsActivity::class.java
        ).apply {
            putExtra(PRODUCT_UI_MODEL_EXTRA, product)
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.startTimer()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopTimer()
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}