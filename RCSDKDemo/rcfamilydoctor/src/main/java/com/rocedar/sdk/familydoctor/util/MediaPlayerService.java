package com.rocedar.sdk.familydoctor.util;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.rocedar.lib.base.unit.RCLog;

import java.io.IOException;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/6/6 上午11:02
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class MediaPlayerService extends Service {
    public static final String BROADCAST_TO_SERVICE = "com.rocedar.lib.sdk.playerfunction";
    public static final String SERVICE_TO_ACTIVITY = "com.rocedar.lib.sdk..currentPlayerStatus";
    public static final String PLAYER_FUNCTION_TYPE = "playerfunction";
    public static final String PLAYER_TRACK_URL = "trackURL";
    public static final int PLAY_MEDIA_PLAYER = 1;
    public static final int PAUSE_MEDIA_PLAYER = 2;
    public static final int RESUME_MEDIA_PLAYER = 3;
    public static final int STOP_MEDIA_PLAYER = 4;
    public static final int CHANGE_PLAYER_TRACK = 5;
    public static final int PLAY_ERROR = 6;
    public static final int PLAY_PROGRESS = 7;
    public static final String PLAYER_STATUS_KEY = "PlayerCurrentStatus";
    public static final String PLAYER_PROGRESS_KEY = "PlayerProgressStatus";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter intentFilter = new IntentFilter(BROADCAST_TO_SERVICE);
        registerReceiver(playerReceiver, intentFilter);
        if (mPlayer != null && mPlayer.isPlaying()) {
            sendPlayerStatus(STOP_MEDIA_PLAYER);
        }
        return START_STICKY;
    }

    private BroadcastReceiver playerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BROADCAST_TO_SERVICE.equalsIgnoreCase(action)) {
                String trackURL = intent.hasExtra(PLAYER_TRACK_URL) ? intent.getStringExtra(PLAYER_TRACK_URL) : "";
                int function = intent.getIntExtra(PLAYER_FUNCTION_TYPE, 0);
                switch (function) {
                    case CHANGE_PLAYER_TRACK:
                        changeTrack(trackURL);
                        break;
                    case STOP_MEDIA_PLAYER:
                        stopPlayer();
                        break;
                    case PLAY_MEDIA_PLAYER:
                        startMediaPlayer(trackURL);
                        break;
                    case PAUSE_MEDIA_PLAYER:
                        pausePlayer();
                        break;
                    case RESUME_MEDIA_PLAYER:
                        resumePlayer();
                        break;
                }

            }
        }
    };
    private MediaPlayer mPlayer;

    private void pausePlayer() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            sendPlayerStatus(PAUSE_MEDIA_PLAYER);
        }
        isRun = false;
    }

    private void resumePlayer() {
        if (mPlayer != null && !mPlayer.isPlaying()) {
            mPlayer.start();
            sendPlayerStatus(RESUME_MEDIA_PLAYER);
        }
        isRun = true;
    }

    private void changeTrack(String url) {
        stopPlayer();
        startMediaPlayer(url);

    }

    private void stopPlayer() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            sendPlayerStatus(STOP_MEDIA_PLAYER);
        }
        isRun = false;
    }

    public void startMediaPlayer(String url) {
        if (TextUtils.isEmpty(url))
            return;
        if (mPlayer == null)
            mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(url);
            mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    if (extra == MediaPlayer.MEDIA_ERROR_SERVER_DIED
                            || extra == MediaPlayer.MEDIA_ERROR_MALFORMED) {
                        sendPlayerStatus(PLAY_ERROR);
                    } else if (extra == MediaPlayer.MEDIA_ERROR_IO) {
                        sendPlayerStatus(PLAY_ERROR);
                        return false;
                    }
                    return false;
                }
            });
            mPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    showProgress();
//                    sendPlayerStatus(PLAY_PROGRESS, 100 * mp.getCurrentPosition() / mp.getDuration());
                }
            });
            mPlayer.prepareAsync();
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {
                    mPlayer.start();
                    sendPlayerStatus(PLAY_MEDIA_PLAYER);
                    isRun = true;
                }
            });
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayer();
                }
            });
            mPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    return false;
                }
            });
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendPlayerStatus(int status) {
        Intent intent = new Intent();
        intent.setAction(SERVICE_TO_ACTIVITY);
        intent.putExtra(PLAYER_STATUS_KEY, status);
        sendBroadcast(intent);
    }

    private void sendPlayerStatus(int status, int progress) {
        RCLog.i("播放进度-" + progress);
        Intent intent = new Intent();
        intent.setAction(SERVICE_TO_ACTIVITY);
        intent.putExtra(PLAYER_STATUS_KEY, status);
        intent.putExtra(PLAYER_PROGRESS_KEY, progress);
        sendBroadcast(intent);
    }

    private boolean isRun = false;

    private void showProgress() {
        //连接之后启动子线程设置当前进度
        new Thread() {
            @Override
            public void run() {
                //改变当前进度条的值
                //设置当前进度
                while (isRun) {
                    try {
                        if (mPlayer != null) {
                            if (!mPlayer.isPlaying()) {
                                break;
                            }
                            sendPlayerStatus(PLAY_PROGRESS,
                                    100 * mPlayer.getCurrentPosition() / mPlayer.getDuration());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SystemClock.sleep(1000);

                }
            }

        }.start();

    }

}
