package com.github.zagum.speechrecognitionview;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.zagum.speechrecognitionview.RecognitionProgressView;

public class MainActivity extends AppCompatActivity implements RecognitionListener {

	private static final String TAG = "MainActivity";

	private SpeechRecognizer speechRecognizer;
	private int streamVolume;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		int[] colors = {
				getResources().getColor(R.color.color1),
				getResources().getColor(R.color.color2),
				getResources().getColor(R.color.color3),
				getResources().getColor(R.color.color4),
				getResources().getColor(R.color.color5)
		};

		int[] heights = {60, 76, 44, 80, 55};

		speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
		speechRecognizer.setRecognitionListener(this);

		AudioManager amanager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);

		final RecognitionProgressView recognitionProgressView = (RecognitionProgressView) findViewById(R.id.recognition_view);
		recognitionProgressView.setSpeechRecognizer(speechRecognizer);
		recognitionProgressView.setRecognitionListener(this);
		recognitionProgressView.setColors(colors);
		recognitionProgressView.setBarMaxHeightsInDp(heights);
		recognitionProgressView.play();

		Button listen = (Button) findViewById(R.id.listen);
		Button reset = (Button) findViewById(R.id.reset);

		listen.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startRecognition();
				recognitionProgressView.postDelayed(new Runnable() {
					@Override
					public void run() {
						startRecognition();
					}
				}, 100);
				startRecognition();
				AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
				streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
			}
		});

		reset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				recognitionProgressView.stop();
				recognitionProgressView.play();
			}
		});
	}

	private void startRecognition() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		speechRecognizer.startListening(intent);
	}

	@Override
	public void onReadyForSpeech(Bundle params) {
		Log.d(TAG, "onReadyForSpeech() called with: " + "params = [" + params + "]");
	}

	@Override
	public void onBeginningOfSpeech() {
		Log.d(TAG, "onBeginningOfSpeech() called with: " + "");
	}

	@Override
	public void onRmsChanged(float rmsdB) {
		Log.d(TAG, "onRmsChanged() called with: " + "rmsdB = [" + rmsdB + "]");
	}

	@Override
	public void onBufferReceived(byte[] buffer) {
		Log.d(TAG, "onBufferReceived() called with: " + "buffer = [" + buffer + "]");
	}

	@Override
	public void onEndOfSpeech() {
		Log.d(TAG, "onEndOfSpeech() called with: " + "");
	}

	@Override
	public void onError(int error) {
		Log.d(TAG, "onError() called with: " + "error = [" + error + "]");
	}

	@Override
	public void onResults(Bundle results) {
		AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, streamVolume, 0);
		Log.d(TAG, "onResults() called with: " + "results = [" + results + "]");
	}

	@Override
	public void onPartialResults(Bundle partialResults) {
		Log.d(TAG, "onPartialResults() called with: " + "partialResults = [" + partialResults + "]");
	}

	@Override
	public void onEvent(int eventType, Bundle params) {
		Log.d(TAG, "onEvent() called with: " + "eventType = [" + eventType + "], params = [" + params + "]");
	}
}