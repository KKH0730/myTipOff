package com.life.myTipOff.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.lang.String
import kotlin.ByteArray
import kotlin.Exception
import kotlin.Int
import kotlin.Long
import kotlin.Pair
import kotlin.also
import kotlin.arrayOf
import kotlin.let

object ConvertUtil {
    fun convertSecondsToMinutesAndSeconds(seconds: Int): Pair<Int, Int> {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return Pair(minutes, remainingSeconds)
    }

    @SuppressLint("Recycle")
    fun convertUriToFile(context: Context, uri: Uri): File? {
        val contentResolver: ContentResolver = context.contentResolver

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val fileDescriptor = contentResolver.openFile(uri, "r", null)
            val fileInputStream = FileInputStream(fileDescriptor?.fileDescriptor)
            return convertInputStreamToFile(fileInputStream)
        } else {
            if (uri.scheme == "content") {
                val projection = arrayOf(MediaStore.Images.Media.DATA)
                val cursor: Cursor? = context.contentResolver.query(uri, projection, null, null, null)
                var filePath: kotlin.String? = null
                cursor?.use {
                    if (it.moveToFirst()) {
                        val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                        filePath = it.getString(columnIndex)
                    }
                }

                return filePath?.let { File(it) }
            } else if (uri.scheme == "file") {
                return uri.path?.let { File(it) }
            }
        }
        return null
    }

    private fun convertInputStreamToFile(inputStream: InputStream): File? {
        val tempFile = File.createTempFile(String.valueOf(inputStream.hashCode()), ".jpg")
        tempFile.deleteOnExit()
        copyInputStreamToFile(inputStream, tempFile)
        inputStream.close()
        return tempFile
    }

    private fun copyInputStreamToFile(inputStream: InputStream, file: File) {
        FileOutputStream(file).use { outputStream ->
            var read: Int
            val bytes = ByteArray(1024)
            try {
                while (inputStream.read(bytes).also { read = it } != -1) {
                    outputStream.write(bytes, 0, read)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            outputStream.close()
        }
    }

    public fun getFileSize(file: File) : Float {
        val fileSizeInBytes: Long = file.length()

        val fileSizeInKB = fileSizeInBytes / 1024L
        val fileSizeInMB = fileSizeInKB.toFloat() / 1024f


        println("파일 크기: " + fileSizeInBytes + "," + fileSizeInKB + "," + fileSizeInMB)
        return fileSizeInMB
    }
}