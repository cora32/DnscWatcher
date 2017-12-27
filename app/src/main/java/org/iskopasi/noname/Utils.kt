package org.iskopasi.noname

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PorterDuff
import android.net.ConnectivityManager
import android.support.v4.content.ContextCompat
import android.view.View
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.StringWriter

/**
 * Created by cora32 on 14.12.2017.
 */
internal object Utils {
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

    fun animateColor(context: Context, colorFromRes: Int, colorToRes: Int, view: View?, duration: Long) {
        if (view == null)
            return

        val colorFrom = ContextCompat.getColor(context, colorFromRes)
        val colorTo = ContextCompat.getColor(context, colorToRes)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = duration
        colorAnimation.addUpdateListener { animator -> view.setBackgroundColor(animator.animatedValue as Int) }
        colorAnimation.start()
    }

    fun animateDrawableColor(context: Context, colorFromRes: Int, colorToRes: Int, view: View?, duration: Long) {
        if (view == null)
            return

        val colorFrom = ContextCompat.getColor(context, colorFromRes)
        val colorTo = ContextCompat.getColor(context, colorToRes)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = duration
        colorAnimation.addUpdateListener { animator -> view.background?.mutate()?.setColorFilter(animator.animatedValue as Int, PorterDuff.Mode.SRC_IN) }
        colorAnimation.start()
    }

    fun isNetworkUp(context: Context): Boolean {
        val conMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = conMgr.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
}