package com.domker.study.androidstudy

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_image.*
import java.util.*
import kotlin.math.abs
import android.view.MotionEvent




class GlideActivity : AppCompatActivity() {
    private var xOrig = 0.0F
    private var yOrig = 0.0F
    private var spanOrig = 0.0F
    private var scaleOrig = 0.0F
    private var itemNum = 0
    private var sumNum = 3

    private val pages: MutableList<String> = ArrayList()
    private var imageView : ImageView? = null

    private lateinit var translationGestureDetector : GestureDetector
    private lateinit var scaleGestureDetector: ScaleGestureDetector

    private val distMin: Int = 50

    override fun onCreate(savedInstanceState: Bundle?) {
        pages.add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F202003%2F30%2F20200330105631_2Aw8r.thumb.1000_0.jpeg")
        pages.add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201701%2F20%2F20170120142750_2VYNQ.thumb.1000_0.gif")
        pages.add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi0.hdslb.com%2Fbfs%2Farticle%2Fa56b6f1cdc9a730ed47b5a566f4077447a60e68a.jpg")
        pages.add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201901%2F01%2F20190101201827_oelmf.thumb.400_0.jpg")
        pages.add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F202003%2F29%2F20200329042030_uCcGM.thumb.400_0.gif")
        sumNum = pages.count()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_image)

        imageView = findViewById(R.id.image_single)
        imageView?.let {
            Glide.with(this)
                .load(pages[0])
                .apply(RequestOptions().circleCrop().diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(it)
        }

        translationGestureDetector = GestureDetector(this, object : GestureDetector.OnGestureListener {
            override fun onDown(p0: MotionEvent?) = true

            override fun onShowPress(p0: MotionEvent?) {}

            override fun onSingleTapUp(p0: MotionEvent?) = true

            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent?,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                return true
            }

            override fun onLongPress(p0: MotionEvent?) {}

            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent?,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                val dx = e2!!.x - e1!!.x
                val dy = e1!!.y - e2!!.y
                Log.d("PicFrag::movement", "$dx, $dy")
                if (dx * dx + dy * dy > distMin) {
                    if (dx > 0) {
                        itemNum = (itemNum - 1 + sumNum) % sumNum
                        Log.d("GEST", "RIGHT")
                    };
                    else {
                        itemNum = (itemNum + 1) % sumNum
                        Log.d("GEST", "LEFT")
                    };
                    imageView?.let {
                        Glide.with(this@GlideActivity)
                            .load(pages[itemNum])
                            .apply(RequestOptions().circleCrop().diskCacheStrategy(DiskCacheStrategy.ALL))
                            .into(it)
                    }
                }
                Log.d("GEST", "Fling")
                return true
            }
        })

        scaleGestureDetector = ScaleGestureDetector(this, object : ScaleGestureDetector.OnScaleGestureListener {
            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                detector?.let { it ->
                    val dx = it.focusX - xOrig
                    val dy = it.focusY - yOrig
                    xOrig = it.focusX
                    yOrig = it.focusY
                    val rate = it.currentSpan / spanOrig

                    imageView?.let { iv->
                        iv.scaleX = scaleOrig * rate
                        iv.scaleY = scaleOrig * rate
                        iv.x += dx
                        iv.y += dy
                    }
                }
                return true
            }

            override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
                detector?.let {
                    xOrig = it.focusX
                    yOrig = it.focusY
                    spanOrig = it.currentSpan
                    scaleOrig = imageView!!.scaleX
                }
                return true
            }

            override fun onScaleEnd(detector: ScaleGestureDetector?) {}

        })
    }

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        if (e != null) {
            return when (e.pointerCount) {
                1 -> {
                    translationGestureDetector.onTouchEvent(e)
                }
                2 -> {
                    scaleGestureDetector.onTouchEvent(e)
                }
                else -> {
                    true
                }
            }
        }
        return true
    }
}