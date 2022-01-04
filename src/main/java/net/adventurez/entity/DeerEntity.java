package net.adventurez.entity;

import java.util.EnumSet;
import java.util.Random;

import net.adventurez.init.EntityInit;
import net.adventurez.init.SoundInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

public class DeerEntity extends AnimalEntity {

    public DeerEntity(EntityType<? extends DeerEntity> entityType, World world) {
        super(entityType, world);
        this.stepHeight = 1.0F;
    }

    public static DefaultAttributeContainer.Builder createDeerAttributes() {
        return AnimalEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.215D);
    }

    public static boolean canSpawn(EntityType<DeerEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
    	//System.out.println("Trying to spawn " + type.toString() + " on " + world.getBlockState(pos.down()).getBlock().toString() + " in " + (new TranslatableText(Util.createTranslationKey("biome", world.getRegistryManager().get(Registry.BIOME_KEY).getId(world.getBiome(pos))))).getString() + " (light level: " + world.getBaseLightLevel(pos, 0) + ") at x:" + pos.getX() + ", y:" + pos.getY() + ", z:" + pos.getZ() + " for reason: " + spawnReason.toString() + "!");
        if (world.getBlockState(pos.down()).isOf(Blocks.GRASS_BLOCK) && world.getBaseLightLevel(pos, 0) > 8) {
        	//System.out.println("Spawn should have succeeded!");
        	return true;
        } else {
        	return false;
        }
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 2.0D));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0D));
        this.goalSelector.add(3, new TemptGoal(this, 1.22D, Ingredient.ofItems(Items.GRASS, Items.TALL_GRASS, Items.FERN), true));
        this.goalSelector.add(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.add(5, new EscapePlayerGoal(this, 2.0D));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isBaby() ? SoundInit.BABY_DEER_IDLE_EVENT : SoundInit.DEER_IDLE_EVENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return this.isBaby() ? SoundInit.BABY_DEER_HURT_EVENT : SoundInit.DEER_HURT_EVENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isBaby() ? SoundInit.BABY_DEER_HURT_EVENT : SoundInit.DEER_DEATH_EVENT;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
    }

    @Override
    public DeerEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        return EntityInit.DEER_ENTITY.create(serverWorld);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.GRASS) || stack.isOf(Items.FERN) || stack.isOf(Items.TALL_GRASS);
    }

    private class EscapePlayerGoal extends Goal {
        protected final DeerEntity deerEntity;
        protected final double speed;
        protected double targetX;
        protected double targetY;
        protected double targetZ;

        public EscapePlayerGoal(DeerEntity deerEntity, double speed) {
            this.deerEntity = deerEntity;
            this.speed = speed;
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canStart() {
            if (this.deerEntity.world.getClosestPlayer(this.deerEntity, 4.0D) == null) {
                return false;
            } else {
                return this.findTarget();
            }
        }

        private boolean findTarget() {
            Vec3d vec3d = NoPenaltyTargeting.find(this.deerEntity, 12, 4);
            if (vec3d == null) {
                return false;
            } else {
                this.targetX = vec3d.x;
                this.targetY = vec3d.y;
                this.targetZ = vec3d.z;
                return true;
            }
        }

        @Override
        public void start() {
            this.deerEntity.getNavigation().startMovingTo(this.targetX, this.targetY, this.targetZ, this.speed);
        }

        @Override
        public boolean shouldContinue() {
            return !this.deerEntity.getNavigation().isIdle();
        }

    }

}
