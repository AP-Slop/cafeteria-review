package dev.apslop.apps.cafeteria_review.data

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/** レビューの読み込み・追加・削除と永続化をまとめた状態ホルダー。 */
class ReviewStore(private val context: Context) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val reviews: SnapshotStateList<Review> = mutableStateListOf()

    var authorName by mutableStateOf("ゲスト")
        private set

    private var loaded = false

    suspend fun loadOnce() {
        if (loaded) return
        loaded = true
        authorName = ProfilePrefs.authorName(context)
        val stored = withContext(Dispatchers.IO) { ReviewStorage.load(context) }
        if (stored == null) {
            reviews.addAll(SampleData.seedReviews())
            persist()
        } else {
            reviews.addAll(stored)
        }
    }

    fun addReview(review: Review) {
        reviews.add(0, review)
        persist()
    }

    fun deleteReview(reviewId: String) {
        val index = reviews.indexOfFirst { it.id == reviewId }
        if (index < 0) return
        val removed = reviews.removeAt(index)
        persist()
        removed.photoPath?.let { path ->
            scope.launch { runCatching { File(path).delete() } }
        }
    }

    /** すべてのレビューと、それに紐づく写真ファイルを削除する。 */
    fun clearAllReviews() {
        if (reviews.isEmpty()) return
        val photoPaths = reviews.mapNotNull { it.photoPath }
        reviews.clear()
        persist()
        if (photoPaths.isNotEmpty()) {
            scope.launch {
                photoPaths.forEach { path -> runCatching { File(path).delete() } }
            }
        }
    }

    fun setAuthorName(name: String) {
        val trimmed = name.trim().ifBlank { "ゲスト" }
        authorName = trimmed
        ProfilePrefs.setAuthorName(context, trimmed)
    }

    private fun persist() {
        val snapshot = reviews.toList()
        scope.launch { ReviewStorage.save(context, snapshot) }
    }
}
