import 'dart:io';

import 'package:flutter/services.dart';

class FileScan {
  static Future<void> addFile({required String filePath}) async {
    try {
      if (Platform.isAndroid) await const MethodChannel('com.filescan/refresh').invokeMethod('refresh', {'path': filePath});
    } catch (e) {
      rethrow;
    }
  }
}
