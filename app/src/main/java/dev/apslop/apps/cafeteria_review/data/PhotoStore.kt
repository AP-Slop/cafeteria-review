package dev.apslop.apps.cafeteria_review.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.util.UUID

/** フォトピッカーで選んだ写真をアプリ内に縮小コピーする。 */
object PhotoStore {

    private const val DIR_NAME = "review_photos"
    private const val MAX_DIMENSION = 1280
    private const val JPEG_QUALITY = 85

    /** 取り込みに成功したら保存先の絶対パスを返す。 */
    fun importFromUri(context: Context, uri: Uri): String? {
        return try {
            val dir = File(context.filesDir, DIR_NAME)
            if (!dir.exists() && !dir.mkdirs()) return null

            val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                ?: return null

            val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size, bounds)
            if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null

            var sampleSize = 1
            while (bounds.outWidth / (sampleSize * 2) >= MAX_DIMENSION ||
                bounds.outHeight / (sampleSize * 2) >= MAX_DIMENSION
            ) {
                sampleSize *= 2
            }

            val options = BitmapFactory.Options().apply { inSampleSize = sampleSize }
            val decoded = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
                ?: return null
            val scaled = scaleDown(decoded)
            if (scaled !== decoded) decoded.recycle()

            val outFile = File(dir, "review_${UUID.randomUUID()}.jpg")
            outFile.outputStream().use { stream ->
                scaled.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, stream)
            }
            scaled.recycle()
            outFile.absolutePath
        } catch (t: Throwable) {
            null
        }
    }

    private fun scaleDown(source: Bitmap): Bitmap {
        val width = source.width
        val height = source.height
        val longest = maxOf(width, height)
        if (longest <= MAX_DIMENSION) return source
        val ratio = MAX_DIMENSION.toFloat() / longest
        val targetWidth = (width * ratio).toInt().coerceAtLeast(1)
        val targetHeight = (height * ratio).toInt().coerceAtLeast(1)
        return Bitmap.createScaledBitmap(source, targetWidth, targetHeight, true)
    }
}
