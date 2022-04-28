package com.grigorenko.yourchance.ui.create_new_startup.detail_fragments.category

import androidx.lifecycle.ViewModel
import com.grigorenko.yourchance.R

class CategoryViewModel : ViewModel() {
    private val categoryList = mutableListOf<Category>()

    fun insertCategoryList() : List<Category> {
        val category1 = Category(
            categoryList.size,
            "Бизнес",
            R.drawable.ic_business_category_black_24dp
        )
        categoryList.add(category1)

        val category2 = Category(
            categoryList.size,
            "Еда",
            R.drawable.ic_food_category_black_24dp
        )
        categoryList.add(category2)

        val category3 = Category(
            categoryList.size,
            "Искусство",
            R.drawable.ic_art_category_black_24dp
        )
        categoryList.add(category3)

        val category4 = Category(
            categoryList.size,
            "Общество",
            R.drawable.ic_society_category_black_24dp
        )
        categoryList.add(category4)

        val category5 = Category(
            categoryList.size,
            "IT",
            R.drawable.ic_it_category_black_24dp
        )
        categoryList.add(category5)

        val category6 = Category(
            categoryList.size,
            "Технологии",
            R.drawable.ic_techn_category_black_24dp
        )
        categoryList.add(category6)

        val category7 = Category(
            categoryList.size,
            "Путешествия",
            R.drawable.ic_trip_category_black_24dp
        )
        categoryList.add(category7)

        val category8 = Category(
            categoryList.size,
            "Спорт",
            R.drawable.ic_sport_category_black_24dp
        )
        categoryList.add(category8)

        return categoryList
    }

    fun clearCategoryList() {
        categoryList.clear()
    }
}