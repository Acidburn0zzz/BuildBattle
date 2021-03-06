/*
 * BuildBattle 3 - Ultimate building competition minigame
 * Copyright (C) 2018  Plajer's Lair - maintained by Plajer
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package pl.plajer.buildbattle3.menus.playerheads;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import pl.plajer.buildbattle3.handlers.ChatManager;
import pl.plajer.buildbattle3.handlers.ConfigurationManager;
import pl.plajer.buildbattle3.utils.Util;

/**
 * Created by Tom on 26/08/2015.
 */
//todo texture loading
public class PlayerHeadsMenu {

  private static List<HeadsItem> headsItems = new ArrayList<>();
  private static Map<String, List<HeadsItem>> playerheadmenus = new HashMap<>();
  private static Map<String, Inventory> inventories = new HashMap<>();

  public static void loadHeadItems() {
    FileConfiguration config = ConfigurationManager.getConfig("playerheadmenu/mainmenu");
    if (!config.contains("animals")) {
      config.set("animals.data", SkullType.PLAYER.ordinal());
      config.set("animals.displayname", "&6" + "Animals");
      config.set("animals.lore", Arrays.asList("Click to open", "animals head menu"));
      config.set("animals.material", Material.SKULL_ITEM.getId());
      config.set("animals.enabled", true);
      config.set("animals.config", "animalheads");
      config.set("animals.permission", "particles.VIP");
      config.set("animals.slot", 7);
      config.set("animals.owner", "MHF_Pig");
      config.set("animals.inventorysize", 3 * 9);
      config.set("animals.menuname", "Animal Heads Menu");
    }
    try {
      config.save(ConfigurationManager.getFile("playerheadmenu/mainmenu"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    for (String str : config.getKeys(false)) {
      HeadsItem headsItem = new HeadsItem();
      headsItem.setData(config.getInt(str + ".data"));
      headsItem.setEnabled(config.getBoolean(str + ".enabled"));
      headsItem.setMaterial(org.bukkit.Material.getMaterial(config.getInt(str + ".material")));
      headsItem.setLore(config.getStringList(str + ".lore"));
      headsItem.setDisplayName(config.getString(str + ".displayname"));
      headsItem.setPermission(config.getString(str + ".permission"));
      headsItem.setOwner(config.getString(str + ".owner"));
      headsItem.setSlot(config.getInt(str + ".slot"));
      headsItem.setConfig(config.getString(str + ".config"));
      headsItem.setSize(config.getInt(str + ".inventorysize"));
      headsItem.setMenuName(config.getString(str + ".menuname"));
      if (headsItem.isEnabled()) headsItems.add(headsItem);
    }
    for (HeadsItem headsItem : headsItems) {
      config = headsItem.getConfig();
      Inventory inv;
      List<HeadsItem> list = new ArrayList<>();
      if (!config.contains("example")) {
        config.set("example.data", SkullType.PLAYER.ordinal());
        config.set("example.displayname", "&6" + "Animals");
        config.set("example.owner", "MHF_Pig");
        config.set("example.lore", Collections.singletonList(ChatManager.colorRawMessage("&7Click to select")));
        config.set("example.material", Material.SKULL_ITEM.getId());
        config.set("example.enabled", true);
        config.set("example.slot", 7);
        try {
          config.save(ConfigurationManager.getFile("playerheadmenu/menus/" + headsItem.getConfigName()));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      for (String path : headsItem.getConfig().getKeys(false)) {
        HeadsItem heads = new HeadsItem();
        heads.setData(config.getInt(path + ".data"));
        heads.setEnabled(config.getBoolean(path + ".enabled"));
        heads.setMaterial(org.bukkit.Material.getMaterial(config.getInt(path + ".material")));
        heads.setLore(config.getStringList(path + ".lore"));
        heads.setDisplayName(config.getString(path + ".displayname"));
        heads.setPermission(config.getString(path + ".permission"));
        heads.setOwner(config.getString(path + ".owner"));
        heads.setSlot(config.getInt(path + ".slot"));
        if (heads.isEnabled()) list.add(heads);
      }
      playerheadmenus.put(headsItem.getMenuName(), list);
      inv = Bukkit.createInventory(null, Util.serializeInt(list.size()), headsItem.getMenuName());
      for (HeadsItem item : list) {
        if (item.isEnabled()) inv.setItem(item.getSlot(), item.getItemStack());
      }
      inventories.put(headsItem.getMenuName(), inv);
    }
  }

  public static void openMenu(Player player) {
    Inventory inventory = player.getServer().createInventory(player, 3 * 9, ChatManager.colorMessage("Menus.Option-Menu.Players-Heads-Inventory-Name"));
    for (HeadsItem headsItem : headsItems) {
      if (headsItem.isEnabled()) inventory.setItem(headsItem.getSlot(), headsItem.getItemStack());
    }
    player.openInventory(inventory);
  }

  public static Set<String> getMenuNames() {
    return playerheadmenus.keySet();
  }

  public static void onClickInMainMenu(Player player, ItemStack itemStack) {
    for (HeadsItem headsItem : headsItems) {
      if (headsItem.getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase(itemStack.getItemMeta().getDisplayName())) {
        if (!player.hasPermission(headsItem.getPermission())) {
          player.sendMessage(ChatManager.colorMessage("Menus.Option-Menu.Players-Heads-No-Permission"));
          return;
        } else {
          player.openInventory(inventories.get(headsItem.getMenuName()));
                    /*Inventory inventory = player.getServer().createInventory(player, headsItem.getSize(), headsItem.getMenuName());
                    List<HeadsItem> list = playerheadmenus.get(headsItem.getMenuName());
                    for(HeadsItem headsItem1 : list) {
                        if(headsItem.isEnabled()) inventory.setItem(headsItem1.getSlot(), headsItem1.getItemStack());
                    }
                    player.openInventory(inventory);*/
          return;
        }
      }
    }
  }

  public static void onClickInDeeperMenu(Player player, ItemStack itemStack) {
    player.getInventory().addItem(itemStack.clone());
    player.closeInventory();
  }

}
