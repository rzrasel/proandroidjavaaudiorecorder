package com.rzrasel.proandroidjavaaudiorecorderusages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContextWrapper;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.rzrasel.proandroidjavaaudiorecorder.ProRecorder;
import com.rzrasel.proandroidjavaaudiorecorder.recorder.AudioChunk;
import com.rzrasel.proandroidjavaaudiorecorder.recorder.AudioRecordConfig;
import com.rzrasel.proandroidjavaaudiorecorder.recorder.PullTransport;
import com.rzrasel.proandroidjavaaudiorecorder.recorder.PullableSource;
import com.rzrasel.proandroidjavaaudiorecorder.recorder.Recorder;
import com.rzrasel.proandroidjavaaudiorecorder.recorder.WriteAction;

import java.io.File;
import java.io.IOException;

public class PcmRecorderActivity extends AppCompatActivity {
    Recorder recorder;
    ImageView recordButton;
    CheckBox skipSilence;
    Button pauseResumeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pcm_recorder);
        getSupportActionBar().setTitle("Pcm Recorder");
        setupRecorder();
        skipSilence = (CheckBox) findViewById(R.id.skipSilence);
        skipSilence.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    setupNoiseRecorder();
                } else {
                    setupRecorder();
                }
            }
        });

        recordButton = (ImageView) findViewById(R.id.recordButton);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                recorder.startRecording();
                skipSilence.setEnabled(false);
            }
        });
        findViewById(R.id.stopButton).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                try {
                    recorder.stopRecording();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                animateVoice(0);
                skipSilence.setEnabled(true);
            }
        });
        pauseResumeButton = (Button) findViewById(R.id.pauseResumeButton);
        pauseResumeButton.setOnClickListener(new View.OnClickListener() {
            boolean isPaused = false;

            @Override public void onClick(View view) {
                if (recorder == null) {
                    Toast.makeText(PcmRecorderActivity.this, "Please start recording first!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isPaused) {
                    pauseResumeButton.setText(getString(R.string.resume_recording));
                    recorder.pauseRecording();
                    pauseResumeButton.postDelayed(new Runnable() {
                        @Override public void run() {
                            animateVoice(0);
                        }
                    }, 100);
                } else {
                    pauseResumeButton.setText(getString(R.string.pause_recording));
                    recorder.resumeRecording();
                }
                isPaused = !isPaused;
            }
        });
    }
    private void setupRecorder() {
        recorder = ProRecorder.pcm(
                new PullTransport.Default(mic(), new PullTransport.OnAudioChunkPulledListener() {
                    @Override public void onAudioChunkPulled(AudioChunk audioChunk) {
                        animateVoice((float) (audioChunk.maxAmplitude() / 200.0));
                    }
                }), file());
    }

    private void setupNoiseRecorder() {
        recorder = ProRecorder.pcm(
                new PullTransport.Noise(mic(),
                        new PullTransport.OnAudioChunkPulledListener() {
                            @Override public void onAudioChunkPulled(AudioChunk audioChunk) {
                                animateVoice((float) (audioChunk.maxAmplitude() / 200.0));
                            }
                        },
                        new WriteAction.Default(),
                        new Recorder.OnSilenceListener() {
                            @Override public void onSilence(long silenceTime) {
                                Log.e("silenceTime", String.valueOf(silenceTime));
                                Toast.makeText(PcmRecorderActivity.this, "silence of " + silenceTime + " detected",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }, 200
                ), file()
        );
    }

    private void animateVoice(final float maxPeak) {
        recordButton.animate().scaleX(1 + maxPeak).scaleY(1 + maxPeak).setDuration(10).start();
    }

    private PullableSource mic() {
        return new PullableSource.Default(
                new AudioRecordConfig.Default(
                        MediaRecorder.AudioSource.MIC, AudioFormat.ENCODING_PCM_16BIT,
                        AudioFormat.CHANNEL_IN_MONO, 44100
                )
        );
    }

    @NonNull
    private File file() {
        ContextWrapper contextWrapper = new ContextWrapper(this);
        return new File(contextWrapper.getExternalMediaDirs()[0], "kailashdabhi.pcm");
    }
}