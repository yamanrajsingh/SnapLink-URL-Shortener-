package snapLink.Url.Util;

import java.security.SecureRandom;

public final class ShortCodeGenerator {

    private static final String BASE62 =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final SecureRandom RANDOM = new SecureRandom();

    private ShortCodeGenerator() {
    }

    public static String generate(int length) {

        StringBuilder shortCode = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            shortCode.append(
                    BASE62.charAt(
                            RANDOM.nextInt(BASE62.length())
                    )
            );
        }

        return shortCode.toString();
    }
}