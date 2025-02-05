# File Scan Plugin for Flutter

A Flutter plugin to update the Android file system using the `MediaScannerConnection` API. This plugin helps detect newly added, modified, or deleted files in the file system, making them visible to apps like Gallery, File Manager, etc.

## Features

- Update the Android file system for a specific file or directory.
- Trigger media scan using Android's `MediaScannerConnection`.

## Getting Started

### Installation

Add the following to your `pubspec.yaml`:

```yaml
dependencies:
  file_scan: 1.0.3
```

Run `flutter pub get` to install the package.

### Usage

#### Import the plugin:

```dart
import 'package:file_scan_plugin/file_scan.dart';
```

#### Add a File to the File System:

```dart
await FileScan.addFile(filePath: '/path/to/your/file_or_directory');
```

### Example

```dart
import 'package:flutter/material.dart';
import 'package:file_scan_plugin/file_scan.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: Text('File Scan Plugin Example')),
        body: Center(
          child: ElevatedButton(
            onPressed: () async {
              await FileScan.addFile(filePath: '/storage/emulated/0/Download/sample.jpg');
            },
            child: Text('Add File to File System'),
          ),
        ),
      ),
    );
  }
}
```

## Platform Support

- ✅ Android

> **Note:** This plugin currently supports Android only. It uses the `MediaScannerConnection` API under the hood.

## Android Configuration

Ensure your `AndroidManifest.xml` has the following permissions:

```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

If targeting Android 10 (API level 29) or higher, consider using `requestLegacyExternalStorage` in your `AndroidManifest.xml`:

```xml
<application
    android:requestLegacyExternalStorage="true"
    ... >
</application>
```

## Plugin Structure

- **Dart Side:** Handles the method channel communication.
- **Android Side:** Uses `MediaScannerConnection` to update the media library.

### Dart Code (`FileScan`):

```dart
static Future<void> addFile({required String filePath}) async {
  await const MethodChannel('com.filescan/refresh').invokeMethod('refresh', {'path': filePath});
}
```

### Android Code (`FileScanPlugin`):

```kotlin
MediaScannerConnection.scanFile(context, arrayOf(path), null,
  MediaScannerConnection.OnScanCompletedListener { scannedPath, uri ->
    if (uri != null) {
      result.success(mapOf("path" to scannedPath, "uri" to uri.toString()))
    } else {
      result.error("SCAN_FAILED", "File scan failed for: $path", null)
    }
  })
```

## Error Handling

The plugin handles common errors like:

- `INVALID_ARGUMENT`: When the provided path is null.
- `FILE_NOT_FOUND`: When the file doesn't exist.
- `SCAN_FAILED`: When the media scan fails.

Handle exceptions in your Dart code:

```dart
try {
  await FileScan.addFile(filePath: '/invalid/path');
} catch (e) {
  print('Error: $e');
}
```

## Contributing

Contributions are welcome! Please open issues and pull requests for any improvements.

## License

MIT License  
Copyright (c) 2025 we.viavek

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is furnished
to do the same, subject to the following conditions.

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

