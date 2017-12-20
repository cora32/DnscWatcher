package org.iskopasi.noname

import android.content.Context
import android.support.v7.util.SortedList
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.util.SortedListAdapterCallback
import android.view.LayoutInflater
import android.view.ViewGroup
import org.iskopasi.noname.databinding.DnscListitemBinding

/**
 * Created by cora32 on 13.12.2017.
 */

class Adapter(context: Context, comparator: Comparator<DnscItem>) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    private var comp: Comparator<DnscItem> = comparator
    private val inflater by lazy { LayoutInflater.from(context) }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder =
            ViewHolder(DnscListitemBinding.inflate(inflater, parent, false))

    override fun getItemCount(): Int = dataList.size()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList.get(position))
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    val dataList: SortedList<DnscItem> = SortedList(DnscItem::class.java, object : SortedListAdapterCallback<DnscItem>(this) {
        override fun areContentsTheSame(item1: DnscItem, item2: DnscItem) =
                item1.id == item2.id && item1.name == item2.name

        override fun compare(a: DnscItem?, b: DnscItem?) = comp.compare(a, b)

        override fun areItemsTheSame(item1: DnscItem, item2: DnscItem) = item1.id == item2.id
    })

    class ViewHolder(private val binding: DnscListitemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: DnscItem) {
            binding.model = model
        }
    }

    fun setComparator(comparator: Comparator<DnscItem>) {
        comp = comparator
        notifyDataSetChanged()
    }
}