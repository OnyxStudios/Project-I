package dev.onyxstudios.projecti.blocks.fluid;

import dev.onyxstudios.projecti.ProjectI;
import dev.onyxstudios.projecti.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.LavaFluid;
import net.minecraftforge.fluids.FluidAttributes;

public abstract class FluidMoltenBlueCrystal extends LavaFluid {

    @Override
    public Fluid getFlowing() {
        return ModBlocks.FLOWING_MOLTEN_BLUE_CRYSTAL;
    }

    @Override
    public Fluid getSource() {
        return ModBlocks.MOLTEN_BLUE_CRYSTAL;
    }

    @Override
    public Item getBucket() {
        return ModBlocks.BLUE_CRYSTAL_BUCKET.get();
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        super.beforeDestroyingBlock(level, pos, state);
    }

    @Override
    public int getSlopeFindDistance(LevelReader level) {
        return 4;
    }

    @Override
    public int getDropOff(LevelReader level) {
        return 1;
    }

    @Override
    public boolean canBeReplacedWith(FluidState fluidState, BlockGetter blockGetter, BlockPos blockPos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && fluid.is(FluidTags.WATER);
    }

    @Override
    public BlockState createLegacyBlock(FluidState fluidState) {
        return ModBlocks.MOLTEN_BLUE_CRYSTAL_BLOCK.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(fluidState));
    }

    @Override
    protected FluidAttributes createAttributes() {
        return FluidAttributes.builder(
                        new ResourceLocation(ProjectI.MODID, "blocks/fluid/blue_crystal_still"),
                        new ResourceLocation(ProjectI.MODID, "blocks/fluid/blue_crystal_flow")
                ).translationKey("block.projecti.molten_blue_crystal")
                .luminosity(10).density(500).viscosity(3000).temperature(1000).build(ModBlocks.MOLTEN_BLUE_CRYSTAL);
    }

    @Override
    public boolean isSame(Fluid fluid) {
        return fluid == ModBlocks.MOLTEN_BLUE_CRYSTAL || fluid == ModBlocks.FLOWING_MOLTEN_BLUE_CRYSTAL;
    }

    public static class Flowing extends FluidMoltenBlueCrystal {

        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }

        @Override
        public boolean isSource(FluidState state) {
            return false;
        }
    }

    public static class Source extends FluidMoltenBlueCrystal {

        @Override
        public int getAmount(FluidState p_207192_1_) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState state) {
            return true;
        }
    }
}
