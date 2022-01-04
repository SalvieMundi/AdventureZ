package net.adventurez.init;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.mixin.tag.extension.MixinMinecraftServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.ImmutableMap;

import net.adventurez.entity.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.BuiltinBiomes;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.SpawnSettings;

public class SpawnInit {
	public static final String[] deerBiomes = { "FOREST", "TAIGA", "PLAINS" };
	public static final String[] fungusBiomes = { "MUSHROOM", "DARK FOREST", "BIOME.VANILLAPLUSBIOMES.ANCIENT_DARK_FOREST" };
	public static final String[] orcBiomes = { "PLAINS", "BIOME.TRAVERSE.CLIFFS", "UNDERGROUND" };
	public static final String[] mammothBiomes = { "ICY", "SNOWY TAIGA", "BIOME.TRAVERSE.SNOWY_CONIFEROUS_FOREST", "SNOWY TUNDRA" };
	public static final String[] iguanaBiomes = { "MESA", "BIOME.TRAVERSE.ARID_HIGHLANDS", "BIOME.TRAVERSE.DESERT_SHRUBLAND", "BIOME.VANILLAPLUSBIOMES.OASIS" };
	public static final String[] desertRhinoBiomes = { "DESERT" };
	public static final String[] shamanBiomes = { "SWAMP" };

    public static void init() {
        setSpawnRestriction();
    	addSpawnEntries(deerBiomes, SpawnGroup.CREATURE, EntityInit.DEER_ENTITY, ConfigInit.CONFIG.deer_spawn_weight, 2, 3);
    	addSpawnEntries(BuiltinRegistries.BIOME.get(BiomeKeys.BASALT_DELTAS));
    	addSpawnEntries(BuiltinRegistries.BIOME.get(BiomeKeys.SOUL_SAND_VALLEY));
    	addSpawnEntries(fungusBiomes, SpawnGroup.MONSTER, EntityInit.RED_FUNGUS_ENTITY, ConfigInit.CONFIG.fungus_spawn_weight, 2, 3);
    	addSpawnEntries(fungusBiomes, SpawnGroup.MONSTER, EntityInit.BROWN_FUNGUS_ENTITY, ConfigInit.CONFIG.fungus_spawn_weight, 2, 3);
    	addSpawnEntries(orcBiomes, SpawnGroup.MONSTER, EntityInit.ORC_ENTITY, ConfigInit.CONFIG.orc_spawn_weight, 1, 4);
    	addSpawnEntries(mammothBiomes, SpawnGroup.CREATURE, EntityInit.MAMMOTH_ENTITY, ConfigInit.CONFIG.mammoth_spawn_weight, 1, 2);
    	addSpawnEntries(iguanaBiomes, SpawnGroup.CREATURE, EntityInit.IGUANA_ENTITY, ConfigInit.CONFIG.iguana_spawn_weight, 1, 2);
    	addSpawnEntries(desertRhinoBiomes, SpawnGroup.CREATURE, EntityInit.DESERT_RHINO_ENTITY, ConfigInit.CONFIG.desert_rhino_spawn_weight, 1, 1);
    	addSpawnEntries(shamanBiomes, SpawnGroup.MONSTER, EntityInit.SHAMAN_ENTITY, ConfigInit.CONFIG.shaman_spawn_weight, 1, 1);
    	for (Biome biome : BuiltinRegistries.BIOME) {
        	if (biome.getCategory() == Biome.Category.THEEND) addSpawnEntries(biome);
    	}
        //RegistryEntryAddedCallback.event(BuiltinRegistries.BIOME).register((i, identifier, biome) -> SpawnInit.addSpawnEntries(biome));
    }

    private static void addSpawnEntries(String[] spawnBiomes, SpawnGroup spawnGroup, EntityType<?> entity, int spawnWeight, int minGroup, int maxGroup) {
    	BiomeModifications.addSpawn(
				selection -> Arrays.stream(spawnBiomes).anyMatch(x -> x.equalsIgnoreCase(selection.getBiome().getCategory().getName()) || x.equalsIgnoreCase((new TranslatableText(Util.createTranslationKey("biome", BuiltinRegistries.BIOME.getId(selection.getBiome())))).getString())),
				spawnGroup,
				entity,
				spawnWeight, minGroup, maxGroup // weight/min group size/max group size
		);
    }
    
    // MONSTER tries to spawn often, CREATURE tries more rarely to spawn + in groups
    private static void addSpawnEntries(Biome biome) {
        if (biome.getCategory().equals(Biome.Category.NETHER)) {
            addMobSpawnToBiome(biome, SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityInit.SMALLSTONEGOLEM_ENTITY, ConfigInit.CONFIG.small_stone_golem_spawn_weight, 1, 1),
                    //new SpawnSettings.SpawnEntry(EntityInit.BLAZEGUARDIAN_ENTITY, ConfigInit.CONFIG.blaze_guardian_spawn_weight, 1, 1),
                    new SpawnSettings.SpawnEntry(EntityInit.SOULREAPER_ENTITY, ConfigInit.CONFIG.nightmare_spawn_weight, 1, 1));
        }
//        if (biome.getCategory().equals(Biome.Category.MUSHROOM)) {
//            addMobSpawnToBiome(biome, SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityInit.BROWN_FUNGUS_ENTITY, ConfigInit.CONFIG.fungus_spawn_weight, 2, 3),
//                    new SpawnSettings.SpawnEntry(EntityInit.RED_FUNGUS_ENTITY, ConfigInit.CONFIG.fungus_spawn_weight, 2, 3));
//        }
//        if (biome.getCategory().equals(Biome.Category.PLAINS)) {
//            addMobSpawnToBiome(biome, SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityInit.ORC_ENTITY, ConfigInit.CONFIG.orc_spawn_weight, 2, 4));
//        }
//        if (biome.getCategory().equals(Biome.Category.FOREST)) {
//            addMobSpawnToBiome(biome, SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityInit.DEER_ENTITY, ConfigInit.CONFIG.deer_spawn_weight, 2, 3));
//        }
//        if (biome.getCategory().equals(Biome.Category.ICY)/* || biome.getCategory().equals(Biome.Category.TAIGA)*/) {
//            addMobSpawnToBiome(biome, SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityInit.MAMMOTH_ENTITY, ConfigInit.CONFIG.mammoth_spawn_weight, 2, 2));
//        }
        if (biome.getCategory().equals(Biome.Category.THEEND)) {
            addMobSpawnToBiome(biome, SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityInit.ENDER_WHALE_ENTITY, ConfigInit.CONFIG.ender_whale_spawn_weight, 1, 1));
        }
//        if (biome.getCategory().equals(Biome.Category.MESA)) {
//            addMobSpawnToBiome(biome, SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityInit.IGUANA_ENTITY, ConfigInit.CONFIG.iguana_spawn_weight, 1, 2));
//        }
//        if (biome.getCategory().equals(Biome.Category.DESERT)) {
//            addMobSpawnToBiome(biome, SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityInit.DESERT_RHINO_ENTITY, ConfigInit.CONFIG.desert_rhino_spawn_weight, 1, 1));
//        }
//        if (biome.getCategory().equals(Biome.Category.SWAMP)) {
//            addMobSpawnToBiome(biome, SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityInit.SHAMAN_ENTITY, ConfigInit.CONFIG.shaman_spawn_weight, 1, 1));
//        }
        if (biome.getCategory().equals(Biome.Category.THEEND)) {
            addMobSpawnToBiome(biome, SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityInit.ENDERWARTHOG_ENTITY, ConfigInit.CONFIG.enderwarthog_spawn_weight, 1, 1));
        }
    }

    private static void addMobSpawnToBiome(Biome biome, SpawnGroup classification, SpawnSettings.SpawnEntry... spawners) {
        convertImmutableSpawners(biome);
        List<SpawnSettings.SpawnEntry> spawnersList = new ArrayList<>(biome.getSpawnSettings().spawners.get(classification).getEntries());
        spawnersList.addAll(Arrays.asList(spawners));
        biome.getSpawnSettings().spawners.put(classification, Pool.of(spawnersList));
    }

    private static void convertImmutableSpawners(Biome biome) {
        if (biome.getSpawnSettings().spawners instanceof ImmutableMap) {
            biome.getSpawnSettings().spawners = new HashMap<>(biome.getSpawnSettings().spawners);
        }
    }

    private static void setSpawnRestriction() {
        SpawnRestriction.register(EntityInit.SMALLSTONEGOLEM_ENTITY, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SmallStoneGolemEntity::canSpawn);
        SpawnRestriction.register(EntityInit.NECROMANCER_ENTITY, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, NecromancerEntity::canSpawn);
        SpawnRestriction.register(EntityInit.SUMMONER_ENTITY, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SummonerEntity::canSpawn);
        //SpawnRestriction.register(EntityInit.BLAZEGUARDIAN_ENTITY, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, BlazeGuardianEntity::canSpawn);
        SpawnRestriction.register(EntityInit.PIGLINBEAST_ENTITY, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnIgnoreLightLevel);
        SpawnRestriction.register(EntityInit.SOULREAPER_ENTITY, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SoulReaperEntity::canSpawn);
        SpawnRestriction.register(EntityInit.RED_FUNGUS_ENTITY, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, RedFungusEntity::canSpawn);
        SpawnRestriction.register(EntityInit.BROWN_FUNGUS_ENTITY, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, BrownFungusEntity::canSpawn);
        SpawnRestriction.register(EntityInit.ORC_ENTITY, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, OrcEntity::canSpawn);
        SpawnRestriction.register(EntityInit.MAMMOTH_ENTITY, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MammothEntity::canSpawn);
        SpawnRestriction.register(EntityInit.ENDER_WHALE_ENTITY, SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EnderWhaleEntity::canSpawn);
        SpawnRestriction.register(EntityInit.IGUANA_ENTITY, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, IguanaEntity::canSpawn);
        SpawnRestriction.register(EntityInit.DESERT_RHINO_ENTITY, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DesertRhinoEntity::canSpawn);
        SpawnRestriction.register(EntityInit.SHAMAN_ENTITY, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ShamanEntity::canSpawn);
        SpawnRestriction.register(EntityInit.DEER_ENTITY, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DeerEntity::canSpawn);
        SpawnRestriction.register(EntityInit.ENDERWARTHOG_ENTITY, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EnderwarthogEntity::canSpawn);
    }

}
