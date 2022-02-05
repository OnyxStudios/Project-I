package dev.onyxstudios.projecti.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

import javax.annotation.Nullable;

public class GlowParticle extends TextureSheetParticle {


    public GlowParticle(ClientLevel level, double x, double y, double z, double targetX, double targetY, double targetZ) {
        super(level, x, y, z);

        double speed = 10 + 2 * level.random.nextDouble();
        double distance = Math.sqrt(targetX * targetX + targetY * targetY + targetZ * targetZ);
        double factor = speed / distance;

        double dirX = targetX - x;
        double dirY = targetY - y;
        double dirZ = targetZ - z;

        this.xd = dirX * factor;
        this.yd = dirY * factor;
        this.zd = dirZ * factor;
        this.lifetime = (int) Math.ceil(distance / speed);//Math.max(10, level.random.nextInt(30));
        scale(1);
        quadSize = 0.15f;
        hasPhysics = false;
    }

    @Override
    public void tick() {
        float scale = Math.max(0.25f, 1 - (age / (float) lifetime));
        scale(scale);
        quadSize = scale * 0.15f;
        super.tick();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Factory(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            float size = Math.max(0.35f, level.random.nextFloat());
            float xOffset = (level.random.nextInt(40) - 20) / 100.0f;
            float zOffset = (level.random.nextInt(40) - 20) / 100.0f;

            GlowParticle particle = new GlowParticle(level, pX + xOffset, pY, pZ + zOffset, pXSpeed, pYSpeed, pZSpeed);
            particle.pickSprite(sprites);
            particle.setSize(size, size);
            particle.setColor(level.random.nextFloat() * 1.8f, level.random.nextFloat() * 1.8f, level.random.nextFloat() * 1.8f);
            return particle;
        }
    }
}
