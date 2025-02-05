import 'package:flutter_test/flutter_test.dart';
import 'package:file_scan/file_scan.dart';
import 'package:file_scan/file_scan_platform_interface.dart';
import 'package:file_scan/file_scan_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockFileScanPlatform
    with MockPlatformInterfaceMixin
    implements FileScanPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final FileScanPlatform initialPlatform = FileScanPlatform.instance;

  test('$MethodChannelFileScan is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelFileScan>());
  });

  test('getPlatformVersion', () async {
    FileScan fileScanPlugin = FileScan();
    MockFileScanPlatform fakePlatform = MockFileScanPlatform();
    FileScanPlatform.instance = fakePlatform;

    expect(await fileScanPlugin.getPlatformVersion(), '42');
  });
}
