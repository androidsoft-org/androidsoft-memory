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

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

/**
 *
 * @author pierre
 */
public class MemoryGridView extends GridView
{
    private Memory mMemory;

    public MemoryGridView(Context context)
    {
        super(context);


        setOnItemClickListener(new OnItemClickListener()
        {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                mMemory.onPosition( position );
            }
        });

    }

    public MemoryGridView (Context context, AttributeSet attrs)
    {
        super( context , attrs );
        setOnItemClickListener(new OnItemClickListener()
        {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                mMemory.onPosition( position );
            }
        });
    }

    public MemoryGridView (Context context, AttributeSet attrs, int defStyle)
    {
        super( context , attrs , defStyle );
        setOnItemClickListener(new OnItemClickListener()
        {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                mMemory.onPosition( position );
            }
        });
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        Log.d( "Memory", "Finish Inflate width:" + getWidth());
    }


    public void setMemory( Memory memory )
    {
        mMemory = memory;
    }

}
