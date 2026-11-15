package studio.glace.glantielytratarget.util.entity;

import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class EntityLookup {
    private static final double NEARBY_RADIUS = 32.0;

    private EntityLookup() {
    }

    @Nullable
    public static Entity byNetworkId(@NotNull Player player, int n) {
        Iterator<Entity> iterator;
        World world = player.getWorld();
        Location location = player.getLocation();
        World world2 = world;
        Location location2 = location;
        double d = 32.0;
        double d2 = 32.0;
        double d3 = 32.0;
        Iterator<Entity> iterator2 = world2.getNearbyEntities(location2, d, d2, d3).iterator();
        while ((iterator = iterator2).hasNext()) {
            Iterator<Entity> iterator3 = iterator2;
            Entity entity = iterator3.next();
            Entity entity2 = entity;
            if (entity2.getEntityId() != n) continue;
            Entity entity3 = entity;
            return entity3;
        }
        return null;
    }
}

