/* Copyright (c) 2010-2014 Pierre LEVY androidsoft.org
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
package org.androidsoft.games.memory.kids.ui;

import org.androidsoft.games.memory.kids.model.Memory;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import java.text.MessageFormat;
import org.androidsoft.games.memory.kids.PreferencesService;
import org.androidsoft.games.memory.kids.R;

/**
 * MainActivity
 * @author pierre
 */
public class MainActivity extends AbstractMainActivity implements Memory.OnMemoryListener
{

    private static final int[] tiles_default =
    {
        R.drawable.item_1, R.drawable.item_2,
        R.drawable.item_3, R.drawable.item_4, R.drawable.item_5, R.drawable.item_6,
        R.drawable.item_7, R.drawable.item_8, R.drawable.item_9, R.drawable.item_10,
        R.drawable.item_11, R.drawable.item_12, R.drawable.item_13, R.drawable.item_14,
        R.drawable.item_15, R.drawable.item_16, R.drawable.item_17, R.drawable.item_18,
        R.drawable.item_19, R.drawable.item_20, R.drawable.item_21, R.drawable.item_22,
        R.drawable.item_23, R.drawable.item_24, R.drawable.item_25, R.drawable.item_26,
        R.drawable.item_27, R.drawable.item_28, R.drawable.item_29, R.drawable.item_30,
        R.drawable.item_31, R.drawable.item_32, R.drawable.item_33, R.drawable.item_34
    };
    
    private static final int[] tiles_season =
    {
        R.drawable.season_1, R.drawable.season_2,
        R.drawable.season_3, R.drawable.season_4, R.drawable.season_5, R.drawable.season_6,
        R.drawable.season_7, R.drawable.season_8, R.drawable.season_9, R.drawable.season_10,
        R.drawable.season_11, R.drawable.season_12, R.drawable.season_13, R.drawable.season_14 /*,
        R.drawable.season_15, R.drawable.season_16, R.drawable.season_17, R.drawable.season_18,
        R.drawable.season_19, R.drawable.season_20, R.drawable.season_21, R.drawable.season_22 */
    };
    
    private static final int[][] icons_set = { tiles_default , tiles_season };
    
    private static final int[] sounds = {
      R.raw.blop, R.raw.chime, R.raw.chtoing, R.raw.tic, R.raw.toc, 
      R.raw.toing, R.raw.toing2, R.raw.toing3, R.raw.toing4, R.raw.toing5,
      R.raw.toing6, R.raw.toong, R.raw.tzirlup, R.raw.whiipz
    };


    private static final int[] not_found_tile_set =
    {
        R.drawable.not_found_default, R.drawable.not_found_season
    };
    private Memory mMemory;
//    private int mNotFoundResId;
    private MemoryGridView mGridView;

    /**
     * {@inheritDoc }
     */
    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);

        PreferencesService.init( this );
        newGame();

    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected View getGameView()
    {
        return mGridView;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected void newGame()
    {
        int set = PreferencesService.instance().getIconsSet(); 
        mMemory = new Memory( icons_set[ set ], sounds , not_found_tile_set[ set ], this);
        mMemory.reset();
        mGridView = (MemoryGridView) findViewById(R.id.gridview);
        mGridView.setMemory(mMemory);
        drawGrid();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected void about()
    {
        Intent intent = new Intent( this , CreditsActivity.class );
        startActivity(intent);
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    protected void preferences()
    {
        Intent intent = new Intent( this , PreferencesActivity.class );
        startActivity(intent);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected void onResume()
    {
        super.onResume();

        mMemory.onResume( PreferencesService.instance().getPrefs() );

        drawGrid();
        
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected void onPause()
    {
        super.onPause();

        mMemory.onPause( PreferencesService.instance().getPrefs() , mQuit);

    }


    /**
     * {@inheritDoc }
     */
    public void onComplete(int countMove)
    {
        int nHighScore = PreferencesService.instance().getHiScore();
        String title = getString(R.string.success_title);
        Object[] args = { countMove, nHighScore };
        String message = MessageFormat.format(getString(R.string.success), args );
        int icon = R.drawable.win;
        if (countMove < nHighScore)
        {
            title = getString(R.string.hiscore_title);
            message = MessageFormat.format(getString(R.string.hiscore), args );
            icon = R.drawable.hiscore;

            PreferencesService.instance().saveHiScore(countMove);
        }
        this.showEndDialog(title, message, icon);
    }

    /**
     * {@inheritDoc }
     */
    public void onUpdateView()
    {
        drawGrid();
    }

    /**
     * Draw or redraw the grid
     */
    private void drawGrid()
    {
        mGridView.update();
    }

}
