package de.aelpecyem.elementaristics.common.feature.potion;

import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import vazkii.patchouli.client.shader.ShaderHelper;

public class StatusEffectIntoxicated extends StatusEffect {
    public StatusEffectIntoxicated() {
        super(StatusEffectType.NEUTRAL, 0x13EB3E);
    }

    @Override
    public void applyInstantEffect(Entity source, Entity attacker, LivingEntity target, int amplifier, double proximity) {

        super.applyInstantEffect(source, attacker, target, amplifier, proximity);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        //add shader effect and stumbling, also some glow particles
        if (entity instanceof PlayerEntity && entity.world.isClient){
            ((ClientPlayerEntity) entity).nextNauseaStrength = MathHelper.lerp(Math.abs(20 -(Math.min(amplifier, 3)) -(entity.age % 40 -(Math.min(amplifier * 2, 6)))) / 20F, 0, 0.2F);
        }
        super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (entity instanceof PlayerEntity && entity.world.isClient){
            ((ClientPlayerEntity) entity).nextNauseaStrength = 0;
            ((ClientPlayerEntity) entity).lastNauseaStrength = 0;
        }
        super.onRemoved(entity, attributes, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
