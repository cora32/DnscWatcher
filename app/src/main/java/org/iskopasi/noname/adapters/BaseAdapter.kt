package org.iskopasi.noname.adapters

import android.support.v7.util.SortedList
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager


/**
 * Created by cora32 on 13.12.2017.
 */

open class BaseAdapter<T>(comparator: Comparator<T>) : RecyclerView.Adapter<ViewHolder>() {
    protected var comp: Comparator<T> = comparator
    protected val delegatesManager: AdapterDelegatesManager<SortedList<T>> by lazy { AdapterDelegatesManager<SortedList<T>>() }
    lateinit var dataList: SortedList<T>

    override fun getItemViewType(position: Int): Int {
        return delegatesManager.getItemViewType(dataList, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder = delegatesManager.onCreateViewHolder(parent, viewType)

    override fun getItemCount(): Int = dataList.size()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        delegatesManager.onBindViewHolder(dataList, position, holder)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setComparator(comparator: Comparator<T>) {
        comp = comparator
        notifyDataSetChanged()
    }
}