package com.example.thinktanktrivia.Adapter

/**
 * adapter for choosing different cat
 */


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.thinktanktrivia.Model.CatogeryModel
import com.example.thinktanktrivia.R


internal class CategoryChooseAdapter(
    private val List: List<CatogeryModel>,
    private val context: Context
) :
    BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private lateinit var Name: TextView
    override fun getCount(): Int {
        return List.size
    }
    override fun getItem(position: Int): Any? {
        return null
    }
    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.catogery_grid_view, null)
        }
        Name = convertView!!.findViewById(R.id.CatName)
        Name.setText(List.get(position).name)
        return convertView
    }
}
