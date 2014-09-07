/* Copyright (c) 2010-2014 Pierre LEVY androidsoft.org
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
package org.androidsoft.games.memory.kids.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import org.androidsoft.games.memory.kids.PreferencesService;
import org.androidsoft.games.memory.kids.R;
import org.androidsoft.utils.ui.BasicActivity;

/**
 *
 * @author pierre
 */
public class PreferencesActivity extends BasicActivity implements OnClickListener
{

    private TextView mTvHiScore;
    private Button mButtonResetHiScore;
    private Button mButtonSupport;
    private CompoundButton mCbSoundEnabled;
    private Spinner mSpinner;
    private int mIconSet;

    /**
     * {@inheritDoc }
     */
    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);

        setContentView(R.layout.preferences);

        mTvHiScore = (TextView) findViewById(R.id.hiscore);
        updateHiScore();

        mButtonResetHiScore = (Button) findViewById(R.id.button_reset_hiscore);
        mButtonResetHiScore.setOnClickListener(this);

        mSpinner = (Spinner) findViewById(R.id.spinner_theme);

        mIconSet = PreferencesService.instance().getIconsSet();
        mSpinner.setSelection( mIconSet );

        mSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                setIconSet(position);
            }

            public void onNothingSelected(AdapterView<?> parent)
            {
                setIconSet( 0 );
            }

        });


        mCbSoundEnabled = (CompoundButton) findViewById(R.id.checkbox_sound);
        mCbSoundEnabled.setOnClickListener(this);
        mCbSoundEnabled.setChecked(PreferencesService.instance().isSoundEnabled());

        mButtonSupport = (Button) findViewById(R.id.button_support);
        mButtonSupport.setOnClickListener(this);

    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int getMenuResource()
    {
        return R.menu.menu_close;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int getMenuCloseId()
    {
        return R.id.menu_close;
    }

    /**
     * {@inheritDoc }
     */
    public void onClick(View view)
    {
        if (view == mButtonResetHiScore)
        {
            PreferencesService.instance().resetHiScore();
            updateHiScore();
        }
        else if (view == mCbSoundEnabled)
        {
            PreferencesService.instance().saveSoundEnabled(mCbSoundEnabled.isChecked());
        }
        else if (view == mButtonSupport)
        {
            openGooglePlay();
        }
    }

    private void updateHiScore()
    {
        int hiscore = PreferencesService.instance().getHiScore();
        if (hiscore == PreferencesService.HISCORE_DEFAULT)
        {
            mTvHiScore.setText(" - ");
        }
        else
        {
            mTvHiScore.setText(" " + hiscore);
        }
    }
    
    private void setIconSet(int iconSet)
    {
        if( iconSet != mIconSet )
        {
            PreferencesService.instance().saveIconsSet( iconSet );
            Toast.makeText(this, R.string.message_effect_new_game, Toast.LENGTH_LONG).show();
            mIconSet = iconSet;
        }
    }

    /**
     * Open the market
     */
    private void openGooglePlay()
    {
        String uri = "market://details?id=org.androidsoft.games.memory.kids";
        Intent intentGoToGooglePlay = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intentGoToGooglePlay);
    }


}
