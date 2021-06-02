package com.lukitor.myapplicationC

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
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
        var layout:ConstraintLayout
        var layoutWhy:ConstraintLayout
        var layoutPenyakit:ConstraintLayout

        init {
            layout= itemVieww.findViewById(R.id.layout11111)
            layoutWhy= itemVieww.findViewById(R.id.layoutWhy)
            layoutPenyakit= itemVieww.findViewById(R.id.layoutPenyakit)
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

        if(position==2){
            viewHolder!!.layout.visibility=View.VISIBLE
            viewHolder!!.layoutWhy.visibility=View.GONE
            viewHolder!!.layoutPenyakit.visibility=View.GONE
        }
        else if(position==3){
            viewHolder!!.layoutWhy.visibility=View.VISIBLE
            viewHolder!!.layout.visibility=View.GONE
            viewHolder!!.layoutPenyakit.visibility=View.GONE
        }
        else if(position==4){
            viewHolder!!.layoutPenyakit.visibility=View.VISIBLE
            viewHolder!!.layout.visibility=View.GONE
            viewHolder!!.layoutWhy.visibility=View.GONE
        }
        else {
            viewHolder!!.layout.visibility=View.GONE
            viewHolder!!.layoutWhy.visibility=View.GONE
            viewHolder!!.layoutPenyakit.visibility=View.GONE
        }

        if(position!=5){viewHolder!!.btnStart.visibility = View.GONE}
        else viewHolder!!.btnStart.visibility=View.VISIBLE

        viewHolder!!.textViewDescription.setText(sliderItem.description)
        viewHolder.imageViewBackground.setImageResource(sliderItem.image!!)
        viewHolder.btnStart.setOnClickListener {
            val intentt= Intent(context,ActivityFormInput::class.java)
            context.startActivity(intentt)
        }

        viewHolder!!.itemView.setOnClickListener { }
    }
}