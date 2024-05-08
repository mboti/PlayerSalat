package com.mboti.playersalat.tool

import android.content.Context

class SharedPreferences (context: Context) {

    val sharedSoundElFatiha = "sharedSoundElFatiha"
    val sharedSoundAyat = "sharedSoundAyat"
    val sharedSoundOther = "sharedSoundOther"
    val sharedSoundCountdown = "sharedSoundCountdown"
    val sharedSoundSpeed = "sharedSoundSpeed"

    private val sharedPreferences = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)


    // Sound El-Fatiha
    fun saveSoundElFatiha(key: String, value: Int) { sharedPreferences.edit().putInt(key, value).apply() }
    fun getSoundElFatiha(key: String, defaultValue: Int): Int { return sharedPreferences.getInt(key, defaultValue) }



    // Sound Ayat
    fun saveSoundAyat(key: String, value: Int) { sharedPreferences.edit().putInt(key, value).apply() }
    fun getSoundAyat(key: String, defaultValue: Int): Int { return sharedPreferences.getInt(key, defaultValue) }



    // Sound Other
    fun saveSoundOther(key: String, value: Int) { sharedPreferences.edit().putInt(key, value).apply() }
    fun getSoundOther(key: String, defaultValue: Int): Int { return sharedPreferences.getInt(key, defaultValue) }



    // Countdown (on/off)
    fun saveCountdown(key: String, value: Boolean) { sharedPreferences.edit().putBoolean(key, value).apply() }
    fun getCountdown(key: String, defaultValue: Boolean): Boolean { return sharedPreferences.getBoolean(key, defaultValue) }



    // Speed
    fun saveSpeed(key: String, value: Float) { sharedPreferences.edit().putFloat(key, value).apply() }
    fun getSpeed(key: String, defaultValue: Float): Float { return sharedPreferences.getFloat(key, defaultValue) }
}