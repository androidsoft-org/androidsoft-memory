/* Copyright (c) 2010-2011 Pierre LEVY androidsoft.org
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
package org.androidsoft.games.memory.kids.model;

import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;
import org.androidsoft.games.memory.kids.PreferencesService;
import org.androidsoft.games.memory.kids.R;
import org.androidsoft.utils.sound.SoundManager;

/**
 *
 * @author pierre
 */
public class Memory
{

    private static final int SOUND_FAILED = 2000;
    private static final int SOUND_SUCCEED = 2001;
    private static final String PREF_LIST = "list";
    private static final String PREF_MOVE_COUNT = "move_count";
    private static final String PREF_SELECTED_COUNT = "seleted_count";
    private static final String PREF_FOUND_COUNT = "found_count";
    private static final String PREF_LAST_POSITION = "last_position";
    private static final String PREF_TILE_VERSO = "tile_verso";
    private static final int MAX_TILES_PER_ROW = 6;
    private static final int MIN_TILES_PER_ROW = 4;
    private static final int SET_SIZE = (MAX_TILES_PER_ROW * MIN_TILES_PER_ROW) / 2;
    private int mSelectedCount;
    private int mMoveCount;
    private int mFoundCount;
    private int mLastPosition = -1;
    private Tile mT1;
    private Tile mT2;
    private TileList mList = new TileList();
    private int[] mTiles;
    private OnMemoryListener mListener;
    private static int[] mSounds;
    private int mTileVerso;

    public Memory(int[] tiles, int[] sounds, int tile_verso, OnMemoryListener listener)
    {
        mTiles = tiles;
        mSounds = sounds;
        mListener = listener;
        mTileVerso = tile_verso;
        Tile.setNotFoundResId(mTileVerso);


    }

    public void onResume(SharedPreferences prefs)
    {
        String serialized = prefs.getString(PREF_LIST, null);
        if (serialized != null)
        {
            mList = new TileList(serialized);
            mMoveCount = prefs.getInt(PREF_MOVE_COUNT, 0);
            ArrayList<Tile> list = mList.getSelected();
            mSelectedCount = list.size();
            mT1 = (mSelectedCount > 0) ? list.get(0) : null;
            mT2 = (mSelectedCount > 1) ? list.get(1) : null;
            mFoundCount = prefs.getInt(PREF_FOUND_COUNT, 0);
            mLastPosition = prefs.getInt(PREF_LAST_POSITION, -1);
            mTileVerso = prefs.getInt(PREF_TILE_VERSO, R.drawable.not_found_default);
            Tile.setNotFoundResId(mTileVerso);
        }

        initSounds();
    }

    public void onPause(SharedPreferences preferences, boolean quit)
    {
        SharedPreferences.Editor editor = preferences.edit();
        if (!quit)
        {
            // Paused without quit - save state
            editor.putString(PREF_LIST, mList.serialize());
            editor.putInt(PREF_MOVE_COUNT, mMoveCount);
            editor.putInt(PREF_SELECTED_COUNT, mSelectedCount);
            editor.putInt(PREF_FOUND_COUNT, mFoundCount);
            editor.putInt(PREF_LAST_POSITION, mLastPosition);
            editor.putInt(PREF_TILE_VERSO, mTileVerso);
        }
        else
        {
            editor.remove(PREF_LIST);
            editor.remove(PREF_MOVE_COUNT);
            editor.remove(PREF_SELECTED_COUNT);
            editor.remove(PREF_FOUND_COUNT);
            editor.remove(PREF_LAST_POSITION);
            editor.remove(PREF_TILE_VERSO);
        }
        editor.commit();
    }

    public int getCount()
    {
        return mList.size();
    }

    public int getMaxTilesPerRow()
    {
        return MAX_TILES_PER_ROW;
    }

    public int getMinTilesPerRow()
    {
        return MIN_TILES_PER_ROW;
    }

    public int getResId(int position)
    {
        return mList.get(position).getResId();
    }

    public void reset()
    {
        mFoundCount = 0;
        mMoveCount = 0;
        mList.clear();
        for (Integer tile : getTileSet())
        {
            addRandomly(tile);
        }
    }

    private void initSounds()
    {
        SoundManager.instance().addSound(SOUND_FAILED, R.raw.failed);
        SoundManager.instance().addSound(SOUND_SUCCEED, R.raw.succeed);
        for (int i = 0; i < mSounds.length; i++)
        {
            SoundManager.instance().addSound(i, mSounds[i]);
        }
    }

    public interface OnMemoryListener
    {

        void onComplete(int moveCount);

        void onUpdateView();
    }

    public void onPosition(int position)
    {
        if (position == mLastPosition)
        {
            // Same item clicked
            return;
        }
        mLastPosition = position;
        Tile tile = mList.get(position);
        tile.select();
        int sound = tile.mResId % mSounds.length;
        playSound(sound);

        switch (mSelectedCount)
        {
            case 0:
                mT1 = tile;
                break;

            case 1:
                mT2 = tile;
                if (mT1.getResId() == mT2.getResId())
                {
                    mT1.setFound(true);
                    mT2.setFound(true);
                    mFoundCount += 2;
                    playSound(SOUND_SUCCEED);
                }
                else
                {
//                    playSound( SOUND_FAILED );
                }
                break;

            case 2:
                if (mT1.getResId() != mT2.getResId())
                {
                    mT1.unselect();
                    mT2.unselect();
                }
                mSelectedCount = 0;
                mT1 = tile;
                break;
        }
        mSelectedCount++;
        mMoveCount++;
        updateView();
        checkComplete();
    }

    private void updateView()
    {
        mListener.onUpdateView();
    }

    private void checkComplete()
    {
        if (mFoundCount == mList.size())
        {
            mListener.onComplete(mMoveCount);
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

    private int rand(int nSize)
    {
        double dPos = Math.random() * nSize;
        return (int) dPos;
    }

    private List<Integer> getTileSet()
    {
        List<Integer> list = new ArrayList<Integer>();

        while (list.size() < SET_SIZE)
        {
            int n = rand(mTiles.length);
            int t = mTiles[n];
            if (!list.contains(t))
            {
                list.add(t);
            }
        }
        return list;
    }

    private void playSound(int index)
    {
        if (PreferencesService.instance().isSoundEnabled())
        {
            SoundManager.instance().playSound(index);
        }
    }
}
