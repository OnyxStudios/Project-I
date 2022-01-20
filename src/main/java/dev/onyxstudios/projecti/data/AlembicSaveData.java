package dev.onyxstudios.projecti.data;

import dev.onyxstudios.projecti.ProjectI;
import dev.onyxstudios.projecti.api.block.AlembicType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static dev.onyxstudios.projecti.api.block.AlembicType.*;

public class AlembicSaveData extends WorldSavedData {

    public static final String ID = ProjectI.MODID + ":alembics";

    private List<AlembicType> alembicList = Arrays.asList(GOURD, SPLITTER, SPIRAL, DECANTER);

    public AlembicSaveData() {
        super(ID);
    }

    public void setAlembicList(List<AlembicType> list) {
        this.alembicList = list;
        this.setDirty();
    }

    public List<AlembicType> getAlembicList() {
        return alembicList;
    }

    public static AlembicSaveData get(ServerWorld world) {
        return world.getDataStorage().computeIfAbsent(() -> {
            AlembicSaveData saveData = new AlembicSaveData();
            List<AlembicType> list = Arrays.asList(GOURD, SPLITTER, SPIRAL, DECANTER);
            Random random = new Random(world.getSeed());
            Collections.shuffle(list, random);
            saveData.setAlembicList(list);
            return saveData;
        }, ID);
    }

    @Override
    public void load(CompoundNBT tag) {
        ListNBT list = tag.getList("alembics", Constants.NBT.TAG_STRING);

        alembicList.clear();
        for (INBT nbt : list) {
            StringNBT stringNBT = (StringNBT) nbt;
            alembicList.add(AlembicType.valueOf(stringNBT.getAsString()));
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        ListNBT list = new ListNBT();
        for (AlembicType type : alembicList) {
            list.add(StringNBT.valueOf(type.toString()));
        }

        tag.put("alembics", list);
        return tag;
    }
}
