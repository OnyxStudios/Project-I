package dev.onyxstudios.projecti.blocks.fluid;

import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorldReader;

import java.util.function.Supplier;

public class BaseFluidBlock extends FlowingFluidBlock {

    private Vector3d color;

    public BaseFluidBlock(Supplier<FlowingFluid> fluidSupplier, Properties properties, Vector3d color) {
        super(fluidSupplier, properties);
        this.color = color;
    }

    @Override
    public Vector3d getFogColor(BlockState state, IWorldReader world, BlockPos pos, Entity entity, Vector3d originalColor, float partialTicks) {
        return color;
    }
}
