package net.jonteohr.frameprotector.events;

import net.jonteohr.frameprotector.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ItemFrameListener implements Listener {

	/*
		It's time to lock down the frame and set the owner.
	 */
	@EventHandler
	public void onItemFramePlaced(HangingPlaceEvent e) {
		if(!(e.getEntity() instanceof ItemFrame)) // Not an item frame, let's ignore it.
			return;

		Player player = e.getPlayer();

		if(player == null) // Make sure something weird hasn't happened..
			return;

		// Check if player should be ignored
		if(player.hasPermission("frameprotect.ignore"))
			return;

		// Let's store data in the frame
		PersistentDataContainer data = e.getEntity().getPersistentDataContainer();
		data.set(Main.getOwner(), PersistentDataType.STRING, player.getName());

		player.sendMessage(Main.prefix + ChatColor.GRAY + "Item Frame has been locked.");
	}

	/*
		Let's stop someone from breaking the item frame, or trying to steal the item on it.
	 */
	@EventHandler
	public void onItemFrameBreak(HangingBreakByEntityEvent e) {
		if(!(e.getRemover() instanceof Player)) // Not broken by a player
			return;
		if(!(e.getEntity() instanceof ItemFrame)) // Not an item frame, let's ignore it.
			return;

		Player player = (Player) e.getRemover();

		if(player == null) // Make sure something weird hasn't happened..
			return;

		PersistentDataContainer data = e.getEntity().getPersistentDataContainer();

		// The item frame is not locked, ignore it.
		if(!data.has(Main.getOwner(), PersistentDataType.STRING))
			return;

		String owner = data.get(Main.getOwner(), PersistentDataType.STRING);

		// If it's the owner, then ignore.
		if(owner.equalsIgnoreCase(player.getName()))
			return;

		e.setCancelled(true);
		player.sendMessage(Main.prefix + ChatColor.RED + "This Item Frame is locked, it's owned by: " + ChatColor.GOLD + owner);
	}

	/*
		Make sure that nobody rotates the item on the frame.
	 */
	@EventHandler
	public void onItemFrameInteract(PlayerInteractEntityEvent e) {
		if(!(e.getRightClicked() instanceof ItemFrame)) // Not an item frame, let's ignore it.
			return;

		Player player = e.getPlayer();

		if(player == null) // Make sure something weird hasn't happened..
			return;

		PersistentDataContainer data = e.getRightClicked().getPersistentDataContainer();

		// The item frame is not locked, ignore it.
		if(!data.has(Main.getOwner(), PersistentDataType.STRING))
			return;

		String owner = data.get(Main.getOwner(), PersistentDataType.STRING);

		// If it's the owner, then ignore.
		if(owner.equalsIgnoreCase(player.getName()))
			return;

		e.setCancelled(true);
		player.sendMessage(Main.prefix + ChatColor.RED + "This Item Frame is locked, it's owned by: " + ChatColor.GOLD + owner);
	}
}
