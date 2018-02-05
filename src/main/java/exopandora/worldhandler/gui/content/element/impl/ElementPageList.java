package exopandora.worldhandler.gui.content.element.impl;

import java.util.List;

import exopandora.worldhandler.format.TextFormatting;
import exopandora.worldhandler.gui.button.GuiButtonWorldHandler;
import exopandora.worldhandler.gui.button.storage.ButtonStorage;
import exopandora.worldhandler.gui.container.Container;
import exopandora.worldhandler.gui.content.Content;
import exopandora.worldhandler.gui.content.element.Element;
import exopandora.worldhandler.gui.content.element.logic.ILogicPageList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ElementPageList<T, K> extends Element
{
	private final List<T> list;
	private final ILogicPageList<T, K> logic;
	private final int length;
	private final int width;
	private final int height;
	private final int[] ids;
	private final ButtonStorage<Integer> storage;
	
	public ElementPageList(int x, int y, List<T> list, K initial, int width, int height, int length, Content container, int[] ids, ILogicPageList<T, K> logic)
	{
		super(x, y);
		this.list = list;
		this.length = length;
		this.width = width;
		this.height = height;
		this.logic = logic;
		this.storage = container.getStorage(logic.getId());
		this.ids = ids;
		
		this.list.sort((a, b) -> this.logic.translate(a).compareTo(this.logic.translate(b)));
		
		if(this.storage.getObject() == null)
		{
			this.storage.setObject(0);
			this.storage.setIndex(Math.max(0, this.list.indexOf(this.logic.convert(initial))));
			
			if(initial == null)
			{
				this.logic.onClick(this.list.get(0));
			}
		}
	}
	
	@Override
	public void initGui(Container container)
	{
		
	}
	
	@Override
	public void initButtons(Container container)
	{
		boolean extended = (this.list.size() == this.length + 1);
		
		if(!extended)
		{
			int buttonWidth = (this.width - 4) / 2;
			
			GuiButtonWorldHandler left = new GuiButtonWorldHandler(this.ids[0], this.x, this.y + (this.height + 4) * this.length, buttonWidth + 1, this.height, "<");
			GuiButtonWorldHandler right = new GuiButtonWorldHandler(this.ids[1], this.x + 5 + buttonWidth, this.y + (this.height + 4) * this.length, buttonWidth, this.height, ">");
			
			left.enabled = this.storage.getObject() > 0;
			right.enabled = this.storage.getObject() < this.getTotalPages() - 1;
			
			container.add(left);
			container.add(right);
		}
		
		int length = (extended ? this.length + 1 : this.length);
		
		for(int x = 0; x < length; x++)
		{
			int index = this.storage.getObject() * length + x;
			
			if(index < this.list.size())
			{
				T entry = this.list.get(index);
				this.logic.onRegister(this.ids[2], this.x, this.y + (this.height + 4) * x, this.width, this.height, TextFormatting.shortenString(this.logic.translate(entry), this.width, Minecraft.getMinecraft().fontRenderer), this.logic.getRegistryName(entry), this.storage.getIndex() != index, entry, container);
			}
			else
			{
				GuiButtonWorldHandler button = new GuiButtonWorldHandler(this.ids[2], this.x, this.y + (this.height + 4) * x, this.width, this.height, null);
				button.enabled = false;
				container.add(button);
			}
		}
	}
	
	@Override
	public boolean actionPerformed(Container container, GuiButton button)
	{
		if(button.id == this.ids[0])
		{
			this.storage.setObject(this.storage.getObject() - 1);
			container.initGui();
			return true;
		}
		else if(button.id == this.ids[1])
		{
			this.storage.setObject(this.storage.getObject() + 1);
			container.initGui();
			return true;
		}
		else if(button.id == this.ids[2])
		{
			for(int x = 0; x < this.list.size(); x++)
			{
				T entry = this.list.get(x);
				
				if(TextFormatting.shortenString(this.logic.translate(entry), this.width, Minecraft.getMinecraft().fontRenderer).equals(button.displayString))
				{
					this.storage.setIndex(x);
					this.logic.onClick(entry);
					container.initGui();
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public void draw()
	{
		Minecraft.getMinecraft().fontRenderer.drawString((this.storage.getObject() + 1) + "/" + this.getTotalPages(), this.x, this.y - 11, 0x4F4F4F);
	}
	
	private int getTotalPages()
	{
		return (int) Math.ceil((float) this.list.size() / this.length);
	}
}
