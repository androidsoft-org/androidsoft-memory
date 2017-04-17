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
import org.androidsoft.games.memory.kids.IconSet;
import org.androidsoft.games.memory.kids.IconSetList;
import org.androidsoft.games.memory.kids.R;

/**
 * MainActivity
 * @author pierre
 */
public class MainActivity extends AbstractMainActivity implements Memory.OnMemoryListener
{
    private static final int[] sounds = {
      R.raw.blop, R.raw.chime, R.raw.chtoing, R.raw.tic, R.raw.toc, 
      R.raw.toing, R.raw.toing2, R.raw.toing3, R.raw.toing4, R.raw.toing5,
      R.raw.toing6, R.raw.toong, R.raw.tzirlup, R.raw.whiipz
    };


    private Memory mMemory;
    private MemoryGridView mGridView;

    /**
     * {@inheritDoc }
     */
    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);

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
	IconSet is = PreferencesService.getIconsSet();
        mMemory = new Memory((int[])(is.getImages()), sounds , (int)(is.getVerso()), this);
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
