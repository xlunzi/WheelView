package cn.xlunzi.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs
import kotlin.math.roundToInt


/**
 * @author xlunzi
 * @date 2020/5/6 20:31
 */
class WheelView : View {

    private var mHeight: Int = 0
    private var mWidth: Int = 0
    private var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.style = Paint.Style.FILL
        it.textAlign = Paint.Align.CENTER
    }
    private var startY: Float = 0f
    private var offsetY: Float = 0f
    private var textSize: Float = 0f
    private var spacePercent: Float = 1f
    private var scalePercent: Float = 1f
    private var textColor: Int = Color.BLACK
    private var showCount: Int = 0

    // TODO 内容
    private val valueList: List<String> = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9")

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initAttrs(attrs)
    }

    private fun initAttrs(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.WheelView)
        textColor = ta.getColor(
            R.styleable.WheelView_textColor,
            Color.parseColor("#FF365ABB")
        )
        val backgroundColor = ta.getColor(
            R.styleable.WheelView_backgroundColor,
            Color.TRANSPARENT
        )
        textSize = ta.getDimension(R.styleable.WheelView_textSize, 120f)
        showCount = ta.getInt(R.styleable.WheelView_showCount, 2)
        spacePercent = ta.getFloat(R.styleable.WheelView_spacePercent, 1f)
        scalePercent = ta.getFloat(R.styleable.WheelView_scalePercent, 1f)
        ta.recycle()

        setBackgroundColor(backgroundColor)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mHeight = height
        mWidth = width
    }

    override fun onDraw(canvas: Canvas) {
        val size = valueList.size
        val middlePosition = size / 2 - (offsetY / (textSize * spacePercent)).roundToInt()

        for (i in (middlePosition - showCount)..(middlePosition + showCount)) {
            drawText(i, canvas)
        }

        drawLine(canvas)
    }

    private fun getRangePosition(i: Int, size: Int): Int {
        var position = i % size
        if (position < 0) {
            position += size
        } else if (position > size) {
            position -= size
        }
        return position
    }

    private fun drawText(i: Int, canvas: Canvas) {
        val size = valueList.size
        val distance = textSize * spacePercent * (i - size / 2)
        val text = valueList[getRangePosition(i, size)]

        val scale = textSize * scalePercent
        val percent = scale / (abs(offsetY + distance) + scale)

        val textSize = textSize * percent

        mPaint.textSize = textSize
        mPaint.color = textColor
        mPaint.alpha = (255 * percent).toInt()

        val fontMetrics: Paint.FontMetrics = mPaint.fontMetrics
        val textHeight = fontMetrics.bottom - fontMetrics.top
        val bottom: Float = textHeight / 2 - fontMetrics.bottom

        val x = mWidth / 2f
        val y = mHeight / 2f + bottom + offsetY + distance

        if (y >= 0 && y < mHeight + textHeight) {
            canvas.drawText(text, x, y, mPaint)
        }
    }

    private fun drawLine(canvas: Canvas) {
        val halfHeight = mHeight / 2f
        canvas.drawLine(0f, halfHeight, mWidth.toFloat(), halfHeight, mPaint)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                offsetY += event.y - startY
                startY = event.y
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                performClick()
            }
            MotionEvent.ACTION_CANCEL -> {
            }
        }
        return true
    }

}