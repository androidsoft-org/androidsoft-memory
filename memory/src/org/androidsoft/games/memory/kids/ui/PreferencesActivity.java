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
package org.androidsoft.games.memory.kids.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
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
    private CompoundButton mCbSoundEnabled;
    private RadioButton mRbNormal;
    private RadioButton mRbSeason;
            
    
    /**
     * {@inheritDoc }
     */
    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);

        setContentView( R.layout.preferences);
        
        mTvHiScore = (TextView) findViewById( R.id.hiscore );
        updateHiScore();

        mButtonResetHiScore = (Button) findViewById(R.id.button_reset_hiscore);
        mButtonResetHiScore.setOnClickListener(this);
        
        mRbNormal = (RadioButton) findViewById(R.id.radio_mode_normal);  
        mRbNormal.setOnClickListener(this);
        mRbSeason = (RadioButton) findViewById(R.id.radio_mode_season);
        mRbSeason.setOnClickListener(this);
        int iconSet = PreferencesService.instance().getIconsSet();
        if( iconSet == PreferencesService.ICONS_SET_NORMAL )
        {
            mRbNormal.setChecked(true);
            mRbSeason.setChecked(false);
        }
        else if ( iconSet == PreferencesService.ICONS_SET_SEASON )
        {
            mRbNormal.setChecked(false);
            mRbSeason.setChecked(true);
        }    
        
        mCbSoundEnabled = (CompoundButton) findViewById(R.id.checkbox_sound);
        mCbSoundEnabled.setOnClickListener(this);
        mCbSoundEnabled.setChecked( PreferencesService.instance().isSoundEnabled() );
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
        if( view == mButtonResetHiScore )
        {
            PreferencesService.instance().resetHiScore();
            updateHiScore();
        } 
        else if ( view == mCbSoundEnabled )
        {
            PreferencesService.instance().saveSoundEnabled( mCbSoundEnabled.isChecked());
        }
        else if ( view == mRbNormal )
        {
            PreferencesService.instance().saveIconsSet( PreferencesService.ICONS_SET_NORMAL );
            Toast.makeText(this, R.string.message_effect_new_game, Toast.LENGTH_LONG).show();
        }
        else if ( view == mRbSeason )
        {
            PreferencesService.instance().saveIconsSet( PreferencesService.ICONS_SET_SEASON );
            Toast.makeText(this, R.string.message_effect_new_game, Toast.LENGTH_LONG).show();
        }
    }

    private void updateHiScore()
    {
        int hiscore = PreferencesService.instance().getHiScore();
        if( hiscore == PreferencesService.HISCORE_DEFAULT )
        {
            mTvHiScore.setText(" - ");
        }
        else
        {
            mTvHiScore.setText(" " + hiscore );
        }
    }

    
  
}
