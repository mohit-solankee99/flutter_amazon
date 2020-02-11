import Flutter
import UIKit
import AWSS3
import AWSCore

public class SwiftFlutterAmazonS3Plugin: NSObject, FlutterPlugin {
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "flutter_amazon_s3", binaryMessenger: registrar.messenger())
        let instance = SwiftFlutterAmazonS3Plugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
    }
    
    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        if(call.method.elementsEqual("uploadImageToAmazon")){
            let arguments = call.arguments as? NSDictionary
            let imagePath = arguments!["filePath"] as? String
            let bucket = arguments!["bucket"] as? String
            let identity = arguments!["identity"] as? String
            let region = arguments!["region"] as? String

            var imageAmazonUrl = ""
            let fileUrl = NSURL(fileURLWithPath: imagePath!)

            let uploadRequest = AWSS3TransferManagerUploadRequest()
            uploadRequest?.bucket = bucket
            uploadRequest?.key = nameGenerator()
            uploadRequest?.contentType = "image/jpeg"
            uploadRequest?.body = fileUrl as URL
            uploadRequest?.acl = .publicReadWrite

            let credentialsProvider = AWSCognitoCredentialsProvider(
                regionType: region,
                identityPoolId: identity!)
            let configuration = AWSServiceConfiguration(
                region: region,
                credentialsProvider: credentialsProvider)
            AWSServiceManager.default().defaultServiceConfiguration = configuration

            AWSS3TransferManager.default().upload(uploadRequest!).continueWith { (task) -> AnyObject? in
                if let error = task.error {
                    print("❌ Upload failed (\(error))")
                }
                if task.result != nil {
                    imageAmazonUrl = "https://s3.amazonaws.com/\(bucket!)/\(uploadRequest!.key!)"
                    print("✅ Upload successed (\(imageAmazonUrl))")
                } else {
                    print("❌ Unexpected empty result.")
                }
                result(imageAmazonUrl)
                return nil
            }
        }
    }
    
    public func nameGenerator() -> String{
        let date = Date()
        let formatter = DateFormatter()
        formatter.dateFormat = "ddMMyyyy"
        let result = formatter.string(from: date)
        return "IMG" + result + String(Int64(date.timeIntervalSince1970 * 1000)) + "jpeg"
    }
}
