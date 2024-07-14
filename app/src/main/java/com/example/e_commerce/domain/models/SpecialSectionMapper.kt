package com.example.e_commerce.domain.models

import com.example.e_commerce.data.models.SpecialSectionModel
import com.example.e_commerce.ui.home.model.SpecialSectionUIModel



fun SpecialSectionModel.toSpecialSectionUIModel(): SpecialSectionUIModel {
    return SpecialSectionUIModel(
        id = id,
        title = title,
        description = description,
        imag = imag,
    )
}

fun SpecialSectionUIModel.toSpecialSectionModel(): SpecialSectionModel {
    return SpecialSectionModel(
        id = id,
        title = title,
        description = description,
        imag= imag,
    )
}