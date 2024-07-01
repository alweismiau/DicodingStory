package com.dicoding.picodiploma.loginwithanimation.view.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.picodiploma.loginwithanimation.R

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private var visibilityButtonImage: Drawable = ContextCompat.getDrawable(context, R.drawable.ic_visibility_black_24dp) as Drawable
    private var isPasswordVisible: Boolean = false

    init {
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    showVisibilityButton()
                    validatePassword(s.toString()) // Validate password length and set error if necessary
                } else {
                    hideVisibilityButton()
                    error = null // Clear error when text is cleared
                }
            }
            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "Enter your password"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun showVisibilityButton() {
        setButtonDrawables(endOfTheText = visibilityButtonImage)
    }

    private fun hideVisibilityButton() {
        setButtonDrawables()
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

    private fun validatePassword(password: String) {
        if (password.length < 8) {
            error = "Password tidak boleh kurang dari 8 karakter"
        } else {
            error = null
        }
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val touchableArea = if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                (visibilityButtonImage.intrinsicWidth + paddingStart).toFloat()
            } else {
                (width - paddingEnd - visibilityButtonImage.intrinsicWidth).toFloat()
            }
            if (event.x > touchableArea && event.action == MotionEvent.ACTION_UP) {
                togglePasswordVisibility()
                return true
            }
        }
        return false
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        transformationMethod = if (isPasswordVisible) HideReturnsTransformationMethod.getInstance() else PasswordTransformationMethod.getInstance()
        visibilityButtonImage = if (isPasswordVisible) ContextCompat.getDrawable(context, R.drawable.ic_visibility_off_black_24dp)!! else ContextCompat.getDrawable(context, R.drawable.ic_visibility_black_24dp)!!
        showVisibilityButton()
    }
}
