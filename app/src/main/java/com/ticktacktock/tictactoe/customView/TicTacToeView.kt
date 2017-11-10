package com.ticktacktock.tictactoe.customView

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.ticktacktock.tictactoe.R
import com.ticktacktock.tictactoe.TicTacToe.SquareCoordinates


/**
 * Created by krishan on 29/10/17.
 */
class TicTacToeView : View, ValueAnimator.AnimatorUpdateListener {

    constructor(ctx: Context) : super(ctx)

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

    private val paint = Paint()
    private val textPaint = Paint()
    private val highLightPaint = Paint()
    private val path = Path()
    private lateinit var squares: Array<Array<Rect>>
    private lateinit var squareData: Array<Array<String>>
    var squarePressListener: SquarePressedListener? = null

    val moveX = "X"
    val moveY = "O"

    val COUNT = 3
    val X_PARTITION_RATIO = 1 / 3f
    val Y_PARTITION_RATIO = 1 / 3f
    var rectIndex = Pair(0, 0)
    var touching: Boolean = false
    var winCoordinates: Array<SquareCoordinates> = Array(3, { SquareCoordinates(-1, -1) })
    var shouldAnimate = false


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = Math.min(measuredHeight, measuredWidth)
        setMeasuredDimension(size, size)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        init()
    }

    private fun init() {
        paint.color = ContextCompat.getColor(context, R.color.colorPrimary)
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = resources.displayMetrics.density * 5

        textPaint.color = paint.color
        textPaint.isAntiAlias = true
        textPaint.typeface = ResourcesCompat.getFont(context, R.font.nunito)
        textPaint.textSize = resources.displayMetrics.scaledDensity * 70

        highLightPaint.color = ContextCompat.getColor(context, R.color.ripple_material_light)
        highLightPaint.style = Paint.Style.FILL
        highLightPaint.isAntiAlias = true
        initializeTicTacToeSquares()
    }

    private fun initializeTicTacToeSquares() {
        squares = Array(3, { Array(3, { Rect() }) })
        squareData = Array(3, { Array(3, { "" }) })

        val xUnit = (width * X_PARTITION_RATIO).toInt() // one unit on x-axis
        val yUnit = (height * Y_PARTITION_RATIO).toInt() // one unit on y-axis

        for (j in 0..COUNT - 1) {
            for (i in 0..COUNT - 1) {
                squares[i][j] = Rect(i * xUnit, j * yUnit, (i + 1) * xUnit, (j + 1) * yUnit)
            }

        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return false
        }
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                rectIndex = getRectIndexesFor(x, y)
                touching = true
                invalidate(squares[rectIndex.first][rectIndex.second])
            }
            MotionEvent.ACTION_MOVE -> {

            }
            MotionEvent.ACTION_UP -> {
                touching = false
                invalidate(squares[rectIndex.first][rectIndex.second])
                val (finalX1, finalY1) = getRectIndexesFor(x, y)
                if ((finalX1 == rectIndex.first) && (finalY1 == rectIndex.second)) { // if initial touch and final touch is in same rectangle or not
                    squarePressListener?.onSquarePressed(rectIndex.first, rectIndex.second)
                }

            }
            MotionEvent.ACTION_CANCEL -> {
                touching = false
            }

        }
        return true
    }

    fun getRectIndexesFor(x: Float, y: Float): Pair<Int, Int> {
        squares.forEachIndexed {
            i, rects ->
            for ((j, rect) in rects.withIndex()) {
                if (rect.contains(x.toInt(), y.toInt()))
                    return Pair(i, j)
            }
        }
        return Pair(-1, -1) // x, y do not lie in our view
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawVerticalLines(canvas)
        drawHorizontalLines(canvas)
        drawSquareStates(canvas)
        if (shouldAnimate) {
            canvas.drawPath(path, paint)
        }
        if (touching) {
            drawHighlightRectangle(canvas)
        }
    }

    private fun animateWin() {
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = 600
        valueAnimator.addUpdateListener(this)
        valueAnimator.start()
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        val measure = PathMeasure(path, false)
        val phase = measure.length - (measure.length * (animation.animatedValue as Float))
        paint.pathEffect = createPathEffect(measure.length, phase, 0.0f)
        invalidate()
    }

    private fun createPathEffect(pathLength: Float, phase: Float, offset: Float): PathEffect {
        return DashPathEffect(floatArrayOf(pathLength, pathLength),
                phase)
    }

    private fun drawSquareStates(canvas: Canvas) {
        squareData.forEachIndexed({ index_i, strings ->
            strings.forEachIndexed({
                index_j, string ->
                if (string.isNotEmpty()) {
                    drawTextInsideRectangle(canvas, squares[index_i][index_j], string)
                }
            })
        })
    }

    private fun drawHighlightRectangle(canvas: Canvas) {
        canvas.drawRect(squares[rectIndex.first][rectIndex.second], highLightPaint)
    }

    private fun drawTextInsideRectangle(canvas: Canvas, rect: Rect, str: String) {
        val xOffset = textPaint.measureText(str) * 0.5f
        val yOffset = textPaint.fontMetrics.ascent * -0.4f
        val textX = (rect.exactCenterX()) - xOffset
        val textY = (rect.exactCenterY()) + yOffset
        canvas.drawText(str, textX, textY, textPaint)
    }

    private fun drawVerticalLines(canvas: Canvas) {
        canvas.drawLine(width * X_PARTITION_RATIO, 0f, width * X_PARTITION_RATIO, height.toFloat(), paint)
        canvas.drawLine(width * (2 * X_PARTITION_RATIO), 0f, width * (2 * X_PARTITION_RATIO), height.toFloat(), paint)
    }

    private fun drawHorizontalLines(canvas: Canvas) {
        canvas.drawLine(0f, height * Y_PARTITION_RATIO, width.toFloat(), height * Y_PARTITION_RATIO, paint)
        canvas.drawLine(0f, height * (2 * Y_PARTITION_RATIO), width.toFloat(), height * (2 * Y_PARTITION_RATIO), paint)
    }

    interface SquarePressedListener {
        fun onSquarePressed(i: Int, j: Int)
    }

    fun drawXAtPosition(x: Int, y: Int) {
        squareData[x][y] = moveX
        invalidate()
    }

    fun drawOAtPosition(x: Int, y: Int) {
        squareData[x][y] = moveY
        invalidate()
    }

    fun reset() {
        squareData = Array(3, { Array(3, { "" }) })
        winCoordinates = Array(3, { SquareCoordinates(-1, -1) })
        path.reset()
        shouldAnimate = false
        invalidate()
    }

    fun animateWin(winCoords: Array<SquareCoordinates>) {
        winCoords.forEachIndexed { index, coords ->
            winCoordinates[index] = coords
        }
        if (winCoordinates[0].i < 0) return
        val x1 = squares[winCoordinates[0].i][winCoordinates[0].j].exactCenterX()
        val y1 = squares[winCoordinates[0].i][winCoordinates[0].j].exactCenterY()
        val x2 = squares[winCoordinates[2].i][winCoordinates[2].j].exactCenterX()
        val y2 = squares[winCoordinates[2].i][winCoordinates[2].j].exactCenterY()
        path.reset()
        path.moveTo(x1, y1)
        path.lineTo(x2, y2)
        shouldAnimate = true
        animateWin()
    }

}