package dev.apslop.apps.cafeteria_review.ui

import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.apslop.apps.cafeteria_review.R
import dev.apslop.apps.cafeteria_review.data.MenuItem
import dev.apslop.apps.cafeteria_review.data.Review
import dev.apslop.apps.cafeteria_review.data.averageRating
import dev.apslop.apps.cafeteria_review.data.formatRating
import dev.apslop.apps.cafeteria_review.data.ratingLabel
import dev.apslop.apps.cafeteria_review.data.starText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuDetailScreen(
    menu: MenuItem,
    reviews: List<Review>,
    onBack: () -> Unit,
    onAddReview: () -> Unit,
    onDeleteReview: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.detail_title_with_count, menu.name, reviews.size))
                },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text(stringResource(R.string.back))
                    }
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddReview,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                content = { Text(stringResource(R.string.write_review)) },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item { MenuHeaderCard(menu = menu, reviews = reviews) }
            item {
                Text(
                    text = stringResource(R.string.reviews_title, reviews.size),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            if (reviews.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.no_reviews),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            items(reviews, key = { it.id }) { review ->
                ReviewCard(review = review, onDelete = onDeleteReview)
            }
        }
    }
}

@Composable
private fun MenuHeaderCard(menu: MenuItem, reviews: List<Review>) {
    val average = averageRating(reviews.map { it.rating })
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = menu.emoji, fontSize = 32.sp)
                }
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(text = menu.name, style = MaterialTheme.typography.headlineSmall)
                    Text(
                        text = stringResource(
                            R.string.category_and_price,
                            menu.category,
                            menu.priceYen,
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(text = menu.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = starText(average),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = stringResource(
                        R.string.average_with_count,
                        formatRating(average),
                        reviews.size,
                    ),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

@Composable
private fun ReviewCard(review: Review, onDelete: (String) -> Unit) {
    val dateText = remember(review.createdAt) {
        SimpleDateFormat("yyyy/MM/dd", Locale.JAPAN).format(Date(review.createdAt))
    }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = review.author,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = dateText,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = starText(review.rating.toDouble()),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = ratingLabel(review.rating),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (review.comment.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(text = review.comment, style = MaterialTheme.typography.bodyMedium)
            }
            val photoPath = review.photoPath
            if (!photoPath.isNullOrBlank()) {
                Spacer(Modifier.height(12.dp))
                PhotoBox(
                    path = photoPath,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp)),
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(onClick = { onDelete(review.id) }) {
                    Text(stringResource(R.string.delete))
                }
            }
        }
    }
}
