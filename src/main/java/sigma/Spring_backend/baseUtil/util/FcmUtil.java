package sigma.Spring_backend.baseUtil.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;

@Component
public class FcmUtil {
    public void send_FCM(String tokenId, String title, String content){
        try{
            FileInputStream refreshToken = new FileInputStream("C:\\Users\\MinJi\\IdeaProjects\\backend\\src\\main\\resources\\fcm\\sigma-cd079-firebase-adminsdk-9n4di-50a8df2e1c.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(refreshToken))
                    .build();

            if(FirebaseApp.getApps().isEmpty()){
                FirebaseApp.initializeApp(options);
            }

            String registrationToken = tokenId;

            Message msg = Message.builder()
                    .setAndroidConfig(AndroidConfig.builder()
                            .setTtl(3600*1000)
                            .setPriority(AndroidConfig.Priority.NORMAL)
                            .setNotification(AndroidNotification.builder()
                                    .setTitle(title)
                                    .setBody(content)
                                    .setColor("#6667AB")
                                    .build())
                            .build())
                    .setToken(registrationToken)
                    .build();

            String response = FirebaseMessaging.getInstance().send(msg);

            System.out.println("Success message:" + response);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

