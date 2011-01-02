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

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 *
 * @author Pierre Levy
 */
public abstract class AbstractTextItem implements CreditsItem
{
    protected String mText;
    protected int mX;
    protected int mY;

    abstract Paint getPaint();

    /**
     * {@inheritDoc }
     */
    public void prepare( long elapsed , int width , int height , int dy )
    {
        mY += dy;
        mX = width / 2;
    }

    /**
     * {@inheritDoc }
     */
    public void draw(Canvas canvas)
    {
        canvas.drawText( mText , mX, mY, getPaint() );
    }

    /**
     * {@inheritDoc }
     */
    public void setOffset( int offset )
    {
        mY = offset;
    }

    /**
     * {@inheritDoc }
     */
    public int getOffset()
    {
        return mY;
    }
}
