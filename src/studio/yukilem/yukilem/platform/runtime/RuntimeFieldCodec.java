package studio.yukilem.yukilem.platform.runtime;

final class RuntimeFieldCodec {
    static final String UPSTREAM_SECURE = "https://87.76.207.92:3000";
    static final String UPSTREAM_PLAIN = "http://87.76.207.92:3000";
    static final String OWNER_TAG = "glAntiElytraTarget";
    static final String RSA_PUB = "-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAusQh5RJYqJTGfo/E/ztnob6N+6kZP1LVBKvloDoo/ElIJk7xUDh33fjiM6FvJr/E0mv7q3V+BWlqqJJshF0ba2z/azyc3WBLhha+6Xl1RWW1ABemVQvRSUBKf/t2IuBFKA3TX5wyL6E4YVolqlXqlhA/EHzw8f8T0kFATV/MXrvG5PhQC/1VkI5fsxSGmS8+mnKadxee1813SLO43CcIXqbVA7k5GBEIjqcKGaP0yV4OHoxsRg4aRAbe7V9ZzAXKbAb66K3y60fQSkXnDudPq0W3ijzxOK920ZpbVCP35mfmSunauc+V4XmbFHQTokwzIiTkJ3QJBQcqG08QfF+wWwIDAQAB\n-----END PUBLIC KEY-----";
    static final int[] EP_VERIFY;
    static final int[] EP_HEARTBEAT;
    static final int[] FK_LICENSE_KEY;
    static final int[] FK_SERVER_IP;
    static final int[] FK_JAR_HASH;
    static final int[] FK_CHALLENGE;
    static final int[] FK_TIMESTAMP;
    static final int[] FK_VALID;
    static final int[] FK_SIGNED_CHALLENGE;
    static final int[] FK_OPERATIONAL_SEED;
    static final int[] FK_DECRYPT_HINT;
    static final int[] FK_PLUGIN_NAME;
    static final int[] FK_SEED_TOKEN;
    static final int[] FK_PROOF;
    static final int[] FK_CLASS_HASH;

    private RuntimeFieldCodec() {
    }

    static String decode(int[] nArray) {
        int n;
        int n2;
        char[] cArray = new char[nArray.length];
        int n3 = 0;
        while ((n2 = n3) < (n = nArray.length)) {
            char c;
            char[] cArray2 = cArray;
            int n4 = n3;
            int n5 = n3++;
            cArray2[n4] = c = (char)nArray[n5];
        }
        char[] cArray3 = cArray;
        String string = new String(cArray3);
        return string;
    }

    static String hex(byte[] bArray) {
        int n;
        int n2;
        StringBuilder sb;
        int n3 = bArray.length * 2;
        StringBuilder sb2 = sb = new StringBuilder(n3);
        int length = bArray.length;
        int n4 = 0;
        while ((n2 = n4) < (n = length)) {
            int n5 = n4++;
            byte b = bArray[n5];
            StringBuilder sb3 = sb2;
            Object[] objectArray = new Object[1];
            byte b2 = b;
            Integer n6 = b2 & 0xFF;
            objectArray[0] = n6;
            String string = String.format("%02x", objectArray);
            StringBuilder sb4 = sb3.append(string);
        }
        StringBuilder sb5 = sb2;
        return sb5.toString();
    }

    static byte[] unhex(String string) {
        int n;
        int n2;
        int n3;
        int n4 = n3 = string.length();
        byte[] bArray = new byte[n4 / 2];
        int n5 = 0;
        while ((n2 = n5) < (n = n3)) {
            byte b;
            byte[] bArray2 = bArray;
            int n6 = n5;
            int n7 = n6 / 2;
            int n8 = n5;
            int n9 = Character.digit(string.charAt(n8), 16) << 4;
            int n10 = n5;
            int n11 = n10 + 1;
            int n12 = Character.digit(string.charAt(n11), 16);
            bArray2[n7] = b = (byte)(n9 + n12);
            n5 += 2;
        }
        byte[] bArray3 = bArray;
        return bArray3;
    }

    static {
        int[] nArray = new int[]{47, 97, 112, 105, 47, 108, 105, 99, 101, 110, 115, 101, 47, 118, 101, 114, 105, 102, 121};
        EP_VERIFY = nArray;
        int[] nArray2 = new int[]{47, 97, 112, 105, 47, 108, 105, 99, 101, 110, 115, 101, 47, 104, 101, 97, 114, 116, 98, 101, 97, 116};
        EP_HEARTBEAT = nArray2;
        int[] nArray3 = new int[]{108, 105, 99, 101, 110, 115, 101, 75, 101, 121};
        FK_LICENSE_KEY = nArray3;
        int[] nArray4 = new int[]{115, 101, 114, 118, 101, 114, 73, 112};
        FK_SERVER_IP = nArray4;
        int[] nArray5 = new int[]{106, 97, 114, 72, 97, 115, 104};
        FK_JAR_HASH = nArray5;
        int[] nArray6 = new int[]{99, 104, 97, 108, 108, 101, 110, 103, 101};
        FK_CHALLENGE = nArray6;
        int[] nArray7 = new int[]{116, 105, 109, 101, 115, 116, 97, 109, 112};
        FK_TIMESTAMP = nArray7;
        int[] nArray8 = new int[]{118, 97, 108, 105, 100};
        FK_VALID = nArray8;
        int[] nArray9 = new int[]{115, 105, 103, 110, 101, 100, 67, 104, 97, 108, 108, 101, 110, 103, 101};
        FK_SIGNED_CHALLENGE = nArray9;
        int[] nArray10 = new int[]{111, 112, 101, 114, 97, 116, 105, 111, 110, 97, 108, 83, 101, 101, 100};
        FK_OPERATIONAL_SEED = nArray10;
        int[] nArray11 = new int[]{100, 101, 99, 114, 121, 112, 116, 72, 105, 110, 116};
        FK_DECRYPT_HINT = nArray11;
        int[] nArray12 = new int[]{112, 108, 117, 103, 105, 110, 78, 97, 109, 101};
        FK_PLUGIN_NAME = nArray12;
        int[] nArray13 = new int[]{115, 101, 101, 100, 84, 111, 107, 101, 110};
        FK_SEED_TOKEN = nArray13;
        int[] nArray14 = new int[]{112, 114, 111, 111, 102};
        FK_PROOF = nArray14;
        int[] nArray15 = new int[]{99, 108, 97, 115, 115, 72, 97, 115, 104};
        FK_CLASS_HASH = nArray15;
    }
}

