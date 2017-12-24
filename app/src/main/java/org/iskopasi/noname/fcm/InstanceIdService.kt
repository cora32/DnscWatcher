package org.iskopasi.noname.fcm

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import java.io.DataOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


/**
 * Created by cora32 on 24.12.2017.
 */
class InstanceIdService : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val token = FirebaseInstanceId.getInstance().token
        Log.e("FCM_nnm", "Token: $token")

//        sendToServer(token)
    }

    private fun sendToServer(token: String?) {
        try {
            val url = URL("https://www.iskopasi.org/store")
            val connection = url.openConnection() as HttpURLConnection

            connection.doOutput = true
            connection.doInput = true
            connection.requestMethod = "POST"

            val dos = DataOutputStream(connection.outputStream)
            dos.writeBytes("token=" + token)

            connection.connect()

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                Log.e("FCM_nnm", "Token $token is saved to server.")
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}