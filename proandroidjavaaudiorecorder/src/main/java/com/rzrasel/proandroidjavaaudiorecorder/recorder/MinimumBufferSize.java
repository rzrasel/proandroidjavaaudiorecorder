package com.rzrasel.proandroidjavaaudiorecorder.recorder;

import android.media.AudioRecord;

/**
 * @author Rz Rasel
 * @date 01 July, 2017 12:34 PM
 */
final class MinimumBufferSize {
  private final int minimumBufferSize;

  MinimumBufferSize(AudioRecordConfig audioRecordConfig) {
    this.minimumBufferSize = AudioRecord.getMinBufferSize(audioRecordConfig.frequency(),
        audioRecordConfig.channelPositionMask(), audioRecordConfig.audioEncoding());
  }

  int asInt() {
    return minimumBufferSize;
  }
}
