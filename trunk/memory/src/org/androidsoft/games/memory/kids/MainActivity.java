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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity
 * @author pierre
 */
public class MainActivity extends AbstractMainActivity
{
    private static final String PREF_LIST = "list";
    private static final String PREF_MOVE_COUNT = "move_count";
    private static final String PREF_SELECTED_COUNT = "seleted_count";
    private static final String PREF_FOUND_COUNT = "found_count";
    private static final String PREF_LAST_POSITION = "last_position";
    private static final String PREF_NOT_FOUND_RESID = "not_found_resid";
    private static final String PREF_BEST_MOVE_COUNT = "best_move_count";

    private static final int[] tiles = { R.drawable.item_1, R.drawable.item_2,
        R.drawable.item_3, R.drawable.item_4, R.drawable.item_5, R.drawable.item_6,
        R.drawable.item_7, R.drawable.item_8, R.drawable.item_9, R.drawable.item_10,
        R.drawable.item_11, R.drawable.item_12, R.drawable.item_13, R.drawable.item_14,
        R.drawable.item_15, R.drawable.item_16, R.drawable.item_17, R.drawable.item_18,
        R.drawable.item_19, R.drawable.item_20, R.drawable.item_21, R.drawable.item_22,
        R.drawable.item_23, R.drawable.item_24, R.drawable.item_25, R.drawable.item_26,
        R.drawable.item_27, R.drawable.item_28, R.drawable.item_29, R.drawable.item_30,
        R.drawable.item_31, R.drawable.item_32, R.drawable.item_33, R.drawable.item_34
    };


    private static final int[] not_found_tile_set = {  R.drawable.not_found_1 , R.drawable.not_found_2 };


    private static final int SET_SIZE = 10;

    private int mSelectedCount;
    private int mMoveCount;
    private int mFoundCount;
    private int mLastPosition;
    private int mNotFoundResId;
    private Tile mT1;
    private Tile mT2;
    private GridView mGridView;

    static TileList mList = new TileList();

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
        initData();
        if( mGridView == null )
        {
            initGrid();
        }
        drawGrid();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected void onResume()
    {
        super.onResume();

        SharedPreferences prefs = getPreferences(0);

        String serialized = prefs.getString(PREF_LIST, null);
        if (serialized != null)
        {
            mList = new TileList(serialized);
            mMoveCount = prefs.getInt(PREF_MOVE_COUNT, 0);
            ArrayList<Tile> list = mList.getSelected();
            mSelectedCount = list.size();
            mT1 = ( mSelectedCount > 0 ) ? list.get(0) : null;
            mT2 = ( mSelectedCount > 1 ) ? list.get(1) : null;
            mFoundCount = prefs.getInt( PREF_FOUND_COUNT, 0);
            mLastPosition = prefs.getInt(PREF_LAST_POSITION, -1);
            mNotFoundResId = prefs.getInt(PREF_NOT_FOUND_RESID, not_found_tile_set[0]);
            Tile.setNotFoundResId( mNotFoundResId );
            
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected void onPause()
    {
        super.onPause();

        SharedPreferences.Editor editor = getPreferences(0).edit();
        if( !mQuit )
        {
            // Paused without quit - save state
            editor.putString(PREF_LIST, mList.serialize());
            editor.putInt(PREF_MOVE_COUNT, mMoveCount);
            editor.putInt(PREF_SELECTED_COUNT, mSelectedCount);
            editor.putInt(PREF_FOUND_COUNT, mFoundCount);
            editor.putInt(PREF_LAST_POSITION, mLastPosition );
            editor.putInt(PREF_NOT_FOUND_RESID, mNotFoundResId);
        }
        else
        {
            editor.remove(PREF_LIST );
            editor.remove(PREF_MOVE_COUNT );
            editor.remove(PREF_SELECTED_COUNT );
            editor.remove(PREF_FOUND_COUNT );
            editor.remove(PREF_LAST_POSITION);
            editor.remove(PREF_NOT_FOUND_RESID);
        }
        editor.commit();
    }

    private int getBestMoveCount()
    {
        SharedPreferences prefs = getPreferences(0);

        return prefs.getInt(PREF_BEST_MOVE_COUNT, 100 );

    }

    private void setBestMoveCount( int nMoveCount )
    {
        SharedPreferences.Editor editor = getPreferences(0).edit();
        editor.putInt(PREF_BEST_MOVE_COUNT, nMoveCount );
        editor.commit();
    }
    /**
     * Initialize game data
     */
    private void initData()
    {
        mLastPosition = -1;
        mFoundCount = 0;
        mMoveCount = 0;
        mList.clear();
        mNotFoundResId = not_found_tile_set[ rand( not_found_tile_set.length)];
        Tile.setNotFoundResId( mNotFoundResId );
        for( Integer tile : getTileSet() )
        {
            addRandomly( tile );
        }
    }

    /**
     * Initialize the grid
     */
    private void initGrid()
    {
        mGridView = (GridView) findViewById(R.id.gridview);

        mGridView.setOnItemClickListener(new OnItemClickListener()
        {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                if (position == mLastPosition)
                {
                    // Same item clicked
                    return;
                }
                mLastPosition = position;
                mList.get(position).select();

                switch (mSelectedCount)
                {
                    case 0:
                        mT1 = mList.get(position);
                        break;

                    case 1:
                        mT2 = mList.get(position);
                        if (mT1.getResId() == mT2.getResId())
                        {
                            mT1.setFound(true);
                            mT2.setFound(true);
                            mFoundCount += 2;
                        }
                        break;

                    case 2:
                        if (mT1.getResId() != mT2.getResId())
                        {
                            mT1.unselect();
                            mT2.unselect();
                        }
                        mSelectedCount = 0;
                        mT1 = mList.get(position);
                        break;
                }
                mSelectedCount++;
                mMoveCount++;
                drawGrid();
                checkComplete();
            }
        });

    }

    /**
     * Draw or redraw the grid
     */
    private void drawGrid()
    {
        mGridView.setAdapter(new ImageAdapter(MainActivity.this));

    }

    /**
     * Check if all pieces has been found
     */
    private void checkComplete()
    {
        if (mFoundCount == mList.size())
        {
            int nHighScore = getBestMoveCount();
            String title = getString( R.string.success_title );
            String message = String.format( getString( R.string.success ) , mMoveCount , nHighScore );
            int icon = R.drawable.win;
            if( mMoveCount < nHighScore )
            {
                title = getString( R.string.hiscore_title );
                message = String.format( getString( R.string.hiscore ) , mMoveCount , nHighScore );
                icon = R.drawable.hiscore;

                setBestMoveCount( mMoveCount );
            }
            this.showEndDialog( title , message , icon );
        }
    }

    /**
     * Add a pair of pieces randomly in the list
     * @param nResId The resid of the piece
     */
    private void addRandomly(int nResId)
    {
        double dPos = Math.random() * mList.size();
        int nPos = (int) dPos;
        mList.add(nPos, new Tile(nResId));
        dPos = Math.random() * mList.size();
        nPos = (int) dPos;
        mList.add(nPos, new Tile(nResId));

    }

    private int rand( int nSize )
    {
        double dPos = Math.random() * nSize;
        return (int) dPos;
    }


    private List<Integer> getTileSet()
    {
        List<Integer> list = new ArrayList<Integer>();

        while( list.size() < SET_SIZE )
        {
            int n = rand( tiles.length );
            int t = tiles[n];
            if( ! list.contains(t))
            {
                list.add(t);
            }
        }
        return list;
    }
}
