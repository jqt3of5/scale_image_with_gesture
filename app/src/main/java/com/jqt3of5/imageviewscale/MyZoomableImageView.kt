package com.jqt3of5.imageviewscale

import android.content.Context
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.ScaleGestureDetector
import android.widget.ImageView
import kotlin.math.max
import kotlin.math.min

/**
 * TODO: document your custom view class.
 */
class MyZoomableImageView : ImageView {

    val MAX_SCALE = 5f
    var aspectFitScale : Float = 1f

    constructor(context: Context) : super(context)
    {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    {
        init(context)
    }

    private fun init(context:Context)
    {
        //Must set this so that we can use the matrix to set the image size/translations
        scaleType = ImageView.ScaleType.MATRIX

    }

    fun postTranslate(x : Float, y: Float)
    {
        val mx = Matrix(imageMatrix)
        val mxValues : FloatArray = FloatArray(9)
        mx.getValues(mxValues)

        //We always use the same scale for x and y
        val scaleFactor = mxValues[Matrix.MSCALE_X]

        //The "maximum" translation is actually a negative number. How much the scaled image can be translated up and to the left before
        //the bottom right of our screen hits the bottom right of the image.
        val max_tx = width - drawable.intrinsicWidth * scaleFactor
        val max_ty = height - drawable.intrinsicHeight * scaleFactor

        //x and y are offsets, (why we called this method "post".) so we need to add the current translation to it so that we can compare it too
        //0 and max_tx/y because those are absolute values
        val tx = min (0f, max(max_tx, x + mxValues[Matrix.MTRANS_X]))
        val ty = min (0f, max(max_ty, y + mxValues[Matrix.MTRANS_Y]))

        //Once we have our proposed absolute translation value, subtract the current one to get the offset, so we can call postTranslate
        mx.postTranslate(tx-mxValues[Matrix.MTRANS_X], ty-mxValues[Matrix.MTRANS_Y])

        imageMatrix = mx
    }

    fun postScale(scale : Float, focusX : Float, focusY: Float)
    {
        val mx = Matrix(imageMatrix)
        val mxValues : FloatArray = FloatArray(9)
        mx.getValues(mxValues)

        //"aspectFitScale" is some scale factor for adjusting the image to fit onto our screen. It is the minimum scale factor,
        //and a multiple of it is the maximum scale factor. "scale" is a relative scale factor, so multiply it by the absolute value stored in the matrix
        // to get the proposed absolute value to compare against.
        val scaleFactor = min(MAX_SCALE*aspectFitScale, max(aspectFitScale, scale*mxValues[Matrix.MSCALE_X]))
        mx.postScale(scaleFactor/mxValues[Matrix.MSCALE_X], scaleFactor/mxValues[Matrix.MSCALE_X], focusX, focusY)

        //We just changed the matrix so we need to get teh values out again.
        mx.getValues(mxValues)

        //The "maximum" translation is actually a negative number. How much the scaled image can be translated up and to the left before
        //the bottom right of our screen hits the bottom right of the image.
        val max_tx = width - drawable.intrinsicWidth * scaleFactor
        val max_ty = height - drawable.intrinsicHeight * scaleFactor

        val tx = min (0f, max(max_tx, mxValues[Matrix.MTRANS_X]))
        val ty = min (0f, max(max_ty, mxValues[Matrix.MTRANS_Y]))

        //Once we have our proposed absolute translation value, subtract the current one to get the offset, so we can call postTranslate
        mx.postTranslate(tx-mxValues[Matrix.MTRANS_X], ty-mxValues[Matrix.MTRANS_Y])


        imageMatrix = mx
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (drawable.intrinsicHeight > drawable.intrinsicWidth)
        {
            aspectFitScale = measuredHeight.toFloat()/drawable.intrinsicHeight
        }
        else
        {
            aspectFitScale = measuredWidth.toFloat()/drawable.intrinsicWidth
        }

        val mx = Matrix(imageMatrix)

        mx.setScale(aspectFitScale, aspectFitScale,0f,0f)

        imageMatrix = mx
    }

}
