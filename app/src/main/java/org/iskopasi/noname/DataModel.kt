package org.iskopasi.noname

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import org.iskopasi.noname.entities.DnscItem
import org.iskopasi.noname.repo.Repo
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete

/**
 * Created by cora32 on 13.12.2017.
 */
class DataModel : ViewModel() {
    private val repo: Repo by lazy { Repo() }
    val liveData = MutableLiveData<List<DnscItem>>()

    init {
        doAsync {
            val data = repo.getData()

            onComplete {
                liveData.value = data
            }
        }
    }

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

    fun checkOnline(ip: String): Boolean {
        return repo.checkOnline(ip)
    }
}