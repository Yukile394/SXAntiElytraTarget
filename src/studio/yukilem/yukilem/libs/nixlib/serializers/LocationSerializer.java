package studio.glace.glantielytratarget.libs.nixlib.serializers;

import java.util.Locale;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public final class LocationSerializer {
    private LocationSerializer() {
    }

    public static String toString(Location location) {
        if (location == null) {
            return null;
        }
        World world = location.getWorld();
        String string = world == null ? "" : world.getName();
        return String.format(Locale.ROOT, "%s;%.4f;%.4f;%.4f;%.2f;%.2f", string, location.getX(), location.getY(), location.getZ(), Float.valueOf(location.getYaw()), Float.valueOf(location.getPitch()));
    }

    public static Optional<Location> fromString(String string) {
        if (string == null || string.isBlank()) {
            return Optional.empty();
        }
        String[] stringArray = string.split(";");
        if (stringArray.length < 4) {
            return Optional.empty();
        }
        try {
            World world = Bukkit.getWorld(stringArray[0]);
            double d = Double.parseDouble(stringArray[1]);
            double d2 = Double.parseDouble(stringArray[2]);
            double d3 = Double.parseDouble(stringArray[3]);
            float f = stringArray.length > 4 ? Float.parseFloat(stringArray[4]) : 0.0f;
            float f2 = stringArray.length > 5 ? Float.parseFloat(stringArray[5]) : 0.0f;
            return Optional.of(new Location(world, d, d2, d3, f, f2));
        }
        catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}

