package com.jcodecraeer.numberrotate3danimation;

 
import com.example.rotatedemo.R;
import com.jcodecraeer.numberrotate3danimation.NumberRotate3DAnimation.InterpolatedTimeListener;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author www.jcodecraeer.com
 * 
 */
public class MainActivity extends Activity implements OnClickListener, InterpolatedTimeListener {
	private Button btnIncrease, btnDecrease;
	private TextView txtNumber;
	private TextView descText;
	private Handler mHandler;
	private int number;
	/** TextNumber是否允许显示最新的数字。 */
	private boolean enableRefresh;

	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.mHandler = new Handler();
		btnIncrease = (Button) findViewById(R.id.btnIncrease);
		btnDecrease = (Button) findViewById(R.id.btnDecrease);
		txtNumber = (TextView) findViewById(R.id.txtNumber);
		descText = (TextView) findViewById(R.id.descText);
		
		btnIncrease.setOnClickListener(this);
		btnDecrease.setOnClickListener(this);

		number = 3;
		Typeface localTypeface = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeueLTPro-ThEx.otf");
		txtNumber.setTypeface(localTypeface);
		txtNumber = (TextView) findViewById(R.id.txtNumber);
		txtNumber.setText(Integer.toString(number));
		 
		final AnimatorSet localAnimatorSet = new AnimatorSet();
		ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(txtNumber, "translationX", -600,0);
		localObjectAnimator1.setDuration(600);
	    localObjectAnimator1.setInterpolator(new OvershootInterpolator(1.2F));	 
	 
	    localAnimatorSet.play(localObjectAnimator1);	
	    ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(descText, "translationX", -600,0);
	    localObjectAnimator2.setDuration(600);
	    localObjectAnimator2.setInterpolator(new OvershootInterpolator(1.2F));
	    localAnimatorSet.play(localObjectAnimator2).after(100);
	    localAnimatorSet.start();
	}

	public void onClick(View v) {
		enableRefresh = true;
		NumberRotate3DAnimation rotateAnim = null;
		float cX = txtNumber.getWidth() / 2.0f;
		float cY = txtNumber.getHeight() / 2.0f;
		if (v == btnDecrease) {
			number--;
			rotateAnim = new NumberRotate3DAnimation(cX, cY, NumberRotate3DAnimation.ROTATE_DECREASE);
		} else if (v == btnIncrease) {
			number++;
			rotateAnim = new NumberRotate3DAnimation(cX, cY, NumberRotate3DAnimation.ROTATE_INCREASE);
		}
		if (rotateAnim != null) {
			rotateAnim.setInterpolatedTimeListener(this);
			rotateAnim.setFillAfter(true);
 
			txtNumber.startAnimation(rotateAnim);
		}

	}

	@SuppressLint("NewApi")
	@Override
	public void interpolatedTime(float interpolatedTime) {
		// update the number of the textview when rotate at the half point 
		if (enableRefresh && interpolatedTime > 0.5f) {
			txtNumber.setText(Integer.toString(number));
			Log.d("ANDROID_LAB", "setNumber:" + number);
			enableRefresh = false;
		}
		//change the alpha
		if(interpolatedTime > 0.5f) {
		    txtNumber.setAlpha((interpolatedTime -0.5f) * 2);
	    } else {
		    txtNumber.setAlpha(1-interpolatedTime * 2);
	    }  
	}
}