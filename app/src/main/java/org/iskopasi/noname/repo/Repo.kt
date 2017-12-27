package org.iskopasi.noname.repo

import android.util.Log
import org.iskopasi.noname.Utils
import org.iskopasi.noname.entities.DnscItem
import java.net.InetSocketAddress
import java.net.Socket
import java.net.URL
import java.util.regex.Pattern
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection

/**
 * Created by cora32 on 13.12.2017.
 */
class Repo {
    private val re by lazy { Pattern.compile("(.*?),(\".*?\"),(\".*?\"),(\".*?\"),(\".*?\"),(.*?),(.*?),(.*?),(.*?),(.*?),(.*?),(.*?),(.*?),(.*)") }
    private val cache: ArrayList<DnscItem> = ArrayList()
    private val db: DnsDb by lazy { DnsDb() }

    fun getData(): List<DnscItem> {
        val list: List<DnscItem> = db.getData()
        cacheData(list as ArrayList<DnscItem>)
        return list
    }

    fun getNewData(): List<DnscItem>? {
        val list = ArrayList<DnscItem>()

        try {
            //Accepting all certificates
            val allHostsValid = HostnameVerifier { hostname, session -> true }
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid)

            //Requesting direct ip in case dns-server fails.
            val url = URL("https://151.101.36.133/jedisct1/dnscrypt-resolvers/master/v1/dnscrypt-resolvers.csv")
            val urlConnection = url.openConnection()
            urlConnection.setRequestProperty("Host", "raw.githubusercontent.com")
            val response = Utils.getStringFromInputStream(urlConnection.getInputStream())

            val splitList = response.split("\n")

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
                                0,                          //online
                                it.group(11)))              //ip
                    }

            saveDataToDb(list)

            //Saving previous online states for correct sorting by online after swipe-update
            if (cache.isNotEmpty() && cache.size == list.size)
                list.zip(cache)
                        .forEach { it.first.online = it.second.online }

            cacheData(list)
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }

        return list
    }

    private fun saveDataToDb(list: ArrayList<DnscItem>) {
        db.saveData(list)
    }

    private fun cacheData(list: ArrayList<DnscItem>) {
        cache.clear()
        cache.addAll(list)
    }

    fun getCachedData(): List<DnscItem> = cache

    fun checkOnline(ip: String): Boolean {
        if (ip.isEmpty())
            return false

        try {
            val socket = Socket()
            val endPoint: InetSocketAddress

            endPoint = when {
                ip.contains("]:") -> { //ipv6
                    val lastIndex = ip.lastIndexOf(":")
                    val port = Integer.valueOf(ip.slice(lastIndex + 1 until ip.length))
                    val ip6 = ip.slice(0 until lastIndex)

                    InetSocketAddress(ip6, port)
                }
                ip.contains(":") -> { //ipv4
                    val ipNPort: List<String> = ip.split(":")
                    InetSocketAddress(ipNPort[0], Integer.valueOf(ipNPort[1]))
                }
                else -> InetSocketAddress(ip, 53)
            }

            socket.connect(endPoint, 2000)

            if (socket.isConnected) {
                socket.close()
                return true
            }
        } catch (ex: Exception) {
            Log.e("Network exception", "ip: " + ip + " : " + ex.localizedMessage)
            ex.printStackTrace()
        }

        return false
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