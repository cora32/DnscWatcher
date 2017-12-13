package org.iskopasi.noname

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel

/**
 * Created by cora32 on 13.12.2017.
 */
class DataModel : ViewModel() {
    //    val liveData: LiveData<List<DnscItem>> by lazy { MutableLiveData<List<DnscItem>>() }
    val liveData: LiveData<List<DnscItem>> by lazy { Repo().getData() }
}