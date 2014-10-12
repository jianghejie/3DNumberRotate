package com.jcodecraeer.numberrotate3danimation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * @author www.jcodecraeer.com
 */
public class NumberRotate3DAnimation extends Animation {
	public static final boolean DEBUG = false;
	public static final boolean ROTATE_DECREASE = true;
	public static final boolean ROTATE_INCREASE = false;
	/** max depth at z axis */
	public static final float DEPTH_Z = 0.0f;
	/** duration of the animation¡£ */
	public static final long DURATION = 500;
	private final boolean type;
	private final float centerX;
	private final float centerY;
	private Camera camera;
	private InterpolatedTimeListener listener;

	public NumberRotate3DAnimation(float cX, float cY, boolean type) {
		centerX = cX;
		centerY = cY;
		this.type = type;
		setDuration(DURATION);
	}

	public void initialize(int width, int height, int parentWidth, int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		camera = new Camera();
	}

	public void setInterpolatedTimeListener(InterpolatedTimeListener listener) {
		this.listener = listener;
	}

	protected void applyTransformation(float interpolatedTime, Transformation transformation) {
		if (listener != null) {
			listener.interpolatedTime(interpolatedTime);
		}
		float from = 0.0f, to = 0.0f;
		if (type == ROTATE_DECREASE) {
			from = 0.0f;
			to = 180.0f;
		} else if (type == ROTATE_INCREASE) {
			from = 360.0f;
			to = 180.0f;
		}
		float degree = from + (to - from) * interpolatedTime;
		boolean overHalf = (interpolatedTime > 0.5f);
		if (overHalf) {
			// this is important ,the number should be readable instead of mirror effect when rotate half point  
			degree = degree - 180;
		}
		float depth = (0.5f - Math.abs(interpolatedTime - 0.5f)) * DEPTH_Z;
		final Matrix matrix = transformation.getMatrix();
		camera.save();
		camera.translate(0.0f, 0.0f, depth);
		camera.rotateY(degree);
		camera.getMatrix(matrix);
		camera.restore();
		if (DEBUG) {
			if (overHalf) {
				matrix.preTranslate(-centerX * 2, -centerY);
				matrix.postTranslate(centerX * 2, centerY);
			}
		} else {
			//make sure the animate view is at the center of the layout during animation
			matrix.preTranslate(-centerX, -centerY);
			matrix.postTranslate(centerX, centerY);
		}
	}

	public static interface InterpolatedTimeListener {
		public void interpolatedTime(float interpolatedTime);
	}
}