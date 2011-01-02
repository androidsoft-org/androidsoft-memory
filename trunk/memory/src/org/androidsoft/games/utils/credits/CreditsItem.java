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

/**
 * Credits Item interface
 * @author Pierre Levy
 */
public interface CreditsItem
{
    /**
     * Draw the item
     * @param canvas The Canvas
     */

    void draw(Canvas canvas);

    /**
     * Prepare elements before drawing
     * @param ellapsed Time elapsed
     * @param width Screen width
     * @param height Screen height
     * @param dy Vertical spreed
     */
    void prepare(long ellapsed, int width, int height, int dy);


    /**
     * Set the Offset
     * @param offset
     */
    void setOffset(int offset);

    /**
     * Get the Offset
     * @return The offset
     */
    int getOffset();

    /**
     * Get spacing before the item
     * @return
     */
    int getBeforeSpacing();

    /**
     * Get spacing after the item
     * @return
     */
    int getAfterSpacing();

}
