import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'file_scan_method_channel.dart';

abstract class FileScanPlatform extends PlatformInterface {
  /// Constructs a FileScanPlatform.
  FileScanPlatform() : super(token: _token);

  static final Object _token = Object();

  static FileScanPlatform _instance = MethodChannelFileScan();

  /// The default instance of [FileScanPlatform] to use.
  ///
  /// Defaults to [MethodChannelFileScan].
  static FileScanPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [FileScanPlatform] when
  /// they register themselves.
  static set instance(FileScanPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
