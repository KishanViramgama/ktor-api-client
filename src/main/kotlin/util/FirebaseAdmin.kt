package util

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.StorageClient
import java.io.File

object FirebaseAdmin {

    private var initialized = false

    fun init(firebaseConfigPath: String) {
        if (!initialized) {
            val serviceAccount = File(firebaseConfigPath).inputStream()

            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("your-bucket-name.appspot.com")  // ✅ your Firebase storage bucket
                .build()

            FirebaseApp.initializeApp(options)
            initialized = true
        }
    }

    fun getStorage() = StorageClient.getInstance().bucket()
}