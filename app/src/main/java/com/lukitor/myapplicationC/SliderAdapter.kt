package com.lukitor.myapplicationC

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
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
        var imagelogo: ImageView
        var textViewDescription: TextView
        var btnStart: Button

        init {
            imagelogo= itemVieww.findViewById(R.id.imageViewLogoAwal)
            imageViewBackground = itemVieww.findViewById(R.id.iv_auto_image_slider)
            imageGifContainer = itemVieww.findViewById(R.id.iv_gif_container)
            textViewDescription = itemVieww.findViewById(R.id.tv_auto_image_slider)
            btnStart = itemVieww.findViewById(R.id.btnGetStarteddd)
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

        if(position==0)viewHolder!!.imagelogo.visibility=View.VISIBLE
        else viewHolder!!.imagelogo.visibility=View.GONE

        if(position!=2){viewHolder!!.btnStart.visibility = View.GONE}
        else viewHolder!!.btnStart.visibility=View.VISIBLE
        viewHolder!!.textViewDescription.setText(sliderItem.description)
        viewHolder!!.textViewDescription.setTextColor(Color.WHITE)
        viewHolder.imageViewBackground.setImageResource(sliderItem.image!!)
        viewHolder.btnStart.setOnClickListener {
            val intentt= Intent(context,ActivityFormInput::class.java)
            context.startActivity(intentt)
        }

        viewHolder!!.itemView.setOnClickListener { Toast.makeText(context, "This is item in position $position", Toast.LENGTH_SHORT).show() }
    }
}