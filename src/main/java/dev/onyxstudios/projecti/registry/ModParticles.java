package dev.onyxstudios.projecti.registry;

import dev.onyxstudios.projecti.ProjectI;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModParticles {

    public static DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ProjectI.MODID);

    public static RegistryObject<BasicParticleType> GLOW = PARTICLES.register("glow", () -> new BasicParticleType(false));
}
