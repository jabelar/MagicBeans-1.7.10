package com.blogspot.jabelarminecraft.magicbeans.renderers;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import com.blogspot.jabelarminecraft.magicbeans.entities.EntityGoldenGoose;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderGoldenGoose extends RenderLiving
{
    protected static final ResourceLocation goldenGooseTextures = new ResourceLocation("magicbeans:textures/entities/golden_goose.png");

    public RenderGoldenGoose(ModelBase par1ModelBase, float par2)
    {
        super(par1ModelBase, par2);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(EntityGoldenGoose parEntityGoldenGoose, double par2, double par4, double par6, float par8, float par9)
    {
        super.doRender(parEntityGoldenGoose, par2, par4, par6, par8, par9);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityGoldenGoose par1EntityGoldenGoose)
    {
        return goldenGooseTextures;
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float handleRotationFloat(EntityGoldenGoose par1EntityGoldenGoose, float par2)
    {
        float f1 = par1EntityGoldenGoose.rotationFloat3 + (par1EntityGoldenGoose.rotationFloat1 - par1EntityGoldenGoose.rotationFloat3) * par2;
        float f2 = par1EntityGoldenGoose.rotationFloat2 + (par1EntityGoldenGoose.destPos - par1EntityGoldenGoose.rotationFloat2) * par2;
        return (MathHelper.sin(f1) + 1.0F) * f2;
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    @Override
	public void doRender(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
        this.doRender((EntityGoldenGoose)par1EntityLiving, par2, par4, par6, par8, par9);
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    @Override
	protected float handleRotationFloat(EntityLivingBase par1EntityLivingBase, float par2)
    {
        return this.handleRotationFloat((EntityGoldenGoose)par1EntityLivingBase, par2);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    @Override
	public void doRender(EntityLivingBase par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.doRender((EntityGoldenGoose)par1Entity, par2, par4, par6, par8, par9);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
	protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return this.getEntityTexture((EntityGoldenGoose)par1Entity);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    @Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.doRender((EntityGoldenGoose)par1Entity, par2, par4, par6, par8, par9);
    }
}