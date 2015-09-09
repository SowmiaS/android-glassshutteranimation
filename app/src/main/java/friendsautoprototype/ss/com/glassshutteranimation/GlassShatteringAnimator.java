package friendsautoprototype.ss.com.glassshutteranimation;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by sowmi on 05/09/15.
 */
public class GlassShatteringAnimator {

    private static final int DEFAULT_SLICED_IMAGE_WIDTH = 50;
    private static final int DEFAULT_ANIMATION_DURATION = 3000;

    private View mGlassShatteringView;
    private int mShatteredImgWidth ,mShatteredImgHeight, mDuration;
    private ArrayList<MyCustomBitmapDrawable> splitImgs = new ArrayList<MyCustomBitmapDrawable>();
    private GlassShatteringImageView mGlassShatteringImageView;

    /**
     * This method adds an ImageView on top of the animatedView.
     * Its parent should be FrameLayout // These part has to be changed.
     * @param mGlassShatteringView
     */
    public GlassShatteringAnimator(View mGlassShatteringView) {
        this.mGlassShatteringView = mGlassShatteringView;
        this.mGlassShatteringImageView = new GlassShatteringImageView(mGlassShatteringView.getContext());
        if (mGlassShatteringView.getParent() != null) {
            FrameLayout mParent = (FrameLayout) mGlassShatteringView.getParent();
            mParent.addView(mGlassShatteringImageView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            mGlassShatteringImageView.bringToFront();
        }
        init();
    }

    /**
     * Thie method intializes the default values.
     */
    private void init() {
        mShatteredImgWidth = mShatteredImgHeight = DEFAULT_SLICED_IMAGE_WIDTH;
        mDuration = DEFAULT_ANIMATION_DURATION;
    }

    /**
     * This method starts the animation.
     */
    public void start() {
        splitImgs.clear();
        getSlicedBitmaps();
        mGlassShatteringImageView.setBitmapDrawables(splitImgs);
        mGlassShatteringView.setBackgroundColor(mGlassShatteringView.getResources().getColor(android.R.color.transparent));
        mGlassShatteringImageView.invalidate();

        for (int i =0 ; i< splitImgs.size() ; i++) {

            ObjectAnimator animator = ObjectAnimator.ofInt(splitImgs.get(i), "mTop", splitImgs.get(i).getMTop(), mGlassShatteringView.getHeight()+100);
            animator.setDuration(randInt(mDuration/4, mDuration));
            animator.setInterpolator(new AccelerateInterpolator());
            animator.start();
        }
        invalidatePeriodically();
    }

    /**
     * This method invalidates the view every 5 millisecs
     */
    public void invalidatePeriodically(){
        mGlassShatteringImageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mGlassShatteringImageView.invalidate();
                invalidatePeriodically();
            }
        }, 5);
    }

    
    public void getSlicedBitmaps() {

        int mShatteredImgWidth = this.mShatteredImgWidth;
        int mShatteredImgHeight = this.mShatteredImgHeight;

        Bitmap mainBitmap = Bitmap.createBitmap(mGlassShatteringView.getDrawingCache());
        int mMainBitmapWidth = mainBitmap.getWidth();
        int mMainBitmapHeight = mainBitmap.getHeight();

        Log.d("Slicing", "mMainBitmapHeight"+mMainBitmapHeight);
        Log.d("Slicing", "mMainBitmapWidth"+mMainBitmapWidth);

        for (int y = 0; y < mMainBitmapHeight ; ){
            for (int x = 0 ; x < mMainBitmapWidth ; ){

                if(x+mShatteredImgWidth >= mMainBitmapWidth){
                    mShatteredImgWidth = mMainBitmapWidth - x;
                }
                if(y+mShatteredImgHeight >= mMainBitmapHeight){
                    mShatteredImgHeight = mMainBitmapHeight - y;
                }

                Log.d("Slicing", "x"+x);
                Log.d("Slicing", "y"+y);
                Log.d("Slicing", "Width" + mShatteredImgWidth);
                Log.d("Slicing", "Height" + mShatteredImgHeight);

                MyCustomBitmapDrawable drawable = new MyCustomBitmapDrawable(mGlassShatteringView.getContext().getResources(), Bitmap.createBitmap(mainBitmap, x, y, mShatteredImgWidth, mShatteredImgHeight));
                drawable.setBounds(x,y, x + mShatteredImgWidth - randInt(1, 4) , y + mShatteredImgHeight-randInt(1, 4));
                splitImgs.add(drawable);

                x += mShatteredImgWidth;
                mShatteredImgWidth = this.mShatteredImgWidth;

            }

            y += mShatteredImgHeight;
            mShatteredImgHeight = this.mShatteredImgHeight;
        }

    }

    public int getmShatteredImgHeight() {
        return mShatteredImgHeight;
    }

    public void setmShatteredImgHeight(int mShatteredImgHeight) {
        this.mShatteredImgHeight = mShatteredImgHeight;
    }

    public int getmShatteredImgWidth() {
        return mShatteredImgWidth;
    }

    public void setmShatteredImgWidth(int mShatteredImgWidth) {
        this.mShatteredImgWidth = mShatteredImgWidth;
    }

    public int getmDuration() {
        return mDuration;
    }

    public void setmDuration(int mDuration) {
        this.mDuration = mDuration;
    }


    public static class GlassShatteringImageView extends  ImageView{

        private ArrayList<MyCustomBitmapDrawable> bitmapDrawables = new ArrayList<MyCustomBitmapDrawable>();

        public GlassShatteringImageView(Context context) {
            super(context);
        }

        public GlassShatteringImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public GlassShatteringImageView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public ArrayList<MyCustomBitmapDrawable> getBitmapDrawables() {
            return bitmapDrawables;
        }

        public void setBitmapDrawables(ArrayList<MyCustomBitmapDrawable> bitmapDrawables) {
            this.bitmapDrawables = bitmapDrawables;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            Log.d("GlassShImageView", "Size of Sliced Bitmap Drawables" + bitmapDrawables.size());
            for (int i = 0; i < bitmapDrawables.size() ; i ++){
                bitmapDrawables.get(i).draw(canvas);
            }
        }
    }

    /**
     * This Class that represents the split bitmapdrawable. The Top and bottom value of this
     * class is animated.
     */
    private static class MyCustomBitmapDrawable extends BitmapDrawable{

        /**
         * Constructor of MyCustomBitmapDrawable
         * To give a real effect, random direction is choosen. 0 - left, 1- Right, 2 & 3 - No Movement.
         * @param res
         * @param bitmap
         */
        public MyCustomBitmapDrawable(Resources res, Bitmap bitmap) {
            super(res, bitmap);
            this.direction = randInt(0,9);
        }

        private int mLeft;
        private int mRight;
        private int mTop;
        private int mBottom;
        private int mWidth;

        /** Direction of movement during animation. It is randmly set in constructor **/
        private int direction;


        public int getMLeft() {
            return mLeft;
        }

        public void setMLeft(int left) {
            setBounds(left, mTop, mRight, mBottom);
        }

        public int getMRight() {
            return mRight;
        }

        public void setMRight(int right) {
            setBounds(mLeft, mTop, right, mBottom);
        }

        public int getMTop() {
            return mTop;
        }

        public void setMTop(int top) {
            Log.d("AnimatedValue" , "Animated Top"+ top);
            Log.d("AnimatedValue" , "Previous Top "+ mTop);
            Log.d("AnimatedValue" , "Width"+ mWidth);
            Log.d("AnimatedValue" , "Previous Bottom"+ mBottom);

            mBottom = top + mWidth;
            mTop = top;

            Log.d("AnimatedValue" , "Animated Bottom"+ mBottom);
            // random movements in left and right direction
            /*
            if(direction == 0){
                int randomLeft = randInt(0,2);
               mLeft += randomLeft;
                mRight -= randomLeft;
            }else if(direction == 1){
                int randomRight = randInt(0,2);
                mRight += randomRight;
                mLeft -= randomRight;
            }
            */
            setBounds(mLeft, mTop, mRight, mBottom);
        }

        public int getMBottom() {
            return mBottom;
        }

        public void setMBottom(int bottom) {
            setBounds(mLeft, mTop, mRight, bottom);
        }


        @Override
        public void setBounds(int left, int top, int right, int bottom) {
            mLeft = left;
            mRight = right;
            mTop = top;
            mBottom = bottom;
            mWidth = bottom - top;
            super.setBounds(left, top, right, bottom);

        }
    }

    /**
     * This method returns a random value in the range specified
     * @param min
     * @param max
     * @return int the random number
     */
    private static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

}
