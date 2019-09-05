package com.rayzem.slot_machine_app.CustomView;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.rayzem.slot_machine_app.R;

public class ImageViewScrolling extends FrameLayout {
    private static int ANIMATION_DURATION = 200;
    private ImageView current_item, next_item;
    private InterfaceEventEndListener eventEndListener;

    private int lastResult = 0, oldValue = 0;


    private static int BAR = 0, SEVEN = 1, CHERRY = 2, LEMON = 3, BANANA = 4, WATERMELON = 5;


    public ImageViewScrolling(Context context) {
        super(context);
        initView(context);
    }

    public ImageViewScrolling( Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public InterfaceEventEndListener getEventEndListener() {
        return eventEndListener;
    }

    public void setEventEndListener(InterfaceEventEndListener eventEndListener) {
        this.eventEndListener = eventEndListener;
    }


    private void initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.image_view_scrolling, this);

        //Init UI elements
        current_item = getRootView().findViewById(R.id.current_item);
        next_item = getRootView().findViewById(R.id.next_item);

        next_item.setTranslationY(getHeight());

    }

    public void setValueRandom(final int image, final int countRotate){
        current_item.animate().translationY(-getHeight()).setDuration(ANIMATION_DURATION).start();
        next_item.setTranslationY(next_item.getHeight());

        next_item.animate().translationY(0)
                .setDuration(ANIMATION_DURATION)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        setItem(current_item, oldValue % 6);
                        current_item.setTranslationY(0);

                        if(oldValue != countRotate){
                            setValueRandom(image, countRotate );
                            oldValue++;
                        }else{
                            lastResult = 0;
                            oldValue = 0;
                            setItem(next_item, image);

                            eventEndListener.endEventListener(image % 6, countRotate );
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
    }



/*
    public void setValueRandomValue(final int image, boolean stop){
        current_item.animate().translationY(-getHeight()).setDuration(ANIMATION_DURATION).start();
        next_item.setTranslationY(next_item.getHeight());
        next_item.animate().translationY(0)
                .setDuration(ANIMATION_DURATION)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        setItem(current_item, oldValue % 6);
                        current_item.setTranslationY(0);

                        if(oldValue != countRotate){
                            setValueRandom(image, countRotate );
                            oldValue++;
                        }else{
                            lastResult = 0;
                            oldValue = 0;
                            setItem(next_item, image);

                            eventEndListener.endEventListener(image % 6, countRotate );
                        }

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
    }
*/

    private void setItem(ImageView imageView, int value) {


        if(value == BAR)
            imageView.setImageResource(R.drawable.bar);
        else if (value == SEVEN)
            imageView.setImageResource(R.drawable.seven);
        else if (value == LEMON)
            imageView.setImageResource(R.drawable.lemon);
        else if (value == CHERRY)
            imageView.setImageResource(R.drawable.cherry);
        else if (value == BANANA)
            imageView.setImageResource(R.drawable.banana);
        else
            imageView.setImageResource(R.drawable.watermelon);

        imageView.setTag(value);
        lastResult = value;
    }

    public int getValue(){
        return Integer.parseInt(next_item.getTag().toString());
    }
}
