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

/**
 *
 * @author pierre
 */
public class Tile
{

    boolean mFound;
    boolean mSelected;
    int mResId;
    private static int mNotFoundResId;

    static void setNotFoundResId(int nNotFoundResId)
    {
        mNotFoundResId = nNotFoundResId;
    }

    public Tile(int nResId)
    {
        mResId = nResId;
    }

    public boolean isFound()
    {
        return mFound;
    }

    public void setFound(boolean bFound)
    {
        mFound = bFound;
    }

    public int getResId()
    {
        return ( mFound || mSelected ) ? mResId : mNotFoundResId;
    }

    public void select()
    {
        mSelected = true;
    }

    public void unselect()
    {
        mSelected = false;
    }
}
