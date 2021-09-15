package com.datdeveloper.randomspawn2;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = RandomSpawn2.MOD_ID)
public class RandomConfig {
    @Config.Name("Save Spawn")
    @Config.Comment("Should the player spawn at the same place every time they go to spawn")
    public static boolean saveSpawn = true;

    @Config.Name("Specific Spawn dimension")
    @Config.Comment("Whether the player should always go to a specific dimension when they type /spawn, set the dimension with \"Spawn dimension\"")
    public static boolean forceSpawnDimension = false;

    @Config.Name("Spawn dimension")
    @Config.Comment("Which dimension the player should go to when they type /spawn, cannot be one of the excluded dimensions, only works when \"Specific Spawn Dimension\" is set")
    public static int spawnDimension = 0;

    @Config.Name("/Spawn Delay")
    @Config.Comment("How long the player must wait to teleport to spawn (0 for disabled)")
    @Config.RangeInt(min = 0)
    public static int spawnDelay = 5;

    @Config.Name("/Spawn Cooldown")
    @Config.Comment("How long the player must wait before they can use /spawn again (0 for disabled)")
    @Config.RangeInt(min = 0)
    public static int spawnReDelay = 5;

    @Config.Name("Spawn Radius X")
    @Config.Comment("The maximum distance from the origin that the player can spawn in the x direction")
    @Config.RangeInt(min = 0)
    public static int spawnDistanceX = 1000;

    @Config.Name("Spawn Radius Z")
    @Config.Comment("The maximum distance from the origin that the player can spawn in the Z direction")
    @Config.RangeInt(min = 0)
    public static int spawnDistanceZ = 1000;

    @Config.Name("Spawn Centre X")
    @Config.Comment("The x component of the centre of the are where players spawn")
    public static int spawnX = 0;

    @Config.Name("Spawn Centre Z")
    @Config.Comment("The z component of the centre of the are where players spawn")
    public static int spawnZ = 0;

    @Config.Name("Dimension Exclusions")
    @Config.Comment("Dimensions that do not have their spawns randomised")
    public static int[] exclusions = {-1, 1};

    @Config.Name("Safe Spawn")
    @Config.Comment("Whether to reset the player's spawn location when teleporting with /spawn or when respawning if it would cause you to drop too far")
    public static boolean safeSpawn = true;

    @Mod.EventBusSubscriber(modid = RandomSpawn2.MOD_ID)
    private static class EventHandler {
        @SubscribeEvent
        public static void configChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(RandomSpawn2.MOD_ID)) {
                ConfigManager.sync(RandomSpawn2.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}
