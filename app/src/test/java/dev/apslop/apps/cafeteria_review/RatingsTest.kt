package dev.apslop.apps.cafeteria_review

import dev.apslop.apps.cafeteria_review.data.MenuItem
import dev.apslop.apps.cafeteria_review.data.Review
import dev.apslop.apps.cafeteria_review.data.averageRating
import dev.apslop.apps.cafeteria_review.data.formatRating
import dev.apslop.apps.cafeteria_review.data.starText
import dev.apslop.apps.cafeteria_review.data.summarizeMenus
import org.junit.Assert.assertEquals
import org.junit.Test

class RatingsTest {

    @Test
    fun averageRating_isZeroForEmptyList() {
        assertEquals(0.0, averageRating(emptyList()), 0.001)
    }

    @Test
    fun averageRating_computesMean() {
        assertEquals(4.0, averageRating(listOf(5, 3)), 0.001)
        assertEquals(4.25, averageRating(listOf(5, 4, 4, 4)), 0.001)
    }

    @Test
    fun formatRating_hidesZero() {
        assertEquals("-", formatRating(0.0))
    }

    @Test
    fun formatRating_usesOneDecimal() {
        assertEquals("4.2", formatRating(4.2))
        assertEquals("4.3", formatRating(4.26))
    }

    @Test
    fun starText_fillsRoundedStars() {
        assertEquals("☆☆☆☆☆", starText(0.0))
        assertEquals("★★★★☆", starText(4.2))
        assertEquals("★★★★★", starText(4.8))
        assertEquals("★★★★★", starText(7.0))
    }

    @Test
    fun summarizeMenus_groupsReviewsAndSortsByRating() {
        val menus = listOf(
            MenuItem("a", "カレー", "カレー", 500, "", ""),
            MenuItem("b", "うどん", "麺類", 400, "", ""),
        )
        val reviews = listOf(
            Review("1", "a", "x", 3, "", null, 0L),
            Review("2", "a", "y", 5, "", null, 0L),
            Review("3", "b", "z", 1, "", null, 0L),
        )

        val byRating = summarizeMenus(menus, reviews, sortByRating = true)
        assertEquals("a", byRating[0].menu.id)
        assertEquals(2, byRating[0].reviewCount)
        assertEquals(4.0, byRating[0].average, 0.001)
        assertEquals("b", byRating[1].menu.id)

        val byName = summarizeMenus(menus, reviews, sortByRating = false)
        assertEquals("b", byName[0].menu.id)
        assertEquals(1, byName[0].reviewCount)
    }
}
