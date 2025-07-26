package net.tarou.tutorialmod;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.tarou.tutorialmod.block.ModBlocks;
import net.tarou.tutorialmod.item.ModItemGroups;
import net.tarou.tutorialmod.item.ModItems;
import net.tarou.tutorialmod.util.ModComponents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// Very important comment
public class TutorialMod implements ModInitializer {
	public static final String MOD_ID = "tutorialmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();

		ModComponents.initialize();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();

		FuelRegistry.INSTANCE.add(ModItems.STARLIGHT_ASHES, 600);
	}
}