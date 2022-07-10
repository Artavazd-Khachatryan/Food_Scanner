package com.useruser.foodscanner.data.maper

import com.useruser.foodscanner.data.models.ProductModel
import com.useruser.foodscanner.data.roomDB.ProductEntity

class ProductMapper: Mapper<ProductModel, ProductEntity>{

    override fun map(value: ProductModel): ProductEntity {
        return ProductEntity(
            value.id,
            value.name,
            value.price,
            value.priceType,
            value.count
        )
    }

    override fun reverseMap(value: ProductEntity): ProductModel {
        var resultProduct: ProductModel? = null
        value.apply {
            resultProduct = ProductModel(name, price, priceType, count, id)
        }

        return resultProduct!!
    }
}