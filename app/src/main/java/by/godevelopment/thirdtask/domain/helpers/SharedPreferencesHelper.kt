package by.godevelopment.thirdtask.domain.helpers

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import by.godevelopment.thirdtask.common.TAG
//import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPreferencesHelper @Inject constructor(
//    @ApplicationContext
    context: Context
) {
    private val sharedPreferences = context.getSharedPreferences(
        PREFERENCE_NAME,
        Context.MODE_PRIVATE
    )

    fun setCurrentLanguage(numberPhoneTask: String) {
        sharedPreferences.edit {
            Log.i(TAG, "SharedPreferencesHelper: $numberPhoneTask")
            putString(PREF_KEY, numberPhoneTask)
        }
    }

    fun getCurrentLanguage(): String = sharedPreferences.getString(PREF_KEY, START_KEY) ?: START_KEY

    companion object {
        private const val PREFERENCE_NAME = "task_pref"
        private const val PREF_KEY = "current_phoneNumber"
        const val START_KEY = ""
    }
}