package com.tripasfactory.thetripaslibrary.Utils;

import android.app.Activity;

import com.tripasfactory.thetripaslibrary.Controllers.InfiniteViewPager;


public class SlideShowTimer {
    private Thread handler;

    private InfiniteViewPager viewPager;

    private Activity main;

    private boolean isTerminated;

    private int seconds;

    private boolean paused;

    public SlideShowTimer(int seconds, InfiniteViewPager viewPager,
                          Activity main) {
        this.viewPager = viewPager;
        this.main = main;
        this.seconds = seconds;
        isTerminated = false;
    }

    public void beginTask() {
        if (handler != null) {
            handler.interrupt();
        }

        handler = new Thread(new SlideShowRunnable(),
                SlideShowTimer.class.getSimpleName());
        handler.start();
    }

    public void terminateTask() {
        if (handler != null) {
            handler.interrupt();
        }
        isTerminated = true;
        handler = null;
    }

    public void pauseSlider() {
        paused = true;

    }

    class SlideShowRunnable extends Thread {
        public void run() {

            while (!isTerminated) {
                try {
                    sleep(seconds * 1000);
                    if (!paused) {
                        updateUI();
                    }
                } catch (InterruptedException e) {
                    L.d(getName(), "Int");
                    break;
                }

                paused = false;

            }

        }

        public void updateUI() {

            main.runOnUiThread(new Runnable() {
                public void run() {
                    viewPager.nextItem();
                }
            });

        }
    }
}
