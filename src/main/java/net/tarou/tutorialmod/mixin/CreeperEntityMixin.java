package net.tarou.tutorialmod.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.entity.projectile.AbstractWindChargeEntity.EXPLOSION_BEHAVIOR;


@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin {

    @Shadow
    private int explosionRadius;


    @Inject(method = "explode", at = @At("HEAD"), cancellable = true)
    private void explode(CallbackInfo ci) {
        CreeperEntity self = (CreeperEntity)(Object)this;
        CreeperEntityInvoker invoker = (CreeperEntityInvoker) self;
        LivingEntityAccessor selfAccessor = (LivingEntityAccessor) self;

        if (!self.getWorld().isClient) {
            float f = self.shouldRenderOverlay() ? 2.0F : 1.0F;
            float power = this.explosionRadius * f;

            selfAccessor.setDead(true);

            World world = self.getWorld();
            for (int i = 0; i < 6; i++) {
                float height = world.random.nextFloat() * 2 - 1;
                ItemEntity itemEntity = new ItemEntity(
                        world,
                        self.getX() + (world.random.nextFloat() * 2 - 1) * height * power * 0.25,
                        self.getY() + height,
                        self.getZ() + (world.random.nextFloat() * 2 - 1) * height * power * 0.25,
                        new ItemStack(Items.EGG));
                itemEntity.setToDefaultPickupDelay();
                world.spawnEntity(itemEntity);
            }
            world.createExplosion(
                    self,
                    null,
                    EXPLOSION_BEHAVIOR,
                    self.getX(),
                    self.getY(),
                    self.getZ(),
                    power,
                    false,
                    World.ExplosionSourceType.TRIGGER,
                    ParticleTypes.GUST_EMITTER_SMALL,
                    ParticleTypes.GUST_EMITTER_LARGE,
                    SoundEvents.ENTITY_BREEZE_WIND_BURST
            );

            invoker.invokeSpawnEffectsCloud();
            selfAccessor.invokeOnRemoval(Entity.RemovalReason.KILLED);
            self.discard();
        }
        ci.cancel(); // Prevents original explode method from running
    }
}

