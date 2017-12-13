package org.iskopasi.noname

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

/**
 * Created by cora32 on 13.12.2017.
 */
class Repo {
    fun getData(): LiveData<List<DnscItem>> {
        val data = MutableLiveData<List<DnscItem>>()
        val list = ArrayList<DnscItem>()

        val data1 = DnscItem(0, "test1", "test1")
        val data2 = DnscItem(0, "test2", "test2")
        val data3 = DnscItem(0, "test3", "test3")

        list.add(data1)
        list.add(data2)
        list.add(data3)

        data.value = list
        return data
    }
}