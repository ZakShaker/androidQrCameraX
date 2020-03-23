package com.zakshaker.scannerx.qrs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zakshaker.scannerx.R
import kotlinx.android.synthetic.main.item_qr.view.*
import kotlin.collections.LinkedHashSet

class QrAdapter : RecyclerView.Adapter<QrAdapter.QrViewHolder>() {

    private val uniqueQrsSet = LinkedHashSet<QrCodeModel>()
    private var asyncListDiffer: AsyncListDiffer<QrCodeModel>

    init {
        asyncListDiffer =
            AsyncListDiffer<QrCodeModel>(this, object : DiffUtil.ItemCallback<QrCodeModel>() {
                override fun areItemsTheSame(oldItem: QrCodeModel, newItem: QrCodeModel): Boolean =
                    oldItem == newItem

                override fun areContentsTheSame(
                    oldItem: QrCodeModel,
                    newItem: QrCodeModel
                ): Boolean =
                    oldItem.text == newItem.text
            })
        asyncListDiffer.submitList(uniqueQrsSet.toList())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : QrViewHolder = QrViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_qr, parent, false)
    )

    override fun getItemCount(): Int = uniqueQrsSet.size

    override fun onBindViewHolder(holder: QrViewHolder, position: Int) {
        holder.attach(uniqueQrsSet.elementAt(position))
    }

    fun addQrs(qrs: List<QrCodeModel>) {
        uniqueQrsSet.addAll(qrs)
        asyncListDiffer.submitList(uniqueQrsSet.toList())
    }

    class QrViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun attach(qr: QrCodeModel) {
            itemView.apply {
                tv_qr.text = qr.text
            }
        }
    }
}