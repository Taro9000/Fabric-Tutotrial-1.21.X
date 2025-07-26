package net.tarou.tutorialmod.util;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModComponents {
    public static final ComponentType<Integer> UNIQUE_ID = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of("tutorialmod", "unique_id"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    public static void initialize() {
        // No-op if already called
    }
}
