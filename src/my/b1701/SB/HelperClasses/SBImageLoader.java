package my.b1701.SB.HelperClasses;

import my.b1701.SB.Platform.Platform;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class SBImageLoader {
	
	private final String TAG = "SBImageLoader";	
	private static  ImageLoader imageLoader;	
	private static SBImageLoader instance = null;

	  public static SBImageLoader getInstance()
	  {
		  if(instance == null)
		  {
			  instance = new SBImageLoader();
			  ImageLoaderConfiguration config  =  ImageLoaderConfiguration.createDefault(Platform.getInstance().getContext());
			  imageLoader = ImageLoader.getInstance();
			  imageLoader.init(config);
		  }
		  
		  return instance;
	  }
	 
	  public void displayImage(String paramString, ImageView paramImageView)
	  {		  
		  imageLoader.displayImage(paramString, paramImageView);
	  }
	  
	  public void displayImageElseStub(String paramString, ImageView paramImageView, int stubResource)
	  {		  
		  DisplayImageOptions options = new DisplayImageOptions.Builder()
		    .showStubImage(stubResource)
		    .cacheInMemory()
		    .build();
		  imageLoader.displayImage(paramString, paramImageView,options);
	  }
	  
	  public void displayImage(String url, ImageView view, DisplayImageOptions options)
	  {
		  imageLoader.displayImage(url, view, options);
	  }

}
