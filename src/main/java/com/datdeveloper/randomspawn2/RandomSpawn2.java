package com.datdeveloper.randomspawn2;

import com.datdeveloper.randomspawn2.capability.IRespawn;
import com.datdeveloper.randomspawn2.capability.Respawn;
import com.datdeveloper.randomspawn2.capability.RespawnStorage;
import com.datdeveloper.randomspawn2.commands.CommandRegister;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = RandomSpawn2.MOD_ID,
        name = RandomSpawn2.MOD_NAME,
        version = RandomSpawn2.VERSION,
        acceptableRemoteVersions = "*",
        dependencies = "required-after:datmoddingapi@[1.2.1,)"
)
public class RandomSpawn2 {
    public static final String MOD_ID = "randomspawn2";
    public static final String MOD_NAME = "Random Spawn 2";
    public static final String VERSION = "0.1.0";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    // TODO: Commands, dimension exclusions

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MOD_ID)
    public static RandomSpawn2 INSTANCE;

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        CapabilityManager.INSTANCE.register(IRespawn.class, new RespawnStorage(), Respawn::new);

        // Check if the spawn dimension is broken
        if (RandomConfig.forceSpawnDimension && Util.isDimensionExcluded(RandomConfig.spawnDimension)) {
            LOGGER.warn("Spawn dimension has been set to a dimension that is excluded, to maintain function, force spawn dimension has been disabled");
            RandomConfig.forceSpawnDimension = false;
            ConfigManager.sync(MOD_ID, Config.Type.INSTANCE);
        }
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Register Permissions
        CommandRegister.registerPermissionNodes();
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent e) {
        // Register Commands
        CommandRegister.registerCommands(e);
    }
}
