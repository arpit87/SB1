package my.b1701.SB.CustomViewsAndListeners;


import my.b1701.SB.ActivityHandlers.MapListActivityHandler;
import my.b1701.SB.MapHelpers.BaseItemizedOverlay;
import my.b1701.SB.Platform.Platform;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

import com.google.android.maps.MapView;

public class SBMapView extends MapView implements OnGestureListener {

    private GestureDetector gd;    
    private OnSingleTapListener singleTapListener;

	public SBMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupGestures();
    }

    public SBMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupGestures();
    }

    public SBMapView(Context context, String apiKey) {
        super(context, apiKey);
        setupGestures();
    }
    
    
    private void setupGestures() {
    	gd = new GestureDetector(Platform.getInstance().getContext(),this);  
        
        //set the on Double tap listener  
        gd.setOnDoubleTapListener(new OnDoubleTapListener() {

			 
			public boolean onSingleTapConfirmed(MotionEvent e) {
				BaseItemizedOverlay nearbyUserOverlay = MapListActivityHandler.getInstance().getNearbyUserItemizedOverlay();
				if(nearbyUserOverlay!=null)
					nearbyUserOverlay.removeExpandedShowSmallViews();
				return true;
			}

			 
			public boolean onDoubleTap(MotionEvent e) {
				SBMapView.this.getController().zoomInFixing((int) e.getX(), (int) e.getY());
				return false;
			}

			 
			public boolean onDoubleTapEvent(MotionEvent e) {
				return false;
			}
        	
        });
    }
    
	 
	/*public boolean onTouchEvent(MotionEvent ev) {
		if (this.gd.onTouchEvent(ev)) {
			return true;
		} else {
			return super.onTouchEvent(ev);
		}
	}*/
	
	public void setOnSingleTapListener(OnSingleTapListener singleTapListener) {
		this.singleTapListener = singleTapListener;
	}

	 
	public boolean onDown(MotionEvent e) {
		return false;
	}

	 
	public void onShowPress(MotionEvent e) {}

	 
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	 
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	 
	public void onLongPress(MotionEvent e) {}

	 
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}
    
}


