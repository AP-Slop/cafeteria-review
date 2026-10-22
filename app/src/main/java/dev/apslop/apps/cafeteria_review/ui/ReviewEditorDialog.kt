package dev.apslop.apps.cafeteria_review.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import dev.apslop.apps.cafeteria_review.R
import dev.apslop.apps.cafeteria_review.data.PhotoStore
import dev.apslop.apps.cafeteria_review.data.ratingLabel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ReviewEditorDialog(
    menuName: String,
    initialAuthor: String,
    onDismiss: () -> Unit,
    onSubmit: (author: String, rating: Int, comment: String, photoPath: String?) -> Unit,
) {
    var author by rememberSaveable { mutableStateOf(initialAuthor) }
    var rating by rememberSaveable { mutableIntStateOf(4) }
    var comment by rememberSaveable { mutableStateOf("") }
    var photoPath by rememberSaveable { mutableStateOf("") }
    var importing by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val pickPhoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            importing = true
            scope.launch {
                val saved = withContext(Dispatchers.IO) { PhotoStore.importFromUri(context, uri) }
                photoPath = saved ?: ""
                importing = false
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = stringResource(R.string.editor_title, menuName),
                    style = MaterialTheme.typography.titleLarge,
                )

                OutlinedTextField(
                    value = author,
                    onValueChange = { if (it.length <= 20) author = it },
                    label = { Text(stringResource(R.string.author_label)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )

                StarSelector(rating = rating, onRatingChange = { rating = it })

                OutlinedTextField(
                    value = comment,
                    onValueChange = { if (it.length <= 400) comment = it },
                    label = { Text(stringResource(R.string.comment_label)) },
                    placeholder = { Text(stringResource(R.string.comment_hint)) },
                    minLines = 3,
                    maxLines = 6,
                    modifier = Modifier.fillMaxWidth(),
                )

                if (photoPath.isNotBlank()) {
                    PhotoBox(
                        path = photoPath,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(12.dp)),
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = {
                            pickPhoto.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        enabled = !importing,
                    ) {
                        Text(
                            text = if (photoPath.isBlank()) {
                                stringResource(R.string.choose_photo)
                            } else {
                                stringResource(R.string.change_photo)
                            }
                        )
                    }
                    if (photoPath.isNotBlank()) {
                        TextButton(onClick = { photoPath = "" }) {
                            Text(stringResource(R.string.remove_photo))
                        }
                    }
                }

                if (importing) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.cancel))
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onSubmit(
                                author.trim(),
                                rating,
                                comment.trim(),
                                photoPath.ifBlank { null },
                            )
                        },
                        enabled = comment.isNotBlank() || photoPath.isNotBlank(),
                    ) {
                        Text(stringResource(R.string.submit))
                    }
                }
            }
        }
    }
}

@Composable
private fun StarSelector(rating: Int, onRatingChange: (Int) -> Unit) {
    Column {
        Text(
            text = stringResource(R.string.rating_label),
            style = MaterialTheme.typography.labelLarge,
        )
        Row {
            (1..5).forEach { star ->
                Text(
                    text = if (star <= rating) "★" else "☆",
                    fontSize = 32.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onRatingChange(star) }
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                )
            }
        }
        Text(
            text = ratingLabel(rating),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
