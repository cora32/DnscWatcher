package org.iskopasi.noname

import android.content.Context
import android.support.v7.util.SortedList
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.iskopasi.noname.databinding.DnscListitemBinding

/**
 * Created by cora32 on 13.12.2017.
 */

class Adapter(context: Context) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    private val context by lazy { context }
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder =
            ViewHolder(DnscListitemBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = dataList.size()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList.get(position))
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    val dataList: SortedList<DnscItem> = SortedList(DnscItem::class.java, object : SortedList.Callback<DnscItem>() {
        override fun onChanged(position: Int, count: Int) {
            notifyItemRangeRemoved(position, count)
        }

        override fun onInserted(position: Int, count: Int) {
            notifyItemRangeRemoved(position, count)
        }

        override fun onMoved(position: Int, count: Int) {
            notifyItemRangeRemoved(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            notifyItemRangeRemoved(position, count)
        }

        override fun areContentsTheSame(item1: DnscItem?, item2: DnscItem?): Boolean {
            return false
        }

        override fun compare(a: DnscItem?, b: DnscItem?): Int =
                Comparator<DnscItem> { (id), (id2) -> id2.compareTo(id) }.compare(a, b)

        override fun areItemsTheSame(item1: DnscItem?, item2: DnscItem?): Boolean = item1?.equals(item2) as Boolean

    })

    class ViewHolder(private val binding: DnscListitemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: DnscItem) {
            binding.model = model
        }
    }
}