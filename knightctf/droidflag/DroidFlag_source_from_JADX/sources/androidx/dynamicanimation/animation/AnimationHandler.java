package androidx.dynamicanimation.animation;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.Choreographer;
import androidx.collection.SimpleArrayMap;
import java.util.ArrayList;

class AnimationHandler {
    private static final long FRAME_DELAY_MS = 10;
    public static final ThreadLocal<AnimationHandler> sAnimatorHandler = new ThreadLocal<>();
    final ArrayList<AnimationFrameCallback> mAnimationCallbacks = new ArrayList<>();
    private final AnimationCallbackDispatcher mCallbackDispatcher = new AnimationCallbackDispatcher();
    long mCurrentFrameTime = 0;
    private final SimpleArrayMap<AnimationFrameCallback, Long> mDelayedCallbackStartTime = new SimpleArrayMap<>();
    private boolean mListDirty = false;
    private AnimationFrameCallbackProvider mProvider;

    interface AnimationFrameCallback {
        boolean doAnimationFrame(long j);
    }

    AnimationHandler() {
    }

    class AnimationCallbackDispatcher {
        AnimationCallbackDispatcher() {
        }

        /* access modifiers changed from: package-private */
        public void dispatchAnimationFrame() {
            AnimationHandler.this.mCurrentFrameTime = SystemClock.uptimeMillis();
            AnimationHandler animationHandler = AnimationHandler.this;
            animationHandler.doAnimationFrame(animationHandler.mCurrentFrameTime);
            if (AnimationHandler.this.mAnimationCallbacks.size() > 0) {
                AnimationHandler.this.getProvider().postFrameCallback();
            }
        }
    }

    public static AnimationHandler getInstance() {
        ThreadLocal<AnimationHandler> threadLocal = sAnimatorHandler;
        if (threadLocal.get() == null) {
            threadLocal.set(new AnimationHandler());
        }
        return threadLocal.get();
    }

    public static long getFrameTime() {
        ThreadLocal<AnimationHandler> threadLocal = sAnimatorHandler;
        if (threadLocal.get() == null) {
            return 0;
        }
        return threadLocal.get().mCurrentFrameTime;
    }

    public void setProvider(AnimationFrameCallbackProvider animationFrameCallbackProvider) {
        this.mProvider = animationFrameCallbackProvider;
    }

    /* access modifiers changed from: package-private */
    public AnimationFrameCallbackProvider getProvider() {
        if (this.mProvider == null) {
            if (Build.VERSION.SDK_INT >= 16) {
                this.mProvider = new FrameCallbackProvider16(this.mCallbackDispatcher);
            } else {
                this.mProvider = new FrameCallbackProvider14(this.mCallbackDispatcher);
            }
        }
        return this.mProvider;
    }

    public void addAnimationFrameCallback(AnimationFrameCallback animationFrameCallback, long j) {
        if (this.mAnimationCallbacks.size() == 0) {
            getProvider().postFrameCallback();
        }
        if (!this.mAnimationCallbacks.contains(animationFrameCallback)) {
            this.mAnimationCallbacks.add(animationFrameCallback);
        }
        if (j > 0) {
            this.mDelayedCallbackStartTime.put(animationFrameCallback, Long.valueOf(SystemClock.uptimeMillis() + j));
        }
    }

    public void removeCallback(AnimationFrameCallback animationFrameCallback) {
        this.mDelayedCallbackStartTime.remove(animationFrameCallback);
        int indexOf = this.mAnimationCallbacks.indexOf(animationFrameCallback);
        if (indexOf >= 0) {
            this.mAnimationCallbacks.set(indexOf, (Object) null);
            this.mListDirty = true;
        }
    }

    /* access modifiers changed from: package-private */
    public void doAnimationFrame(long j) {
        long uptimeMillis = SystemClock.uptimeMillis();
        for (int i = 0; i < this.mAnimationCallbacks.size(); i++) {
            AnimationFrameCallback animationFrameCallback = this.mAnimationCallbacks.get(i);
            if (animationFrameCallback != null && isCallbackDue(animationFrameCallback, uptimeMillis)) {
                animationFrameCallback.doAnimationFrame(j);
            }
        }
        cleanUpList();
    }

    private boolean isCallbackDue(AnimationFrameCallback animationFrameCallback, long j) {
        Long l = this.mDelayedCallbackStartTime.get(animationFrameCallback);
        if (l == null) {
            return true;
        }
        if (l.longValue() >= j) {
            return false;
        }
        this.mDelayedCallbackStartTime.remove(animationFrameCallback);
        return true;
    }

    private void cleanUpList() {
        if (this.mListDirty) {
            for (int size = this.mAnimationCallbacks.size() - 1; size >= 0; size--) {
                if (this.mAnimationCallbacks.get(size) == null) {
                    this.mAnimationCallbacks.remove(size);
                }
            }
            this.mListDirty = false;
        }
    }

    private static class FrameCallbackProvider16 extends AnimationFrameCallbackProvider {
        private final Choreographer mChoreographer = Choreographer.getInstance();
        private final Choreographer.FrameCallback mChoreographerCallback = new Choreographer.FrameCallback() {
            public void doFrame(long j) {
                FrameCallbackProvider16.this.mDispatcher.dispatchAnimationFrame();
            }
        };

        FrameCallbackProvider16(AnimationCallbackDispatcher animationCallbackDispatcher) {
            super(animationCallbackDispatcher);
        }

        /* access modifiers changed from: package-private */
        public void postFrameCallback() {
            this.mChoreographer.postFrameCallback(this.mChoreographerCallback);
        }
    }

    private static class FrameCallbackProvider14 extends AnimationFrameCallbackProvider {
        private final Handler mHandler = new Handler(Looper.myLooper());
        long mLastFrameTime = -1;
        private final Runnable mRunnable = new Runnable() {
            public void run() {
                FrameCallbackProvider14.this.mLastFrameTime = SystemClock.uptimeMillis();
                FrameCallbackProvider14.this.mDispatcher.dispatchAnimationFrame();
            }
        };

        FrameCallbackProvider14(AnimationCallbackDispatcher animationCallbackDispatcher) {
            super(animationCallbackDispatcher);
        }

        /* access modifiers changed from: package-private */
        public void postFrameCallback() {
            this.mHandler.postDelayed(this.mRunnable, Math.max(AnimationHandler.FRAME_DELAY_MS - (SystemClock.uptimeMillis() - this.mLastFrameTime), 0));
        }
    }

    static abstract class AnimationFrameCallbackProvider {
        final AnimationCallbackDispatcher mDispatcher;

        /* access modifiers changed from: package-private */
        public abstract void postFrameCallback();

        AnimationFrameCallbackProvider(AnimationCallbackDispatcher animationCallbackDispatcher) {
            this.mDispatcher = animationCallbackDispatcher;
        }
    }
}
