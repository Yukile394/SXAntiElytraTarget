package studio.yukilem.yukilem.platform.runtime;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class RuntimeJarDigest {
   private RuntimeJarDigest() {
   }

   @NotNull
   static String jarHash(@NotNull File file0) {
      try {
         MessageDigest messageDigest5 = MessageDigest.getInstance("SHA-256");
         FileInputStream fileInputStream3 = new FileInputStream(file0);
         JarInputStream jarInputStream11 = new JarInputStream(fileInputStream3);
         JarInputStream jarInputStream6 = jarInputStream11;

         try {
            byte[] bs8 = new byte[8192];

            while (true) {
               JarInputStream jarInputStream14 = jarInputStream6;
               JarEntry jarEntry15 = jarInputStream14.getNextJarEntry();
               JarEntry jarEntry7 = jarEntry15;
               if (jarEntry15 == null) {
                  break;
               }

               JarEntry jarEntry16 = jarEntry7;
               if (jarEntry16.getName().endsWith(".class")) {
                  MessageDigest messageDigest26 = messageDigest5;
                  JarEntry jarEntry36 = jarEntry7;
                  String string37 = jarEntry36.getName();
                  Charset charset42 = StandardCharsets.UTF_8;
                  byte[] bs38 = string37.getBytes(charset42);
                  messageDigest26.update(bs38);

                  while (true) {
                     JarInputStream jarInputStream27 = jarInputStream6;
                     byte[] bs39 = bs8;
                     int i28 = jarInputStream27.read(bs39);
                     int i9 = i28;
                     if (i28 == -1) {
                        break;
                     }

                     MessageDigest messageDigest29 = messageDigest5;
                     byte[] bs41 = bs8;
                     int i44 = i9;
                     messageDigest29.update(bs41, 0, i44);
                  }
               }
            }
         } catch (Throwable throwable49) {
            try {
               JarInputStream jarInputStream20 = jarInputStream6;
               jarInputStream20.close();
            } catch (Throwable throwable48) {
               throwable49.addSuppressed(throwable48);
            }

            throw throwable49;
         }

         JarInputStream jarInputStream30 = jarInputStream6;
         jarInputStream30.close();
         MessageDigest messageDigest31 = messageDigest5;
         return RuntimeFieldCodec.hex(messageDigest31.digest());
      } catch (Exception exception50) {
         return "HASH_FAILED";
      }
   }

   @Nullable
   static String classHash(@NotNull File file0) {
      try {
         byte[] bs4 = segmentForClass(file0, "RuntimeAuthority.class");
         byte[] bs5 = segmentForBootGateClasses(file0);
         byte[] bs6 = segmentForCheckClasses(file0);
         byte[] bs13 = bs4;
         if (bs13 != null) {
            byte[] bs14 = bs5;
            if (bs14 != null) {
               byte[] bs15 = bs6;
               if (bs15 != null) {
                  MessageDigest messageDigest7 = MessageDigest.getInstance("SHA-256");
                  MessageDigest messageDigest18 = messageDigest7;
                  byte[] bs27 = bs4;
                  messageDigest18.update(bs27);
                  MessageDigest messageDigest19 = messageDigest7;
                  byte[] bs28 = bs5;
                  messageDigest19.update(bs28);
                  MessageDigest messageDigest20 = messageDigest7;
                  byte[] bs29 = bs6;
                  messageDigest20.update(bs29);
                  MessageDigest messageDigest21 = messageDigest7;
                  return RuntimeFieldCodec.hex(messageDigest21.digest());
               }
            }
         }

         return null;
      } catch (Exception exception31) {
         return null;
      }
   }

   static long artifactDrift(@Nullable String string0, @NotNull File file1) {
      if (string0 != null && !string0.isEmpty() && !"HASH_FAILED".equals(string0)) {
         String string5 = jarHash(file1);
         String string12 = string5;
         if (string12 != null) {
            String string23 = string5;
            if (!"HASH_FAILED".equals(string23)) {
               String string15 = string5;
               long l21;
               if (string15.equals(string0)) {
                  l21 = 0L;
               } else {
                  l21 = 25L;
               }

               return l21;
            }
         }

         return 24L;
      } else {
         return 0L;
      }
   }

   private static byte @Nullable [] segmentForClass(@NotNull File file0, @NotNull String string1) {
      try {
         MessageDigest messageDigest6 = MessageDigest.getInstance("SHA-256");
         FileInputStream fileInputStream4 = new FileInputStream(file0);
         JarInputStream jarInputStream13 = new JarInputStream(fileInputStream4);
         JarInputStream jarInputStream7 = jarInputStream13;

         try {
            byte[] bs9 = new byte[8192];

            while (true) {
               JarInputStream jarInputStream16 = jarInputStream7;
               JarEntry jarEntry17 = jarInputStream16.getNextJarEntry();
               JarEntry jarEntry8 = jarEntry17;
               if (jarEntry17 == null) {
                  break;
               }

               JarEntry jarEntry23 = jarEntry8;
               String string10 = jarEntry23.getName();
               String string25 = string10;
               if (string25.endsWith(string1)) {
                  MessageDigest messageDigest32 = messageDigest6;
                  String string38 = string10;
                  Charset charset43 = StandardCharsets.UTF_8;
                  byte[] bs39 = string38.getBytes(charset43);
                  messageDigest32.update(bs39);

                  while (true) {
                     JarInputStream jarInputStream33 = jarInputStream7;
                     byte[] bs40 = bs9;
                     int i34 = jarInputStream33.read(bs40);
                     int i11 = i34;
                     if (i34 == -1) {
                        break;
                     }

                     MessageDigest messageDigest35 = messageDigest6;
                     byte[] bs42 = bs9;
                     int i45 = i11;
                     messageDigest35.update(bs42, 0, i45);
                  }
               }
            }
         } catch (Throwable throwable50) {
            try {
               JarInputStream jarInputStream28 = jarInputStream7;
               jarInputStream28.close();
            } catch (Throwable throwable49) {
               throwable50.addSuppressed(throwable49);
            }

            throw throwable50;
         }

         JarInputStream jarInputStream18 = jarInputStream7;
         jarInputStream18.close();
         MessageDigest messageDigest19 = messageDigest6;
         return messageDigest19.digest();
      } catch (Exception exception51) {
         return null;
      }
   }

   private static byte @Nullable [] segmentForBootGateClasses(@NotNull File file0) {
      try {
         MessageDigest messageDigest5 = MessageDigest.getInstance("SHA-256");
         FileInputStream fileInputStream3 = new FileInputStream(file0);
         JarInputStream jarInputStream12 = new JarInputStream(fileInputStream3);
         JarInputStream jarInputStream6 = jarInputStream12;

         try {
            byte[] bs8 = new byte[8192];

            label81:
            while (true) {
               String string9;
               String string50;
               do {
                  JarInputStream jarInputStream15 = jarInputStream6;
                  JarEntry jarEntry16 = jarInputStream15.getNextJarEntry();
                  JarEntry jarEntry7 = jarEntry16;
                  if (jarEntry16 == null) {
                     break label81;
                  }

                  JarEntry jarEntry22 = jarEntry7;
                  string9 = jarEntry22.getName();
                  String string24 = string9;
                  if (string24.endsWith("glAntiElytraTarget.class")) {
                     break;
                  }

                  String string26 = string9;
                  if (string26.endsWith("RuntimeProfile.class")) {
                     break;
                  }

                  String string28 = string9;
                  if (string28.endsWith("PluginContext.class")) {
                     break;
                  }

                  String string30 = string9;
                  if (string30.endsWith("PacketDispatcher.class")) {
                     break;
                  }

                  String string32 = string9;
                  if (string32.endsWith("CheckRegistry.class")) {
                     break;
                  }

                  String string34 = string9;
                  if (string34.endsWith("Check.class")) {
                     break;
                  }

                  String string36 = string9;
                  if (string36.endsWith("ViolationService.class")) {
                     break;
                  }

                  String string38 = string9;
                  if (string38.endsWith("PluginCommandRouter.class")) {
                     break;
                  }

                  String string40 = string9;
                  if (string40.endsWith("ElytraBoostListener.class")) {
                     break;
                  }

                  String string42 = string9;
                  if (string42.endsWith("DamageListener.class")) {
                     break;
                  }

                  String string44 = string9;
                  if (string44.endsWith("FireworkListener.class")) {
                     break;
                  }

                  String string46 = string9;
                  if (string46.endsWith("PlayerLifecycleListener.class")) {
                     break;
                  }

                  String string48 = string9;
                  if (string48.endsWith("RuntimeGate.class")) {
                     break;
                  }

                  string50 = string9;
               } while (!string50.endsWith("LicenseBootstrap.class"));

               MessageDigest messageDigest57 = messageDigest5;
               String string76 = string9;
               Charset charset81 = StandardCharsets.UTF_8;
               byte[] bs77 = string76.getBytes(charset81);
               messageDigest57.update(bs77);

               while (true) {
                  JarInputStream jarInputStream58 = jarInputStream6;
                  byte[] bs78 = bs8;
                  int i59 = jarInputStream58.read(bs78);
                  int i10 = i59;
                  if (i59 == -1) {
                     break;
                  }

                  MessageDigest messageDigest60 = messageDigest5;
                  byte[] bs80 = bs8;
                  int i83 = i10;
                  messageDigest60.update(bs80, 0, i83);
               }
            }
         } catch (Throwable throwable88) {
            try {
               JarInputStream jarInputStream53 = jarInputStream6;
               jarInputStream53.close();
            } catch (Throwable throwable87) {
               throwable88.addSuppressed(throwable87);
            }

            throw throwable88;
         }

         JarInputStream jarInputStream17 = jarInputStream6;
         jarInputStream17.close();
         MessageDigest messageDigest18 = messageDigest5;
         return messageDigest18.digest();
      } catch (Exception exception89) {
         return null;
      }
   }

   private static byte @Nullable [] segmentForCheckClasses(@NotNull File file0) {
      try {
         MessageDigest messageDigest5 = MessageDigest.getInstance("SHA-256");
         FileInputStream fileInputStream3 = new FileInputStream(file0);
         JarInputStream jarInputStream12 = new JarInputStream(fileInputStream3);
         JarInputStream jarInputStream6 = jarInputStream12;

         try {
            byte[] bs8 = new byte[8192];

            label69:
            while (true) {
               String string9;
               String string37;
               do {
                  JarInputStream jarInputStream15 = jarInputStream6;
                  JarEntry jarEntry16 = jarInputStream15.getNextJarEntry();
                  JarEntry jarEntry7 = jarEntry16;
                  if (jarEntry16 == null) {
                     break label69;
                  }

                  JarEntry jarEntry17 = jarEntry7;
                  string9 = jarEntry17.getName();
                  String string19 = string9;
                  if (string19.endsWith("CombatCheck.class")) {
                     break;
                  }

                  String string21 = string9;
                  if (string21.endsWith("HotbarOrderCheck.class")) {
                     break;
                  }

                  String string23 = string9;
                  if (string23.endsWith("SequenceCheck.class")) {
                     break;
                  }

                  String string25 = string9;
                  if (string25.endsWith("ConsistencyCheck.class")) {
                     break;
                  }

                  String string27 = string9;
                  if (string27.endsWith("FWInputCheck.class")) {
                     break;
                  }

                  String string29 = string9;
                  if (string29.endsWith("ShootCheck.class")) {
                     break;
                  }

                  String string31 = string9;
                  if (string31.endsWith("PivotCheck.class")) {
                     break;
                  }

                  String string33 = string9;
                  if (string33.endsWith("ElytraPatternCheck.class")) {
                     break;
                  }

                  String string35 = string9;
                  if (string35.endsWith("ReloadSubCommand.class")) {
                     break;
                  }

                  string37 = string9;
               } while (!string37.endsWith("PacketRouter.class"));

               MessageDigest messageDigest46 = messageDigest5;
               String string64 = string9;
               Charset charset69 = StandardCharsets.UTF_8;
               byte[] bs65 = string64.getBytes(charset69);
               messageDigest46.update(bs65);

               while (true) {
                  JarInputStream jarInputStream47 = jarInputStream6;
                  byte[] bs66 = bs8;
                  int i48 = jarInputStream47.read(bs66);
                  int i10 = i48;
                  if (i48 == -1) {
                     break;
                  }

                  MessageDigest messageDigest49 = messageDigest5;
                  byte[] bs68 = bs8;
                  int i71 = i10;
                  messageDigest49.update(bs68, 0, i71);
               }
            }
         } catch (Throwable throwable76) {
            try {
               JarInputStream jarInputStream40 = jarInputStream6;
               jarInputStream40.close();
            } catch (Throwable throwable75) {
               throwable76.addSuppressed(throwable75);
            }

            throw throwable76;
         }

         JarInputStream jarInputStream50 = jarInputStream6;
         jarInputStream50.close();
         MessageDigest messageDigest51 = messageDigest5;
         return messageDigest51.digest();
      } catch (Exception exception77) {
         return null;
      }
   }
}
