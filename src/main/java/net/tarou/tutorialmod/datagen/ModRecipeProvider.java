package net.tarou.tutorialmod.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SmokingRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.tarou.tutorialmod.block.ModBlocks;
import net.tarou.tutorialmod.item.ModItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter recipeExporter) {
        List<ItemConvertible> PINK_GARNET_SMELTABLES = List.of(
                ModItems.RAW_PINK_GARNET,
                ModBlocks.PINK_GARNET_ORE,
                ModBlocks.PINK_GARNET_DEEPSLATE_ORE);

        offerSmelting(recipeExporter, PINK_GARNET_SMELTABLES, RecipeCategory.MISC, ModItems.PINK_GARNET, 0.25f, 200, "pink_garnet");
        offerBlasting(recipeExporter, PINK_GARNET_SMELTABLES, RecipeCategory.MISC, ModItems.PINK_GARNET, 0.25f, 100, "pink_garnet");

        offerReversibleCompactingRecipes(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModItems.PINK_GARNET,     RecipeCategory.DECORATIONS, ModBlocks.PINK_GARNET_BLOCK);
        offerReversibleCompactingRecipes(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModItems.RAW_PINK_GARNET, RecipeCategory.DECORATIONS, ModBlocks.RAW_PINK_GARNET_BLOCK);

        SmithingTransformRecipeJsonBuilder.create(
                Ingredient.ofItems(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.ofItems(Items.NETHERITE_AXE), Ingredient.ofItems(ModItems.PINK_GARNET), RecipeCategory.TOOLS, ModItems.CHISEL
        ).criterion("has_netherite_ingot", conditionsFromItem(Items.NETHERITE_INGOT)).offerTo(recipeExporter, getItemPath(ModItems.CHISEL) + "_smithing");


        List<ItemConvertible> EGG = List.of(
                Items.EGG);

        offerSmelting(recipeExporter,     EGG, RecipeCategory.FOOD, ModItems.FRIED_EGG, 0.35f, 200, "egg");
        CookingRecipeJsonBuilder.create(
                Ingredient.ofItems(Items.EGG), RecipeCategory.FOOD, ModItems.FRIED_EGG, 0.35f, 100, RecipeSerializer.SMOKING,                  SmokingRecipe::new
        ).criterion(hasItem(EGG.getFirst()), conditionsFromItem(EGG.getFirst())).offerTo(recipeExporter, getItemPath(ModItems.FRIED_EGG) + "_from_smoking");
        CookingRecipeJsonBuilder.create(
                Ingredient.ofItems(Items.EGG), RecipeCategory.FOOD, ModItems.FRIED_EGG, 0.35f, 600, RecipeSerializer.CAMPFIRE_COOKING, CampfireCookingRecipe::new
        ).criterion(hasItem(EGG.getFirst()), conditionsFromItem(EGG.getFirst())).offerTo(recipeExporter, getItemPath(ModItems.FRIED_EGG) + "_from_campfire_cooking");
    }
}
