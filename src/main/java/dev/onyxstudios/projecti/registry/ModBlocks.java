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
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ProjectI.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ProjectI.MODID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, ProjectI.MODID);

    //Blocks
    private static final Item.Properties ITEM_BLOCK_PROPS = new Item.Properties().tab(ProjectI.TAB);

    public static RegistryObject<Block> BLUE_CRYSTAL = BLOCKS.register("blue_crystal", BlueCrystalBlock::new);
    public static RegistryObject<Item> BLUE_CRYSTAL_ITEM = ITEMS.register("blue_crystal", BlueCrystalItem::new);

    public static RegistryObject<Block> STAMPER = BLOCKS.register("circuit_stamper", CircuitStamperBlock::new);
    public static RegistryObject<Item> STAMPER_ITEM = ITEMS.register("circuit_stamper", () -> new BlockItem(STAMPER.get(), ITEM_BLOCK_PROPS));

    public static RegistryObject<Block> BELLOWS = BLOCKS.register("bellows", BellowsBlock::new);
    public static RegistryObject<Item> BELLOWS_ITEM = ITEMS.register("bellows", () -> new BlockItem(BELLOWS.get(), ITEM_BLOCK_PROPS));

    public static RegistryObject<Block> FUNNEL = BLOCKS.register("funnel", () -> new AlembicBlock(AlembicType.FUNNEL));
    public static RegistryObject<Item> FUNNEL_ITEM = ITEMS.register("funnel", () -> new BlockItem(FUNNEL.get(), ITEM_BLOCK_PROPS));

    public static RegistryObject<Block> DECANTER = BLOCKS.register("decanter", () -> new AlembicBlock(AlembicType.DECANTER));
    public static RegistryObject<Item> DECANTER_ITEM = ITEMS.register("decanter", () -> new BlockItem(DECANTER.get(), ITEM_BLOCK_PROPS));

    public static RegistryObject<Block> GOURD = BLOCKS.register("gourd", () -> new AlembicBlock(AlembicType.GOURD));
    public static RegistryObject<Item> GOURD_ITEM = ITEMS.register("gourd", () -> new BlockItem(GOURD.get(), ITEM_BLOCK_PROPS));

    public static RegistryObject<Block> SPIRAL = BLOCKS.register("spiral", () -> new AlembicBlock(AlembicType.SPIRAL));
    public static RegistryObject<Item> SPIRAL_ITEM = ITEMS.register("spiral", () -> new BlockItem(SPIRAL.get(), ITEM_BLOCK_PROPS));

    public static RegistryObject<Block> SPLITTER = BLOCKS.register("splitter", () -> new AlembicBlock(AlembicType.SPLITTER));
    public static RegistryObject<Item> SPLITTER_ITEM = ITEMS.register("splitter", () -> new BlockItem(SPLITTER.get(), ITEM_BLOCK_PROPS));

    public static RegistryObject<Block> BONE_CAGE = BLOCKS.register("bone_cage", BoneCageBlock::new);
    public static RegistryObject<Item> BONE_CAGE_ITEM = ITEMS.register("bone_cage", BoneCageItem::new);

    public static RegistryObject<Block> SOUL_RELAY = BLOCKS.register("soul_relay", SoulRelayBlock::new);
    public static RegistryObject<Item> SOUL_RELAY_ITEM = ITEMS.register("soul_relay", () -> new BlockItem(SOUL_RELAY.get(), ITEM_BLOCK_PROPS));

    public static RegistryObject<Block> BENIGN_STONE = BLOCKS.register("benign_stone", () -> new Block(BlockBehaviour.Properties.of(Material.STONE)));
    public static RegistryObject<Item> BENIGN_STONE_ITEM = ITEMS.register("benign_stone", () -> new BlockItem(BENIGN_STONE.get(), ITEM_BLOCK_PROPS));

    public static RegistryObject<Block> BLOW_MOLD = BLOCKS.register("blow_mold", BlowMoldBlock::new);
    public static RegistryObject<Item> BLOW_MOLD_ITEM = ITEMS.register("blow_mold", () -> new BlockItem(BLOW_MOLD.get(), ITEM_BLOCK_PROPS));

    //Fluids
    private static final BlockBehaviour.Properties HOT_FLUID_PROPS = BlockBehaviour.Properties.of(Material.LAVA).randomTicks().lightLevel(value -> 100).noDrops();
    private static final Item.Properties BUCKET_PROPS = new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(ProjectI.TAB);

    public static FluidMoltenBlueCrystal MOLTEN_BLUE_CRYSTAL = new FluidMoltenBlueCrystal.Source();
    public static FluidMoltenBlueCrystal FLOWING_MOLTEN_BLUE_CRYSTAL = new FluidMoltenBlueCrystal.Flowing();

    public static RegistryObject<FluidMoltenBlueCrystal> MOLTEN_BLUE_CRYSTAL_REG = FLUIDS.register("molten_blue_crystal", () -> MOLTEN_BLUE_CRYSTAL);
    public static RegistryObject<FluidMoltenBlueCrystal> FLOWING_BLUE_CRYSTAL_REG = FLUIDS.register("flowing_molten_blue_crystal", () -> FLOWING_MOLTEN_BLUE_CRYSTAL);

    public static RegistryObject<BaseFluidBlock> MOLTEN_BLUE_CRYSTAL_BLOCK = BLOCKS.register("molten_blue_crystal", () -> new BaseFluidBlock(MOLTEN_BLUE_CRYSTAL_REG::get, HOT_FLUID_PROPS));
    public static RegistryObject<Item> BLUE_CRYSTAL_BUCKET = ITEMS.register("blue_crystal_bucket", () -> new BucketItem(() -> MOLTEN_BLUE_CRYSTAL, BUCKET_PROPS));
}
