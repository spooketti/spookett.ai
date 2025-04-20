package spooketti.spookettai.AIControl;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.

public class InventoryManagerAI {
    private DefaultedList<ItemStack> previousInventory = DefaultedList.ofSize(36, ItemStack.EMPTY);

    public void InventoryTick(MinecraftClient client)
    {
        if(client.player == null)
        {
            return;
        }
        for(int i=0;i<client.player.getInventory().size();i++)
        {
            ItemStack current = client.player.getInventory().getStack(i);
            ItemStack previous = previousInventory.get(i);

            if(!ItemStack.areEqual(current, previous))
            {
               Item pickedUpItem = current.getItem();
                ArmorMaterials.
               if(pickedUpItem.getComponents().contains(DataComponentTypes.EQUIPPABLE))
               {
                   pickedUpItem.
               }
            }
            previousInventory.set(i,current.copy());
        }
    }
}
