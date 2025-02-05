import 'dart:io';
import 'package:flutter/services.dart';

/// A utility class to trigger file system updates on Android devices.
///
/// This class interacts with the Android `MediaScannerConnection` to ensure
/// that newly added or modified files are recognized by media-related apps
/// like Gallery and File Manager.
class FileScan {
  /// Adds a file or directory to the Android file system, making it visible
  /// to media applications.
  ///
  /// The [filePath] parameter should be the absolute path of the file or
  /// directory you want to add.
  ///
  /// Throws an exception if the operation fails.
  static Future<void> addFile({required String filePath}) async {
    try {
      if (Platform.isAndroid) {
        await const MethodChannel('com.filescan/refresh')
            .invokeMethod('refresh', {'path': filePath});
      }
    } catch (e) {
      rethrow;
    }
  }
}
