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

package com.blogspot.jabelarminecraft.magicbeans.models;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;

import com.blogspot.jabelarminecraft.magicbeans.entities.EntityGiant;

/**
 * @author jabelar
 *
 */
public class ModelGiant extends ModelBiped
{
    public ModelGiant()
    {
        this(0.0F);
    }

    public ModelGiant(float parScaleFactor)
    {
        super(parScaleFactor, 0.0F, 64, 32);
        // DEBUG
        System.out.println("ModelGiant() constructor");
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    @Override
	public void render(Entity parEntity, float par2, float par3, float par4, float par5, float par6, float par7)
    {
    	renderGiant((EntityGiant)parEntity, par2, par3, par4, par5, par6, par7);
    }
    
    /**
     * Renders specific entity to allow access to any specific fields
     * @param parEntity
     * @param par2
     * @param par3
     * @param par4
     * @param par5
     * @param par6
     * @param par7
     */
    public void renderGiant(EntityGiant parEntity, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        setRotationAngles(par2, par3, par4, par5, par6, par7, parEntity);

        // scale the whole thing for big or small entities
        GL11.glPushMatrix();
        GL11.glTranslatef(0F, 1.5F-1.5F*parEntity.getScaleFactor(), 0F); 
    	GL11.glScalef(parEntity.getScaleFactor(), parEntity.getScaleFactor(), parEntity.getScaleFactor());

    	super.render(parEntity, par2, par3, par4, par5, par6, par7);

        // don't forget to pop the matrix for overall scaling
        GL11.glPopMatrix();
    }
}
