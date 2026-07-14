package com.tintina.vocesenelaire

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.android.material.navigation.NavigationView
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors

class MainActivity : AppCompatActivity() {

    private var controller: MediaController? = null
    private lateinit var controllerFuture: ListenableFuture<MediaController>

    private lateinit var btnPlayPause: ImageButton
    private lateinit var tvStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1001)
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.drawer_open, R.string.drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        findViewById<NavigationView>(R.id.navView).setNavigationItemSelectedListener { item ->
            onDrawerItemSelected(item, drawerLayout)
        }

        btnPlayPause = findViewById(R.id.btnPlayPause)
        tvStatus = findViewById(R.id.tvStatus)

        findViewById<android.widget.Button>(R.id.btnMessage).setOnClickListener {
            startActivity(Intent(this, MessageActivity::class.java))
        }

        btnPlayPause.setOnClickListener {
            controller?.let { if (it.isPlaying) it.pause() else it.play() }
        }
    }

    override fun onStart() {
        super.onStart()
        val sessionToken = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture.addListener({
            controller = controllerFuture.get()
            controller?.addListener(playerListener)
            updateUi()
        }, MoreExecutors.directExecutor())
    }

    override fun onStop() {
        MediaController.releaseFuture(controllerFuture)
        controller = null
        super.onStop()
    }

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) = updateUi()
        override fun onPlaybackStateChanged(playbackState: Int) = updateUi()
    }

    private fun updateUi() {
        val playing = controller?.isPlaying == true
        btnPlayPause.setImageResource(
            if (playing) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play
        )
        tvStatus.text = if (playing) "● EN VIVO" else "Pausado"
    }

    private fun onDrawerItemSelected(item: MenuItem, drawerLayout: DrawerLayout): Boolean {
        when (item.itemId) {
            R.id.nav_about -> AlertDialog.Builder(this)
                .setTitle("Quiénes somos")
                .setMessage(Constants.ABOUT_TEXT)
                .setPositiveButton("Cerrar", null)
                .show()
            R.id.nav_facebook -> openUrl(Constants.FACEBOOK_URL)
            R.id.nav_instagram -> openUrl(Constants.INSTAGRAM_URL)
            R.id.nav_x -> openUrl(Constants.X_URL)
            R.id.nav_whatsapp -> openUrl("https://wa.me/${Constants.WHATSAPP_NUMBER}")
            R.id.nav_website -> openUrl(Constants.WEBSITE_URL)
        }
        drawerLayout.closeDrawers()
        return true
    }

    private fun openUrl(url: String) {
        if (url.isBlank()) return
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}
