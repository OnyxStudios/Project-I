package dev.onyxstudios.projecti.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;

import javax.annotation.Nullable;

public class GlowParticle extends SpriteTexturedParticle {


    public GlowParticle(ClientWorld world, double x, double y, double z, double targetX, double targetY, double targetZ) {
        super(world, x, y, z);

        double speed = 10 + 2 * world.random.nextDouble();
        double distance = Math.sqrt(targetX * targetX + targetY * targetY + targetZ * targetZ);
        double factor = speed / distance;

        double dirX = targetX - x;
        double dirY = targetY - y;
        double dirZ = targetZ - z;

        this.xd = dirX * factor;
        this.yd = dirY * factor;
        this.zd = dirZ * factor;
        this.lifetime = (int) Math.ceil(distance / speed);//Math.max(10, world.random.nextInt(30));
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
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements IParticleFactory<BasicParticleType> {

        private final IAnimatedSprite sprites;

        public Factory(IAnimatedSprite sprites) {
            this.sprites = sprites;
        }

        @Nullable
        @Override
        public Particle createParticle(BasicParticleType type, ClientWorld world, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            float size = Math.max(0.35f, world.random.nextFloat());
            float xOffset = (world.random.nextInt(40) - 20) / 100.0f;
            float zOffset = (world.random.nextInt(40) - 20) / 100.0f;

            GlowParticle particle = new GlowParticle(world, pX + xOffset, pY, pZ + zOffset, pXSpeed, pYSpeed, pZSpeed);
            particle.pickSprite(sprites);
            particle.setSize(size, size);
            particle.setColor(world.random.nextFloat() * 1.8f, world.random.nextFloat() * 1.8f, world.random.nextFloat() * 1.8f);
            return particle;
        }
    }
}
