package net.jonteohr.frameprotector;

import net.jonteohr.frameprotector.events.ItemFrameListener;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.permission.Permission;
import org.bukkit.plugin.java.annotation.permission.Permissions;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

@org.bukkit.plugin.java.annotation.plugin.Plugin(
		name = "FrameProtector",
		version = "1.0"
)
@Author("rlHypr")
@Description("Minimalistic plugin that locks item frames to the owner.")
@ApiVersion(ApiVersion.Target.v1_15)
@Permissions({
		@Permission(
				name = "frameprotector.override",
				desc = "Ability to override protection."
		),
		@Permission(
				name = "frameprotect.ignore",
				desc = "Will not protect frames from users with this permission.",
				defaultValue = PermissionDefault.FALSE
		)
})
public class Main extends JavaPlugin {

	private static Plugin plugin;
	private static NamespacedKey owner;

	public static String prefix = ChatColor.DARK_RED + "[" + ChatColor.RED + "FrameProtector" + ChatColor.DARK_RED + "]" + ChatColor.RESET + " ";

	@Override
	public void onEnable() {
		plugin = this;

		owner = new NamespacedKey(plugin, "owner");

		getServer().getPluginManager().registerEvents(new ItemFrameListener(), this);

		getLogger().info("Frame Protector loaded!");
	}

	@Override
	public void onDisable() {}

	public static NamespacedKey getOwner() {
		return owner;
	}
}
