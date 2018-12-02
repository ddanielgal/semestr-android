package hu.danielgaldev.semestr;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import hu.danielgaldev.semestr.model.pojo.Requirement;
import hu.danielgaldev.semestr.model.pojo.Subject;

public class StudyNotificationService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        requirementName = intent.getStringExtra("requirementName");
        subjectName = intent.getStringExtra("subjectName");
        return START_REDELIVER_INTENT;
    }

    private String requirementName;
    private String subjectName;

    @Override
    public void onCreate() {
        Intent intent = new Intent(this, RequirementsActivity.class);
        long[] pattern = {0, 300, 0};
        PendingIntent pi = PendingIntent.getActivity(this, 1, intent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.baseline_library_books_black_36dp)
                .setContentTitle(getString(R.string.task_coming_up))
                .setContentText(new StringBuilder()
                    .append(requirementName)
                    .append(getString(R.string.within_3_days)))
                .setVibrate(pattern)
                .setAutoCancel(true);

        mBuilder.setContentIntent(pi);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }
}
