package de.aelpecyem.elementaristics.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntityWithAi;
import net.minecraft.world.World;

public class EntityNexus extends MobEntityWithAi {
    //nexus can spawn, but again only server side... maybe see how the packet is used in item entity
    public EntityNexus(EntityType<? extends MobEntityWithAi> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public boolean doesRenderOnFire() {
        return false;
    }
}
