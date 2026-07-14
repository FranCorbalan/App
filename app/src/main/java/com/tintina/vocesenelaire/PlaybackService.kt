package com.tintina.vocesenelaire

import android.app.PendingIntent
import android.content.Intent
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

/**
 * Servicio en primer plano que mantiene el stream sonando aunque
 * el usuario minimice la app o bloquee el celular (como cualquier
 * app de radio real). Usa Media3, la librería moderna de Google
 * para audio/video, reemplazo de la vieja MediaPlayer.
 */
class PlaybackService : MediaSessionService() {

    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()

        val player = ExoPlayer.Builder(this).build()

        val metadata = MediaMetadata.Builder()
            .setTitle(Constants.RADIO_NAME)
            .setArtist(Constants.RADIO_SLOGAN)
            .build()

        val mediaItem = MediaItem.Builder()
            .setUri(Constants.STREAM_URL)
            .setMediaMetadata(metadata)
            .setLiveConfiguration(MediaItem.LiveConfiguration.Builder().build())
            .build()

        player.setMediaItem(mediaItem)
        player.prepare()

        // Al tocar la notificación, vuelve a abrir la app
        val sessionActivityIntent = Intent(this, MainActivity::class.java)
        val sessionActivityPendingIntent = PendingIntent.getActivity(
            this, 0, sessionActivityIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        mediaSession = MediaSession.Builder(this, player)
            .setSessionActivity(sessionActivityPendingIntent)
            .build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
}
