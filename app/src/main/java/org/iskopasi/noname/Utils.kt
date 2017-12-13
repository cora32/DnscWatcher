package org.iskopasi.noname

import java.io.*

/**
 * Created by cora32 on 14.12.2017.
 */
internal object Utils {
    @Throws(IOException::class)
    fun streamToString(inputStream: InputStream): String {
        val sb = StringBuilder(inputStream.available())
        val rd = BufferedReader(InputStreamReader(inputStream))
        var line: String? = rd.readLine()
        while (line != null) {
            sb.append(line)
            line = rd.readLine()
        }
        return sb.toString()
    }

    @Throws(IOException::class)
    fun getStringFromInputStream(stream: InputStream): String {
        val buffer = CharArray(1024 * 4)
        val reader = InputStreamReader(stream, "UTF8")
        val writer = StringWriter()
        var n = reader.read(buffer)
        while (n != -1) {
            writer.write(buffer, 0, n)
            n = reader.read(buffer)
        }
        return writer.toString()
    }
}