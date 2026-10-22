package dev.apslop.apps.cafeteria_review.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

/** レビューをアプリ内部ストレージの JSON ファイルに保存する。 */
object ReviewStorage {

    private const val FILE_NAME = "reviews.json"

    /** 保存済みレビュー。ファイルが無い（＝初回起動）場合は null。 */
    fun load(context: Context): List<Review>? {
        val file = File(context.filesDir, FILE_NAME)
        if (!file.exists()) return null
        return try {
            val array = JSONArray(file.readText())
            (0 until array.length()).mapNotNull { index ->
                val json = array.optJSONObject(index) ?: return@mapNotNull null
                Review(
                    id = json.optString("id"),
                    menuId = json.optString("menuId"),
                    author = json.optString("author").ifBlank { "ゲスト" },
                    rating = json.optInt("rating", 3).coerceIn(1, 5),
                    comment = json.optString("comment"),
                    photoPath = json.optString("photoPath").takeIf { it.isNotBlank() },
                    createdAt = json.optLong("createdAt", 0L),
                )
            }
        } catch (t: Throwable) {
            emptyList()
        }
    }

    fun save(context: Context, reviews: List<Review>) {
        try {
            val array = JSONArray()
            reviews.forEach { review ->
                array.put(
                    JSONObject().apply {
                        put("id", review.id)
                        put("menuId", review.menuId)
                        put("author", review.author)
                        put("rating", review.rating)
                        put("comment", review.comment)
                        put("photoPath", review.photoPath ?: "")
                        put("createdAt", review.createdAt)
                    }
                )
            }
            File(context.filesDir, FILE_NAME).writeText(array.toString())
        } catch (t: Throwable) {
            // 保存に失敗してもアプリは動作し続ける（次回起動時に消えるだけ）。
        }
    }
}
