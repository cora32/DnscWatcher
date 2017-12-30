package org.iskopasi.noname.adapterdelegates

import android.content.Context
import android.support.annotation.Nullable
import android.support.v7.util.SortedList
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.view.animation.DecelerateInterpolator
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.iskopasi.noname.R
import org.iskopasi.noname.ScrollSwitchingLayoutManager
import org.iskopasi.noname.databinding.DnscListitemBinding
import org.iskopasi.noname.entities.DnscItem
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton


/**
 * Created by cora32 on 21.12.2017.
 */
class DnsAdapterDelegate(private val context: Context,
                         private val lm: ScrollSwitchingLayoutManager) : AdapterDelegate<SortedList<DnscItem>>() {
    private val inflater by lazy { LayoutInflater.from(context) }
    private val bottomViewWidth by lazy { context.resources.getDimension(R.dimen.bottom_view_width) }

    override fun isForViewType(items: SortedList<DnscItem>, position: Int): Boolean {
        return items.get(position) is DnscItem
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(DnscListitemBinding.inflate(inflater, parent, false))

    override fun onBindViewHolder(items: SortedList<DnscItem>, position: Int,
                                  holder: RecyclerView.ViewHolder, @Nullable payloads: List<Any>) {
        val item = items.get(position)
        (holder as ViewHolder).bind(item)

//        val gestureDetector = GestureDetector(context, DnsItemViewDetector(holder.itemView))
        var initX = 0f
        var initViewPosX = 0f
        val upperView: View? = holder.itemView.findViewById(R.id.upper)
        upperView?.setOnTouchListener({ view, event ->
            //            gestureDetector.onTouchEvent(event)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initX = event.rawX
                    initViewPosX = view.translationX
                }
                MotionEvent.ACTION_MOVE -> {
                    lm.allowScrolling = false
                    val delta = event.rawX - initX
                    val newX = initViewPosX + delta
                    if (newX < 0 && newX > -bottomViewWidth)
                        view.translationX = newX
                    else if (newX >= 0)
                        view.translationX = 0f
                    else if (newX <= -bottomViewWidth)
                        view.translationX = -bottomViewWidth
                }
                MotionEvent.ACTION_UP -> {
                    lm.allowScrolling = true
                    if (view.translationX > -bottomViewWidth / 2f)
                        upperView.animate()
                                .translationX(0f)
                                .setInterpolator(DecelerateInterpolator())
                                .duration = DURATION
                    else
                        upperView.animate()
                                .translationX(-bottomViewWidth)
                                .setInterpolator(DecelerateInterpolator())
                                .duration = DURATION
                }
            }

            true
        })

        val bottomView: View? = holder.itemView.findViewById(R.id.bottom)

        bottomView?.setOnClickListener({
            context.alert("Testing alerts") {
                title = "Alert"
                yesButton { context.toast("Deleted") }
                noButton { }
            }.show()
        })
    }

    class ViewHolder(private val binding: DnscListitemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: DnscItem) {
            binding.model = model
        }
    }

    companion object {
        private const val SWIPE_MIN_DISTANCE = 150
        private const val SWIPE_MAX_OFF_PATH = 100
        private const val SWIPE_THRESHOLD_VELOCITY = 100
        private const val DURATION = 200L
    }

    inner class DnsItemViewDetector(val view: View) : GestureDetector.SimpleOnGestureListener() {
        private var mLastOnDownEvent: MotionEvent? = null
        private var initialViewXPos = view.x

        override fun onDown(e: MotionEvent): Boolean {
            //Android 4.0 bug means e1 in onFling may be NULL due to onLongPress eating it.
            mLastOnDownEvent = e
            Log.e("Down", "Down")
            return super.onDown(e)
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            if (e1 == null || e2 == null) return false
            val dX = e2.x - e1.x
            val dY = e1.y - e2.y

            if (Math.abs(dY) < SWIPE_MAX_OFF_PATH && Math.abs(velocityX) >= SWIPE_THRESHOLD_VELOCITY && Math.abs(dX) >= SWIPE_MIN_DISTANCE) {
                if (dX > 0) {
                    //Swipe Right
                    Log.e("Swipe right", "Swipe right")
                } else {
                    // Swipe Left
                    Log.e("Swipe left", "Swipe left")
                }
                return true
            } else
                if (Math.abs(dX) < SWIPE_MAX_OFF_PATH && Math.abs(velocityY) >= SWIPE_THRESHOLD_VELOCITY && Math.abs(dY) >= SWIPE_MIN_DISTANCE) {
                    if (dY > 0) {
                        // Swipe UP
                        Log.e("Swipe UP", "Swipe UP")
                    } else {
                        // Swipe DOWN
                        Log.e("Swipe DOWN", "Swipe DOWN")
                    }
                    return true
                }
            return false
        }

        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            val delta = e1.rawX - e2.rawX
            view.x = initialViewXPos - delta
            return true
        }
    }
}