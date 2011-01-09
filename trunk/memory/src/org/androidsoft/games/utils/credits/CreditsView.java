/* Copyright (c) 2010 Pierre LEVY androidsoft.org
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.androidsoft.games.utils.credits;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Pierre Levy
 */
public class CreditsView extends SurfaceView implements SurfaceHolder.Callback
{

    private SurfaceHolder mHolder;
    private Bitmap mBackground;
    private Bitmap mBackgroundLandscape;
    private int mWidth, mHeight;
    private int period;
    private Rect mbgIn;
    private Paint mBgPaint = new Paint();
    private CreditsThread mThread;
    private Handler mHandler = new Handler();
    private List<CreditsItem> credits = new ArrayList<CreditsItem>();
    private float mPreviousX;
    private float mPreviousY;
    private int mDY;
    private boolean mTouch;

    public CreditsView(Context context , CreditsParams params )
    {
        super(context);
        mHolder = getHolder();
        mHolder.addCallback(this);

        init( context , params );

    }

    private void init( Context context , CreditsParams p )
    {
        Paint paintPerson = new Paint();
        paintPerson.setAntiAlias(true);
        paintPerson.setStrokeWidth(5);
        paintPerson.setStrokeCap(Paint.Cap.ROUND);
        paintPerson.setTextSize( p.getTextSizeDefault());
        paintPerson.setTypeface( p.getTypefaceDefault());
        paintPerson.setColor( p.getColorDefault() );
        paintPerson.setTextAlign(Paint.Align.CENTER);

        Person.setPaint(paintPerson);
        Person.setSpacings( p.getSpacingBeforeDefault() , p.getSpacingAfterDefault() );

        Paint paintCategory = new Paint();
        paintCategory.setAntiAlias(true);
        paintCategory.setStrokeWidth(5);
        paintCategory.setStrokeCap(Paint.Cap.ROUND);
        paintCategory.setTextSize( p.getTextSizeCategory());
        paintCategory.setTypeface( p.getTypefaceCategory() );
        paintCategory.setColor( p.getColorCategory() );
        paintCategory.setTextAlign(Paint.Align.CENTER);

        Category.setPaint(paintCategory);
        Category.setSpacings( p.getSpacingBeforeCategory() , p.getSpacingAfterCategory() );

        credits.add(new Person( context.getString( p.getAppNameRes())));
        credits.add(new Category(context.getString( p.getAppVersionRes())));
        loadFromResources(credits, p.getArrayCreditsRes());

        mBackground = BitmapFactory.decodeResource(getResources(), p.getBitmapBackgroundRes());
        mBackgroundLandscape = BitmapFactory.decodeResource(getResources(), p.getBitmapBackgroundLandscapeRes());

    }

    private void calculatedItemSpacing(List<CreditsItem> list, int height)
    {
        int offset = height + 30;
        for (CreditsItem item : list)
        {
            offset += item.getBeforeSpacing();
            item.setOffset(offset);
            offset += item.getAfterSpacing();
        }
    }
    private final Runnable mDrawFrames = new Runnable()
    {

        public void run()
        {
            drawFrame();
        }
    };

    public void surfaceCreated(SurfaceHolder holder)
    {
        mThread = new CreditsThread();
        mThread.start();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        mHandler.removeCallbacks(mDrawFrames);
        mWidth = width;
        mHeight = height;
        mbgIn = null;
        calculatedItemSpacing(credits, mHeight);

        mThread.alive = false;
        while (mThread.isAlive())
        {
            try
            {
                Thread.sleep(5);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        mThread = new CreditsThread();
        mThread.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder)
    {
        mHandler.removeCallbacks(mDrawFrames);
        if (mThread != null)
        {
            mThread.alive = false;
        }
    }

    private void loadFromResources(List<CreditsItem> list, int resArray)
    {
        Resources res = getResources();
        TypedArray entries = res.obtainTypedArray(resArray);
        for (int i = 0; i < entries.length(); i++)
        {
            CreditsItem item = null;
            String entry = entries.getString(i);
            if (entry.startsWith("*"))
            {
                item = new Category(entry.substring(1));
            } else
            {
                item = new Person(entry);
            }
            list.add(item);
        }
    }

    private class CreditsThread extends Thread
    {

        public boolean cont = true;
        public boolean alive = true;
        private long lastTime;
        private long current;
        private long ellapsed;

        public CreditsThread()
        {
            setName("Credits");
            lastTime = System.currentTimeMillis();
        }

        @Override
        public void run()
        {
            while (alive)
            {
                current = System.currentTimeMillis();
                ellapsed = current - lastTime;
                lastTime = current;
                prepareFrame(ellapsed);
                cont = true;
                mHandler.postDelayed(mDrawFrames, period - ellapsed);
                while (cont && alive)
                {
                    try
                    {
                        Thread.sleep(5);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void onSurfaceChanged()
    {
    }

    private synchronized void prepareFrame(long ellapsed)
    {
        if (credits.size() > 0)
        {
            CreditsItem last = credits.get(credits.size() - 1);
            if (last.getOffset() < 0)
            {
                calculatedItemSpacing(credits, mHeight);
            }
        }

        if( mTouch )
        {
            mDY = 0;
        }
        else
        {
            mDY = (mDY / 3) - 1;
        }

        for (CreditsItem item : credits)
        {
            item.prepare(ellapsed, mWidth, mHeight, mDY);
        }
    }

    private synchronized void drawFrame()
    {
        if (mThread != null)
        {
            mThread.cont = false;
        }

        Canvas canvas = null;
        try
        {
            canvas = mHolder.lockCanvas(null);
            if (canvas != null)
            {
                drawBackground(canvas);

                for (CreditsItem item : credits)
                {
                    item.draw(canvas);
                }
            }

        } finally
        {
            if (canvas != null)
            {
                mHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    void drawBackground(Canvas c)
    {
        if (mbgIn == null)
        {
            mbgIn = new Rect(0, 0, mWidth, mHeight);
        }
        if( mWidth < mHeight )
        {
            c.drawBitmap(mBackground, mbgIn, mbgIn, mBgPaint);
        }
        else
        {
            c.drawBitmap(mBackgroundLandscape, mbgIn, mbgIn, mBgPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        float x = e.getX();
        float y = e.getY();
        switch (e.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mPreviousX = x;
                mPreviousY = y;
                mTouch = true;
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;
                mDY = (int) dy;
                mPreviousX = x;
                mPreviousY = y;

            case MotionEvent.ACTION_UP:
                mTouch = false;
        }
        return true;
    }
}
