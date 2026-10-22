package dev.apslop.apps.cafeteria_review.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.apslop.apps.cafeteria_review.R
import dev.apslop.apps.cafeteria_review.data.MenuItem
import dev.apslop.apps.cafeteria_review.data.MenuSummary
import dev.apslop.apps.cafeteria_review.data.Review
import dev.apslop.apps.cafeteria_review.data.SampleData
import dev.apslop.apps.cafeteria_review.data.formatRating
import dev.apslop.apps.cafeteria_review.data.starText
import dev.apslop.apps.cafeteria_review.data.summarizeMenus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuListScreen(
    menus: List<MenuItem>,
    reviews: List<Review>,
    onSelect: (MenuItem) -> Unit,
    onClearAllReviews: () -> Unit,
) {
    var sortByRating by rememberSaveable { mutableStateOf(true) }
    var showClearDialog by rememberSaveable { mutableStateOf(false) }
    val summaries = summarizeMenus(menus, reviews, sortByRating)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    TextButton(
                        onClick = { showClearDialog = true },
                        enabled = reviews.isNotEmpty(),
                    ) {
                        Text(stringResource(R.string.clear_all))
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Column {
                    Text(
                        text = stringResource(R.string.list_header),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.menu_and_review_count, menus.size, reviews.size),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = sortByRating,
                            onClick = { sortByRating = true },
                            label = { Text(stringResource(R.string.sort_rating)) },
                        )
                        FilterChip(
                            selected = !sortByRating,
                            onClick = { sortByRating = false },
                            label = { Text(stringResource(R.string.sort_name)) },
                        )
                    }
                }
            }
            items(summaries, key = { it.menu.id }) { summary ->
                MenuCard(summary = summary, onClick = { onSelect(summary.menu) })
            }
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text(stringResource(R.string.clear_all_confirm_title)) },
            text = {
                Text(stringResource(R.string.clear_all_confirm_message, reviews.size))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showClearDialog = false
                        onClearAllReviews()
                    },
                ) {
                    Text(stringResource(R.string.clear_all_confirm_action))
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            },
        )
    }
}

@Composable
private fun MenuCard(summary: MenuSummary, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = summary.menu.emoji, fontSize = 28.sp)
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = summary.menu.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = stringResource(
                        R.string.category_and_price,
                        summary.menu.category,
                        summary.menu.priceYen,
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(6.dp))
                if (summary.reviewCount == 0) {
                    Text(
                        text = stringResource(R.string.no_review_short),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = starText(summary.average),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = stringResource(
                                R.string.average_with_count,
                                formatRating(summary.average),
                                summary.reviewCount,
                            ),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MenuListPreview() {
    AppTheme {
        MenuListScreen(
            menus = SampleData.menus,
            reviews = SampleData.seedReviews(),
            onSelect = {},
            onClearAllReviews = {},
        )
    }
}
