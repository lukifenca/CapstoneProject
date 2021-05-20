package com.lukitor.myapplicationC

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.smarteist.autoimageslider.SliderViewAdapter


open class SliderAdapter constructor(context: Context): SliderViewAdapter<SliderAdapter.SliderAdapterVH>()  {

    private var context: Context = context
    private var mSliderItems: MutableList<SliderItem> = ArrayList()

    fun renewItems(sliderItems: ArrayList<SliderItem>) {
        mSliderItems = sliderItems
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        mSliderItems.removeAt(position)
        notifyDataSetChanged()
    }

    open fun addItem(sliderItem: SliderItem) {
        println("Masuk")
        mSliderItems.add(sliderItem)
        println(mSliderItems.size)
        notifyDataSetChanged()
    }


    class SliderAdapterVH(itemVieww: View) :
        ViewHolder(itemVieww) {

        var itemViews: View
        var imageViewBackground: ImageView
        var imageGifContainer: ImageView
        var textViewDescription: TextView

        init {
            imageViewBackground = itemVieww.findViewById(R.id.iv_auto_image_slider)
            imageGifContainer = itemVieww.findViewById(R.id.iv_gif_container)
            textViewDescription = itemVieww.findViewById(R.id.tv_auto_image_slider)
            itemViews = itemVieww
        }
    }


    override fun getCount(): Int {

        //slider view count could be dynamic size
        return mSliderItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapterVH {
        val inflate = LayoutInflater.from(parent!!.context).inflate(
                R.layout.image_slider_layout_item,
                null
        )

        return SliderAdapterVH(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH?, position: Int) {
        val sliderItem = mSliderItems[position]

        viewHolder!!.textViewDescription.setText(sliderItem.description)
        viewHolder!!.textViewDescription.setTextColor(Color.WHITE)
        viewHolder.imageViewBackground.setImageResource(sliderItem.image!!)
        viewHolder!!.itemView.setOnClickListener { Toast.makeText(context, "This is item in position $position", Toast.LENGTH_SHORT).show() }
    }
}