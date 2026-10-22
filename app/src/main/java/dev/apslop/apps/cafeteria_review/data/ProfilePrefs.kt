package dev.apslop.apps.cafeteria_review.data

import android.content.Context

/** ニックネームなどの小さな設定を SharedPreferences に保存する。 */
object ProfilePrefs {

    private const val PREFS_NAME = "cafeteria_review_prefs"
    private const val KEY_AUTHOR = "author_name"
    private const val DEFAULT_AUTHOR = "ゲスト"

    fun authorName(context: Context): String =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_AUTHOR, DEFAULT_AUTHOR)
            ?.ifBlank { DEFAULT_AUTHOR }
            ?: DEFAULT_AUTHOR

    fun setAuthorName(context: Context, name: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_AUTHOR, name)
            .apply()
    }
}
