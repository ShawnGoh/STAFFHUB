package com.example.blewifiterm5project.Layout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.example.blewifiterm5project.R;
import com.example.blewifiterm5project.Utils.DensityUtil;
import com.github.chrisbanes.photoview.OnMatrixChangedListener;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ImageDotLayout extends FrameLayout implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = ImageDotLayout.class.getSimpleName();
    private List<ImageView> iconList;
    public PhotoView photoView;
    private RectF tempRectF;
    private OnIconClickListener onIconClickListener;
    private OnIconLongClickListener onIconLongClickListener;
    private OnLayoutReadyListener onLayoutReadyListener;
    private OnImageClickListener onImageClickListener;
    private Matrix photoViewMatrix;
    boolean firstLoadPhotoView = true;

    public Bitmap bitmap;



    public ImageDotLayout(@NonNull Context context) {
        this(context, null);
    }

    public ImageDotLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageDotLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    // Message handler
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x1234) {
                photoView.setImageBitmap(bitmap);
            }
        }
    };

    private Drawable mIconDrawable = ContextCompat.getDrawable(getContext(), R.drawable.icon_location);

    void initView(final Context context) {
        photoView = new PhotoView(context);
        LayoutParams layoutParams =
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(photoView, layoutParams);
        photoView.setOnMatrixChangeListener(new OnMatrixChangedListener() {
            @Override
            public void onMatrixChanged(RectF rectF) {
                if (iconList != null && iconList.size() > 0) {
                    for (ImageView icon : iconList) {
                        IconBean bean = (IconBean) icon.getTag();
                        float newX = bean.sx * (rectF.right - rectF.left);
                        float newY = bean.sy * (rectF.bottom - rectF.top);
                        icon.setX(rectF.left + newX - DensityUtil.dp2px(getContext(), 45) / 2);
                        icon.setY(rectF.top + newY - DensityUtil.dp2px(getContext(), 48));
                    }
                }
                tempRectF = rectF;
                // Check whether the image is loaded
                if (onLayoutReadyListener != null) {
                    onLayoutReadyListener.onLayoutReady();
                    // Ensure that it is only called once
                    onLayoutReadyListener = null;
                }

            }
        });

        // Tap listener to set Pin
        photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float v, float v1) {
                Log.i(TAG, "onPhotoTap");
                int id = 0;
                if (iconList != null && iconList.size() > 0) {
                    id = iconList.size();
                }
                IconBean bean = new IconBean(id, v, v1, mIconDrawable);
                if (onImageClickListener != null) {
                    onImageClickListener.onImageClick(bean);
                }
            }
        });
    }

    public void setIconDrawable(Drawable drawable) {
        mIconDrawable = drawable;
    }

    public void addIcon(IconBean bean) {
        //Save current matrix
        if (photoViewMatrix == null) {
            photoViewMatrix = new Matrix();
        }
        photoView.getAttacher().getSuppMatrix(photoViewMatrix);
        if (iconList == null) {
            iconList = new ArrayList<>();
        }
        final ImageView icon = new ImageView(getContext());
        LayoutParams layoutParams = new LayoutParams(DensityUtil.dp2px(getContext(), 45), DensityUtil.dp2px(getContext(), 48));
        icon.setImageDrawable(bean.drawable == null ? mIconDrawable : bean.drawable);
        icon.setTag(bean);
        float newX = bean.sx * (tempRectF.left - tempRectF.right);
        float newY = bean.sy * (tempRectF.bottom - tempRectF.top);
        icon.setX(tempRectF.left + newX);
        icon.setY(tempRectF.top + newY);
        icon.setOnClickListener(this);
        icon.setOnLongClickListener(this);
        addView(icon, layoutParams);
        iconList.add(icon);
    }

    public void updateIconResource(ImageView icon, Drawable drawable) {
        icon.setImageDrawable(drawable);
    }

    public void addIcon(int id, float sx, float sy, Drawable drawable) {
        IconBean iconBean = new IconBean(id, sx, sy, drawable);
        addIcon(iconBean);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onClick(View v) {
        if (onIconClickListener != null) {
            onIconClickListener.onIconClick(v);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (photoViewMatrix != null) {
            System.out.println("Change matrix...");
            photoView.getAttacher().setDisplayMatrix(photoViewMatrix);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (onIconLongClickListener != null) {
            onIconLongClickListener.onIconLongClick(v);
        }
        return true;
    }


    public static class IconBean {
        public int id;
        public float sx;
        public float sy;
        public Drawable drawable;//Icon

        public IconBean(int id, float sx, float sy, Drawable drawable) {
            this.id = id;
            this.sx = sx;
            this.sy = sy;
            this.drawable = drawable;
        }
    }

    public interface OnIconClickListener {
        void onIconClick(View v);
    }

    public interface OnIconLongClickListener {
        void onIconLongClick(View v);
    }

    public interface OnImageClickListener {
        void onImageClick(IconBean bean);
    }

    public interface OnLayoutReadyListener {
        void onLayoutReady();
    }

    public void setOnIconClickListener(OnIconClickListener onIconClickListener) {
        this.onIconClickListener = onIconClickListener;
    }

    public void setOnIconLongClickListener(OnIconLongClickListener onIconLongClickListener) {
        this.onIconLongClickListener = onIconLongClickListener;
    }

    public void setOnLayoutReadyListener(OnLayoutReadyListener onLayoutReadyListener) {
        this.onLayoutReadyListener = onLayoutReadyListener;
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    /**
     * Set image to photoView
     *
     * @param url url of image in string
     */
    public void setImage(String url) {
        firstLoadPhotoView = true;

        Thread thread = new DownloadImageThread(url);
        thread.start();
    }

    /**
     * Remove icon
     */
    public void removeIcon(ImageView icon) {
        removeView(icon);
    }

    /**
     * Remove all icon
     */
    public void removeAllIcon() {
        if (iconList != null && iconList.size() > 0) {
            for (ImageView icon : iconList) {
                removeView(icon);
            }
            iconList.clear();
        }
    }

    /**
     * Get icon infomation
     *
     * @return
     */
    public List<IconBean> getAllIconInfos() {
        List<IconBean> rectBeans = new ArrayList<>();
        if (iconList != null && iconList.size() > 0) {
            for (ImageView icon : iconList) {
                IconBean rectBean = (IconBean) icon.getTag();
                rectBeans.add(rectBean);
            }
        }
        return rectBeans;
    }

    public void addIcons(List<IconBean> iconBeanList) {
        if (iconBeanList != null && iconBeanList.size() > 0) {
            for (IconBean bean : iconBeanList) {
                addIcon(bean);
            }
        }
    }

    // Working thread to download the image from url and apply to photoView
    private class DownloadImageThread extends Thread{
        String url;

        public DownloadImageThread(String url){
            this.url = url;
        }

        @Override
        public void run() {
            super.run();
            System.out.println("Working thread running");
            try {
                // Define URL
                URL url = new URL(this.url);
                // Open the input stream
                InputStream is = url.openStream();
                // Get image bitmap from inputstream
                bitmap = BitmapFactory.decodeStream(is);
                System.out.println("Work done!");
                // Send message to handler
                handler.sendEmptyMessage(0x1234);
                is.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
