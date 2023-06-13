package com.dicoding.temantani.ui.upload

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.temantani.R

class EdUploadCustom : AppCompatEditText {

    private lateinit var iconMoney: Drawable
    private lateinit var bgEdText: Drawable

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

    private fun init() {

        iconMoney = when(id){
            R.id.ed_harga_barang -> ContextCompat.getDrawable(context, R.drawable.ic_money_green) as Drawable

            else -> ContextCompat.getDrawable(context, R.drawable.ic_nama_green) as Drawable
        }

        bgEdText = ContextCompat.getDrawable(context, R.drawable.bg_border_solid) as Drawable

        setButtonDrawables(startOfTheText = iconMoney)
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

        background = bgEdText

        hint = when(id){
            R.id.ed_nama_barang -> "Nama Barang"

            R.id.ed_harga_barang -> "Harga Barang"

            else -> ""
        }

        textSize = 14f
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
}