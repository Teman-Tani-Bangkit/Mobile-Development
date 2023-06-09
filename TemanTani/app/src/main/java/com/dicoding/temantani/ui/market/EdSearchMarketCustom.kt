package com.dicoding.temantani.ui.market

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.temantani.R

class EdSearchMarketCustom : AppCompatEditText {

    private lateinit var iconSearch : Drawable
    private lateinit var bgSearchBar : Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init(){
        bgSearchBar = ContextCompat.getDrawable(context, R.drawable.bg_search_bar_market) as Drawable
        iconSearch = ContextCompat.getDrawable(context, R.drawable.ic_search_black) as Drawable

        setButtonDrawables(endOfTheText = iconSearch)

    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = bgSearchBar
        hint = "Search Product"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

}