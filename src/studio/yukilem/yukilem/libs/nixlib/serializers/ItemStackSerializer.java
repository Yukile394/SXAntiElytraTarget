package studio.glace.glantielytratarget.libs.nixlib.serializers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

public final class ItemStackSerializer {
    private ItemStackSerializer() {
    }

    public static String toBase64(ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();){
            String string;
            try (BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);){
                bukkitObjectOutputStream.writeObject(itemStack);
                string = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
            }
            return string;
        }
        catch (IOException e) {
            throw new IllegalStateException("failed to serialize item", e);
        }
    }

    public static Optional<ItemStack> fromBase64(String string) {
        if (string == null || string.isBlank()) {
            return Optional.empty();
        }
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(string));){
            Optional<ItemStack> optional;
            try (BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(byteArrayInputStream);){
                optional = Optional.of((ItemStack)bukkitObjectInputStream.readObject());
            }
            return optional;
        }
        catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static String toBase64Array(ItemStack[] itemStackArray) {
        if (itemStackArray == null) {
            return null;
        }
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();){
            String string;
            try (BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);){
                bukkitObjectOutputStream.writeInt(itemStackArray.length);
                for (ItemStack itemStack : itemStackArray) {
                    bukkitObjectOutputStream.writeObject(itemStack);
                }
                string = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
            }
            return string;
        }
        catch (IOException e) {
            throw new IllegalStateException("failed to serialize array", e);
        }
    }

    public static Optional<ItemStack[]> fromBase64Array(String string) {
        if (string == null || string.isBlank()) {
            return Optional.empty();
        }
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(string));){
            Optional<ItemStack[]> optional;
            try (BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(byteArrayInputStream);){
                int n = bukkitObjectInputStream.readInt();
                ItemStack[] itemStackArray = new ItemStack[n];
                for (int n2 = 0; n2 < n; ++n2) {
                    itemStackArray[n2] = (ItemStack)bukkitObjectInputStream.readObject();
                }
                optional = Optional.of(itemStackArray);
            }
            return optional;
        }
        catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}

