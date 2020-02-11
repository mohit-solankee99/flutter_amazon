# flutter_amazon_s3

Amazon S3 plugin for Flutter

Unofficial Amazon S3 plugin written in Dart for Flutter.

## Usage
To use this plugin, add `flutter_amazon_s3` as a [dependency in your pubspec.yaml file](https://flutter.io/platform-plugins/).


```yaml
dependencies:
  flutter_amazon_s3: '^0.1.2'
```

### Example


``` dart
import 'package:flutter_amazon_s3/flutter_amazon_s3.dart';

String uploadedImageUrl = await FlutterAmazonS3.uploadImage(
          _image.path, BUCKET_NAME, IDENTITY_POOL_ID, REGION);
```
          
## Installation


### Android

No configuration required - the plugin should work out of the box.          


### iOS

No configuration required - the plugin should work out of the box.          

### Authors
```
Android version written by Tony Darko
IOS version written by Vladislav Blago
```