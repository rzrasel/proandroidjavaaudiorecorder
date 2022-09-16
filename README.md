# proandroidjavaaudiorecorder
Awesome Android Java Audio Recorder

[![](https://jitpack.io/v/rzrasel/proandroidjavaaudiorecorder.svg)](https://jitpack.io/#rzrasel/proandroidjavaaudiorecorder)
[![Rz Rasel](https://img.shields.io/badge/Rz%20Rasel-ProRecorder-orange.svg?style=flat)](https://github.com/rzrasel/proandroidjavaaudiorecorder)


Download
--------
Steep 1:

    allprojects { 
        repositories { 
            ... 
            maven { url "https://jitpack.io" } 
        } 
    }

Steep 2:

    dependencies { 
        implementation "com.github.rzrasel:proandroidjavaaudiorecorder:0.0.1"
    }

A Simple Pcm / Wav audio recorder with nice api.

* Record Pcm audio
* Record Wav audio
* Configure audio source to have desired output
* Record with pause / resume feature

<a href='https://play.google.com/store/apps/details?id=com.kingbull.com.com.omrecoder.omrecorder&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play'  height="80" src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'/></a>

Add these permissions into your `AndroidManifest.xml` and [request for them in Android 6.0+](https://developer.android.com/training/permissions/requesting.html)

``` xml
<uses-permission android:name="android.permission.RECORD_AUDIO"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```

``` java

  recorder = OmRecorder.wav(
        new PullTransport.Default(mic(), new PullTransport.OnAudioChunkPulledListener() {
          @Override public void onAudioChunkPulled(AudioChunk audioChunk) {
            animateVoice((float) (audioChunk.maxAmplitude() / 200.0));
          }
        }), file());
```   
__For Skip Silence__
``` java
  // FOR SKIP SILENCE     
 recorder = OmRecorder.wav(
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
                Toast.makeText(WavRecorderActivity.this, "silence of " + silenceTime + " detected",
                    Toast.LENGTH_SHORT).show();
              }
            }, 200
        ), file()
    );
      
 @NonNull private File file() {
     ContextWrapper contextWrapper = new ContextWrapper(this);
     return new File(contextWrapper.getExternalMediaDirs()[0], "demo.wav");
  }
  
```
__Configure Audio Source__
``` java
  return new PullableSource.Default(
         new AudioRecordConfig.Default(
             MediaRecorder.AudioSource.MIC, AudioFormat.ENCODING_PCM_16BIT,
             AudioFormat.CHANNEL_IN_MONO, 44100
         )
     );
     
   To Enable NoiseSuppresor (android.media.audiofx.NoiseSuppressor)
   
   return new PullableSource.NoiseSuppressor(
          new PullableSource.Default(
              new AudioRecordConfig.Default(
                  MediaRecorder.AudioSource.MIC, AudioFormat.ENCODING_PCM_16BIT,
                  AudioFormat.CHANNEL_IN_MONO, 44100
              )
          )
      );   
      
   To Enable AutomaticGainControl (android.media.audiofx.AutomaticGainControl)
   
   return new PullableSource.AutomaticGainControl(
            new PullableSource.Default(
                new AudioRecordConfig.Default(
                    MediaRecorder.AudioSource.MIC, AudioFormat.ENCODING_PCM_16BIT,
                    AudioFormat.CHANNEL_IN_MONO, 44100
                )
            )
        );    
        
   and if you want both NoiseSuppressor & AutomaticGainControl :-
   
   return new PullableSource.AutomaticGainControl(
           new PullableSource.NoiseSuppressor(
               new PullableSource.Default(
                   new AudioRecordConfig.Default(
                       MediaRecorder.AudioSource.MIC, AudioFormat.ENCODING_PCM_16BIT,
                       AudioFormat.CHANNEL_IN_MONO, 44100
                   )
               )
           )
       );
       
```
__Start & Stop Recording__
``` java
    recorder.startRecording();
    recorder.stopRecording();
```
__Pause & Resume Recording__
``` java
    recorder.pauseRecording();
    recorder.resumeRecording();
```

For documentation and additional information see [the website][1].