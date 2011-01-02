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

import android.graphics.Paint;

/**
 *
 * @author Pierre Levy
 */
public class Person extends AbstractTextItem implements CreditsItem
{
    private static Paint mPaint;
    private static int mBeforeSpacing;
    private static int mAfterSpacing;

    public Person( String name )
    {
        mText = name;
    }

    public static void setPaint( Paint paint )
    {
        mPaint = paint;
    }

    public Paint getPaint()
    {
        return mPaint;
    }

    public static void setSpacings( int before , int after )
    {
        mBeforeSpacing = before;
        mAfterSpacing = after;
    }

    public int getBeforeSpacing()
    {
        return mBeforeSpacing;
    }

    public int getAfterSpacing()
    {
        return mAfterSpacing;
    }
}
