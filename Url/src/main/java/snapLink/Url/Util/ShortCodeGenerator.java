package snapLink.Url.Util;

import java.security.SecureRandom;

public final class ShortCodeGenerator {

    private static final String BASE62 =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final SecureRandom RANDOM = new SecureRandom();

    private ShortCodeGenerator() {
    }

    // Base62 encoding (old approach)
    public static String encode(Long id) {

        if (id == 0) {
            return String.valueOf(BASE62.charAt(0));
        }

        StringBuilder shortCode = new StringBuilder();

        while (id > 0) {
            int rem = (int) (id % 62);
            shortCode.append(BASE62.charAt(rem));
            id /= 62;
        }

        return shortCode.reverse().toString();
    }

    // Random code generation (production approach)

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