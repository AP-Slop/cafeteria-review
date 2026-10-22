package dev.apslop.apps.cafeteria_review

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import dev.apslop.apps.cafeteria_review.data.Review
import dev.apslop.apps.cafeteria_review.data.ReviewStore
import dev.apslop.apps.cafeteria_review.data.SampleData
import dev.apslop.apps.cafeteria_review.ui.AppTheme
import dev.apslop.apps.cafeteria_review.ui.MenuDetailScreen
import dev.apslop.apps.cafeteria_review.ui.MenuListScreen
import dev.apslop.apps.cafeteria_review.ui.ReviewEditorDialog
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    AppRoot()
                }
            }
        }
    }
}

@Composable
fun AppRoot() {
    val context = LocalContext.current
    val store = remember { ReviewStore(context.applicationContext) }
    LaunchedEffect(Unit) { store.loadOnce() }

    var selectedMenuId by rememberSaveable { mutableStateOf("") }
    var showEditor by rememberSaveable { mutableStateOf(false) }

    val menus = SampleData.menus
    val selectedMenu = menus.firstOrNull { it.id == selectedMenuId }

    if (selectedMenu == null) {
        MenuListScreen(
            menus = menus,
            reviews = store.reviews,
            onSelect = { selectedMenuId = it.id },
        )
    } else {
        MenuDetailScreen(
            menu = selectedMenu,
            reviews = store.reviews.filter { it.menuId == selectedMenu.id },
            onBack = { selectedMenuId = "" },
            onAddReview = { showEditor = true },
            onDeleteReview = { store.deleteReview(it) },
        )
        if (showEditor) {
            ReviewEditorDialog(
                menuName = selectedMenu.name,
                initialAuthor = store.authorName,
                onDismiss = { showEditor = false },
                onSubmit = { author, rating, comment, photoPath ->
                    store.updateAuthorName(author)
                    store.addReview(
                        Review(
                            id = UUID.randomUUID().toString(),
                            menuId = selectedMenu.id,
                            author = store.authorName,
                            rating = rating,
                            comment = comment,
                            photoPath = photoPath,
                            createdAt = System.currentTimeMillis(),
                        )
                    )
                    showEditor = false
                },
            )
        }
    }
}
