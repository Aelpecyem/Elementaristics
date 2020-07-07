package de.aelpecyem.elementaristics.registry;

import de.aelpecyem.elementaristics.client.render.entity.RenderNexus;
import de.aelpecyem.elementaristics.common.entity.EntityNexus;
import de.aelpecyem.elementaristics.lib.Util;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;

public class ModEntities {
    public static final EntityType<EntityNexus> NEXUS = FabricEntityTypeBuilder.create(SpawnGroup.MISC, EntityNexus::new).dimensions(EntityDimensions.fixed(0.5F, 0.5F)).fireImmune().build();

    public static void init() {
        Util.register(Registry.ENTITY_TYPE, "nexus", NEXUS);
        FabricDefaultAttributeRegistry.register(NEXUS, EntityNexus.createMobAttributes());
    }

    @Environment(EnvType.CLIENT)
    public static void registerRenderers() {
        EntityRendererRegistry.INSTANCE.register(NEXUS, (entityRenderDispatcher, context) -> new RenderNexus(entityRenderDispatcher));
    }
}
