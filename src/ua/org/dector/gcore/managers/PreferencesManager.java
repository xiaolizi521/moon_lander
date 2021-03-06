package ua.org.dector.gcore.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * @author dector (dector9@gmail.com)
 */
public class PreferencesManager {
    private Preferences preferences;

    public PreferencesManager(String preferencesName) {
        preferences = Gdx.app.getPreferences(preferencesName);
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        return preferences.getBoolean(name, defaultValue);
    }

    public int getInt(String name, int defaultValue) {
        return preferences.getInteger(name, defaultValue);
    }

    public float getFloat(String name, float defaultValue) {
        return preferences.getFloat(name, defaultValue);
    }

    public String getString(String name, String defaultValue) {
        return preferences.getString(name, defaultValue);
    }

    public void putBoolean(String name, boolean value) {
        preferences.putBoolean(name, value);
        save();
    }

    public void putInt(String name, int value) {
        preferences.putInteger(name, value);
        save();
    }

    public void putFloat(String name, float value) {
        preferences.putFloat(name, value);
        save();
    }

    public void putString(String name, String value) {
        preferences.putString(name, value);
        save();
    }

    public void save() {
        preferences.flush();
    }
}
