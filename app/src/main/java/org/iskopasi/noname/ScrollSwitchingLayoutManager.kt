package org.iskopasi.noname

import android.content.Context
import android.support.v7.widget.LinearLayoutManager

/**
 * Created by cora32 on 30.12.2017.
 */
class ScrollSwitchingLayoutManager(context: Context) : LinearLayoutManager(context) {
    var allowScrolling = true

    override fun canScrollVertically(): Boolean {
        return allowScrolling
    }
}