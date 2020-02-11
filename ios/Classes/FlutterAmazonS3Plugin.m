#import "FlutterAmazonS3Plugin.h"
#import <flutter_amazon_s3/flutter_amazon_s3-Swift.h>

@implementation FlutterAmazonS3Plugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterAmazonS3Plugin registerWithRegistrar:registrar];
}
@end
