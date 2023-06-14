package com.dicoding.temantani.ui.register

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

class EdRegisterCustom : AppCompatEditText {

    private lateinit var iconPerson: Drawable
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

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                when(id){
                    R.id.ed_email -> {
                        val stringEmail = s.toString()
                        if(Patterns.EMAIL_ADDRESS.matcher(stringEmail).matches() == false) error = "Format Email Salah" else error = null
                    }

                    R.id.ed_telephone -> {
                        val stringPhone = s.toString()
                        if(Patterns.PHONE.matcher(stringPhone).matches() == false && stringPhone.length < 10) error = "Format No.Telepon Salah" else error = null
                    }
                }

            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }

        })

        iconPerson = when(id){
            R.id.ed_nama -> ContextCompat.getDrawable(context, R.drawable.ic_person_green) as Drawable

            R.id.ed_email -> ContextCompat.getDrawable(context, R.drawable.ic_mail_green) as Drawable

            R.id.ed_telephone -> ContextCompat.getDrawable(context, R.drawable.ic_phone_green) as Drawable

            R.id.ed_password -> ContextCompat.getDrawable(context, R.drawable.ic_lock_green) as Drawable

            else -> bgEdText
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
            R.id.ed_nama -> "Nama"

            R.id.ed_email -> "Email"

            R.id.ed_telephone -> "No.Telepon"

            R.id.ed_password -> "Password"

            else -> ""
        }

        textSize = 14f
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

}