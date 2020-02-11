package studio.dipdev.flutter.amazon.s3

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client

import java.io.File
import java.io.UnsupportedEncodingException
import java.util.Locale

class AwsHelper(private val context: Context, private val onUploadCompleteListener: OnUploadCompleteListener,
                private val BUCKET_NAME: String,
                private val IDENTITY_POOL_ID: String,
                private val REGION: String) {

    private var transferUtility: TransferUtility
    private var nameOfUploadedFile: String? = null

    init {
        val credentialsProvider = CognitoCachingCredentialsProvider(context, IDENTITY_POOL_ID, Regions.fromName(REGION))

        val amazonS3Client = AmazonS3Client(credentialsProvider)
        amazonS3Client.setRegion(com.amazonaws.regions.Region.getRegion(Regions.fromName(REGION)))
        transferUtility = TransferUtility(amazonS3Client, context)
    }

    private val uploadedUrl: String
        get() = getUploadedUrl(nameOfUploadedFile)

    private fun getUploadedUrl(key: String?): String {
        return String.format(Locale.getDefault(), URL_TEMPLATE_BUCKET,REGION, BUCKET_NAME, key)
    }

    @Throws(UnsupportedEncodingException::class)
    fun uploadImage(image: File): String {
        val credentialsProvider = CognitoCachingCredentialsProvider(context, IDENTITY_POOL_ID, Regions.fromName(REGION))

        val amazonS3Client = AmazonS3Client(credentialsProvider)
        amazonS3Client.setRegion(com.amazonaws.regions.Region.getRegion(Regions.fromName(REGION)))
        transferUtility = TransferUtility(amazonS3Client, context)

        nameOfUploadedFile = clean(image.name)
        val transferObserver = transferUtility.upload(BUCKET_NAME, nameOfUploadedFile, image)

        transferObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state == TransferState.COMPLETED) {
                    onUploadCompleteListener.onUploadComplete(getUploadedUrl(nameOfUploadedFile))
                }
                if (state == TransferState.FAILED) {
                    onUploadCompleteListener.onFailed()
                }
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {}
            override fun onError(id: Int, ex: Exception) {
                Log.e(TAG, "error in upload id [ " + id + " ] : " + ex.message)
            }
        })
        return uploadedUrl
    }

    @Throws(UnsupportedEncodingException::class)
    fun clean(filePath: String): String {
        return filePath.replace("[^A-Za-z0-9 ]".toRegex(), "")
    }

    interface OnUploadCompleteListener {
        fun onUploadComplete(imageUrl: String)
        fun onFailed()
    }

    companion object {
        private val TAG = AwsHelper::class.java.simpleName
        private const val URL_TEMPLATE = "https://s3.amazonaws.com/%s/%s"
        private const val URL_TEMPLATE_BUCKET = "https://s3-%s.amazonaws.com/%s/%s"
    }
}