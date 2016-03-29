package crazypants.enderio.power;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import cofh.api.energy.IEnergyConnection;
import crazypants.enderio.machine.capbank.TileCapBank;

public class PowerHandlerUtil {

  public static final String STORED_ENERGY_NBT_KEY = "storedEnergyRF";

  public static IPowerInterface create(Object o) {
    if (o instanceof TileCapBank) {
      return new CapBankPI((TileCapBank) o);
    } else if (o instanceof IEnergyConnection) {
      return new PowerInterfaceRF((IEnergyConnection) o);
    }     
    return null;
  }

  public static int getStoredEnergyForItem(ItemStack item) {
    if (!item.hasTagCompound()) {
      return 0;
    }
    NBTTagCompound tag = item.getTagCompound();

    if(tag.hasKey("storedEnergy")) {
      double storedMj = tag.getDouble("storedEnergy");
      return (int) (storedMj * 10);
    }

    return tag.getInteger(STORED_ENERGY_NBT_KEY);
  }

  public static void setStoredEnergyForItem(ItemStack item, int storedEnergy) {
    NBTTagCompound tag = item.getTagCompound();
    if(tag == null) {
      tag = new NBTTagCompound();
    }
    tag.setInteger(STORED_ENERGY_NBT_KEY, storedEnergy);
    item.setTagCompound(tag);
  }


  public static int recieveInternal(IInternalPoweredTile target, int maxReceive, EnumFacing from, boolean simulate) {
    int result = Math.min(target.getMaxEnergyRecieved(from), maxReceive);
    result = Math.min(target.getMaxEnergyStored() - target.getEnergyStored(), result);
    result = Math.max(0, result);
    if(result > 0 && !simulate) {
      target.setEnergyStored(target.getEnergyStored() + result);
    }
    return result;
  }

  
}
