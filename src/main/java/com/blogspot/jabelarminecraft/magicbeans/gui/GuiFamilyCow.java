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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;
import com.blogspot.jabelarminecraft.magicbeans.entities.EntityCowMagicBeans;
import com.blogspot.jabelarminecraft.magicbeans.networking.MessageGiveItemLeadToServer;

/**
 * @author jabelar
 *
 */
public class GuiFamilyCow extends GuiScreen
{
	private EntityCowMagicBeans entityCowMagicBeans = null;
	
	private final int bookImageHeight = 192;
	private final int bookImageWidth = 192;
	private final int currPage = 0;
	private final int bookTotalPages = 1;
    private static final ResourceLocation bookGuiTextures = new ResourceLocation(MagicBeans.MODID+":textures/gui/book_jack.png");
	private GuiButton buttonDone;
    
	public GuiFamilyCow()
	{
		// DEBUG
		System.out.println("GuiFamilyCow() constructor");
		// Don't need to do anything in constructor because the init() function is 
		// also directly called.
	}

	public GuiFamilyCow(EntityCowMagicBeans parCowMagicBeans)
	{
		// DEBUG
		System.out.println("GuiFamilyCow() constructor");
		// Don't need to do anything in constructor because the init() function is 
		// also directly called.
		entityCowMagicBeans = parCowMagicBeans;
	}

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
	public void initGui() 
    {
    	// DEBUG
    	System.out.println("GuiFamilyCow initGUI()");
        buttonList.clear();
        Keyboard.enableRepeatEvents(true);

        buttonDone = new GuiButton(0, width / 2 + 2, 4 + bookImageHeight, 98, 20, I18n.format("gui.done", new Object[0]));
		
        buttonList.add(buttonDone);
//        buttonList.add(new GuiButton(5, width / 2 - 100, 4 + bookImageHeight, 98, 20, I18n.format("book.finalizeButton", new Object[0])));
//        buttonList.add(new GuiButton(4, width / 2 + 2, 4 + bookImageHeight, 98, 20, I18n.format("gui.cancel", new Object[0])));

        updateButtons();

    }

    private void updateButtons()
    {
//        buttonNextPage.visible = !field_146480_s && (currPage < bookTotalPages - 1 || bookIsUnsigned);
//        buttonPreviousPage.visible = !field_146480_s && currPage > 0;
//        buttonDone.visible = !bookIsUnsigned || !field_146480_s;
//
//            buttonSign.visible = !field_146480_s;
//            buttonCancel.visible = field_146480_s;
//            buttonFinalize.visible = field_146480_s;
//            buttonFinalize.enabled = bookTitle.trim().length() > 0;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
	public void updateScreen() 
    {
    	
    }
	
    /**
     * Draws the screen and all the components in it.
     */
    @Override
	public void drawScreen(int parWidth, int parHeight, float p_73863_3_)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(bookGuiTextures);
        int k = (width - bookImageWidth ) / 2;
        byte b0 = 2;
        drawTexturedModalRect(k, b0, 0, 0, bookImageWidth, bookImageHeight);
        String s;
        String s1;
        int l;
        {
            s = I18n.format("book.pageIndicator", new Object[] {Integer.valueOf(currPage + 1), bookTotalPages});
            s1 = "";

                if (fontRendererObj.getBidiFlag())
                {
                    s1 = s1 + "_";
                }
                else
                {
                    s1 = s1 + "" + EnumChatFormatting.GRAY + "_";
                }

            l = fontRendererObj.getStringWidth(s);
            fontRendererObj.drawString(s, k - l + bookImageWidth - 44, b0 + 16, 0);
            fontRendererObj.drawSplitString(s1, k + 36, b0 + 16 + 16, 116, 0);
        }

        super.drawScreen(parWidth, parHeight, p_73863_3_);

//    	drawWorldBackground(0);
//
//        for (int k = 0; k < buttonList.size(); ++k)
//        {
//            ((GuiButton)buttonList.get(k)).drawButton(mc, parWidth, parHeight);
//        }
//
//        for (int k = 0; k < labelList.size(); ++k)
//        {
//            ((GuiLabel)labelList.get(k)).func_146159_a(mc, parWidth, parHeight);
//        }
    }

    @Override
	protected void renderToolTip(ItemStack parItemStack, int parWidth, int parHeight)
    {
        List list = parItemStack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);

        for (int k = 0; k < list.size(); ++k)
        {
            if (k == 0)
            {
                list.set(k, parItemStack.getRarity().rarityColor + (String)list.get(k));
            }
            else
            {
                list.set(k, EnumChatFormatting.GRAY + (String)list.get(k));
            }
        }

        FontRenderer font = parItemStack.getItem().getFontRenderer(parItemStack);
        drawHoveringText(list, parWidth, parHeight, (font == null ? fontRendererObj : font));
    }

    /**
     * Draws the text when mouse is over creative inventory tab. Params: current creative tab to be checked, current
     * mouse x position, current mouse y position.
     */
    @Override
	protected void drawCreativeTabHoveringText(String parCreativeTabHoverText, int parWidth, int parHeight)
    {
        func_146283_a(Arrays.asList(new String[] {parCreativeTabHoverText}), parWidth, parHeight);
    }

    @Override
	protected void func_146283_a(List parList, int parWidth, int parHeight)
    {
        drawHoveringText(parList, parWidth, parHeight, fontRendererObj);   
    }

    @Override
	protected void drawHoveringText(List parList, int parWidth, int parHeight, FontRenderer font)
    {
        if (!parList.isEmpty())
        {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int k = 0;
            Iterator iterator = parList.iterator();

            while (iterator.hasNext())
            {
                String s = (String)iterator.next();
                int l = font.getStringWidth(s);

                if (l > k)
                {
                    k = l;
                }
            }

            int j2 = parWidth + 12;
            int k2 = parHeight - 12;
            int i1 = 8;

            if (parList.size() > 1)
            {
                i1 += 2 + (parList.size() - 1) * 10;
            }

            if (j2 + k > width)
            {
                j2 -= 28 + k;
            }

            if (k2 + i1 + 6 > height)
            {
                k2 = height - i1 - 6;
            }

            zLevel = 300.0F;
            itemRender.zLevel = 300.0F;
            int j1 = -267386864;
            drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
            drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
            drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
            drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
            drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
            int k1 = 1347420415;
            int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
            drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
            drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
            drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
            drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

            for (int i2 = 0; i2 < parList.size(); ++i2)
            {
                String s1 = (String)parList.get(i2);
                font.drawStringWithShadow(s1, j2, k2, -1);

                if (i2 == 0)
                {
                    k2 += 2;
                }

                k2 += 10;
            }

            zLevel = 0.0F;
            itemRender.zLevel = 0.0F;
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            RenderHelper.enableStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
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
    		MagicBeans.network.sendToServer(new MessageGiveItemLeadToServer());
    		mc.displayGuiScreen((GuiScreen)null);
    	}
    }

    /**
     * Causes the screen to lay out its subcomponents again. This is the equivalent of the Java call
     * Container.validate()
     */
    @Override
	public void setWorldAndResolution(Minecraft parMC, int parWidth, int parHeight)
    {
        mc = parMC;
        fontRendererObj = parMC.fontRenderer;
        width = parWidth;
        height = parHeight;
        if (!MinecraftForge.EVENT_BUS.post(new InitGuiEvent.Pre(this, buttonList)))
        {
            buttonList.clear();
            initGui();
        }
        MinecraftForge.EVENT_BUS.post(new InitGuiEvent.Post(this, buttonList));
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    @Override
	public void onGuiClosed() 
    {
    	
    }

    /**
     * Draws either a gradient over the background screen (when it exists) or a flat gradient over background.png
     */
    @Override
	public void drawDefaultBackground()
    {
        drawWorldBackground(0);
    }

    @Override
	public void drawWorldBackground(int parBackgroundIndex)
    {
        if (mc.theWorld != null)
        {
            drawGradientRect(0, 0, width, height, -1072689136, -804253680);
        }
       else
       {
           drawBackground(parBackgroundIndex);
        }
    }

    /**
     * Draws the background (i is always 0 as of 1.2.2)
     */
    @Override
	public void drawBackground(int p_146278_1_)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator tessellator = Tessellator.instance;
        mc.getTextureManager().bindTexture(optionsBackground);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(4210752);
        tessellator.addVertexWithUV(0.0D, height, 0.0D, 0.0D, height / f + p_146278_1_);
        tessellator.addVertexWithUV(width, height, 0.0D, width / f, height / f + p_146278_1_);
        tessellator.addVertexWithUV(width, 0.0D, 0.0D, width / f, p_146278_1_);
        tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, p_146278_1_);
        tessellator.draw();
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    @Override
	public boolean doesGuiPauseGame()
    {
        return true;
    }
}
