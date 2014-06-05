package crazypants.enderio.machine.monitor;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import crazypants.enderio.EnderIO;
import crazypants.enderio.network.MessageTileEntity;
import crazypants.enderio.network.NetworkUtil;

public class PacketPowerInfo extends MessageTileEntity<TilePowerMonitor> implements IMessageHandler<PacketPowerInfo, IMessage>  {

  private NBTTagCompound nbtRoot;

  public PacketPowerInfo() {
  }

  public PacketPowerInfo(TilePowerMonitor tile) {
    super(tile);
    nbtRoot = new NBTTagCompound();
    tile.writePowerInfoToNBT(nbtRoot);
  }

  @Override
  public void toBytes(ByteBuf buf) {
    super.toBytes(buf);
    NetworkUtil.writeNBTTagCompound(nbtRoot, buf);
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    super.fromBytes(buf);
    nbtRoot = NetworkUtil.readNBTTagCompound(buf);
  }

  @Override
  public IMessage onMessage(PacketPowerInfo message, MessageContext ctx) {
    EntityPlayer player = EnderIO.proxy.getClientPlayer();
    TilePowerMonitor te = message.getTileEntity(player.worldObj);
    if(te != null) {
      te.readPowerInfoFromNBT(message.nbtRoot);
    }
    return null;
  }
}