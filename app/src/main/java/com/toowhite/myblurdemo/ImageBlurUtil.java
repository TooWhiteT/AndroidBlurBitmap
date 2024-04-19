package com.toowhite.myblurdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.HardwareRenderer;
import android.graphics.PixelFormat;
import android.graphics.RecordingCanvas;
import android.graphics.RenderEffect;
import android.graphics.RenderNode;
import android.graphics.Shader;
import android.hardware.HardwareBuffer;
import android.media.Image;
import android.media.ImageReader;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import com.google.android.renderscript.Toolkit;

/**
 * @author TooWhiteT
 * @description 高斯模糊工具类
 * @createtime 2024年04月18日 14:25
 */

public class ImageBlurUtil {

    /**
     * Android纯自带的
     * @param context 老版本RS需要上下文
     * @param bitmap 源位图
     * @param radius 模糊半径
     * @return 返回高斯模糊的位图
     */
    public static Bitmap blurBitmap(Context context, Bitmap bitmap, float radius) {
        // 先缩放
        float scale = 0.1f; // 缩放越小 越模糊
        int width = Math.round(bitmap.getWidth() * scale);
        int height = Math.round(bitmap.getHeight() * scale);
        Bitmap outBmp = Bitmap.createScaledBitmap(bitmap, width, height, false);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            ImageReader imageReader = ImageReader.newInstance(
                    outBmp.getWidth(),
                    outBmp.getHeight(),
                    PixelFormat.RGBA_8888,
                    2,
                    HardwareBuffer.USAGE_GPU_SAMPLED_IMAGE | HardwareBuffer.USAGE_GPU_COLOR_OUTPUT);

            RenderNode renderNode = new RenderNode("BlurBitmapNode");
            HardwareRenderer hardwareRenderer = new HardwareRenderer();
            hardwareRenderer.setSurface(imageReader.getSurface());
            hardwareRenderer.setContentRoot(renderNode);
            renderNode.setPosition(0, 0, imageReader.getWidth(), imageReader.getHeight());
            RenderEffect blurEffect = RenderEffect.createBlurEffect(radius, radius, Shader.TileMode.MIRROR);
            renderNode.setRenderEffect(blurEffect);
            RecordingCanvas canvas = renderNode.beginRecording();
            canvas.drawBitmap(outBmp, 0, 0, null);
            renderNode.endRecording();

            hardwareRenderer.createRenderRequest()
                    .setWaitForPresent(true)
                    .syncAndDraw();

            Image image = imageReader.acquireNextImage();
            HardwareBuffer imageHardwareBuffer = image.getHardwareBuffer();
            if (imageHardwareBuffer != null) {
                Bitmap blurTemp = Bitmap.wrapHardwareBuffer(imageHardwareBuffer, null);
                imageHardwareBuffer.close();
                image.close();
                return blurTemp;
            }
        } else {
            // 创建 RenderScript 实例
            RenderScript rs = RenderScript.create(context);
            // 将 Bitmap 转换为 Allocation
            Allocation input = Allocation.createFromBitmap(rs, outBmp);
            Allocation output = Allocation.createTyped(rs, input.getType());
            // 创建高斯模糊效果的 IntrisicBlur RenderScript
            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setInput(input);
            // 设置模糊半径
            script.setRadius(radius);
            // 执行高斯模糊
            script.forEach(output);
            // 将输出拷贝到 Bitmap
            output.copyTo(outBmp);
            // 销毁 RenderScript 实例
            rs.destroy();
            script.destroy();
            input.destroy();
            output.destroy();
        }
        return outBmp;
    }

    /**
     * 使用RS-ToolKit代替RS，不用传上下文
     * @param bitmap 源位图
     * @param radius 模糊半径
     * @return 返回高斯模糊的位图
     */
    public static Bitmap blurBitmap(Bitmap bitmap, float radius) {
        // 先缩放
        float scale = 0.1f; // 缩放越小 越模糊
        int width = Math.round(bitmap.getWidth() * scale);
        int height = Math.round(bitmap.getHeight() * scale);
        Bitmap outBmp = Bitmap.createScaledBitmap(bitmap, width, height, false);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            ImageReader imageReader = ImageReader.newInstance(
                    outBmp.getWidth(),
                    outBmp.getHeight(),
                    PixelFormat.RGBA_8888,
                    2,
                    HardwareBuffer.USAGE_GPU_SAMPLED_IMAGE | HardwareBuffer.USAGE_GPU_COLOR_OUTPUT);

            RenderNode renderNode = new RenderNode("BlurBitmapNode");
            HardwareRenderer hardwareRenderer = new HardwareRenderer();
            hardwareRenderer.setSurface(imageReader.getSurface());
            hardwareRenderer.setContentRoot(renderNode);
            renderNode.setPosition(0, 0, imageReader.getWidth(), imageReader.getHeight());
            RenderEffect blurEffect = RenderEffect.createBlurEffect(radius, radius, Shader.TileMode.MIRROR);
            renderNode.setRenderEffect(blurEffect);
            RecordingCanvas canvas = renderNode.beginRecording();
            canvas.drawBitmap(outBmp, 0, 0, null);
            renderNode.endRecording();

            hardwareRenderer.createRenderRequest()
                    .setWaitForPresent(true)
                    .syncAndDraw();

            Image image = imageReader.acquireNextImage();
            HardwareBuffer imageHardwareBuffer = image.getHardwareBuffer();
            if (imageHardwareBuffer != null) {
                Bitmap blurTemp = Bitmap.wrapHardwareBuffer(imageHardwareBuffer, null);
                imageHardwareBuffer.close();
                image.close();
                return blurTemp;
            }
        } else {
            // 用RS-ToolKit 代替 RS
            return Toolkit.INSTANCE.blur(outBmp, (int) radius, null);
        }
        return outBmp;
    }
}
