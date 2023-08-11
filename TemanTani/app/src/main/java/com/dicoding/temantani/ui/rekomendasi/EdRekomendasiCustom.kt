package com.dicoding.temantani.ui.rekomendasi

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.temantani.R

class EdRekomendasiCustom : AppCompatEditText {

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
        bgEdText = ContextCompat.getDrawable(context, R.drawable.bg_ed_rekomen) as Drawable
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = bgEdText

        hint = when(id){
            R.id.ed_nitrogen -> "N (Nitrogen)"

            R.id.ed_fosfor -> "P (Fosfor)"

            R.id.ed_kalium -> "K (Kalium)"

            R.id.ed_ph -> "Ph Tanah"

            R.id.ed_rainfall -> "Curah Hujan (Rainfall)"

            else -> ""
        }

        textSize = 14f
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

}