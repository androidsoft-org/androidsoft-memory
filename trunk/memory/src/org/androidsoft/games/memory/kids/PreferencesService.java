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
package org.androidsoft.games.memory.kids;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Preference Service
 * 
 * @author Pierre Levy
 */
public class PreferencesService
{

    public static final int ICONS_SET_NORMAL = 0;
    public static final int ICONS_SET_SEASON = 1;
    public static final int HISCORE_DEFAULT = 200;
    private static final String PREFS_NAME = "MemoryPrefsFile";
    private static final String PREF_BEST_MOVE_COUNT = "best_move_count";
    private static final String PREF_SOUND_ENABLED = "sound_enabled";
    private static final String PREF_ICONS_SET = "icons_set";
    private static PreferencesService mSingleton = new PreferencesService();
    private static Context mContext;

    private PreferencesService()
    {
    }

    public static PreferencesService instance()
    {
        return mSingleton;
    }

    public static void init(Context context)
    {
        mContext = context;
    }

    public SharedPreferences getPrefs()
    {
        return mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveHiScore(int hiscore)
    {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putInt(PREF_BEST_MOVE_COUNT, hiscore);
        editor.commit();

    }

    public int getHiScore()
    {
        return getPrefs().getInt(PREF_BEST_MOVE_COUNT, HISCORE_DEFAULT);

    }

    public void resetHiScore()
    {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.remove(PREF_BEST_MOVE_COUNT);
        editor.commit();
    }

    public boolean isSoundEnabled()
    {
        return getPrefs().getBoolean(PREF_SOUND_ENABLED, true);
    }

    public void saveSoundEnabled(boolean enabled)
    {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putBoolean(PREF_SOUND_ENABLED, enabled);
        editor.commit();
    }

    public void saveIconsSet(int set)
    {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putInt(PREF_ICONS_SET, set);
        editor.commit();

    }

    public int getIconsSet()
    {
        return getPrefs().getInt(PREF_ICONS_SET, ICONS_SET_SEASON);

    }
}
