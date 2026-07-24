package snapLink.Url.Util;

import java.util.Base64;

public final class ShortCodeGenerator {
    private static final String BASE62 =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String encode(Long id){
//        if(id == null || id< 0 )
//        {
//            throw new IllegalAccessException("Invalid ID");
//        }
        if(id == 0) return String.valueOf(BASE62.charAt(0));

        StringBuilder shortCode = new StringBuilder();

        while(id>0)
        {
            int rem = (int) (id%62);
            shortCode.append(BASE62.charAt(rem));
            id = id/62;
        }
        return shortCode.reverse().toString();
    }

}
