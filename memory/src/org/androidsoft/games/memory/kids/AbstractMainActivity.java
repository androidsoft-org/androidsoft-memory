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
package org.androidsoft.games.memory.kids;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

/**
 * AbstractMainActivity
 * @author pierre
 */
public abstract class AbstractMainActivity extends Activity implements OnClickListener
{
    private static final int SPLASH_SCREEN_ROTATION_COUNT = 2;
    private static final int SPLASH_SCREEN_ROTATION_DURATION = 2000;
    private static final int GAME_SCREEN_ROTATION_COUNT = 2;
    private static final int GAME_SCREEN_ROTATION_DURATION = 2000;

    private ViewGroup mContainer;
    private View mSplash;
    private Button mButtonPlay;

    protected abstract View getGameView();
    protected abstract void newGame();

    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        mContainer = (ViewGroup) findViewById(R.id.container);
        mSplash = (View) findViewById(R.id.splash);

        mButtonPlay = (Button) findViewById(R.id.button_play);
        mButtonPlay.setOnClickListener(this);

        ImageView image = (ImageView) findViewById(R.id.image_splash);
        image.setImageResource(R.drawable.splash);
    }

    /**
     * Implement the OnClickListener callback
     */
    public void onClick(View v)
    {
        if (v == mButtonPlay)
        {
            applyRotation(0, 0, SPLASH_SCREEN_ROTATION_COUNT * 360 );
        }
    }

    protected void showEndDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.success)).setCancelable(false).setPositiveButton(getString(R.string.new_game), new DialogInterface.OnClickListener()
        {

            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();
                newGame();
            }
        }).setNegativeButton(getString(R.string.quit), new DialogInterface.OnClickListener()
        {

            public void onClick(DialogInterface dialog, int id)
            {
                AbstractMainActivity.this.finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    // 3D anim from Splash Screen to Game screen
    /**
     * Setup a new 3D rotation on the container view.
     *
     * @param position the item that was clicked to show a picture, or -1 to show the list
     * @param start the start angle at which the rotation must begin
     * @param end the end angle of the rotation
     */
    private void applyRotation(int position, float start, float end)
    {
        // Find the center of the container
        final float centerX = mContainer.getWidth() / 2.0f;
        final float centerY = mContainer.getHeight() / 2.0f;

        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        final Rotate3dAnimation rotation =
                new Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true);
        rotation.setDuration( SPLASH_SCREEN_ROTATION_DURATION );
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView(position));

        mContainer.startAnimation(rotation);
    }

    /**
     * This class listens for the end of the first half of the animation.
     * It then posts a new action that effectively swaps the views when the container
     * is rotated 90 degrees and thus invisible.
     */
    private final class DisplayNextView implements Animation.AnimationListener
    {

        private final int mPosition;

        private DisplayNextView(int position)
        {
            mPosition = position;
        }

        public void onAnimationStart(Animation animation)
        {
        }

        public void onAnimationEnd(Animation animation)
        {
            mContainer.post(new SwapViews(mPosition));
        }

        public void onAnimationRepeat(Animation animation)
        {
        }
    }

    /**
     * This class is responsible for swapping the views and start the second
     * half of the animation.
     */
    private final class SwapViews implements Runnable
    {

        private final int mPosition;

        public SwapViews(int position)
        {
            mPosition = position;
        }

        public void run()
        {
            final float centerX = mContainer.getWidth() / 2.0f;
            final float centerY = mContainer.getHeight() / 2.0f;
            Rotate3dAnimation rotation;

            mSplash.setVisibility(View.GONE);
            getGameView().setVisibility(View.VISIBLE);
            getGameView().requestFocus();

            rotation = new Rotate3dAnimation(0, 360 * GAME_SCREEN_ROTATION_COUNT, centerX, centerY, 310.0f, false);

            rotation.setDuration( GAME_SCREEN_ROTATION_DURATION );
            rotation.setFillAfter(true);
            rotation.setInterpolator(new DecelerateInterpolator());

            mContainer.startAnimation(rotation);
        }
    }
}
