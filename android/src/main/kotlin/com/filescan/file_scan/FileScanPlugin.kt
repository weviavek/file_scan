package com.filescan.file_scan

import android.content.Context
import android.media.MediaScannerConnection
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** FileScanPlugin */
class FileScanPlugin: FlutterPlugin, MethodCallHandler {

  private lateinit var channel: MethodChannel
  private lateinit var context: Context  // Store the context

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "file_scan")
    channel.setMethodCallHandler(this)
    context = flutterPluginBinding.applicationContext // Initialize context

    // Separate channel for refresh (if needed)
    val refreshChannel = MethodChannel(flutterPluginBinding.binaryMessenger, "com.filescan/refresh")
    refreshChannel.setMethodCallHandler { call, result ->
      if (call.method == "refresh") {
        val path = call.argument<String>("path")
        if (path != null) {
          scanFile(path, result) // Use the helper function
        } else {
          result.error("INVALID_ARGUMENT", "Path is null", null)
        }
      } else {
        result.notImplemented()
      }
    }
  }


  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    } else if (call.method == "scanFile") { // Directly handle scanFile
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

  private fun scanFile(path: String, result: Result) {
    MediaScannerConnection.scanFile(context, arrayOf(path), null,
      MediaScannerConnection.OnScanCompletedListener { path, uri ->
        if (uri != null) {
          result.success("File scanned: $path, URI: $uri")
        } else {
          result.error("SCAN_FAILED", "File scan failed for: $path", null)
        }
      })
  }


  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}