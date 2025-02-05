package com.filescan.file_scan

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.io.File

class FileScanPlugin: FlutterPlugin, MethodCallHandler {

  private lateinit var channel: MethodChannel
  private lateinit var context: Context

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "file_scan")
    channel.setMethodCallHandler(this)
    context = flutterPluginBinding.applicationContext

    // Separate channel for refresh (optional, but good practice)
    val refreshChannel = MethodChannel(flutterPluginBinding.binaryMessenger, "com.filescan/refresh")
    refreshChannel.setMethodCallHandler { call, result ->
      if (call.method == "refresh") {
        val path = call.argument<String>("path")
        if (path != null) {
          scanFile(path, result)
        } else {
          result.error("INVALID_ARGUMENT", "Path is null", null)
        }
      } else {
        result.notImplemented()
      }
    }
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    when (call.method) {
      "getPlatformVersion" -> result.success("Android ${android.os.Build.VERSION.RELEASE}")
      "scanFile" -> {
        val path = call.argument<String>("path")
        if (path != null) {
          scanFile(path, result)
        } else {
          result.error("INVALID_ARGUMENT", "Path is null", null)
        }
      }
      else -> result.notImplemented()
    }
  }

  private fun scanFile(path: String, result: Result) {
    val file = File(path)
    if (!file.exists()) {
      result.error("FILE_NOT_FOUND", "File not found: $path", null)
      return
    }

    MediaScannerConnection.scanFile(context, arrayOf(path), null,
      MediaScannerConnection.OnScanCompletedListener { scannedPath, uri ->
        if (uri != null) {
          result.success(mapOf("path" to scannedPath, "uri" to uri.toString())) // Return a map
        } else {
          // Check if it was a directory
          if (file.isDirectory) {
            result.success(mapOf("path" to scannedPath, "uri" to null)) // Return success even if directory
          } else {
            result.error("SCAN_FAILED", "File scan failed for: $path", null)
          }
        }
      })
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}