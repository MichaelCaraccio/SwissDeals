package ch.swissdeals.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

//TODO try with extends IntentService
public class DealDownloaderService extends Service {
    private static final String TAG = DealDownloaderService.class.getSimpleName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        work();
        return super.onStartCommand(intent, flags, startId);
    }

    private void work() {
        // For our recurring task, we'll just display a message
        Toast.makeText(getApplicationContext(), new Date().toString() + " - Je suis couru !", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Je suis couru !");
    }
}
