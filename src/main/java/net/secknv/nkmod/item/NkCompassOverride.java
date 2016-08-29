package net.secknv.nkmod.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Items;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.secknv.nkmod.tileentity.TileEntityCoil;
import net.secknv.nkmod.util.WorldHelper;

import javax.annotation.Nullable;

public class NkCompassOverride {
	
	public static void asdf() {
		
		IItemPropertyGetter underlyingGetter = Items.COMPASS.getPropertyGetter(new ResourceLocation("angle"));
		Items.COMPASS.addPropertyOverride(new ResourceLocation("angle"), new IItemPropertyGetter() {

            @SideOnly(Side.CLIENT)
            double rotation;
            @SideOnly(Side.CLIENT)
            double rota;
            @SideOnly(Side.CLIENT)
            long lastUpdateTick;
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {

                if (entityIn == null && !stack.isOnItemFrame()) {

                    return 0.0F;
                }
                else {

                    boolean flag = entityIn != null;
                    Entity entity = flag ? entityIn : stack.getItemFrame();

                    if (worldIn == null) {

                        worldIn = entity.worldObj;
                    }

                    double d0;

                    if (worldIn.provider.isSurfaceWorld()) {

                        double d1 = flag ? (double)entity.rotationYaw : this.getFrameRotation((EntityItemFrame)entity);
                        d1 = d1 % 360.0D;
                        double d2 = this.getSpawnToAngle(worldIn, entity);
                        d0 = Math.PI - ((d1 - 90.0D) * 0.01745329238474369D - d2);
                    }
                    else {

                        d0 = Math.random() * (Math.PI * 2D);
                    }

                    if (flag) {

                        d0 = this.wobble(worldIn, d0);
                    }

                    float f = (float)(d0 / (Math.PI * 2D));
                    return MathHelper.positiveModulo(f, 1.0F);
                }
            }

            
            @SideOnly(Side.CLIENT)
            private double wobble(World worldIn, double num) {

                if (worldIn.getTotalWorldTime() != this.lastUpdateTick)
                {
                    this.lastUpdateTick = worldIn.getTotalWorldTime();
                    double d0 = num - this.rotation;
                    d0 = d0 % (Math.PI * 2D);
                    d0 = MathHelper.clamp_double(d0, -1.0D, 1.0D);
                    this.rota += d0 * 0.1D;
                    this.rota *= 0.8D;
                    this.rotation += this.rota;
                }

                return this.rotation;
            }
            @SideOnly(Side.CLIENT)
            private double getFrameRotation(EntityItemFrame entFrame)
            {
                return (double)MathHelper.clampAngle(180 + entFrame.facingDirection.getHorizontalIndex() * 90);
            }
            @SideOnly(Side.CLIENT)
            private double getSpawnToAngle(World worldIn, Entity ent)
            {
            	
            	BlockPos blockpos = worldIn.getSpawnPoint();

                for (TileEntity tileEntity : WorldHelper.getTileEntitiesWithinAABB(worldIn, TileEntityCoil.class, WorldHelper.createAABBFromBlockPos(ent.getPosition(), 5D))) {

                    TileEntityCoil coil = (TileEntityCoil) tileEntity;
                    if (coil.messUpCompass) {
                        blockpos = coil.getPos();
                    }
                }
            	
            	return Math.atan2((double)blockpos.getZ() - ent.posZ, (double)blockpos.getX() - ent.posX);
            }
        });
		
	}

}
