package com.sibaamap.foregroundservice;

import static com.sibaamap.foregroundservice.MyApplication.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    private MediaPlayer mediaPlayer;


    @Override
    public void onCreate() {
        super.onCreate();

        Log.e("huyanh","My Service onCreate");

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle bundle = intent.getExtras();
        if(bundle != null){
            Song song = (Song) bundle.get("object_song");

            if(song != null){
                startMusic(song);

                sendNotification(song);
            }
        }


        return START_NOT_STICKY;
    }

    private void startMusic(Song song) {
        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(getApplicationContext(),song.getResource());
        }
        mediaPlayer.start();
    }

    private void sendNotification(Song song) {

        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),song.getImage());

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_customniitification);
        remoteViews.setTextViewText(R.id.tv_title_song,song.getTitle());
        remoteViews.setTextViewText(R.id.tv_single_song,song.getSingle());
        remoteViews.setImageViewBitmap(R.id.img_song,bitmap);

        remoteViews.setImageViewResource(R.id.img_play_or_pause,R.drawable.baseline_pause_black_36);



        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.bell)
                .setContentIntent(pendingIntent)
                .setCustomContentView(remoteViews)
                .setSound(null)
                .build();

        startForeground(1,notification);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();Log.e("huyanh","My Service onDestroy");
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
