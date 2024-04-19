package com.toowhite.myblurdemo

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.BitmapFactory
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.toowhite.myblurdemo.databinding.ActivityMainBinding
import eightbitlab.com.blurview.RenderEffectBlur
import eightbitlab.com.blurview.RenderScriptBlur

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private val binding by lazy { ActivityMainBinding.inflate(LayoutInflater.from(this), null, false)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.imageView1.loadImg(applicationContext, R.drawable.vol_189_1)
        binding.imageView2.loadImg(applicationContext, R.drawable.vol_189_4)
        binding.imageView3.loadImg(applicationContext, R.drawable.vol_189_5)
        binding.imageView4.loadImg(applicationContext, R.drawable.vol_189_2)

        binding.blurBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Log.i(TAG, "onCreate: version true")
//                binding.root.setRenderEffect(
//                    RenderEffect.createBlurEffect(15f, 15f, Shader.TileMode.MIRROR)
//                )
            } else {
                Log.i(TAG, "onCreate: version false")
            }
            binding.blurView1.setupWith(binding.main, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                RenderEffectBlur()
            } else {
                RenderScriptBlur(this)
            }).setBlurEnabled(true)
                .setBlurRadius(5f)

            binding.blurView2.setupWith(binding.main, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                RenderEffectBlur()
            } else {
                RenderScriptBlur(this)
            }).setBlurEnabled(true)
                .setBlurRadius(10f)

            binding.blurView3.setupWith(binding.main, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                RenderEffectBlur()
            } else {
                RenderScriptBlur(this)
            }).setBlurEnabled(true)
                .setBlurRadius(15f)

            binding.blurView4.setupWith(binding.main, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                RenderEffectBlur()
            } else {
                RenderScriptBlur(this)
            }).setBlurEnabled(true)
                .setBlurRadius(20f)
        }

        binding.clearBtn.setOnClickListener {
            binding.blurView1.setBlurEnabled(false)
            binding.blurView2.setBlurEnabled(false)
            binding.blurView3.setBlurEnabled(false)
            binding.blurView4.setBlurEnabled(false)
        }

        binding.imageView1.setOnClickListener {
            val intent = Intent(this, ColorBlurActivity::class.java)
            startActivity(intent)
        }
    }

    private fun ImageView.loadImg(context: Context, img: Any) {
        Glide.with(context)
            .load(when (img) {
                is String -> {
                    img
                }
                is Int -> {
                    img
                }
                is Bitmap -> {
                    img
                }
                else -> {
                    null
                }
            })
            .into(this)
    }
}