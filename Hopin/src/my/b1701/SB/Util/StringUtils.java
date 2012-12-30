package my.b1701.SB.Util;

public class StringUtils {
    
    public static boolean isEmpty(String s){
        return (s == null || s.length() == 0);
    }

    public static boolean isBlank(String s){
        if (isEmpty(s)) {
            return true;
        }
            
        int strLen = s.length();
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(s.charAt(i)) == false){
                return false;
            }
        }

        return true;
    }
}
