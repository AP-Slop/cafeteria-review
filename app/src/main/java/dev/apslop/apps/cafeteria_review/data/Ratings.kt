package dev.apslop.apps.cafeteria_review.data

import java.util.Locale
import kotlin.math.roundToInt

/** 平均評価。レビューが無ければ 0.0。 */
fun averageRating(ratings: List<Int>): Double =
    if (ratings.isEmpty()) 0.0 else ratings.sum().toDouble() / ratings.size

/** 平均評価の表示用文字列。未評価は "-"。 */
fun formatRating(value: Double): String =
    if (value <= 0.0) "-" else String.format(Locale.JAPAN, "%.1f", value)

/** 0〜5 の星文字列。四捨五入して塗り分ける。 */
fun starText(value: Double): String {
    val filled = value.coerceIn(0.0, 5.0).roundToInt()
    return "★".repeat(filled) + "☆".repeat(5 - filled)
}

/** 星の数の短い説明。 */
fun ratingLabel(rating: Int): String = when (rating) {
    1 -> "がっかり"
    2 -> "いまいち"
    3 -> "ふつう"
    4 -> "おいしい"
    5 -> "最高！"
    else -> "未評価"
}

/** 一覧表示用のメニュー要約。 */
data class MenuSummary(
    val menu: MenuItem,
    val reviewCount: Int,
    val average: Double,
)

/** メニューとレビューから一覧用の要約を作る。sortByRating=true で評価順。 */
fun summarizeMenus(
    menus: List<MenuItem>,
    reviews: List<Review>,
    sortByRating: Boolean,
): List<MenuSummary> {
    val byMenu = reviews.groupBy { it.menuId }
    val summaries = menus.map { menu ->
        val ratings = byMenu[menu.id].orEmpty().map { it.rating }
        MenuSummary(menu = menu, reviewCount = ratings.size, average = averageRating(ratings))
    }
    return if (sortByRating) {
        summaries.sortedWith(
            compareByDescending<MenuSummary> { it.average }
                .thenByDescending { it.reviewCount }
                .thenBy { it.menu.name }
        )
    } else {
        summaries.sortedBy { it.menu.name }
    }
}
