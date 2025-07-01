package com.yawpie.rotidigitalent.ui.bread

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yawpie.rotidigitalent.R
import com.yawpie.rotidigitalent.data.Bread


class BreadListAdapter(
    private val breads: List<Bread>
) : RecyclerView.Adapter<BreadListAdapter.BreadViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreadViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bread, parent, false)
        return BreadViewHolder(view)
    }

    override fun onBindViewHolder(holder: BreadViewHolder, position: Int) {
        holder.bind(breads[position])
    }

    override fun getItemCount(): Int = breads.size

    class BreadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivLogo: ImageView = itemView.findViewById(R.id.iv_logo)
        private val tvName: TextView = itemView.findViewById(R.id.tv_bread_name)
        private val tvDesc: TextView = itemView.findViewById(R.id.tv_bread_desc)

        fun bind(bread: Bread) {
            tvName.text = bread.name
            tvDesc.text = bread.description
            Glide.with(itemView.context)
                .load(bread.imageUrl)
                .placeholder(R.drawable.roti_placeholder) // fallback drawable
                .into(ivLogo)
        }
    }
}
