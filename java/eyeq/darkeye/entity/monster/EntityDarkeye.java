package eyeq.darkeye.entity.monster;

import eyeq.darkeye.Darkeye;
import eyeq.util.entity.EntityLivingUtils;
import eyeq.util.entity.IEntityLeashable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import eyeq.util.entity.ai.UEntityAIMoveTowardsRestriction;
import eyeq.util.entity.ai.UEntityAITempt;

import java.util.ArrayList;
import java.util.List;

public class EntityDarkeye extends EntitySlime implements IEntityLeashable {
    private static final DataParameter<Byte> DYE_COLOR = EntityDataManager.createKey(EntityDarkeye.class, DataSerializers.BYTE);

    public EntityDarkeye(World world) {
        super(world);
        this.setSkinColor(EnumDyeColor.byMetadata(rand.nextInt(EnumDyeColor.values().length)));
        this.setCustomNameTag(String.valueOf((char) ('A' + rand.nextInt(26))));
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();

        List<EntityAITasks.EntityAITaskEntry> removeList = new ArrayList<>();
        int i = 0;
        for(EntityAITasks.EntityAITaskEntry taskEntry : this.tasks.taskEntries) {
            if(i == 1 || i == 3) {
                removeList.add(taskEntry);
            }
            i++;
        }
        for(EntityAITasks.EntityAITaskEntry taskEntry : removeList) {
            this.tasks.taskEntries.remove(taskEntry);
        }
        this.tasks.addTask(0, new UEntityAITempt(this, 1.8, false, Items.CLAY_BALL));
        this.tasks.addTask(4, new UEntityAIMoveTowardsRestriction(this, 1.6));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(DYE_COLOR, (byte) 0);
    }

    public EnumDyeColor getSkinColor() {
        return EnumDyeColor.byMetadata(this.dataManager.get(DYE_COLOR) & 15);
    }

    public void setSkinColor(EnumDyeColor color) {
        byte b0 = this.dataManager.get(DYE_COLOR);
        this.dataManager.set(DYE_COLOR, (byte) (b0 & 240 | color.getMetadata() & 15));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setByte("Color", (byte) this.getSkinColor().getMetadata());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.setSkinColor(EnumDyeColor.byMetadata(tagCompund.getByte("Color")));
    }

    @Override
    public boolean canBeLeashedTo(EntityPlayer player) {
        return !this.getLeashed();
    }

    @Override
    protected void updateLeashedState() {
        super.updateLeashedState();
        EntityLivingUtils.updateLeashedState(this);
    }

    @Override
    public double followLeashSpeed()
    {
        return 1.0;
    }

    @Override
    public void onLeashDistance(float distance) {}

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    protected SoundEvent getHurtSound() {
        return Darkeye.entityDarkeyeAmbient;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return Darkeye.entityDarkeyeAmbient;
    }

    @Override
    protected SoundEvent getSquishSound() {
        return Darkeye.entityDarkeyeAmbient;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.heal(0.1F);
    }

    @Override
    public void setJumping(boolean isJumping) {
        if(!isJumping) {
            this.isJumping = false;
        } else if(rand.nextInt(50) == 0) {
            this.isJumping = true;
        }
    }

    @Override
    public void setCustomNameTag(String name) {
        super.setCustomNameTag(name);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(name.equals("Q") ? 100 : 1);
        this.setHealth(this.getMaxHealth());
        if(name.equals("Q")) {
            this.setSkinColor(EnumDyeColor.WHITE);
        }
    }

    @Override
    public float getEyeHeight() {
        return this.height;
    }

    @Override
    public int getSlimeSize() {
        return 1;
    }

    @Override
    protected void setSlimeSize(int size, boolean initHealth) {
        super.setSlimeSize(size, initHealth);
        this.setSize(0.5F, 1.1F);
    }
}
