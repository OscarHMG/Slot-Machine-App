package com.rayzem.slot_machine_app;

public class Wheel extends Thread {

    public int currentIndex;
    private WheelListener wheelListener;
    private long frameDuration;
    private long startIn;
    private boolean isStarted;

    interface WheelListener {
        void newItem(int img);
    }


    private static int[] imgs = {R.drawable.bar, R.drawable.banana, R.drawable.cherry, R.drawable.seven,
            R.drawable.watermelon, R.drawable.lemon};




    public Wheel(WheelListener wheelListener, long frameDuration, long startIn) {
        this.wheelListener = wheelListener;
        this.frameDuration = frameDuration;
        this.startIn = startIn;
        currentIndex = 0;
        isStarted = true;
    }


    public void netItem() {
        currentIndex++;

        if (currentIndex == imgs.length) {
            currentIndex = 0;
        }
    }


    @Override
    public void run() {
        try {
            Thread.sleep(startIn);
        } catch (InterruptedException e) {
        }

        while(isStarted) {
            try {
                Thread.sleep(frameDuration);
            } catch (InterruptedException e) {
            }

            netItem();

            if (wheelListener != null) {
                wheelListener.newItem(imgs[currentIndex]);
            }
        }
    }

    public void stopWheel() {
        isStarted = false;
    }


}
