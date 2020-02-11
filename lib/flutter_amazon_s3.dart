import 'dart:async';

import 'package:flutter/services.dart';

class FlutterAmazonS3 {
  static const MethodChannel _channel =
      const MethodChannel('flutter_amazon_s3');

  static Future<String> uploadImage(
      String filepath, String bucket, String identity, String region) async {
    final Map<String, dynamic> params = <String, dynamic>{
      'filePath': filepath,
      'bucket': bucket,
      'identity': identity,
      'region': region,
    };
    final String imagePath =
        await _channel.invokeMethod('uploadImageToAmazon', params);
    return imagePath;
  }
}
