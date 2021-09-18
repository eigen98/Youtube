package com.example.youtube

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout

class CustomMotionLayout(context: Context, attributeSet: AttributeSet) :
    MotionLayout(context, attributeSet) {
    //다른 모션레이아웃 사용
    private var motionTouchStarted = false //터치이벤트 먹을 때
    private val mainContainerView by lazy { //터치영역을 가져오기 위해서
        findViewById<View>(R.id.mainContainerLayout)
    }

    private val hitRect = Rect()    //

    init {
        setTransitionListener(object : TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {

            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                motionTouchStarted = false
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
            }

        })
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
       // return super.onTouchEvent(event)    //기존의 터치 이벤트

        when (event.actionMasked) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                motionTouchStarted = false
                return super.onTouchEvent(event)

            }
        }
        if (!motionTouchStarted) {
            mainContainerView.getHitRect(hitRect)
            motionTouchStarted = hitRect.contains(event.x.toInt(), event.y.toInt())

        }
        return super.onTouchEvent(event) && motionTouchStarted


    }

    private val getstureListner by lazy {
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(
                e1: MotionEvent,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                mainContainerView.getHitRect(hitRect)

                return hitRect.contains(e1.x.toInt(), e1.y.toInt())
            }
        }
    }

    private val gestureDetector by lazy {
        GestureDetector(context, getstureListner)
    }

    override fun onInterceptHoverEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)


    }
}