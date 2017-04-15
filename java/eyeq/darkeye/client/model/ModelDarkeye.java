package eyeq.darkeye.client.model;

import java.util.Random;

import eyeq.darkeye.entity.monster.EntityDarkeye;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;

public class ModelDarkeye extends ModelBase {
    public ModelRenderer body;
    public ModelRenderer head;
    public ModelRenderer armRight;
    public ModelRenderer armLeft;
    public ModelRenderer bomb;
    public ModelRenderer fuse;
    public ModelRenderer eyeRight;
    public ModelRenderer eyeLeft;
    public ModelRenderer mouse;

    private float preRotateAngleZ;

    public ModelDarkeye() {
        body = new ModelRenderer(this, 0, 0);
        body.addBox(-4.5F, -3.5F, -4.5F, 9, 7, 9);
        body.setRotationPoint(0F, 20F, 0F);
        head = new ModelRenderer(this, 1, 1);
        head.addBox(-3.5F, -3.5F, -3.5F, 7, 7, 7);
        head.setRotationPoint(0F, 14F, 0F);

        armRight = new ModelRenderer(this, 8, 0);
        armRight.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
        armRight.setRotationPoint(-2F, 17F, -4.5F);
        armRight.rotateAngleZ = -0.2F;
        armLeft = new ModelRenderer(this, 8, 0);
        armLeft.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
        armLeft.setRotationPoint(2F, 17F, -4.5F);
        armLeft.rotateAngleZ = 0.2F;

        bomb = new ModelRenderer(this, 0, 16);
        bomb.addBox(-2F, -2F, -2F, 4, 4, 4);
        bomb.setRotationPoint(0F, 18F, -7F);
        bomb.isHidden = true;
        fuse = new ModelRenderer(this, 4, 16);
        fuse.addBox(0F, 0F, 0F, 0, 2, 1);
        fuse.addBox(0F, 0F, 0F, 1, 0, 1);
        fuse.addBox(1F, 0F, 0F, 1, 1, 1);
        fuse.setRotationPoint(0F, -3.5F, 0F);
        fuse.rotateAngleZ = 0.2F;
        bomb.addChild(fuse);

        eyeRight = new ModelRenderer(this, 0, 0);
        eyeRight.addBox(-1F, -1F, -1F, 2, 2, 2);
        eyeRight.setRotationPoint(-3F, 10F, -3F);
        eyeLeft = new ModelRenderer(this, 0, 0);
        eyeLeft.addBox(-1F, -1F, -1F, 2, 2, 2);
        eyeLeft.setRotationPoint(3F, 10F, -3F);
        eyeLeft.rotateAngleZ = -(float) (Math.PI / 2);
        mouse = new ModelRenderer(this, 0, 0);
        mouse.addBox(-1F, 0F, 0F, 2, 0, 1);
        mouse.setRotationPoint(0F, 11.5F, -4F);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        bomb.render(scale);
        eyeRight.render(scale);
        eyeLeft.render(scale);
        mouse.render(scale);
        float[] afloat = EntitySheep.getDyeRgb(((EntityDarkeye) entity).getSkinColor());
        GlStateManager.color(afloat[0], afloat[1], afloat[2]);
        body.render(scale);
        head.render(scale);
        armRight.render(scale);
        armLeft.render(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
        bomb.isHidden = !entity.getCustomNameTag().equals("Q");
        final float f = 180F / (float) Math.PI;
        final float z = netHeadYaw / f / 9;
        eyeRight.rotateAngleZ += z - preRotateAngleZ;
        eyeLeft.rotateAngleZ += z - preRotateAngleZ;
        mouse.rotateAngleZ += z - preRotateAngleZ;
        head.rotateAngleZ += z - preRotateAngleZ;
        preRotateAngleZ = z;
    }

    private float getRandomRotate(Random rand, float f) {
        f *= 10;
        if(f % 1 != 0) {
            return f > Math.round(f) ? -0.04F : 0.04F;
        }
        return rand.nextBoolean() ? -0.04F : 0.04F;
    }

    private void randomRotate(Random rand, ModelRenderer renderer, float rotateZ) {
        if(rand.nextInt(600) == 0) {
            renderer.rotateAngleX += this.getRandomRotate(rand, renderer.rotateAngleX);
        }
        if(rand.nextInt(600) == 0) {
            renderer.rotateAngleY += this.getRandomRotate(rand, renderer.rotateAngleY);
        }
        if(rand.nextInt(600) == 0) {
            renderer.rotateAngleZ += this.getRandomRotate(rand, renderer.rotateAngleZ - rotateZ);
        }
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTickTime) {
        Random rand = entity.getRNG();
        this.randomRotate(rand, fuse, 0F);
        this.randomRotate(rand, armRight, 0F);
        this.randomRotate(rand, armLeft, 0F);
        this.randomRotate(rand, eyeRight, preRotateAngleZ);
        this.randomRotate(rand, eyeLeft, preRotateAngleZ);
    }
}
