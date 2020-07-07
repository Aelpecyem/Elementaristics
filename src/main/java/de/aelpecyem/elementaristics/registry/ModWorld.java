package de.aelpecyem.elementaristics.registry;

import de.aelpecyem.elementaristics.common.block.BlockMorningGloryPlant;
import de.aelpecyem.elementaristics.lib.DummyConfig;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;

public class ModWorld {
    public static final RandomPatchFeatureConfig MORNING_GLORY_CONFIG = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(ModObjects.MORNING_GLORY.getDefaultState().with(BlockMorningGloryPlant.AGE, 4)), SimpleBlockPlacer.field_24871)).spreadX(3).spreadZ(3).spreadY(5).tries(48).build();
    public static void init(){
        addVanillaBiomeFeatures();
    }

    private static void addVanillaBiomeFeatures() {
        Registry.BIOME.forEach(biome -> {
            if (biome.getCategory() == Biome.Category.JUNGLE || biome.getCategory() == Biome.Category.FOREST) {
                biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.FLOWER.configure(MORNING_GLORY_CONFIG).createDecoratedFeature(Decorator.CHANCE_HEIGHTMAP.configure(new ChanceDecoratorConfig(DummyConfig.morning_glory_chance))));
            }
        });
    }
}
