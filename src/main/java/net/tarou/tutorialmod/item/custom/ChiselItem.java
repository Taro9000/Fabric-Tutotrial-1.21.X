package net.tarou.tutorialmod.item.custom;

import net.minecraft.block.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ChiselItem extends Item {
    public static final String prefix = "stripped_";

    public ChiselItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        // Make sure the context is a server
        final World world = context.getWorld();
        if (world.isClient())
            return ActionResult.SUCCESS;

        // bootleg AxeItem.tryStrip logic
        // at-least it's not as nested as the real "enterprise code"
        final BlockPos blockPos = context.getBlockPos();
        final BlockState blockState = world.getBlockState(blockPos);
        final Identifier identifier = Registries.BLOCK.getId(blockState.getBlock());

        final String namespace = identifier.getNamespace();
        final String path      = identifier.getPath();

        final boolean strippedBlock = path.startsWith(prefix);

        final Optional<Block> candidateBlock = Registries.BLOCK.getOrEmpty(Identifier.of(namespace, strippedBlock
                ? path.substring(prefix.length())
                : prefix + path
        ));

        if(candidateBlock.isEmpty())
            return ActionResult.SUCCESS;

        // voodoo OOP dark magic to change the block
        final Block electedBlock = candidateBlock.get();

        world.setBlockState(blockPos, electedBlock.getDefaultState().with(PillarBlock.AXIS, blockState.get(PillarBlock.AXIS)), Block.NOTIFY_ALL_AND_REDRAW);

        context.getStack().damage(1, (ServerWorld) world, (ServerPlayerEntity) context.getPlayer(),
                item -> Objects.requireNonNull(context.getPlayer()).sendEquipmentBreakStatus(item, EquipmentSlot.MAINHAND));


        final SoundEvent sound = strippedBlock ? SoundEvents.ITEM_AXE_SCRAPE : SoundEvents.ITEM_AXE_STRIP;
        world.playSound(null, blockPos, sound,  SoundCategory.BLOCKS);

        return ActionResult.SUCCESS;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (Screen.hasShiftDown())
            tooltip.add(Text.translatable("tooltip.tutorialmod.chisel.shift_down"));
        else
            tooltip.add(Text.translatable("tooltip.tutorialmod.chisel"));
        super.appendTooltip(stack, context, tooltip, type);
    }
}
