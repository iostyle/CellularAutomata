package com.iostyle.cellularautomata.playground

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import com.iostyle.cellular.bean.IAtom
import com.iostyle.cellular.bean.IUniverse
import com.iostyle.cellular.bean.Universe

class BasePlayground @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), IPlayground {

    private var widthSize: Int = 0
    private var heightSize: Int = 0

    private var itemWidth: Float = 0f
    private var itemHeight: Float = 0f

    private var itemPadding = 5f

    private val data = mutableListOf<IAtom>()

    var callback: IPlayground.PlaygroundCallback? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun bind(universe: Universe) {


        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                widthSize = width / universe.cellSize
                heightSize = height / universe.cellSize

                itemWidth = (width / widthSize).toFloat()
                itemHeight = (height / heightSize).toFloat()
                postInvalidate()
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_UP -> {
                    val clickLocation = getClickLocation(motionEvent.x.toInt(), motionEvent.y.toInt()).also {
                        Log.d("touch up", "${it.x} ${it.y}")
                    }
                    callback?.onClickCoordinate(clickLocation)
                }
            }
            true
        }
    }

    private val gridPaint = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    private val atomPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    override fun resetData(data: MutableList<IAtom>) {
        this.data.clear()
        this.data.addAll(data)
        postInvalidate()
    }

    override fun getClickLocation(x: Int, y: Int): IUniverse.Coordinate {
        Log.d("click", "$x $y ${itemWidth} ${itemHeight} ${(x / itemWidth.toInt())}  ${(y / itemHeight.toInt())}")
        return IUniverse.Coordinate((x / itemWidth.toInt()), (y / itemHeight.toInt()))
    }

    override fun bindCallback(callback: IPlayground.PlaygroundCallback) {
        this.callback = callback
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // draw gird
        val screenWidth = width.toFloat()
        val screenHeight = height.toFloat()

        val columnGap = screenWidth / widthSize
        val rowGap = screenHeight / heightSize

        for (i in 0..widthSize) {
            val x = i * columnGap
            canvas.drawLine(x, 0f, x, screenHeight, gridPaint)
        }
        for (i in 0..heightSize) {
            val y = i * rowGap
            canvas.drawLine(0f, y, screenWidth, y, gridPaint)
        }

        var has = false
        // draw item
        data.forEach {
            it.coordinate.apply {
                if (!has) {
                    if (x in 0..widthSize && x in 0..heightSize) {
                        has = true
                    }
                }
                canvas.drawCircle(x * columnGap + (itemWidth / 2), y * rowGap + (itemHeight / 2), itemWidth / 2f - itemPadding, atomPaint)
            }
        }
        if(!has){
            callback?.onRenderBlank()
        }
    }

}