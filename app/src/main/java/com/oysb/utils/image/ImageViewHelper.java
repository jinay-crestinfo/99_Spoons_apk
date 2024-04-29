package com.oysb.utils.image;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.widget.ImageView;
import com.oysb.utils.Loger;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: classes2.dex */
public class ImageViewHelper {
    static Handler handler;
    static ExecutorService service;

    public static void stopPlay(ImageView imageView) {
        try {
            ((AnimationDrawable) imageView.getBackground()).stop();
            imageView.clearAnimation();
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
            Loger.writeException("WxFacePayErrorCode", e);
        }
    }

    public static void playImages(ImageView imageView, List<Integer> list, int i) {
        imageView.clearAnimation();
        AnimationDrawable animationDrawable = new AnimationDrawable();
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            animationDrawable.addFrame(imageView.getContext().getResources().getDrawable(it.next().intValue()), i);
        }
        animationDrawable.setOneShot(false);
        imageView.setBackgroundDrawable(animationDrawable);
        animationDrawable.start();
    }

    public static void playImages(ImageView imageView, List<Integer> list, int i, boolean z) {
        imageView.clearAnimation();
        AnimationDrawable animationDrawable = new AnimationDrawable();
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            animationDrawable.addFrame(imageView.getContext().getResources().getDrawable(it.next().intValue()), i);
        }
        animationDrawable.setOneShot(!z);
        imageView.setBackgroundDrawable(animationDrawable);
        animationDrawable.start();
    }

    public static void playImages2(ImageView imageView, List<Bitmap> list, int i) {
        imageView.clearAnimation();
        AnimationDrawable animationDrawable = new AnimationDrawable();
        Iterator<Bitmap> it = list.iterator();
        while (it.hasNext()) {
            animationDrawable.addFrame(new BitmapDrawable(imageView.getContext().getResources(), it.next()), i);
        }
        animationDrawable.setOneShot(false);
        imageView.setBackgroundDrawable(animationDrawable);
        animationDrawable.start();
    }

    public static void slowLoadImage(ImageView imageView, ImageLoadRunnable imageLoadRunnable) {
        if (service == null) {
            service = Executors.newCachedThreadPool();
        }
        if (handler == null) {
            handler = new Handler();
        }
        imageLoadRunnable.imageView = new WeakReference<>(imageView);
        service.submit(imageLoadRunnable);
    }

    /* loaded from: classes2.dex */
    public static abstract class ImageLoadRunnable implements Runnable {
        WeakReference<ImageView> imageView;

        public abstract Bitmap loadBitmap();

        @Override
        public void run() {
            Bitmap bitmap = loadBitmap();
            if (bitmap != null && imageView.get() != null) {
                ImageView imageViewRef = imageView.get();
                ImageViewHelper.handler.post(() -> {
                    if (imageViewRef != null) {
                        imageViewRef.setImageBitmap(bitmap);
                    }
                });
            }
        }
    }

}
