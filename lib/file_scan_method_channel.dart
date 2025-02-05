import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'file_scan_platform_interface.dart';

/// An implementation of [FileScanPlatform] that uses method channels.
class MethodChannelFileScan extends FileScanPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('file_scan');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
