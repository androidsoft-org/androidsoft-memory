/* Copyright (c) 2017 Raphaël Droz <raphael.droz@gmail.com>
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

import java.util.List;
import java.util.ArrayList;
import android.R.drawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.content.Context;



public class IconSet
{
    // instance properties
    public String title;

    // only when fetched remotely, optionnal if
    // image does not have corresponding path
    public String verso_path;
    public String[] images_path;

    // when actually loaded, optionnal when the IconSet
    // is in a transitory state (manifest file loaded, but image are not */
    public BitmapDrawable verso;
    public List<BitmapDrawable> images = new ArrayList<BitmapDrawable>();


    // constructor from filepath/URL
    // http://stackoverflow.com/a/9490060
    public IconSet(String title, String verso_path, List<String> images_path, Context ctx) {
	if (title.equals("") || verso_path == null || images_path.size() == 0) {
	    return;
	}

	this.title = title;
	this.verso_path = verso_path;
	this.images_path = images_path.toArray();
	this.verso = new BitmapDrawable(ctx.getResources(), verso_path);
	this.images = new ArrayList<BitmapDrawable>(images_path.size());
	for (String temp : images_path) {
	    this.images.add(new BitmapDrawable(ctx.getResources(), temp));
	    
	}
    }

    // useful constructor for the default IconSet
    public IconSet(String title, int verso, int[] images) {
	this.title = title;
	this.verso = verso;
	this.images = (ArrayList<BitmapDrawable>)new ArrayList<Int>(images);
    }

    public List<BitmapDrawable> getImages() {
	return images;
    }

    public BitmapDrawable getVerso() {
	return verso;
    }

    public String getTitle() {
	return title;
    }
}
