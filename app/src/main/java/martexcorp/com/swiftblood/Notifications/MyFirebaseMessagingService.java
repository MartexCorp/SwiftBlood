package martexcorp.com.swiftblood.Notifications;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;
import java.util.Map;

import martexcorp.com.swiftblood.BoardRequest.BoardRequest;
import martexcorp.com.swiftblood.BoardRequest.BoardRequestActivity;

import martexcorp.com.swiftblood.MainActivity;
import martexcorp.com.swiftblood.R;
import martexcorp.com.swiftblood.UserProfile.UserProfileActivity;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String APP_PACKAGE = "martexcorp.com.swiftblood";
    private final String REQUEST_CHANNEL_ID = APP_PACKAGE + ".REQUEST_CHANNEL";
    //private final String HELP_CHANNEL_ID = APP_PACKAGE + ".HELP_CHANNEL";
    //private final String DATABASE_CHANNEL_ID = APP_PACKAGE + ".REQUEST_CHANNEL";


    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d("BG LOG","App RECEIVE BACKGROUND NOTIFICATION");
        sendNotification(remoteMessage);


    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param remoteMessage FCM RemoteMessage received.
     */
    private void sendNotification(RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();

        Intent intent = new Intent(this, BoardRequestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);



// this is a my insertion looking for a solution
        int icon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? R.drawable.share_icon: R.mipmap.ic_launcher_round;

        NotificationChannel mChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(REQUEST_CHANNEL_ID, "Requests", NotificationManager.IMPORTANCE_HIGH);
            mChannel.setDescription(getString(R.string.messagingService_channel_description));
            mChannel.setShowBadge(true);
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,REQUEST_CHANNEL_ID)
                .setSmallIcon(icon)
                .setContentTitle(data.get("title"))
                .setContentText(data.get("body"))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setNumber(1)
                .setVibrate(new long[] { 1000, 1000})
                .setFullScreenIntent(pendingIntent,true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setShortcutId("SwiftBlood")
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}


