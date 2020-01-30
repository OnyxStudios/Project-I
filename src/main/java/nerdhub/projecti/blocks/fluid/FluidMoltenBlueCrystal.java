package nerdhub.projecti.blocks.fluid;

import nerdhub.projecti.ProjectI;
import nerdhub.projecti.registry.ModBlocks;
import nerdhub.projecti.registry.ModItems;
import nerdhub.projecti.registry.ModTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.fluids.FluidAttributes;

public abstract class FluidMoltenBlueCrystal extends FlowingFluid {

    public FluidMoltenBlueCrystal() {
        super();
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.SOLID;
    }

    @Override
    public Item getFilledBucket() {
        return ModItems.BLUE_CRYSTAL_BUCKET;
    }

    @Override
    public boolean func_215665_a(IFluidState p_215665_1_, IBlockReader p_215665_2_, BlockPos p_215665_3_, Fluid p_215665_4_, Direction p_215665_5_) {
        return p_215665_5_ == Direction.DOWN && !p_215665_4_.isIn(ModTags.MOLTEN_BLUE_CRYSTAL);
    }

    @Override
    public int getTickRate(IWorldReader worldReader) {
        return 15;
    }

    @Override
    public float getExplosionResistance() {
        return 100;
    }

    @Override
    public BlockState getBlockState(IFluidState state) {
        return ModBlocks.MOLTEN_BLUE_CRYSTAL_BLOCK.getDefaultState().with(FlowingFluidBlock.LEVEL, getLevelFromState(state));
    }

    @Override
    public Fluid getFlowingFluid() {
        return ModBlocks.FLOWING_MOLTEN_BLUE_CRYSTAL;
    }

    @Override
    public Fluid getStillFluid() {
        return ModBlocks.MOLTEN_BLUE_CRYSTAL;
    }

    @Override
    public boolean canSourcesMultiply() {
        return false;
    }

    @Override
    public void beforeReplacingBlock(IWorld iWorld, BlockPos blockPos, BlockState blockState) {
    }

    @Override
    public int getSlopeFindDistance(IWorldReader worldReader) {
        return 4;
    }

    @Override
    public int getLevelDecreasePerBlock(IWorldReader worldReader) {
        return 1;
    }

    @Override
    public boolean isEquivalentTo(Fluid fluid) {
        return fluid == ModBlocks.MOLTEN_BLUE_CRYSTAL || fluid == ModBlocks.FLOWING_MOLTEN_BLUE_CRYSTAL;
    }

    @Override
    public FluidAttributes createAttributes() {
        return FluidAttributes.builder(
                new ResourceLocation(ProjectI.MODID, "blocks/fluid/blue_crystal_still"),
                new ResourceLocation(ProjectI.MODID, "blocks/fluid/blue_crystal_flow"))
                .translationKey("block.projecti.molten_blue_crystal")
                .luminosity(10).density(1500).viscosity(3000).temperature(1000).build(ModBlocks.MOLTEN_BLUE_CRYSTAL);
    }

    public static class Flowing extends FluidMoltenBlueCrystal {

        public Flowing() {
            this.setRegistryName("flowing_molten_blue_crystal");
        }

        public void fillStateContainer(StateContainer.Builder<Fluid, IFluidState> builder) {
            super.fillStateContainer(builder);
            builder.add(LEVEL_1_8);
        }

        public int getLevel(IFluidState state) {
            return state.get(LEVEL_1_8);
        }

        public boolean isSource(IFluidState state) {
            return false;
        }
    }

    public static class Source extends FluidMoltenBlueCrystal {

        public Source() {
            this.setRegistryName("molten_blue_crystal");
        }

        public int getLevel(IFluidState state) {
            return 8;
        }

        public boolean isSource(IFluidState state) {
            return true;
        }
    }
}
