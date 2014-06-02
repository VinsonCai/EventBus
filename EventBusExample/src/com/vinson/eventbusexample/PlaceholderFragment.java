/**
 * 
 */
package com.vinson.eventbusexample;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.greenrobot.event.EventBus;

/**
 * @author VinsonCai
 *
 */
public class PlaceholderFragment extends Fragment {

	private static final String TAG = "PlaceholderFragment";

	private EventBus mEventBus;
	BackgroundThread backgroundThread;
	BackgroundThread2 backgroundThread2;

	private TextView mCountView;
	private TextView mBackground2View;

	public PlaceholderFragment() {
		mEventBus = new EventBus();
		mEventBus.register(this);

		backgroundThread = new BackgroundThread();
		backgroundThread.start();

		backgroundThread2 = new BackgroundThread2();
		backgroundThread2.start();
	}

	public void onEventMainThread(CustomEvent1 event1) {
		mCountView.setText("value:" + event1.mConter);
	}

	public void onEventMainThread(CustomEvent2 event2) {
		mBackground2View.setText(event2.mText);
	}

	public void onEventBackgroundThread(CustomEvent2 event2) {
		// mBackground2View.setText("back:" + event1.mConter);
		Log.v(TAG, "back :" + event2.mText);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		mCountView = (TextView) rootView.findViewById(R.id.countView);
		mBackground2View = (TextView) rootView
				.findViewById(R.id.backgroundView);
		return rootView;
	}


	@Override
	public void onStop() {
		super.onStop();
		backgroundThread.stopThread();
		backgroundThread2.stopThread();
	}

	private class BackgroundThread extends Thread {
		private boolean mIsRunning;

		public void run() {
			mIsRunning = true;
			int i = 0;
			while (mIsRunning) {

				mEventBus.post(new CustomEvent1(i++));
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

		public void stopThread() {
			mIsRunning = false;
		}
	}

	private class BackgroundThread2 extends Thread {
		private boolean mIsRunning = false;

		@Override
		public void run() {
			super.run();
			mIsRunning = true;
			int i = 0;
			while (mIsRunning) {

				mEventBus.post(new CustomEvent2(" text:" + (i++)));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public void stopThread() {
			mIsRunning = false;
		}
	}
}
