package com.kliksembuh.ks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;

public class TestScroolView extends AppCompatActivity {
    private ScrollView mScrollView;
    private ImageView mPhotoIV;
    private FrameLayout mWrapperFL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scrool_view);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mPhotoIV = (ImageView) findViewById(R.id.contactPic);
        mWrapperFL = (FrameLayout) findViewById(R.id.flWrapper);

        mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ScrollPositionObserver());

    }
    private class ScrollPositionObserver implements ViewTreeObserver.OnScrollChangedListener {

        private int mImageViewHeight;

        public ScrollPositionObserver() {
            mImageViewHeight = getResources().getDimensionPixelSize(R.dimen.contact_photo_height);
        }

        @Override
        public void onScrollChanged() {
            int scrollY = Math.min(Math.max(mScrollView.getScrollY(), 0), mImageViewHeight);

            // changing position of ImageView
            mPhotoIV.setTranslationY(scrollY / 2);

            // alpha you could set to ActionBar background
            float alpha = scrollY / (float) mImageViewHeight;
        }
    }
}
