package org.iskopasi.noname.adapters

import android.content.Context
import android.support.v7.util.SortedList
import android.support.v7.widget.util.SortedListAdapterCallback
import org.iskopasi.noname.adapterdelegates.DnsAdapterDelegate
import org.iskopasi.noname.entities.DnscItem

/**
 * Created by cora32 on 21.12.2017.
 */
class DnsAdapter(context: Context, comparator: Comparator<DnscItem>) : BaseAdapter<DnscItem>(comparator) {
    init {
        delegatesManager.addDelegate(DnsAdapterDelegate(context))

        dataList = SortedList(DnscItem::class.java,
                object : SortedListAdapterCallback<DnscItem>(this) {
                    override fun areContentsTheSame(item1: DnscItem, item2: DnscItem) =
                            item1.id == item2.id && item1.name == item2.name

                    override fun compare(a: DnscItem?, b: DnscItem?) = comp.compare(a, b)

                    override fun areItemsTheSame(item1: DnscItem, item2: DnscItem) = item1.id == item2.id
                })
    }
}