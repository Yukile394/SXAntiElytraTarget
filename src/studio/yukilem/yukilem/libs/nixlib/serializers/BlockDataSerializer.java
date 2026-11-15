package studio.yukilem.yukilem.libs.nixlib.serializers;

import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;

public final class BlockDataSerializer {
    private BlockDataSerializer() {
    }

    public static String toString(BlockData blockData) {
        return blockData == null ? null : blockData.getAsString();
    }

    public static Optional<BlockData> fromString(String string) {
        if (string == null || string.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(Bukkit.createBlockData(string));
        }
        catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}

