package my.b1701.SB.HelperClasses;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

import my.b1701.SB.Platform.Platform;

import android.content.Context;
import android.content.SharedPreferences;

public class ThisAppInstallation {
	
	 private static String sID = null;
	    private static final String INSTALLATION = "INSTALLATION";

	    public synchronized static String id(Context context) {
	        if (sID == null) {  
	            File installation = new File(context.getFilesDir(), INSTALLATION);
	            try {
	                if (!installation.exists())
	                {
	                    writeInstallationFile(installation);
	                    //always initialize platform before calling this!
	                    ThisAppConfig.getInstance().putLong("networkfreq", 1*60*1000);
	                    ThisAppConfig.getInstance().putLong("gpsfreq", 2*60*1000);	           
	                }
	                sID = readInstallationFile(installation);
	            } catch (Exception e) {
	                throw new RuntimeException(e);
	            }
	        }
	        return sID;
	    }

	    private static String readInstallationFile(File installation) throws IOException {
	        RandomAccessFile f = new RandomAccessFile(installation, "r");
	        byte[] bytes = new byte[(int) f.length()];
	        f.readFully(bytes);
	        f.close();
	        return new String(bytes);
	    }

	    private static void writeInstallationFile(File installation) throws IOException {
	        FileOutputStream out = new FileOutputStream(installation);
	        String id = UUID.randomUUID().toString();
	        out.write(id.getBytes());
	        out.close();
	    }

}
