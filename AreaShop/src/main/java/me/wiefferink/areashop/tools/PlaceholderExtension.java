package me.wiefferink.areashop.tools;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.wiefferink.areashop.AreaShop;
import me.wiefferink.areashop.regions.GeneralRegion;

public class PlaceholderExtension extends PlaceholderExpansion{
    @Override
    public String getAuthor() {
        return "Curtis73";
    }

    @Override
    public String getIdentifier() {
        return "areashop";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
        // areashop_<region>_owner etc.
        String regionName = identifier.split("_")[1];
        GeneralRegion region = AreaShop.getInstance().getFileManager().getRegions().stream().filter(regions -> regions.getName().equals(regionName)).findFirst().orElse(null);
        if (region == null) {
            return null;
        }
        
        if (identifier.endsWith("owner")) {
            return Bukkit.getPlayer(region.getOwner()).getName();
        }
        return null;
    }
}
