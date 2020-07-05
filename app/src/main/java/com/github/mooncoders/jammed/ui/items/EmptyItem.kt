package com.github.mooncoders.jammed.ui.items

import android.view.View
import com.github.mooncoders.jammed.R
import net.gotev.recycleradapter.AdapterItem
import net.gotev.recycleradapter.RecyclerAdapterViewHolder

internal class EmptyItem(
    private val model: String,
    val onClick: (() -> Unit)? = null,
    val onLongClick: (() -> Unit)? = null
) : AdapterItem<EmptyItem.Holder>(model) {

    override fun getLayoutId() = R.layout.empty_item

    override fun bind(firstTime: Boolean, holder: Holder) {

    }

    class Holder(itemView: View) : RecyclerAdapterViewHolder(itemView) {

    }
}