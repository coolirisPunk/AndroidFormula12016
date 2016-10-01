/**
 * PhotoSorterView.java
 * 
 * (c) Luke Hutchison (luke.hutch@mit.edu)
 * 
 * TODO: Add OpenGL acceleration.
 * 
 * --
 * 
 * Released under the MIT license (but please notify me if you use this code, so that I can give your project credit at
 * http://code.google.com/p/android-multitouch-controller ).
 * 
 * MIT license: http://www.opensource.org/licenses/MIT
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */
package autodromo.punkmkt.com.ahrapp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import autodromo.punkmkt.com.ahrapp.models.ImgSticker;
import autodromo.punkmkt.com.ahrapp.utils.MultiTouchController;

public class PhotoSortrView extends View implements MultiTouchController.MultiTouchObjectCanvas<ImgSticker> {

	// --

	private MultiTouchController<ImgSticker> multiTouchController = new MultiTouchController<>(this);

	// --

	private MultiTouchController.PointInfo currTouchPoint = new MultiTouchController.PointInfo();

	private static final int UI_MODE_ROTATE = 1, UI_MODE_ANISOTROPIC_SCALE = 2;

	private int mUIMode = UI_MODE_ROTATE;
    private ArrayList<ImgSticker> mImages = new ArrayList<>();
    private ArrayList<ImgSticker> mImages_copy = new ArrayList<>();

	private Set<Integer> indexes = new HashSet<>();
	// --

	private Paint mLinePaintTouchPointCircle = new Paint();
	private String TAG = "PhotoSortrView";
    Context context;

	// ---------------------------------------------------------------------------------------------------

	public PhotoSortrView(Context context, ArrayList<ImgSticker> mImages, String mCurrentPhotoPath) {
		this(context, null, mImages, mCurrentPhotoPath);
	}

	public PhotoSortrView(Context context, AttributeSet attrs, ArrayList<ImgSticker> mImages, String mCurrentPhotoPath) {
		this(context, attrs, 0, mImages, mCurrentPhotoPath);
	}

	public PhotoSortrView(Context context, AttributeSet attrs, int defStyle, ArrayList<ImgSticker> mImages, String mCurrentPhotoPath) {
		super(context, attrs, defStyle);
        this.context = context;
        this.mImages = mImages;
		init();
	}

	private void init() {
        Log.d(TAG,"init");
		mLinePaintTouchPointCircle.setColor(Color.YELLOW);
		mLinePaintTouchPointCircle.setStrokeWidth(5);
		mLinePaintTouchPointCircle.setStyle(Style.STROKE);
		mLinePaintTouchPointCircle.setAntiAlias(true);
		//setBackgroundColor(Color.WHITE);
        for(int i=0;i<mImages.size();i++)
            mImages.get(i).load(getResources());
	}

	/** Called by activity's onResume() method to load the images */
	public void addImagetoCanvas(Context context, int pos) {
        Log.d("Click sticker",String.valueOf(pos));
        this.indexes.add(pos);
        if(!mImages_copy.contains(mImages.get(pos))){
            Log.d("Click sticker agrega","sip");
            mImages_copy.add(mImages.get(pos));
        }
        Log.d("Click sticker indexes",String.valueOf(indexes.size()));
        invalidate();
        //this.mImages.get(this.pos).draw(canvas);
    }

	/** Called by activity's onPause() method to free memory used for loading the images */
	public void unloadImages() {
        Log.d(TAG,"unloadImages");
		//int n = mImages.size();
		//for (int i = 0; i < n; i++)
		//	mImages.get(i).unload();
	}

	// ---------------------------------------------------------------------------------------------------

	@Override
	protected void onDraw(Canvas canvas) {


		super.onDraw(canvas);
        Log.d(TAG,"ondraw");
        for(ImgSticker im: mImages_copy){
            im.draw(canvas);
        }
    }

	// ---------------------------------------------------------------------------------------------------

	public void trackballClicked() {
		mUIMode = (mUIMode + 1) % 3;
		invalidate();
	}

	// ---------------------------------------------------------------------------------------------------

	/** Pass touch events to the MT controller */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return multiTouchController.onTouchEvent(event);
	}

	/** Get the image that is under the single-touch point, or return null (canceling the drag op) if none */
	public ImgSticker getDraggableObjectAtPoint(MultiTouchController.PointInfo pt) {
		float x = pt.getX(), y = pt.getY();
		int n = mImages_copy.size();
		for (int i = n - 1; i >= 0; i--) {
            ImgSticker im = mImages_copy.get(i);
			if (im.containsPoint(x, y))
				return im;
		}
		return null;
	}

	/**
	 * Select an object for dragging. Called whenever an object is found to be under the point (non-null is returned by getDraggableObjectAtPoint())
	 * and a drag operation is starting. Called with null when drag op ends.
	 */
	public void selectObject(ImgSticker img, MultiTouchController.PointInfo touchPoint) {
		Log.d(TAG, "selectobject");
		currTouchPoint.set(touchPoint);
		if (img != null) {
			// Move image to the top of the stack when selected
			//mImages.remove(img);
			//mImages.add(img);
            mImages_copy.remove(img);
            mImages_copy.add(img);
		} else {
			// Called with img == null when drag stops.
		}
		invalidate();
	}

	/** Get the current position and scale of the selected image. Called whenever a drag starts or is reset. */
	public void getPositionAndScale(ImgSticker img, MultiTouchController.PositionAndScale objPosAndScaleOut) {
		// FIXME affine-izem (and fix the fact that the anisotropic_scale part requires averaging the two scale factors)
		objPosAndScaleOut.set(img.getCenterX(), img.getCenterY(), (mUIMode & UI_MODE_ANISOTROPIC_SCALE) == 0,
				(img.getScaleX() + img.getScaleY()) / 2, (mUIMode & UI_MODE_ANISOTROPIC_SCALE) != 0, img.getScaleX(), img.getScaleY(),
				(mUIMode & UI_MODE_ROTATE) != 0, img.getAngle());
	}

	/** Set the position and scale of the dragged/stretched image. */
	public boolean setPositionAndScale(ImgSticker img, MultiTouchController.PositionAndScale newImgPosAndScale, MultiTouchController.PointInfo touchPoint) {
		Log.d(TAG, "setPositionAndScale");
		currTouchPoint.set(touchPoint);
		boolean ok = img.setPos(newImgPosAndScale);
		if (ok)
			invalidate();
		return ok;
	}

	// ----------------------------------------------------------------------------------------------

}
