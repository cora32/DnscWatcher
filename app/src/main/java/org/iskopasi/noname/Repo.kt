package org.iskopasi.noname

import android.arch.lifecycle.MutableLiveData
import java.net.URL
import java.util.regex.Pattern

/**
 * Created by cora32 on 13.12.2017.
 */
class Repo {
    private val re by lazy { Pattern.compile("(.*?),(\".*?\"),(\".*?\"),(\".*?\"),(\".*?\"),(.*?),(.*?),(.*?),(.*?),(.*?),(.*?),(.*?),(.*?),(.*)") }
    fun getData(): MutableLiveData<List<DnscItem>> {
        val data = MutableLiveData<List<DnscItem>>()
        data.value = ArrayList()
        return data
    }

    fun getNewData(): List<DnscItem> {
        val url = URL("https://raw.githubusercontent.com/jedisct1/dnscrypt-resolvers/master/v1/dnscrypt-resolvers.csv")
        val urlConnection = url.openConnection()
        val response = Utils.getStringFromInputStream(urlConnection.getInputStream())

        val splitList = response.split("\n")

        val list = ArrayList<DnscItem>()
        (1 until splitList.size)
                .map { splitList[it] }
                .map { re.matcher(it) }
                .filter { it.find() }
                .forEach {
                    list.add(DnscItem(list.size.toLong(),      //id
                            it.group(1),                //name
                            it.group(2),                //fullname
                            it.group(7),                //version
                            it.group(10),               //namecoin
                            it.group(9),                //noLogs
                            it.group(4),                //location
                            it.group(3),                //comment
                            true))                      //online
                }

        return sortBy(list, DnscItem::namecoin)
    }

    fun sortBy(list: List<DnscItem>, property: Any): List<DnscItem> {
        return list.sortedWith(compareByDescending(property as (DnscItem) -> Comparable<*>?))
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