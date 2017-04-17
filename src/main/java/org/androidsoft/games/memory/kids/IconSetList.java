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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.net.ssl.HttpsURLConnection;
import android.R.drawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.content.Context;
import android.net.Uri;

import org.androidsoft.games.memory.kids.IconSet;

public class IconSetList
{


    // properties of the set of icon sets
    public static final String DEFAULT_SET_NAME = "easter";

    private static HashMap<String, IconSet> set;
    private static HashMap<String, IconSet> default_set;

    static int[] tiles_default = {R.drawable.default_1, R.drawable.default_2,
				  R.drawable.default_3, R.drawable.default_4, R.drawable.default_5, R.drawable.default_6,
				  R.drawable.default_7, R.drawable.default_8, R.drawable.default_9, R.drawable.default_10,
				  R.drawable.default_11, R.drawable.default_12, R.drawable.default_13, R.drawable.default_14,
				  R.drawable.default_15, R.drawable.default_16, R.drawable.default_17, R.drawable.default_18,
				  R.drawable.default_19, R.drawable.default_20, R.drawable.default_21, R.drawable.default_22,
				  R.drawable.default_23, R.drawable.default_24, R.drawable.default_25, R.drawable.default_26,
				  R.drawable.default_27, R.drawable.default_28, R.drawable.default_29, R.drawable.default_30,
				  R.drawable.default_31, R.drawable.default_32, R.drawable.default_33, R.drawable.default_34};

    static int[] tiles_christmas = {R.drawable.christmas_1, R.drawable.christmas_2,
				    R.drawable.christmas_3, R.drawable.christmas_4, R.drawable.christmas_5, R.drawable.christmas_6,
				    R.drawable.christmas_7, R.drawable.christmas_8, R.drawable.christmas_9, R.drawable.christmas_10,
				    R.drawable.christmas_11, R.drawable.christmas_12, R.drawable.christmas_13, R.drawable.christmas_14,
				    R.drawable.christmas_15, R.drawable.christmas_16, R.drawable.christmas_17, R.drawable.christmas_18,
				    R.drawable.christmas_19, R.drawable.christmas_20, R.drawable.christmas_21, R.drawable.christmas_22};

    static int[] tiles_easter = {R.drawable.easter_1, R.drawable.easter_2,
				 R.drawable.easter_3, R.drawable.easter_4, R.drawable.easter_5, R.drawable.easter_6,
				 R.drawable.easter_7, R.drawable.easter_8, R.drawable.easter_9, R.drawable.easter_10,
				 R.drawable.easter_11, R.drawable.easter_12, R.drawable.easter_13, R.drawable.easter_14};

    static int[] tiles_tux = {R.drawable.tux_1, R.drawable.tux_2,
			      R.drawable.tux_3, R.drawable.tux_4, R.drawable.tux_5, R.drawable.tux_6,
			      R.drawable.tux_7, R.drawable.tux_8, R.drawable.tux_9, R.drawable.tux_10,
			      R.drawable.tux_11, R.drawable.tux_12, R.drawable.tux_13, R.drawable.tux_14,
			      R.drawable.tux_15, R.drawable.tux_16, R.drawable.tux_17, R.drawable.tux_18,
			      R.drawable.tux_19, R.drawable.tux_20, R.drawable.tux_21, R.drawable.tux_22,
			      R.drawable.tux_23, R.drawable.tux_24, R.drawable.tux_25, R.drawable.tux_26,
			      R.drawable.tux_27, R.drawable.tux_28, R.drawable.tux_29, R.drawable.tux_30,
			      R.drawable.tux_31, R.drawable.tux_32, R.drawable.tux_33};

    static {
	HashMap<String, IconSet> aset = new HashMap<String, IconSet>();
	aset.put("default",   new IconSet("default",   R.drawable.not_found_default,   tiles_default));
	aset.put("christmas", new IconSet("christmas", R.drawable.not_found_christmas, tiles_christmas));
	aset.put("easter",    new IconSet("easter",    R.drawable.not_found_easter,    tiles_easter));
	aset.put("tux",       new IconSet("tux",       R.drawable.not_found_tux,       tiles_tux));
	default_set.putAll(aset);
	set.putAll(aset);
    }

    /* apply to the various IconSet */
    public static IconSet get(String title) {
	return set.get(title);
    }

    public static IconSet getDefault() {
	return set.get(DEFAULT_SET_NAME);
    }

    public static boolean addFromUrl(URL url, Context ctx) {
	IconSet x = loadFromUrl(url, true, ctx);
	if (x != null) {
	    set.put(x.getTitle(), x);
	    return true;
	}
	return false;
    }

    // http://stackoverflow.com/a/8897653
    public static IconSet loadFromUrl(URL url, boolean store, Context context) {
	try {
	    String t, v;
	    List<String> img = new ArrayList<String>();

	    URLConnection conn = url.openConnection();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(conn.getInputStream());

	    Element title_element = doc.getElementById("title");
	    t = title_element.getTextContent();

	    Element verso_element = doc.getElementById("verso");
	    if (verso_element != null) {
		v = verso_element.getTextContent();
	    }

            NodeList nodes = doc.getElementsByTagName("images");
	    for (int i = 0; i < nodes.getLength(); i++) {
		Element element = (Element) nodes.item(i);
		NodeList image_element = element.getElementsByTagName("image");
		Element image = (Element) image_element.item(0);
		img.add(image.getTextContent());
		if (store == true) {
		    // https://developer.android.com/training/basics/data-storage/files.html
		    File file = File.createTempFile(Uri.parse(image.getTextContent()).getLastPathSegment(), null, context.getCacheDir());
		}
	    }

	    IconSet is = new IconSet(t, v, img, context);
	    // add to the currently usable IconSets
	    set.put(t, is);
	    return is;
	}
	catch (IOException e) {
	    e.printStackTrace();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public static void loadAdditionalIconSets(/*TODO*/) {
    }
}
