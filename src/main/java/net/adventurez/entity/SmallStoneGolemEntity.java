package net.adventurez.entity;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import net.adventurez.init.SoundInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public class SmallStoneGolemEntity extends HostileEntity {

    public SmallStoneGolemEntity(EntityType<? extends SmallStoneGolemEntity> entityType, World world) {
        super(entityType, world);
        this.stepHeight = 1.0F;
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 0.85D, true));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.85D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.add(3, new FollowTargetGoal<>(this, PlayerEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder createSmallStoneGolemAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 50.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.27D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 2.5D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0D).add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 38.0D);
    }

    public static boolean canSpawn(EntityType<SmallStoneGolemEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        //System.out.println("Trying to spawn " + type.toString() + " on " + world.getBlockState(pos.down()).getBlock().toString() + " in " + (new TranslatableText(Util.createTranslationKey("biome", world.getRegistryManager().get(Registry.BIOME_KEY).getId(world.getBiome(pos))))).getString() + " (light level: " + world.getBaseLightLevel(pos, 0) + ") at x:" + pos.getX() + ", y:" + pos.getY() + ", z:" + pos.getZ() + " for reason: " + spawnReason.toString() + "!");
        if (world.getDifficulty() != Difficulty.PEACEFUL && world.getBlockState(pos.down()).isOf(Blocks.BASALT) || world.getBlockState(pos.down()).isOf(Blocks.BLACKSTONE)) {
        	//System.out.println("Spawn should have succeeded!");
        	return true;
        } else {
        	return false;
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundInit.SMALL_GOLEM_IDLE_EVENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundInit.SMALL_GOLEM_HIT_EVENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundInit.SMALL_GOLEM_DEATH_EVENT;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundInit.SMALL_GOLEM_WALK_EVENT, 0.15F, 1.0F);
    }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.DEFAULT;
    }

    @Override
    public int getLimitPerChunk() {
        return 1;
    }

    @Override
    public boolean canSpawn(WorldView view) {
        BlockPos posentity = new BlockPos(this.getX(), this.getY(), this.getZ());
        return view.intersectsEntities(this) && !world.containsFluid(this.getBoundingBox()) && this.world.getBlockState(posentity).getBlock().canMobSpawnInside();
    }

}