/**
    Copyright (C) 2014 by jabelar

    This file is part of jabelar's Minecraft Forge modding examples; as such,
    you can redistribute it and/or modify it under the terms of the GNU
    General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    For a copy of the GNU General Public License see <http://www.gnu.org/licenses/>.
*/

package com.blogspot.jabelarminecraft.magicbeans.renderers;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;
import com.blogspot.jabelarminecraft.magicbeans.entities.EntityMysteriousStranger;

/**
 * @author jabelar
 *
 */
public class RenderMysteriousStranger extends RenderLiving
{
    protected ResourceLocation mysteriousStrangerTexture;

    public RenderMysteriousStranger(ModelBase par1ModelBase, float parShadowSize)
    {
        super(par1ModelBase, parShadowSize);
        setEntityTexture();        
    }
 
    @Override
    protected void preRenderCallback(EntityLivingBase entity, float f)
    {
        preRenderCallbackMysteriousStranger((EntityMysteriousStranger) entity, f);
    }
  
    protected void preRenderCallbackMysteriousStranger(EntityMysteriousStranger entity, float f)
    {
        // some people do some G11 transformations or blends here, like you can do
        // GL11.glScalef(2F, 2F, 2F); to scale up the entity
        // which is used for Slime entities.  I suggest having the entity cast to
        // your custom type to make it easier to access fields from your 
        // custom entity, eg. GL11.glScalef(entity.scaleFactor, entity.scaleFactor, 
        // entity.scaleFactor); 
    }

    protected void setEntityTexture()
    {
        mysteriousStrangerTexture = new ResourceLocation(MagicBeans.MODID+":textures/entities/mysterious_stranger.png");
    }

    /**
    * Returns the location of an entity's texture. Doesn't seem to be called 
    * unless you call Render.bindEntityTexture.
    */
    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return mysteriousStrangerTexture;
    }
}