package studio.glace.glantielytratarget.libs.nixlib.items;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Locale;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

public final class SkullBuilder {
    private static final String TEXTURE_URL_PREFIX = "http://textures.minecraft.net/texture/";
    private final ItemStack skull = new ItemStack(Material.PLAYER_HEAD);

    private SkullBuilder() {
    }

    public static SkullBuilder head() {
        return new SkullBuilder();
    }

    public SkullBuilder owner(OfflinePlayer offlinePlayer) {
        SkullMeta skullMeta = (SkullMeta)this.skull.getItemMeta();
        if (skullMeta != null) {
            skullMeta.setOwningPlayer(offlinePlayer);
            this.skull.setItemMeta(skullMeta);
        }
        return this;
    }

    public SkullBuilder ownerName(String string) {
        SkullMeta skullMeta = (SkullMeta)this.skull.getItemMeta();
        if (skullMeta == null) {
            return this;
        }
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(string));
        this.skull.setItemMeta(skullMeta);
        return this;
    }

    public SkullBuilder texture(String string) {
        boolean bl;
        SkullMeta skullMeta = (SkullMeta)this.skull.getItemMeta();
        if (skullMeta == null || string == null || string.isBlank()) {
            return this;
        }
        String string2 = string.trim();
        String string3 = SkullBuilder.resolveTextureUrl(string2);
        boolean bl2 = bl = string3 != null && this.applyProfile(skullMeta, SkullBuilder.safeUri(string3));
        if (!bl) {
            String string4 = string3 != null ? SkullBuilder.encodeUrl(string3) : string2;
            this.applyLegacyTexture(skullMeta, string4);
        }
        this.skull.setItemMeta(skullMeta);
        return this;
    }

    static String resolveTextureUrl(String string) {
        if (string == null || string.isBlank()) {
            return null;
        }
        String string2 = string.trim();
        if (string2.startsWith("http://") || string2.startsWith("https://")) {
            return string2;
        }
        String string3 = SkullBuilder.tryDecodeBase64(string2);
        if (string3 != null && string3.contains("\"url\"")) {
            return SkullBuilder.extractUrl(string3);
        }
        if (SkullBuilder.isTextureHash(string2)) {
            return TEXTURE_URL_PREFIX + string2;
        }
        return null;
    }

    private static String tryDecodeBase64(String string) {
        try {
            return new String(Base64.getDecoder().decode(string), StandardCharsets.UTF_8);
        }
        catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static boolean isTextureHash(String string) {
        if (string.length() < 16) {
            return false;
        }
        for (int n = 0; n < string.length(); ++n) {
            boolean bl;
            char c = string.charAt(n);
            boolean bl2 = bl = c >= '0' && c <= '9' || c >= 'a' && c <= 'f' || c >= 'A' && c <= 'F';
            if (bl) continue;
            return false;
        }
        return true;
    }

    private static URI safeUri(String string) {
        try {
            return URI.create(string);
        }
        catch (IllegalArgumentException e) {
            return null;
        }
    }

    public SkullBuilder textureUrl(String string) {
        SkullMeta skullMeta = (SkullMeta)this.skull.getItemMeta();
        if (skullMeta == null) {
            return this;
        }
        this.applyProfile(skullMeta, SkullBuilder.safeUri(string));
        this.skull.setItemMeta(skullMeta);
        return this;
    }

    public ItemStack build() {
        return this.skull;
    }

    private boolean applyProfile(SkullMeta skullMeta, URI uri) {
        if (uri == null) {
            return false;
        }
        try {
            PlayerProfile playerProfile = Bukkit.createPlayerProfile(UUID.randomUUID(), "nixlib");
            PlayerTextures textures = playerProfile.getTextures();
            textures.setSkin(uri.toURL());
            playerProfile.setTextures(textures);
            skullMeta.setOwnerProfile(playerProfile);
            return true;
        }
        catch (Throwable e) {
            return false;
        }
    }

    private void applyLegacyTexture(SkullMeta skullMeta, String string) {
        try {
            Class<?> clazz = Class.forName("com.mojang.authlib.GameProfile");
            Object obj = clazz.getConstructor(UUID.class, String.class).newInstance(UUID.randomUUID(), "nixlib");
            Object object = clazz.getMethod("getProperties", new Class[0]).invoke(obj, new Object[0]);
            Object obj2 = Class.forName("com.mojang.authlib.properties.Property").getConstructor(String.class, String.class).newInstance("textures", string);
            object.getClass().getMethod("put", Object.class, Object.class).invoke(object, "textures", obj2);
            if (!SkullBuilder.assignProfileField(skullMeta, obj)) {
                SkullBuilder.trySetProfileViaSetter(skullMeta, obj);
            }
        }
        catch (Throwable e) {
            // empty catch block
        }
    }

    private static boolean assignProfileField(SkullMeta skullMeta, Object object) {
        for (Class<?> clazz = skullMeta.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            for (Field field : clazz.getDeclaredFields()) {
                if (!field.getName().toLowerCase(Locale.ROOT).contains("profile")) continue;
                try {
                    field.setAccessible(true);
                    if (!field.getType().isAssignableFrom(object.getClass())) continue;
                    field.set(skullMeta, object);
                    return true;
                }
                catch (Throwable e) {
                    // empty catch block
                }
            }
        }
        return false;
    }

    private static void trySetProfileViaSetter(SkullMeta skullMeta, Object object) {
        for (Method method : skullMeta.getClass().getMethods()) {
            if (!method.getName().equals("setProfile") || method.getParameterCount() != 1 || !method.getParameterTypes()[0].isAssignableFrom(object.getClass())) continue;
            try {
                method.setAccessible(true);
                method.invoke((Object)skullMeta, object);
                return;
            }
            catch (Throwable e) {
                return;
            }
        }
    }

    static String extractUrl(String string) {
        if (string == null) {
            return null;
        }
        int n = string.indexOf("\"url\"");
        if (n < 0) {
            return null;
        }
        int n2 = string.indexOf(34, n + 5);
        if (n2 < 0) {
            return null;
        }
        int n3 = string.indexOf(34, n2 + 1);
        if (n3 < 0) {
            return null;
        }
        return string.substring(n2 + 1, n3);
    }

    private static String encodeUrl(String string) {
        String string2 = "{\"textures\":{\"SKIN\":{\"url\":\"" + string + "\"}}}";
        return Base64.getEncoder().encodeToString(string2.getBytes());
    }
}

