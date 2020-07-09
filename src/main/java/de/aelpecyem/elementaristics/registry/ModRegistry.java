package de.aelpecyem.elementaristics.registry;

import de.aelpecyem.elementaristics.common.feature.potion.StatusEffectIntoxicated;
import de.aelpecyem.elementaristics.common.feature.stats.AscensionPath;
import de.aelpecyem.elementaristics.lib.Constants;
import de.aelpecyem.elementaristics.lib.Util;
import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.TrinketSlots;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModRegistry {
    public static AscensionPath STANDARD_PATH = new AscensionPath("standard");

    public static final StatusEffect INTOXICATED = new StatusEffectIntoxicated();

    public static void init() {
        initTrackedData();
        ModObjects.init();
        ModWorld.init();
        ModEntities.init();
        ModCommands.registerCommands();
        initAscensionPaths();
        initTrinketSlots();
        initStatusEffects();
    }

    private static void initTrackedData() {
        TrackedDataHandlerRegistry.register(Constants.DataTrackers.ATTUNEMENT_TRACKER);
    }

    private static void initStatusEffects() {
        Util.register(Registry.STATUS_EFFECT, "intoxicated", INTOXICATED);
    }

    private static void initTrinketSlots() {
        TrinketSlots.addSlot(SlotGroups.LEGS, Slots.BELT, new Identifier("trinkets", "textures/item/empty_trinket_slot_belt.png"));

    }

    private static void initAscensionPaths() {
        STANDARD_PATH = new AscensionPath("standard");
    }
}
