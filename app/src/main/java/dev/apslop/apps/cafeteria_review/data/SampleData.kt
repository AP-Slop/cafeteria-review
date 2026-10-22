package dev.apslop.apps.cafeteria_review.data

object SampleData {

    val menus: List<MenuItem> = listOf(
        MenuItem(
            id = "karaage",
            name = "唐揚げ定食",
            category = "定食",
            priceYen = 520,
            description = "揚げたての鶏もも唐揚げが3個。ごはん・味噌汁・小鉢付き。",
            emoji = "🍗",
        ),
        MenuItem(
            id = "daily",
            name = "日替わりランチ",
            category = "定食",
            priceYen = 480,
            description = "日によって変わるおすすめランチ。11:30〜13:30限定。",
            emoji = "🍱",
        ),
        MenuItem(
            id = "curry",
            name = "カツカレー",
            category = "カレー",
            priceYen = 550,
            description = "とんかつがのった王道カレー。辛さは控えめ。",
            emoji = "🍛",
        ),
        MenuItem(
            id = "miso-ramen",
            name = "味噌ラーメン",
            category = "麺類",
            priceYen = 500,
            description = "野菜たっぷりの味噌スープ。替え玉は +100 円。",
            emoji = "🍜",
        ),
        MenuItem(
            id = "carbonara",
            name = "カルボナーラ",
            category = "パスタ",
            priceYen = 580,
            description = "濃厚チーズのクリームパスタ。サラダ付き。",
            emoji = "🍝",
        ),
        MenuItem(
            id = "sandwich",
            name = "ミックスサンド",
            category = "軽食",
            priceYen = 380,
            description = "たまご・ハム・野菜の3種。テイクアウト可。",
            emoji = "🥪",
        ),
        MenuItem(
            id = "udon",
            name = "冷やしうどん",
            category = "麺類",
            priceYen = 420,
            description = "夏限定。冷たいだしと天かすがよく合う。",
            emoji = "🍲",
        ),
        MenuItem(
            id = "parfait",
            name = "いちごパフェ",
            category = "デザート",
            priceYen = 350,
            description = "カフェテリア名物の大きめパフェ。",
            emoji = "🍨",
        ),
    )

    /** 初回起動時に入れておくサンプルレビュー（写真なし）。 */
    fun seedReviews(): List<Review> {
        val now = System.currentTimeMillis()
        val day = 24L * 60L * 60L * 1000L
        return listOf(
            Review(
                id = "seed-1",
                menuId = "karaage",
                author = "ゆうた",
                rating = 5,
                comment = "衣がサクサクでごはんが進む。520円は安い！",
                photoPath = null,
                createdAt = now - day,
            ),
            Review(
                id = "seed-2",
                menuId = "karaage",
                author = "みさき",
                rating = 4,
                comment = "唐揚げはおいしいけど混雑するのが難点。12時前に行くのが吉。",
                photoPath = null,
                createdAt = now - day * 2,
            ),
            Review(
                id = "seed-3",
                menuId = "curry",
                author = "けん",
                rating = 3,
                comment = "普通においしい。もう少しスパイスが効いてほしい。",
                photoPath = null,
                createdAt = now - day * 3,
            ),
            Review(
                id = "seed-4",
                menuId = "miso-ramen",
                author = "あや",
                rating = 4,
                comment = "野菜が多くてヘルシー。スープまで飲み干しました。",
                photoPath = null,
                createdAt = now - day * 4,
            ),
            Review(
                id = "seed-5",
                menuId = "daily",
                author = "そうた",
                rating = 5,
                comment = "日替わりはコスパ最強。今日はチキン南蛮でした。",
                photoPath = null,
                createdAt = now - day * 5,
            ),
            Review(
                id = "seed-6",
                menuId = "carbonara",
                author = "はる",
                rating = 3,
                comment = "量は多いけど少しぬるかった。",
                photoPath = null,
                createdAt = now - day * 6,
            ),
        )
    }
}
