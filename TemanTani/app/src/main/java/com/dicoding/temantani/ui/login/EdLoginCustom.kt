package com.dicoding.temantani.ui.login

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.temantani.R

class EdLoginCustom : AppCompatEditText {
    private var iconPerson: Drawable ?= null
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

        iconPerson = when(id){
            R.id.ed_login_email -> ContextCompat.getDrawable(context, R.drawable.ic_mail_green) as Drawable

            R.id.ed_login_password -> ContextCompat.getDrawable(context, R.drawable.ic_lock_green) as Drawable

            else -> null
        }

        bgEdText = ContextCompat.getDrawable(context, R.drawable.bg_ed_text_custom) as Drawable

        setButtonDrawables(startOfTheText = iconPerson)
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
            R.id.ed_login_email -> "Email"

            R.id.ed_login_password -> "Password"

            else -> ""
        }

        textSize = 14f
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
}