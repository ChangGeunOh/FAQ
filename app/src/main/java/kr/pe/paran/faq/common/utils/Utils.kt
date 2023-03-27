package kr.pe.paran.faq.common.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.FileProvider
import java.io.File

object Utils {

    fun shareFile(context: Context, file: File) {
        val dataType = "text/plain"
        val attachmentUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        val intent = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TITLE, "작업지시서 입니다.")
            type = dataType
            putExtra(Intent.EXTRA_STREAM, attachmentUri)
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }, null)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        @Suppress("DEPRECATION")
        val resInfoList = context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        resInfoList.forEach { resolveInfo->
            val packageName = resolveInfo.activityInfo.packageName
            context.grantUriPermission(packageName, attachmentUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(intent)
    }

}