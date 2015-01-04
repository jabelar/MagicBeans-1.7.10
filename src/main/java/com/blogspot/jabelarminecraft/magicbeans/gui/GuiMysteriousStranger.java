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

package com.blogspot.jabelarminecraft.magicbeans.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;
import com.blogspot.jabelarminecraft.magicbeans.entities.EntityMysteriousStranger;
import com.blogspot.jabelarminecraft.magicbeans.networking.MessageGiveItemMagicBeansToServer;
import com.blogspot.jabelarminecraft.magicbeans.utilities.MagicBeansUtilities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author jabelar
 *
 */
public class GuiMysteriousStranger extends GuiScreen
{
	private EntityMysteriousStranger entityMysteriousStranger = null;
	
	private final int bookImageHeight = 192;
	private final int bookImageWidth = 192;
	private int currPage = 0;
	private static final int bookTotalPages = 3;
	private static ResourceLocation[] bookPageTextures = new ResourceLocation[bookTotalPages];
	private static String[] stringPageText = new String[bookTotalPages];
	private GuiButton buttonDone;
    private NextPageButton buttonNextPage;
    private NextPageButton buttonPreviousPage;
    
	public GuiMysteriousStranger()
	{
		// DEBUG
		System.out.println("GuiMysteriousStranger() constructor");
		// Don't need to do anything in constructor because the init() function is 
		// also directly called.
	}

	public GuiMysteriousStranger(EntityMysteriousStranger parMysteriousStranger)
	{
		// DEBUG
		System.out.println("GuiMysteriousStranger() constructor");
		entityMysteriousStranger = parMysteriousStranger;
	    bookPageTextures[0] = new ResourceLocation(MagicBeans.MODID+":textures/gui/book.png");
	    bookPageTextures[1] = new ResourceLocation(MagicBeans.MODID+":textures/gui/book.png");
	    bookPageTextures[2] = new ResourceLocation(MagicBeans.MODID+":textures/gui/book.png");
	    stringPageText[0] = "The "+MagicBeansUtilities.stringToRainbow("Mysterious Stranger", true)
	    		+" admired your family cow and asked if it was for sale.\n\nWhen you nodded, he offered to trade some "
	    		+MagicBeansUtilities.stringToRainbow("Magic Beans", true)
	    		+", that (if planted in tilled dirt) would lead to more wealth than you could imagine.";
	    stringPageText[1]="So you handed him your cow, and grabbed the "
	    		+MagicBeansUtilities.stringToRainbow("Magic Beans", true)
	    		+".\n\nPleased with yourself, you hurried away, looking for tilled dirt in which to plant the "
	    		+MagicBeansUtilities.stringToRainbow("Magic Beans", true)
	    		+".\n\nYou couldn't wait to see how proud your mother would be for";
	    stringPageText[2]="being so shrewd!  Untold wealth in return for an old, milkless cow; what a good deal you made!\n\nSo off you went, looking for a place to plant the "
	    		+MagicBeansUtilities.stringToRainbow("Magic Beans", true)
	    		+" with room to grow...";
	}

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
	public void initGui() 
    {
    	// DEBUG
    	System.out.println("GuiMysteriousStranger initGUI()");
        buttonList.clear();
        Keyboard.enableRepeatEvents(true);

        buttonDone = new GuiButton(0, width / 2 + 2, 4 + bookImageHeight, 98, 20, I18n.format("gui.done", new Object[0]));
		
        buttonList.add(buttonDone);
        int offsetFromScreenLeft = (width - bookImageWidth) / 2;
        buttonList.add(buttonNextPage = new NextPageButton(1, offsetFromScreenLeft + 120, 156, true));
        buttonList.add(buttonPreviousPage = new NextPageButton(2, offsetFromScreenLeft + 38, 156, false));

    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
	public void updateScreen() 
    {
    	buttonDone.visible = (currPage == bookTotalPages - 1);
        buttonNextPage.visible = (currPage < bookTotalPages - 1);
        buttonPreviousPage.visible = currPage > 0;
    }
	
    /**
     * Draws the screen and all the components in it.
     */
    @Override
	public void drawScreen(int parWidth, int parHeight, float p_73863_3_)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (currPage == 0)
    	{
        	mc.getTextureManager().bindTexture(bookPageTextures[0]);
    	}
        else
        {
        	mc.getTextureManager().bindTexture(bookPageTextures[1]);
        }
        int offsetFromScreenLeft = (width - bookImageWidth ) / 2;
        drawTexturedModalRect(offsetFromScreenLeft, 2, 0, 0, bookImageWidth, bookImageHeight);
        int widthOfString;
        String stringPageIndicator = I18n.format("book.pageIndicator", new Object[] {Integer.valueOf(currPage + 1), bookTotalPages});

        widthOfString = fontRendererObj.getStringWidth(stringPageIndicator);
        fontRendererObj.drawString(stringPageIndicator, offsetFromScreenLeft - widthOfString + bookImageWidth - 44, 18, 0);
        fontRendererObj.drawSplitString(stringPageText[currPage], offsetFromScreenLeft + 36, 34, 116, 0);

        super.drawScreen(parWidth, parHeight, p_73863_3_);

    }

    /**
     * Called when a mouse button is pressed and the mouse is moved around. Parameters are : mouseX, mouseY,
     * lastButtonClicked & timeSinceMouseClick.
     */
    @Override
	protected void mouseClickMove(int parMouseX, int parMouseY, int parLastButtonClicked, long parTimeSinceMouseClick) 
    {
    	
    }

    @Override
	protected void actionPerformed(GuiButton parButton) 
    {
    	if (parButton == buttonDone)
    	{
    		// DEBUG
    		System.out.println("actionPerformed() buttonDone");
    		if (entityMysteriousStranger.getCowSummonedBy() != null)
    		{
    			MagicBeans.network.sendToServer(new MessageGiveItemMagicBeansToServer(entityMysteriousStranger.getCowSummonedBy()));
       		}
    		mc.displayGuiScreen((GuiScreen)null);
    	}
        else if (parButton == buttonNextPage)
        {
            if (currPage < bookTotalPages - 1)
            {
                ++currPage;
            }
        }
        else if (parButton == buttonPreviousPage)
        {
            if (currPage > 0)
            {
                --currPage;
            }
        }
   }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    @Override
	public void onGuiClosed() 
    {
    	
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    @Override
	public boolean doesGuiPauseGame()
    {
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    static class NextPageButton extends GuiButton
    {
        private final boolean isNextButton;

        public NextPageButton(int parButtonId, int parPosX, int parPosY, boolean parIsNextButton)
        {
            super(parButtonId, parPosX, parPosY, 23, 13, "");
            isNextButton = parIsNextButton;
        }

        /**
         * Draws this button to the screen.
         */
        @Override
		public void drawButton(Minecraft mc, int parX, int parY)
        {
            if (visible)
            {
                boolean isButtonPressed = parX >= xPosition && parY >= yPosition && parX < xPosition + width && parY < yPosition + height;
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(bookPageTextures[1]);
                int textureX = 0;
                int textureY = 192;

                if (isButtonPressed)
                {
                    textureX += 23;
                }

                if (!isNextButton)
                {
                    textureY += 13;
                }

                drawTexturedModalRect(xPosition, yPosition, textureX, textureY, 23, 13);
            }
        }
    }
}
