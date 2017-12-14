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
        val list = ArrayList<DnscItem>()

        list.add(DnscItem(0, "test", "test6", "test6", "test6", "test6", "test6", "comment"))
        list.add(DnscItem(0, "test", "test6", "test6", "test6", "test6", "test6", "comment"))
        list.add(DnscItem(0, "test", "test6", "test6", "test6", "test6", "test6", "comment"))
        list.add(DnscItem(0, "test", "test6", "test6", "test6", "test6", "test6", "comment"))

        data.value = list
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
                    list.add(DnscItem(list.size.toLong(),
                            it.group(1),
                            it.group(2),
                            it.group(7),
                            it.group(10),
                            it.group(9),
                            it.group(4),
                            it.group(3)))
                }

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