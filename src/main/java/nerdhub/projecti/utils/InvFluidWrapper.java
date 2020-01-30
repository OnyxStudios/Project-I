package nerdhub.projecti.utils;

import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

public class InvFluidWrapper extends InvCapWrapper {

    private FluidTank tank;

    public InvFluidWrapper(ItemStackHandler cap, FluidTank tank) {
        super(cap);
        this.tank = tank;
    }

    public FluidTank getTank() {
        return tank;
    }
}
