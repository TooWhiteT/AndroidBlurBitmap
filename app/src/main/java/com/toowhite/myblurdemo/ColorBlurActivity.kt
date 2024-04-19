package com.toowhite.myblurdemo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Outline
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewOutlineProvider
import androidx.appcompat.app.AppCompatActivity
import com.google.android.renderscript.Toolkit
import com.toowhite.myblurdemo.databinding.ActivityColorBlurBinding
import eightbitlab.com.blurview.RenderEffectBlur
import eightbitlab.com.blurview.RenderScriptBlur

class ColorBlurActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityColorBlurBinding.inflate(LayoutInflater.from(this), null, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.finsh.setOnClickListener {
            finish()
        }

        binding.blur.setOnClickListener {
            binding.blurView.setupWith(binding.main, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                RenderEffectBlur()
            } else {
                RenderScriptBlur(this)
            }).setBlurEnabled(true)
                .setBlurRadius(5f)
            binding.blurView.outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    val left = 0
                    val top = 0
                    val right = view.width
                    val bottom = view.height
                    val radius = 45

                    // 全部圆角
//                    outline.setRoundRect(left, top, right, bottom, radius.toFloat())
                    // 上边圆角
                    outline.setRoundRect(left, top, right, bottom + radius, radius.toFloat())
                }
            }
            binding.blurView.clipToOutline = true
        }

        binding.loadImg.setOnClickListener {
//            val blurBmp = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888)
//            val canvas = Canvas(blurBmp)
//            canvas.drawColor(Color.parseColor("#80FFFFFF"))
            binding.blurImgView.setImageBitmap(
                ImageBlurUtil.blurBitmap(loadBitmap(R.drawable.vol_189_5), 25f)
            )
        }
    }

    fun loadBitmap(resId: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        return BitmapFactory.decodeResource(resources, resId, options)
    }
}