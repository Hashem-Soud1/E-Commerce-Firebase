package com.example.e_commerce.ui.product.fragment

import android.util.Log
import android.widget.ImageView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentProductDetailsBinding
import com.example.e_commerce.ui.common.fragments.BaseFragment
import com.example.e_commerce.ui.common.views.sliderIndicatorsView
import com.example.e_commerce.ui.common.views.updateIndicators
import com.example.e_commerce.ui.home.model.ProductUIModel
import com.example.e_commerce.ui.product.adapter.ProductImagesAdapter
import com.example.e_commerce.ui.product.viewmodel.ProductDetailsViewModel
import com.example.e_commerce.utils.DepthPageTransformer
import com.training.ecommerce.ui.theme.ECommerceTypography
import com.training.ecommerce.ui.theme.EcommerceTheme
import com.training.ecommerce.ui.theme.NeutralGreyColor
import com.training.ecommerce.ui.theme.RatingColor


import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : BaseFragment<FragmentProductDetailsBinding, ProductDetailsViewModel>() {

    override val viewModel: ProductDetailsViewModel by activityViewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_product_details

    override fun init() {
        initViewModel()
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            viewModel.productDetailsState.collectLatest {
                initView(it)
            }
        }
    }

    private fun initView(it: ProductUIModel) {
        it.name.let { binding.titleTv.text = it }
        initImagesView(it.images)
        initComposViews()
    }

    private fun initComposViews() {
        binding.composeView.setContent {
            EcommerceTheme {
                Column (
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                ) {
                    val product =  remember { viewModel.productDetailsState.value}

                    Row {

                        Column(
                        modifier = Modifier.weight(1f)
                    ){
                            AddDescription(product.description)
                        }

                        AddFavoriteIcon(product)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {

                        StarRating(product.rate)
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Row {
                        Log.d("ProductDetailsFragment", "initComposViews: ${product.getFormattedPrice()}")
                        AddPrice(product.formatPrice)
                    }

                    Row {
                        Button(
                            onClick = { /*TODO*/ },
                            shape = CircleShape,
                            border = null,
                            
                        ) {

                            Text(
                                text = product.sizes.size.toString(),
                                style = ECommerceTypography.titleMedium,
                            )
                        }
                    }

                }
            }
        }
    }



    @Composable
    private fun AddDescription(description: String) {


            Text(
                text = description,
                style = ECommerceTypography.titleLarge
            )

    }
    @Composable

    private fun AddFavoriteIcon(product: ProductUIModel) {
        Icon(
            imageVector = Icons.Filled.FavoriteBorder,
            contentDescription = null,
            tint = NeutralGreyColor,
            modifier = Modifier.padding(start = 8.dp, top = 1.dp)


        )
    }

    @Composable
    fun StarRating(rating: Float) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 1..5) {
              val  startColor = if (i <= rating)  RatingColor
              else NeutralGreyColor

                Icon(
                    painter = painterResource(id =R.drawable.ic_star),
                    contentDescription = null,
                    tint = startColor,
                    modifier = Modifier.size(width = 16.dp, height = 16.dp)

                )
            }
        }
    }

    @Composable
    private fun AddPrice(formattedPrice: String) {

        Text(
            text = formattedPrice,
            style = ECommerceTypography.titleMedium
        )
    }

    private var indicators = mutableListOf<ImageView>()
    private fun initImagesView(images: List<String>) {

        sliderIndicatorsView(
            requireContext(),
            binding.productImagesViewPager,
            binding.indicatorView,
            indicators,
            images.size
        )
        binding.productImagesViewPager.apply {
            adapter = ProductImagesAdapter(images)
            setPageTransformer(DepthPageTransformer())

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updateIndicators(requireContext(), indicators, position)
                }
            })
        }
    }


    companion object
}