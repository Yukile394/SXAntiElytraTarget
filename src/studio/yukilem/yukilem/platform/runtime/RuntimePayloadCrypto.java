package studio.glace.glantielytratarget.platform.runtime;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Base64.Decoder;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.glace.glantielytratarget.libs.json.JSONObject;

final class RuntimePayloadCrypto {
   private static final long FNV_OFFSET = -3750763034362895579L;
   private static final long FNV_PRIME = 1099511628211L;

   private RuntimePayloadCrypto() {
   }

   static long verifyChallengeSignature(@NotNull String string0, @NotNull String string1) {
      try {
         String string6 = "-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAusQh5RJYqJTGfo/E/ztnob6N+6kZP1LVBKvloDoo/ElIJk7xUDh33fjiM6FvJr/E0mv7q3V+BWlqqJJshF0ba2z/azyc3WBLhha+6Xl1RWW1ABemVQvRSUBKf/t2IuBFKA3TX5wyL6E4YVolqlXqlhA/EHzw8f8T0kFATV/MXrvG5PhQC/1VkI5fsxSGmS8+mnKadxee1813SLO43CcIXqbVA7k5GBEIjqcKGaP0yV4OHoxsRg4aRAbe7V9ZzAXKbAb66K3y60fQSkXnDudPq0W3ijzxOK920ZpbVCP35mfmSunauc+V4XmbFHQTokwzIiTkJ3QJBQcqG08QfF+wWwIDAQAB\n-----END PUBLIC KEY-----"
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replaceAll("\\s+", "");
         Decoder decoder15 = Base64.getDecoder();
         String string36 = string6;
         byte[] bs7 = decoder15.decode(string36);
         byte[] bs45 = bs7;
         X509EncodedKeySpec x509EncodedKeySpec17 = new X509EncodedKeySpec(bs45);
         X509EncodedKeySpec x509EncodedKeySpec8 = x509EncodedKeySpec17;
         KeyFactory keyFactory19 = KeyFactory.getInstance("RSA");
         X509EncodedKeySpec x509EncodedKeySpec37 = x509EncodedKeySpec8;
         PublicKey publicKey9 = keyFactory19.generatePublic(x509EncodedKeySpec37);
         Signature signature10 = Signature.getInstance("SHA256withRSA");
         Signature signature23 = signature10;
         PublicKey publicKey38 = publicKey9;
         signature23.initVerify(publicKey38);
         Signature signature24 = signature10;
         Charset charset46 = StandardCharsets.UTF_8;
         byte[] bs40 = string0.getBytes(charset46);
         signature24.update(bs40);
         Signature signature25 = signature10;
         byte[] bs42 = Base64.getDecoder().decode(string1);
         boolean flag33;
         if (!signature25.verify(bs42)) {
            flag33 = true;
         } else {
            flag33 = false;
         }

         boolean flag11 = flag33;
         boolean flag27 = flag11;
         long l28;
         if (!flag27) {
            l28 = 0L;
         } else {
            l28 = 4L;
         }

         return l28;
      } catch (Exception exception49) {
         return 5L;
      }
   }

   static long verifySeedToken(@NotNull byte[] bs0, @NotNull String string1, @Nullable String string2) {
      if (string2 != null && !string2.isEmpty()) {
         try {
            Mac mac8 = Mac.getInstance("HmacSHA256");
            Mac mac17 = mac8;
            SecretKeySpec secretKeySpec5 = new SecretKeySpec(bs0, "HmacSHA256");
            mac17.init(secretKeySpec5);
            Mac mac18 = mac8;
            Charset charset49 = StandardCharsets.UTF_8;
            byte[] bs44 = string1.getBytes(charset49);
            String string9 = RuntimeFieldCodec.hex(mac18.doFinal(bs44));
            String string21 = string9;
            int i22 = string21.length();
            int i46 = string2.length();
            if (i22 != i46) {
               return 7L;
            }

            long l10 = 0L;
            int i12 = 0;

            while (true) {
               int i26 = i12;
               String string47 = string9;
               int i48 = string47.length();
               if (i26 >= i48) {
                  long l34 = l10;
                  long l55 = 0L;
                  long l41;
                  if (l34 == l55) {
                     l41 = 0L;
                  } else {
                     l41 = 8L;
                  }

                  return l41;
               }

               long l27 = l10;
               String string50 = string9;
               int i57 = i12;
               char c51 = string50.charAt(i57);
               int i60 = i12;
               char c59 = string2.charAt(i60);
               long l53 = c51 ^ c59;
               l10 = l27 | l53;
               i12++;
            }
         } catch (Exception exception62) {
            return 9L;
         }
      } else {
         return 6L;
      }
   }

   static int scoreSeedIntegrity(byte[] bs0) {
      if (bs0 != null && bs0.length == 32) {
         int[] is6 = new int[256];
         int i7 = 0;
         int i8 = 0;
         int i9 = 1;
         int i10 = 1;
         int i11 = 0;

         while (true) {
            int i22 = i11;
            int i47 = bs0.length;
            if (i22 >= i47) {
               int i37 = i7;
               if (i37 < 8) {
                  return 17;
               }

               int i39 = i10;
               if (i39 > 6) {
                  return 18;
               }

               int i40 = i8;
               if (i40 == 0) {
                  int i41 = i7;
                  if (i41 < 16) {
                     return 19;
                  }
               }

               return 0;
            }

            int i48 = i11;
            int i12 = bs0[i48] & 255;
            int[] is26 = is6;
            int i50 = i12;
            if (is26[i50] == 0) {
               int[] is28 = is6;
               int i51 = i12;
               is28[i51] = 1;
               i7++;
            }

            label40: {
               int i29 = i8;
               int i52 = i12;
               int i61 = i11;
               int i62 = i61 % 24;
               int i53 = i52 << i62;
               i8 = i29 ^ i53;
               int i31 = i11;
               if (i31 > 0) {
                  int i54 = i11;
                  byte b33 = bs0[i54];
                  int i63 = i11;
                  int i64 = i63 - 1;
                  byte b56 = bs0[i64];
                  if (b33 == b56) {
                     int i34 = ++i9;
                     int i57 = i10;
                     if (i34 > i57) {
                        int i35 = i9;
                        i10 = i35;
                     }
                     break label40;
                  }
               }

               i9 = 1;
            }

            i11++;
         }
      } else {
         return 16;
      }
   }

   static byte @Nullable [] decryptOperationalSeed(@NotNull String string0, @NotNull String string1, @Nullable String string2) {
      try {
         String[] strings9 = string0.split(":");
         String[] strings18 = strings9;
         if (strings18.length != 2) {
            return null;
         }

         String[] strings23 = strings9;
         byte[] bs10 = RuntimeFieldCodec.unhex(strings23[0]);
         String[] strings26 = strings9;
         byte[] bs11 = RuntimeFieldCodec.unhex(strings26[1]);
         String string42;
         if (string2 != null) {
            string42 = string2;
         } else {
            string42 = "";
         }

         String string12 = string42;
         MessageDigest messageDigest13 = MessageDigest.getInstance("SHA-256");
         MessageDigest messageDigest32 = messageDigest13;
         String string6 = string12;
         String string47 = string1 + string6;
         Charset charset51 = StandardCharsets.UTF_8;
         byte[] bs48 = string47.getBytes(charset51);
         messageDigest32.update(bs48);
         MessageDigest messageDigest33 = messageDigest13;
         byte[] bs14 = messageDigest33.digest();
         Cipher cipher15 = Cipher.getInstance("AES/CBC/PKCS5Padding");
         Cipher cipher37 = cipher15;
         byte[] bs7 = bs14;
         SecretKeySpec secretKeySpec52 = new SecretKeySpec(bs7, "AES");
         byte[] bs56 = bs10;
         IvParameterSpec ivParameterSpec54 = new IvParameterSpec(bs56);
         cipher37.init(2, secretKeySpec52, ivParameterSpec54);
         Cipher cipher38 = cipher15;
         byte[] bs50 = bs11;
         byte[] bs16 = cipher38.doFinal(bs50);
         byte[] bs53 = bs16;
         Charset charset55 = StandardCharsets.UTF_8;
         String string40 = new String(bs53, charset55);
         return RuntimeFieldCodec.unhex(string40);
      } catch (Exception exception58) {
         return null;
      }
   }

   static long seedTokenRef(@NotNull byte[] bs0, @NotNull String string1, long l2) {
      long l11 = -3750763034362895579L;
      int i14 = bs0.length;
      int i15 = 0;

      while (true) {
         int i21 = i15;
         int i7 = i14;
         if (i21 >= i7) {
            long l32 = l11;
            long l36 = l32 ^ l2;
            long l58 = 1099511628211L;
            long l40 = l36 * l58;
            long l64 = (long)string1.hashCode() << 17;
            long l75 = l40 ^ l64;
            long l44 = l75;
            long l66 = 0L;
            long l47;
            if (l44 == l66) {
               l47 = 1L;
            } else {
               l47 = l75;
            }

            return l47;
         }

         int i49 = i15;
         byte b16 = bs0[i49];
         long l24 = l11;
         byte b8 = b16;
         long l50 = b8;
         long l9 = 255L;
         long l52 = l50 & l9;
         long l28 = l24 ^ l52;
         long l54 = 1099511628211L;
         l11 = l28 * l54;
         i15++;
      }
   }

   static long seedMark(@NotNull byte[] bs0, @NotNull String string1, long l2) {
      long l12 = -3750763034362895579L;
      int i14 = 0;

      while (true) {
         int i16 = i14;
         int i47 = bs0.length;
         int i46 = Math.min(8, i47);
         if (i16 >= i46) {
            long l25 = l12;
            long l29 = l25 ^ l2;
            long l58 = 1099511628211L;
            long l33 = l29 * l58;
            long l60 = 6840346602683415230L;
            long l37 = l33 ^ l60;
            long l66 = (long)string1.hashCode() << 19;
            long l79 = l37 ^ l66;
            long l41 = l79;
            long l68 = 0L;
            long l44;
            if (l41 == l68) {
               l44 = 1L;
            } else {
               l44 = l79;
            }

            return l44;
         }

         long l17 = l12;
         int i9 = i14;
         long l50 = bs0[i9];
         long l10 = 255L;
         long l52 = l50 & l10;
         long l21 = l17 ^ l52;
         long l54 = 1099511628211L;
         l12 = l21 * l54;
         i14++;
      }
   }

   static long keyFingerprint(@NotNull String string0) {
      long l9 = -3750763034362895579L;
      int i11 = 0;

      while (true) {
         int i13 = i11;
         int i26 = string0.length();
         if (i13 >= i26) {
            long l22 = l9;
            long l34 = 6491862716820340463L;
            return l22 ^ l34;
         }

         long l14 = l9;
         int i6 = i11;
         long l28 = string0.charAt(i6);
         long l7 = 65535L;
         long l30 = l28 & l7;
         long l18 = l14 ^ l30;
         long l32 = 1099511628211L;
         l9 = l18 * l32;
         i11++;
      }
   }

   static long validResponseFold(@NotNull JSONObject jsonObject0) {
      String string43 = RuntimeFieldCodec.decode(RuntimeFieldCodec.FK_VALID);
      Object object6 = jsonObject0.opt(string43);
      Object object12 = object6;
      if (object12 == null) {
         return -3750763034362895398L;
      }

      long l7 = 255L;
      Object object17 = object6;
      long l51;
      if (object17 instanceof Number) {
         Object object19 = object6;
         long l22 = ((Number)object19).longValue();
         long l4 = 1L;
         long l37;
         if (l22 == l4) {
            l37 = 0L;
         } else {
            l37 = 255L;
         }

         l51 = l37;
      } else {
         Object object29 = object6;
         if (object29 instanceof String) {
            Object object31 = object6;
            String string45 = ((String)object31).trim();
            long l41;
            if ("true".equalsIgnoreCase(string45)) {
               l41 = 0L;
            } else {
               l41 = 255L;
            }

            l51 = l41;
         } else {
            Object object46 = object6;
            String string48 = String.valueOf(object46).trim();
            long l39;
            if ("true".equalsIgnoreCase(string48)) {
               l39 = 0L;
            } else {
               l39 = 255L;
            }

            l51 = l39;
         }
      }

      long l25 = -3750763034362895579L;
      long l49 = l51;
      return l25 ^ l49;
   }
}
