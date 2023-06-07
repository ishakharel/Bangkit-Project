package com.ecoloops.ecoloopsapp.ui.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ecoloops.ecoloopsapp.R
import com.ecoloops.ecoloopsapp.ui.page.data.CategoryAdapter
import com.ecoloops.ecoloopsapp.ui.page.data.CategoryItem

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val categoryList = ArrayList<CategoryItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = rootView.findViewById(R.id.parent_rv_category)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        addDataToList()
        val adapter = CategoryAdapter(categoryList)
        recyclerView.adapter = adapter

        return rootView
    }

    private fun addDataToList() {

        categoryList.add(CategoryItem("Medis", R.drawable.medic))
        categoryList.add(CategoryItem("Logam", R.drawable.metal))
        categoryList.add(CategoryItem("Plastik", R.drawable.plastic))
        categoryList.add(CategoryItem("Glass", R.drawable.glass))
        categoryList.add(CategoryItem("Karton", R.drawable.cardboard))
        categoryList.add(CategoryItem("Kertas", R.drawable.paper))
        
    }
}



