package net.tarou.tutorialmod.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {

    //@Accessor("dead")
    //boolean getDead();

    @Accessor("dead")
    void setDead(boolean dead);

    @Invoker("onRemoval")
    void invokeOnRemoval(Entity.RemovalReason reason);
}
