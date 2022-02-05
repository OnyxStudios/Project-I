package dev.onyxstudios.projecti.utils;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.HashSet;
import java.util.Set;

public class BlockUtils {

    public static VoxelShape rotateShape(VoxelShape shape, Direction direction) {
        if (direction == Direction.NORTH) return shape;
        Set<VoxelShape> rotatedShapes = new HashSet<>();

        shape.forAllBoxes((x1, y1, z1, x2, y2, z2) -> {
            y1 = (y1 * 16) - 8;
            y2 = (y2 * 16) - 8;
            x1 = (x1 * 16) - 8;
            x2 = (x2 * 16) - 8;
            z1 = (z1 * 16) - 8;
            z2 = (z2 * 16) - 8;

            if (direction == Direction.EAST)
                rotatedShapes.add(box(8 - z1, y1 + 8, 8 + x1, 8 - z2, y2 + 8, 8 + x2));
            else if (direction == Direction.SOUTH)
                rotatedShapes.add(box(8 - x1, y1 + 8, 8 - z1, 8 - x2, y2 + 8, 8 - z2));
            else if (direction == Direction.WEST)
                rotatedShapes.add(box(8 + z1, y1 + 8, 8 - x1, 8 + z2, y2 + 8, 8 - x2));
            else if (direction == Direction.UP)
                rotatedShapes.add(box(x1 + 8, 8 - z2, 8 + y1, x2 + 8, 8 - z1, 8 + y2));
            else if (direction == Direction.DOWN)
                rotatedShapes.add(box(x1 + 8, 8 + z1, 8 - y2, x2 + 8, 8 + z2, 8 - y1));
        });

        return rotatedShapes.stream().reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    }

    public static VoxelShape box(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Block.box(
                Math.min(x1, x2),
                Math.min(y1, y2),
                Math.min(z1, z2),
                Math.max(x1, x2),
                Math.max(y1, y2),
                Math.max(z1, z2)
        );
    }
}
