import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import java.io.IOException

class CustomMediaPlayer(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null

    init {
        mediaPlayer = MediaPlayer()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()

            mediaPlayer?.setAudioAttributes(audioAttributes)
        } else {
            mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        }
    }

    fun playWavConVariableFlotante(variableFlotante: Float, resourceId: Int) {
        try {
            val progreso = (variableFlotante * getDuration()).toInt()

            setupMediaPlayer(resourceId)

            mediaPlayer?.setOnPreparedListener {
                mediaPlayer?.seekTo(progreso)
                mediaPlayer?.start()
            }

            mediaPlayer?.prepareAsync()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    fun getDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }

    fun release() {
        mediaPlayer?.release()
    }

    private fun setupMediaPlayer(resourceId: Int) {
        try {
            val packageName = context.resources.getResourcePackageName(resourceId)
            val typeName = context.resources.getResourceTypeName(resourceId)
            val entryName = context.resources.getResourceEntryName(resourceId)

            val resName = if (typeName == "raw") entryName else "raw/$entryName"
            val identifier = context.resources.getIdentifier(resName, typeName, packageName)

            mediaPlayer?.setDataSource(context, Uri.parse("android.resource://$packageName/$identifier"))
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }
}
