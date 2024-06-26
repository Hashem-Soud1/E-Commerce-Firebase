package com.example.e_commerce.ui.home.model

import java.util.Date

data class SalesAdUIModel(
   val id: String? = null,
   val title: String? = null,
   val description: String? = null,

   var imagUrl: String? = null,
   val type: String? = null,

   var productId: String? = null,

   var categoryId: String? = null,

   var externalLink: String? = null,

   var endAt: Date? = null
)
