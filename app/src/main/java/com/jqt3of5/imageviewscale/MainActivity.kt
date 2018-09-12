package com.jqt3of5.imageviewscale

import android.graphics.Matrix
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import java.io.Console

class MainActivity : AppCompatActivity() {

    var gesture : ScaleGestureDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val image = findViewById<ImageView>(R.id.image_view)
        image.scaleType = ImageView.ScaleType.MATRIX

        gesture = ScaleGestureDetector(this, object:ScaleGestureDetector.SimpleOnScaleGestureListener(){
            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                detector?.let {

                    val mx = Matrix(image.imageMatrix)

                    mx.postScale(detector.scaleFactor, detector.scaleFactor, detector.focusX, detector.focusY)

                    image.imageMatrix = mx
                }
                return true
            }


        })
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gesture?.onTouchEvent(event)
        return true
    }
}
