package com.useruser.foodscanner.view.customview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log.d
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.*
import com.useruser.foodscanner.R

@BindingMethods(
    BindingMethod(
        type = CustomEditText::class,
        attribute = "textAttrChanged",
        method = "setTextChangeListener"
    )
)
class CustomEditText(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var view: View
    private lateinit var tvTitle: TextView
    private lateinit var etText: EditText

    private lateinit var title: String
    private var titleColor: Int = Color.BLACK
    private var titleBackgroundColor = Color.WHITE
    private var titleSize: Float = 12f
    private var titleTypeface: Typeface? = null
    private var textTypeface: Typeface? = null
    private var maxTextLength: Int = -1

    private var titlePaddingStart = 0f
    private var titlePaddingEnd = 0f

    private var textColor = Color.BLACK
    private var textSize = 18f
    private var textStartPadding = 0
    private var textTopPadding = 0
    private var textEndPadding = 0
    private var textBottomPadding = 0
    private var inputType = 0

    private var borderColor = Color.BLACK
    private var borderWidth = 0
    private var borderRadius = 0

    private var textChangeListener: InverseBindingListener? = null

    var text: String = ""
        get() = etText.text.toString()
        set(value) {
            etText.setText(value)
            field = value
        }

    fun setTextChangeListener(listener: InverseBindingListener?) {
        textChangeListener = listener
    }

    init {

        val layoutInflater = LayoutInflater.from(context)
        view = layoutInflater.inflate(R.layout.castom_edit_text, this, false)
        initView(view)
        getAttributeResult(attrs)



        drawEditTextBackground(etText)
        addView(view)

    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        d("CustomEditText", "--------------------------onFinishInflate------------------------")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        d("CustomEditText", "----------------------onMeasure----------------------------")

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        d("CustomEditText", "--------------------------------------------------")

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        d("CustomEditText", "----------------------onLayout----------------------------")

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        d("CustomEditText", "---------------------onDraw-----------------------------")

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
        d("CustomEditText", "-------------------onTouchEvent-------------------------------")

    }

    override fun onFocusChanged(gainFocus: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect)
        d("CustomEditText", "-----------------------onFocusChanged---------------------------")

    }

    fun initView(view: View) {
        etText = view.findViewById(R.id.etText)
        etText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textChangeListener?.onChange()
            }

        })
        tvTitle = view.findViewById(R.id.tvTitle)
    }

    private fun getAttributeResult(attrs: AttributeSet) {
        val typedArray: TypedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomEditText, 0, R.style.CustomEditTex)
        try {
            title = typedArray.getText(R.styleable.CustomEditText_title) as String
            titleColor = typedArray.getColor(R.styleable.CustomEditText_titleColor, ContextCompat.getColor(context, android.R.color.black))
            titleSize = typedArray.getDimension(R.styleable.CustomEditText_titleSize, 8f)

            val titleFont = typedArray.getResourceId(R.styleable.CustomEditText_titleFont, -1)
            if (titleFont != -1) {
                titleTypeface = ResourcesCompat.getFont(context, titleFont)
            }

            val textFont = typedArray.getResourceId(R.styleable.CustomEditText_textFont, -1)
            if (textFont != -1) {
                textTypeface = ResourcesCompat.getFont(context, textFont)
            }

            titlePaddingStart = typedArray.getDimensionPixelOffset(R.styleable.CustomEditText_titlePaddingStart, 0).toFloat()
            titlePaddingEnd = typedArray.getDimension(R.styleable.CustomEditText_titlePaddingEnd, 0f)

            titleBackgroundColor = typedArray.getColor(R.styleable.CustomEditText_titleBackgroundColor, Color.WHITE)

            titleTypeface?.let { tvTitle.typeface = titleTypeface }

            textColor = typedArray.getColor(R.styleable.CustomEditText_textColor, Color.BLACK)
            textSize = typedArray.getDimensionPixelOffset(R.styleable.CustomEditText_textSize, 18).toFloat()
            maxTextLength = typedArray.getInt(R.styleable.CustomEditText_maxLength, -1)
            textStartPadding = typedArray.getDimensionPixelSize(R.styleable.CustomEditText_textStartPadding, 0)
            textTopPadding = typedArray.getDimensionPixelSize(R.styleable.CustomEditText_textTopPadding, 0)
            textEndPadding = typedArray.getDimensionPixelSize(R.styleable.CustomEditText_textEndPadding, 0)
            textBottomPadding = typedArray.getDimensionPixelSize(R.styleable.CustomEditText_textBottomPadding, 0)
            inputType = typedArray.getInt(R.styleable.CustomEditText_inputType, 0)

            borderColor = typedArray.getColor(R.styleable.CustomEditText_borderColor, Color.BLACK)
            borderWidth = typedArray.getDimensionPixelSize(R.styleable.CustomEditText_borderWidth, 0)
            borderRadius = typedArray.getDimensionPixelSize(R.styleable.CustomEditText_borderRadius, 0)

            tvTitle.text = title
            tvTitle.setTextColor(titleColor)
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize)
            tvTitle.setPadding(titlePaddingStart.toInt(), 0, titlePaddingEnd.toInt(), 0)
            tvTitle.setBackgroundColor(titleBackgroundColor)
            val attrText = typedArray.getString(R.styleable.CustomEditText_text)

            if (attrText != null) text = attrText
            etText.setTextColor(textColor)
            etText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            etText.setPadding(textStartPadding, textTopPadding, textEndPadding, textBottomPadding)
            if (maxTextLength > 0) {
                etText.filters = arrayOf(InputFilter.LengthFilter(maxTextLength))
            }

            textTypeface?.let { etText.typeface = textTypeface }

            when (inputType) {
                0 -> etText.inputType = InputType.TYPE_CLASS_TEXT
                1 -> {
                    etText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                }
            }

        } finally {
            typedArray.recycle()
        }
    }

    private fun drawEditTextBackground(editText: EditText) {
        val gd = GradientDrawable()

        gd.shape = GradientDrawable.RECTANGLE

        gd.setColor(Color.TRANSPARENT)

        gd.setStroke(borderWidth, borderColor) //width = 2

        gd.cornerRadius = borderRadius.toFloat() //radius = 15

        etText.setBackground(gd)
    }

    private fun reconcileSize(contentSize: Int, measureSpec: Int): Int {
        val mode = MeasureSpec.EXACTLY
        val specSize = MeasureSpec.getSize(contentSize)

        return when (mode) {
            MeasureSpec.EXACTLY -> specSize
            MeasureSpec.AT_MOST -> if (contentSize < specSize) contentSize else specSize
            MeasureSpec.UNSPECIFIED -> contentSize
            else -> contentSize
        }
    }

    companion object {

        @BindingAdapter("text")
        @JvmStatic
        fun setCustomText(customEditText: CustomEditText, text: String?) {
            text?.let {
                if (customEditText.text != text) {
                    customEditText.text = text
                }
            }

        }

        @InverseBindingAdapter(attribute = "text")
        @JvmStatic
        fun getCustomText(customEditText: CustomEditText): String {
            return customEditText.text
        }
    }
}
