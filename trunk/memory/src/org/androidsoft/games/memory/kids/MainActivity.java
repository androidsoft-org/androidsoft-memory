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

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import java.util.ArrayList;

/**
 * MainActivity
 * @author pierre
 */
public class MainActivity extends AbstractMainActivity
{
    private final static int[] tiles = { R.drawable.item_1, R.drawable.item_2,
        R.drawable.item_3, R.drawable.item_4, R.drawable.item_5, R.drawable.item_6,
        R.drawable.item_7, R.drawable.item_8, R.drawable.item_9, R.drawable.item_10 };

    private int mSelectedCount;
    private int mMoveCount;
    private int mFoundCount;
    private int mLastPosition;
    private Tile mP1;
    private Tile mP2;
    private GridView mGridView;

    static ArrayList<Tile> mList = new ArrayList<Tile>();

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
     * Initialize game data
     */
    private void initData()
    {
        mLastPosition = -1;
        mFoundCount = 0;
        mMoveCount = 0;
        mList.clear();
        Tile.setNotFoundResId(R.drawable.not_found);
        for( int i = 0 ; i < tiles.length ; i++ )
        {
            addRandomly( tiles[i] );
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
                        mP1 = mList.get(position);
                        break;

                    case 1:
                        mP2 = mList.get(position);
                        if (mP1.getResId() == mP2.getResId())
                        {
                            mP1.setFound(true);
                            mP2.setFound(true);
                            mFoundCount += 2;
                        }
                        break;

                    case 2:
                        if (mP1.getResId() != mP2.getResId())
                        {
                            mP1.unselect();
                            mP2.unselect();
                        }
                        mSelectedCount = 0;
                        mP1 = mList.get(position);
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
     * Check if all pieces has been 
     */
    private void checkComplete()
    {
        if (mFoundCount == mList.size())
        {
            this.showEndDialog();
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
}
