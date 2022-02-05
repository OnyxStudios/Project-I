package dev.onyxstudios.projecti.registry;

import dev.onyxstudios.projecti.ProjectI;
import dev.onyxstudios.projecti.api.block.AlembicType;
import dev.onyxstudios.projecti.blocks.*;
import dev.onyxstudios.projecti.blocks.fluid.BaseFluidBlock;
import dev.onyxstudios.projecti.blocks.fluid.FluidMoltenBlueCrystal;
import dev.onyxstudios.projecti.items.BlueCrystalItem;
import dev.onyxstudios.projecti.items.BoneCageItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ProjectI.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ProjectI.MODID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, ProjectI.MODID);

    private static final Item.Properties ITEM_BLOCK_PROPS = new Item.Properties().tab(ProjectI.TAB);

    //Blocks
    public static final BlueCrystalBlock BLUE_CRYSTAL = new BlueCrystalBlock();
    public static final BlueCrystalItem BLUE_CRYSTAL_ITEM = new BlueCrystalItem();

    public static final CircuitStamperBlock CIRCUIT_STAMPER = new CircuitStamperBlock();
    public static final BlockItem CIRCUIT_STAMPER_ITEM = new BlockItem(CIRCUIT_STAMPER, ITEM_BLOCK_PROPS);

    public static final Block BELLOWS = new BellowsBlock();
    public static final BlockItem BELLOWS_ITEM = new BlockItem(BELLOWS, ITEM_BLOCK_PROPS);

    public static final AlembicBlock FUNNEL = new AlembicBlock(AlembicType.FUNNEL);
    public static final BlockItem FUNNEL_ITEM = new BlockItem(FUNNEL, ITEM_BLOCK_PROPS);

    public static final AlembicBlock DECANTER = new AlembicBlock(AlembicType.DECANTER);
    public static final BlockItem DECANTER_ITEM = new BlockItem(DECANTER, ITEM_BLOCK_PROPS);

    public static final AlembicBlock GOURD = new AlembicBlock(AlembicType.GOURD);
    public static final BlockItem GOURD_ITEM = new BlockItem(GOURD, ITEM_BLOCK_PROPS);

    public static final AlembicBlock SPIRAL = new AlembicBlock(AlembicType.SPIRAL);
    public static final BlockItem SPIRAL_ITEM = new BlockItem(SPIRAL, ITEM_BLOCK_PROPS);

    public static final AlembicBlock SPLITTER = new AlembicBlock(AlembicType.SPLITTER);
    public static final BlockItem SPLITTER_ITEM = new BlockItem(SPLITTER, ITEM_BLOCK_PROPS);

    public static final BoneCageBlock BONE_CAGE = new BoneCageBlock();
    public static final BlockItem BONE_CAGE_ITEM = new BoneCageItem();

    public static final SoulRelayBlock SOUL_RELAY = new SoulRelayBlock();
    public static final BlockItem SOUL_RELAY_ITEM = new BlockItem(SOUL_RELAY, ITEM_BLOCK_PROPS);

    public static final Block BENIGN_STONE = new Block(BlockBehaviour.Properties.of(Material.STONE));
    public static final BlockItem BENIGN_STONE_ITEM = new BlockItem(BENIGN_STONE, ITEM_BLOCK_PROPS);

    public static final BlowMoldBlock BLOW_MOLD = new BlowMoldBlock();
    public static final BlockItem BLOW_MOLD_ITEM = new BlockItem(BLOW_MOLD, ITEM_BLOCK_PROPS);

    //Fluids
    private static final Item.Properties BUCKET_PROPS = new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(ProjectI.TAB);
    private static final BlockBehaviour.Properties HOT_FLUID_PROPS = BlockBehaviour.Properties.of(Material.LAVA).randomTicks().lightLevel(value -> 100).noDrops();

    public static FluidMoltenBlueCrystal MOLTEN_BLUE_CRYSTAL = new FluidMoltenBlueCrystal.Source();
    public static FluidMoltenBlueCrystal FLOWING_MOLTEN_BLUE_CRYSTAL = new FluidMoltenBlueCrystal.Flowing();

    public static BaseFluidBlock MOLTEN_BLUE_CRYSTAL_BLOCK = new BaseFluidBlock(() -> MOLTEN_BLUE_CRYSTAL, HOT_FLUID_PROPS);
    public static Item BLUE_CRYSTAL_BUCKET = new BucketItem(() -> MOLTEN_BLUE_CRYSTAL, BUCKET_PROPS);

    public static void init() {
        BLOCKS.register("blue_crystal", () -> BLUE_CRYSTAL);
        ITEMS.register("blue_crystal", () -> BLUE_CRYSTAL_ITEM);

        BLOCKS.register("circuit_stamper", () -> CIRCUIT_STAMPER);
        ITEMS.register("circuit_stamper", () -> CIRCUIT_STAMPER_ITEM);

        BLOCKS.register("bellows", () -> BELLOWS);
        ITEMS.register("bellows", () -> BELLOWS_ITEM);

        BLOCKS.register("funnel", () -> FUNNEL);
        ITEMS.register("funnel", () -> FUNNEL_ITEM);

        BLOCKS.register("decanter", () -> DECANTER);
        ITEMS.register("decanter", () -> DECANTER_ITEM);

        BLOCKS.register("gourd", () -> GOURD);
        ITEMS.register("gourd", () -> GOURD_ITEM);

        BLOCKS.register("spiral", () -> SPIRAL);
        ITEMS.register("spiral", () -> SPIRAL_ITEM);

        BLOCKS.register("splitter", () -> SPLITTER);
        ITEMS.register("splitter", () -> SPLITTER_ITEM);

        BLOCKS.register("bone_cage", () -> BONE_CAGE);
        ITEMS.register("bone_cage", () -> BONE_CAGE_ITEM);

        BLOCKS.register("soul_relay", () -> SOUL_RELAY);
        ITEMS.register("soul_relay", () -> SOUL_RELAY_ITEM);

        BLOCKS.register("benign_stone", () -> BENIGN_STONE);
        ITEMS.register("benign_stone", () -> BENIGN_STONE_ITEM);

        BLOCKS.register("blow_mold", () -> BLOW_MOLD);
        ITEMS.register("blow_mold", () -> BLOW_MOLD_ITEM);

        FLUIDS.register("molten_blue_crystal", () -> MOLTEN_BLUE_CRYSTAL);
        FLUIDS.register("flowing_molten_blue_crystal", () -> FLOWING_MOLTEN_BLUE_CRYSTAL);

        BLOCKS.register("molten_blue_crystal", () -> MOLTEN_BLUE_CRYSTAL_BLOCK);
        ITEMS.register("blue_crystal_bucket", () -> BLUE_CRYSTAL_BUCKET);
    }
}
