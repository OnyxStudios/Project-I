package dev.onyxstudios.projecti.data;

import dev.onyxstudios.projecti.ProjectI;
import dev.onyxstudios.projecti.api.block.AlembicType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

import static dev.onyxstudios.projecti.api.block.AlembicType.*;

public class AlembicSaveData extends SavedData {

    public static final String ID = ProjectI.MODID + ":alembics";
    private List<AlembicType> alembicList = Arrays.asList(GOURD, SPLITTER, SPIRAL, DECANTER);

    public void setAlembicList(List<AlembicType> list) {
        this.alembicList = list;
        this.setDirty();
    }

    public List<AlembicType> getAlembicList() {
        return alembicList;
    }

    public static AlembicSaveData get(ServerLevel level) {
        return level.getServer()
                .overworld()
                .getDataStorage()
                .computeIfAbsent(AlembicSaveData::load, () -> {
                    AlembicSaveData saveData = new AlembicSaveData();
                    List<AlembicType> list = Arrays.asList(GOURD, SPLITTER, SPIRAL, DECANTER);
                    Random random = new Random(level.getSeed());
                    Collections.shuffle(list, random);
                    saveData.setAlembicList(list);

                    return saveData;
                }, ID);
    }

    public static AlembicSaveData load(CompoundTag tag) {
        AlembicSaveData saveData = new AlembicSaveData();
        ListTag list = tag.getList("alembics", Tag.TAG_STRING);

        List<AlembicType> alembicList = new ArrayList<>();
        for (Tag nbt : list) {
            StringTag stringNBT = (StringTag) nbt;
            alembicList.add(AlembicType.valueOf(stringNBT.getAsString()));
        }

        saveData.setAlembicList(alembicList);
        return saveData;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        for (AlembicType type : alembicList) {
            list.add(StringTag.valueOf(type.toString()));
        }

        tag.put("alembics", list);
        return tag;
    }
}
