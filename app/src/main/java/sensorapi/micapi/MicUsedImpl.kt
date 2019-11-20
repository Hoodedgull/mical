package sensorapi.micapi

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.core.app.ActivityCompat
import kotlin.random.Random

class MicUsedImpl : MicAlerterInterface {
    override fun isMicBeingUsed(): IsMicInUseResult {


      if(!validateMicAvailability()){
          return IsMicInUseResult(true,"noapp")
      }else {
          return IsMicInUseResult(false,"")
      }
    }



    fun validateMicAvailability() : Boolean{
        var available = true;
        val recorder = AudioRecord(
            MediaRecorder.AudioSource.MIC, 44100,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_DEFAULT, 44100);
        try{
            if(recorder.recordingState != AudioRecord.RECORDSTATE_STOPPED ){
                available = false;

            }
            recorder.startRecording();
            if(recorder.recordingState != AudioRecord.RECORDSTATE_RECORDING){
                recorder.stop();
                available = false;

            }
            recorder.stop();
        } finally{
            recorder.release();
        }

        return available;
    }
}