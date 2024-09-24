package me.wiefferink.areashop.tools;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.wiefferink.areashop.AreaShop;
import me.wiefferink.areashop.managers.FileManager;
import me.wiefferink.areashop.regions.GeneralRegion;

public class PlaceholderExtension extends PlaceholderExpansion {

	private final AreaShop plugin;

	public PlaceholderExtension(AreaShop plugin) {
		this.plugin = plugin;
	}

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
		PlaceholderType placeholderType = null;
		// areashop_<type>_<region/group>_<value>
		// areashop_rent_<region>_price
		// areashop_buy_<region>_price
		// areashop_region_<region>_owner
		// areashop_group_<group>_price

		String type = identifier.split("_")[0];
		if (type.equalsIgnoreCase("rent")) {
			placeholderType = PlaceholderType.RENT;
		} else if (type.equalsIgnoreCase("buy")) {
			placeholderType = PlaceholderType.BUY;
		} else if (type.equalsIgnoreCase("region")) {
			placeholderType = PlaceholderType.REGION;
		} else if (type.equalsIgnoreCase("group")) {
			placeholderType = PlaceholderType.GROUP;
		}

		String regionName = identifier.split("_")[1];
		FileManager fileManager = plugin.getFileManager();
		GeneralRegion region = fileManager.getRegions().stream().filter(regions -> regions.getName().equals(regionName))
				.findFirst().orElse(null);
		if (region == null) {
			return null;
		}

		if (placeholderType.equals(PlaceholderType.RENT)) {
			// Region Status
			if (identifier.endsWith("status")) {
				return fileManager.getRent(regionName).isRented() ? "Rented" : "Not Rented";
			}

			// Region Renter
			if (identifier.endsWith("owner")) {
				if (fileManager.getRent(regionName).isAvailable()) {
					return null;
				}
				return Bukkit.getPlayer(region.getOwner()).getName();
			}

			// Region Rent Price
			if (identifier.endsWith("price")) {
				return fileManager.getRent(regionName).getFormattedPrice();
			}

			// Duration of each rent period
			if (identifier.endsWith("rent_duration")) {
				return fileManager.getRent(regionName).getDurationString();
			}

			// Time left on rent, if rented
			if (identifier.endsWith("time_left")) {
				return fileManager.getRent(regionName).getTimeLeftString();
			}

			if (identifier.endsWith("expiry")) {
				return fileManager.getRent(regionName).provideReplacement(AreaShop.tagRentedUntil).toString();
			}

			// List of groups that a region may be members of
			if (identifier.endsWith("group")) {
				return fileManager.getRent(regionName).getGroupNames().toString();
			}

		}

		if (placeholderType.equals(PlaceholderType.REGION)) {
			// Region Landlord
			if (identifier.endsWith("landlord")) {
				return region.getLandlordName();
			}

			// Region Owner (Renter or Buyer)
			if (identifier.endsWith("owner")) {
				Player owner = Bukkit.getPlayer(region.getOwner());
				if (owner == null) {
					return "No Owner";
				} else {
					return owner.getName();
				}
			}
		}

		return null;
	}

	public enum PlaceholderType {
		RENT, BUY, REGION, GROUP
	}
}
