package com.fsryan.tools.dvm.testtoolsonlibdemo

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsExample(context: Context) {
    private val appContext = context.applicationContext

    fun getStringPref(): String = acquirePrefs().getString(STRING_PREF_KEY, "")!!
    fun storeStringPref(value: String): Boolean = editPrefs().putString(STRING_PREF_KEY, value).commit()
    fun getBooleanPref(): Boolean = acquirePrefs().getBoolean(BOOLEAN_PREF_KEY, false)
    fun storeBooleanPref(value: Boolean): Boolean = editPrefs().putBoolean(BOOLEAN_PREF_KEY, value).commit()
    fun getStringSetPref(): Set<String> = acquirePrefs().getStringSet(STRING_SET_PREF_KEY, setOf())!!
    fun storeStringSetPref(value: Set<String>): Boolean = editPrefs().putStringSet(STRING_SET_PREF_KEY, value).commit()

    private fun acquirePrefs(): SharedPreferences = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private fun editPrefs(): SharedPreferences.Editor = acquirePrefs().edit()

    companion object {
        const val PREF_NAME = "example_shared_prefs"
        private const val STRING_PREF_KEY = "string_pref"
        private const val STRING_SET_PREF_KEY = "string_set_pref"
        private const val BOOLEAN_PREF_KEY = "boolean_pref"
    }
}