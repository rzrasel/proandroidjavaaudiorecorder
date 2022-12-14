/**
 * Copyright 2017 Rz Rasel (Rz Rasel Technology)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rzrasel.proandroidjavaaudiorecorder;

import com.rzrasel.proandroidjavaaudiorecorder.recorder.Pcm;
import com.rzrasel.proandroidjavaaudiorecorder.recorder.PullTransport;
import com.rzrasel.proandroidjavaaudiorecorder.recorder.Recorder;
import com.rzrasel.proandroidjavaaudiorecorder.recorder.Wav;

import java.io.File;

/**
 * Essential APIs for working with ProRecorder.
 *
 * @author Rz Rasel
 * @date 31-07-2016
 */
public final class ProRecorder {
  private ProRecorder() {
  }

  public static Recorder pcm(PullTransport pullTransport, File file) {
    return new Pcm(pullTransport, file);
  }

  public static Recorder wav(PullTransport pullTransport, File file) {
    return new Wav(pullTransport, file);
  }
}
