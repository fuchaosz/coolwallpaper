package com.coolwallpaper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.coolwallpaper.activity.BaseActivity;
import com.coolwallpaper.bean.PictureBean;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 显示图片详情
 * Created by fuchao on 2015/10/21.
 */
public class ShowPictureDetailActivity extends BaseActivity implements View.OnClickListener {

    private PictureBean pictureBean;
    private ImageLoader imageLoader;
    private Matrix matrix;
    private float maxMoveLength;//最大可以移动的距离
    private int currentProgress = 50;//当前进度

    @ViewInject(R.id.iv_image)
    ImageView ivImage;

    //浮动层菜单
    @ViewInject(R.id.ly_picture_detail_menu)
    View lyPictureDetailMenu;

    @ViewInject(R.id.pb_progress)
    ProgressBar progressBar;

    @ViewInject(R.id.sb_seekbar)
    SeekBar seekBar;

    /**
     * 启动方法，要显示的图片
     *
     * @param context
     * @param bean
     */
    public static void startActivity(Context context, PictureBean bean) {
        Intent intent = new Intent(context, ShowPictureDetailActivity.class);
        intent.putExtra("PICTURE_BEAN", bean);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_detail);
        this.pictureBean = (PictureBean) getIntent().getSerializableExtra("PICTURE_BEAN");
        //初始化
        this.init();
        //添加简体器
        this.addListener();
    }

    //初始化
    private void init() {
        //创建ImageLoader
        this.imageLoader = ImageLoader.getInstance();
        this.imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        //显示图片
        this.imageLoader.displayImage(pictureBean.getImageUrl(), ivImage, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                //调整图片，使得图片的高度与屏幕一样高
                scalePictureToScreenHeight();
                seekBar.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }

    //添加监听器
    private void addListener() {
        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //移动图片
                movePicture(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //使得下层的可以点击
        lyPictureDetailMenu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            //点击图片
            case R.id.iv_image:
                //弹出浮动层菜单
                lyPictureDetailMenu.setVisibility(View.VISIBLE);
                break;
            //顶部向左的箭头
            case R.id.btn_left:
                this.finish();
                break;
        }
    }

    //放大图片
    private void scalePictureToScreenHeight() {
        this.matrix = ivImage.getImageMatrix();
        //获取ImageView的大小
        int viewHeight = ivImage.getHeight() - ivImage.getPaddingBottom() - ivImage.getPaddingTop();
        int viewWidth = ivImage.getWidth() - ivImage.getPaddingLeft() - ivImage.getPaddingRight();
        //获取图片的实际大小
        int drawWidth = ivImage.getDrawable().getIntrinsicWidth();
        int drawHeight = ivImage.getDrawable().getIntrinsicHeight();
        //图片缩放，保证高度铺满整个屏幕
        float scale = viewHeight * 1.0f / drawHeight;
        matrix.postScale(scale, scale);
        ivImage.setImageMatrix(matrix);
        //计算出放大之后的图片的宽度,高度就是屏幕的高度
        float widthAfterScale = drawWidth * scale;
        //最大移动距离
        this.maxMoveLength = (widthAfterScale - viewWidth) / 2;
        //将图片移动到中间去
        matrix.postTranslate(-maxMoveLength, 0);
        ivImage.setImageMatrix(matrix);
        //        //屏幕向右移动
        //        btnRight.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                moveLength -= 100;
        //                if (moveLength >= -maxMoveLength) {
        //                    matrix.postTranslate(-100, 0);
        //                    image.setImageMatrix(matrix);
        //                    count += 100;
        //                } else {
        //                    //超过屏幕范围之后要控制刚好显示到屏幕边上
        //                    float tmp = maxMoveLength + (moveLength + 100);
        //                    matrix.postTranslate(-tmp, 0);
        //                    count += tmp;
        //                    image.setImageMatrix(matrix);
        //                    moveLength = -maxMoveLength;
        //                }
        //            }
        //        });
        //        //向右移动图片
        //        btnLeft.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                moveLength += 100;
        //                if (moveLength <= maxMoveLength) {
        //                    matrix.postTranslate(100, 0);
        //                    image.setImageMatrix(matrix);
        //                } else {
        //                    //超过屏幕范围之后要控制刚好显示到屏幕边上
        //                    float tmp = maxMoveLength - (moveLength - 100);
        //                    matrix.postTranslate(tmp, 0);
        //                    image.setImageMatrix(matrix);
        //                    moveLength = maxMoveLength;
        //                }
        //            }
        //        });
    }

    //移动图片
    private void movePicture(int progress) {
        //计算需要移动的比例
        float moveDistance = (progress - currentProgress) / 50f * maxMoveLength;
        matrix.postTranslate(-moveDistance, 0);
        ivImage.setImageMatrix(matrix);
        currentProgress = progress;
    }

}