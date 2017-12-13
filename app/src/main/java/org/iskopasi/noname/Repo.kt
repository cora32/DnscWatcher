package org.iskopasi.noname

import android.arch.lifecycle.MutableLiveData
import java.net.URL

/**
 * Created by cora32 on 13.12.2017.
 */
class Repo {
    fun getData(): MutableLiveData<List<DnscItem>> {
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

    fun getNewData(): List<DnscItem> {
        val url = URL("https://raw.githubusercontent.com/jedisct1/dnscrypt-resolvers/master/v1/dnscrypt-resolvers.csv")
        val urlConnection = url.openConnection()
        val response = Utils.getStringFromInputStream(urlConnection.getInputStream())

        val splitList = response.split("\n")

        var counter = 0
        val list = ArrayList<DnscItem>()
        splitList.mapTo(list) { DnscItem(counter++.toLong(), it, "test6") }

        return list
    }

//    fun getNewDataFuture(): List<DnscItem> {
//        val job: (AnkoAsyncContext<Repo>.() -> List<DnscItem>) = {
//            doAsync {
//                val url = URL("https://raw.githubusercontent.com/jedisct1/dnscrypt-resolvers/master/v1/dnscrypt-resolvers.csv")
//                val urlConnection = url.openConnection()
//                val inStream = urlConnection.getInputStream()
////            copyInputStreamToOutputStream(inStream, System.out)
//                onComplete {
//                }
//            }
//
//            val list = ArrayList<DnscItem>()
//
//            val data1 = DnscItem(0, "test4", "test6")
//            val data2 = DnscItem(0, "test5", "test5")
//            val data3 = DnscItem(0, "test6", "test4")
//
//            list.add(data1)
//            list.add(data2)
//            list.add(data3)
//
//            list
//        }
//        return doAsyncResult(null, job).get()
//    }
}