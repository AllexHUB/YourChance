package com.grigorenko.yourchance.ui.create_new_startup.detail_fragments.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.grigorenko.yourchance.databinding.CategoryItemBinding

class CategoryAdapter(
    private val categoryList: List<Category>,
    private val categoryClickListener: CategoryClickListener) :
    RecyclerView.Adapter<CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CategoryItemBinding.inflate(inflater, parent, false)
        return CategoryViewHolder(binding, categoryClickListener)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindCategory(categoryList[position])
    }

    override fun getItemCount() = categoryList.size
}

class CategoryViewHolder(
    private val categoryItemBinding: CategoryItemBinding,
    private val categoryClickListener: CategoryClickListener
    ) : RecyclerView.ViewHolder(categoryItemBinding.root){

    fun bindCategory(category: Category) {
        categoryItemBinding.categoryImage.setImageResource(category.image)
        categoryItemBinding.categoryName.text = category.name
        categoryItemBinding.categoryCardView.setOnClickListener{
            categoryClickListener.onClick(category)
        }
    }
}
