package org.iskopasi.noname

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete

/**
 * Created by cora32 on 13.12.2017.
 */
class DataModel : ViewModel() {
    private val repo: Repo by lazy { Repo() }
    val liveData: MutableLiveData<List<DnscItem>> by lazy { repo.getData() }

    fun getNewData() {
        doAsync {
            val data = repo.getNewData()

            onComplete {
                liveData.value = data
            }
        }
    }

    fun getCachedData() {
        doAsync {
            val data = repo.getCachedData()

            onComplete {
                liveData.value = data
            }
        }
    }
}