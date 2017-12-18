package org.iskopasi.noname

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete

/**
 * Created by cora32 on 13.12.2017.
 */
class DataModel : ViewModel() {
    val liveData: MutableLiveData<List<DnscItem>> by lazy { Repo().getData() }

    fun getNewData() {
        doAsync {
            val data = Repo().getNewData()

            onComplete {
                liveData.value = data
            }
        }
    }

    fun sortBy(list: List<DnscItem>, property: Any) {
        doAsync {
            val data = Repo().sortBy(list, property)

            onComplete {
                liveData.value = data
            }
        }
    }
}