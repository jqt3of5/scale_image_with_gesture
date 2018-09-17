package com.jqt3of5.imageviewscale

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView

class MainActivity : AppCompatActivity() {

    var scaleGesture : ScaleGestureDetector? = null
    var panGesture : GestureDetector? = null
    var scaleFactor : Float = 1f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val image = findViewById<MyZoomableImageView>(R.id.image_view)
        image.scaleType = ImageView.ScaleType.MATRIX

        scaleGesture = ScaleGestureDetector(this, object:ScaleGestureDetector.SimpleOnScaleGestureListener(){
            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                detector?.let {

                    image.postScale(detector.scaleFactor, detector.focusX, detector.focusY)
                }
                return true
            }

        })


        panGesture = GestureDetector(this, object:GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                image.postTranslate(-distanceX, -distanceY)
                return true
            }
        })

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        scaleGesture?.onTouchEvent(event)

        panGesture?.onTouchEvent(event)

        return true
    }
}
