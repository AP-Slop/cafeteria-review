package dev.apslop.apps.cafeteria_review.data

/** カフェテリアのメニュー 1 品。 */
data class MenuItem(
    val id: String,
    val name: String,
    val category: String,
    val priceYen: Int,
    val description: String,
    val emoji: String,
)

/** 1 件のレビュー。photoPath はアプリ内部ストレージに保存した写真の絶対パス。 */
data class Review(
    val id: String,
    val menuId: String,
    val author: String,
    val rating: Int,
    val comment: String,
    val photoPath: String?,
    val createdAt: Long,
)
