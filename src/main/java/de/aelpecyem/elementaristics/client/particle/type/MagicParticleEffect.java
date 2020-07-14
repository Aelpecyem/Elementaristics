package de.aelpecyem.elementaristics.client.particle.type;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.registry.Registry;

public class MagicParticleEffect implements ParticleEffect {
    public static final ParticleEffect.Factory<MagicParticleEffect> PARAMETERS_FACTORY = new ParticleEffect.Factory<MagicParticleEffect>() {
        public MagicParticleEffect read(ParticleType<MagicParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
            stringReader.expect(' ');
            return new MagicParticleEffect(particleType, MagicParticleInfo.read(stringReader));
        }

        public MagicParticleEffect read(ParticleType<MagicParticleEffect> particleType, PacketByteBuf packetByteBuf) {
            return new MagicParticleEffect(particleType, MagicParticleInfo.read(packetByteBuf));
        }
    };

    public static Codec<MagicParticleEffect> getCodec(ParticleType<MagicParticleEffect> type) {
        return MagicParticleInfo.CODEC.xmap((info) -> new MagicParticleEffect(type, info), (effect) -> effect.getInfo());
    }

    private final ParticleType<MagicParticleEffect> TYPE;
    private final MagicParticleInfo INFO;

    public MagicParticleEffect(ParticleType<MagicParticleEffect> type, MagicParticleInfo info) {
        this.TYPE = type;
        this.INFO = info;
    }

    @Override
    public ParticleType<?> getType() {
        return TYPE;
    }

    public MagicParticleInfo getInfo() {
        return INFO;
    }

    @Override
    public void write(PacketByteBuf buf) {
        INFO.write(buf);
    }

    @Override
    public String asString() {
        return Registry.PARTICLE_TYPE.getId(this.getType()) + " " + INFO.toString();
    }


    public static class MagicParticleInfo {
        private static final Codec<MagicParticleInfo> CODEC = RecordCodecBuilder.create((builder) -> builder.group(
                Codec.INT.fieldOf("Size").forGetter(MagicParticleInfo::getSize)
        ).apply(builder, MagicParticleInfo::new));

        private final int SIZE;

        public MagicParticleInfo(int size) {
            this.SIZE = size;
        }


        public int getSize() {
            return SIZE;
        }

        public void write(PacketByteBuf buf) {
            buf.writeInt(SIZE);
        }

        public static MagicParticleInfo read(PacketByteBuf buf) {
            return new MagicParticleInfo(buf.readInt());
        }

        public static MagicParticleInfo read(StringReader reader) throws CommandSyntaxException {
            return new MagicParticleInfo(reader.readInt());
        }

        @Override
        public String toString() {
            return SIZE + " ";
        }
    }
}
