package org.iskopasi.noname.adapterdelegates

import android.content.Context
import android.support.annotation.Nullable
import android.support.v7.util.SortedList
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.iskopasi.noname.databinding.DnscListitemBinding
import org.iskopasi.noname.entities.DnscItem


/**
 * Created by cora32 on 21.12.2017.
 */
class DnsAdapterDelegate(private val context: Context) : AdapterDelegate<SortedList<DnscItem>>() {
    private val inflater by lazy { LayoutInflater.from(context) }

    override fun isForViewType(items: SortedList<DnscItem>, position: Int): Boolean {
        return items.get(position) is DnscItem
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(DnscListitemBinding.inflate(inflater, parent, false))

    override fun onBindViewHolder(items: SortedList<DnscItem>, position: Int,
                                  holder: RecyclerView.ViewHolder, @Nullable payloads: List<Any>) {
        val item = items.get(position)
        (holder as ViewHolder).bind(item)
    }

    class ViewHolder(private val binding: DnscListitemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: DnscItem) {
            binding.model = model
        }
    }
}