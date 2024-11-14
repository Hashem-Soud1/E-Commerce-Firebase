package com.example.e_commerce.ui.product

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.e_commerce.R
import com.example.e_commerce.ui.home.model.ProductUIModel
import com.example.e_commerce.ui.product.viewmodel.ProductDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsActivity : AppCompatActivity() {

    val productUiModel: ProductUIModel by lazy {
        val product = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(PRODUCT_UI_MODEL_EXTRA, ProductUIModel::class.java)
        } else {
            intent.getParcelableExtra(PRODUCT_UI_MODEL_EXTRA)
        }

        product ?: throw IllegalArgumentException("ProductUIModel is required")
    }

    val viewModel: ProductDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)


        lifecycleScope.launch {
            viewModel.productDetailsState.collectLatest {
                // Update UI
            }
        }
    }

    companion object {
        private const val TAG = "ProductDetailsActivity"
        const val PRODUCT_UI_MODEL_EXTRA = "PRODUCT_UI_MODEL_EXTRA"
    }
}