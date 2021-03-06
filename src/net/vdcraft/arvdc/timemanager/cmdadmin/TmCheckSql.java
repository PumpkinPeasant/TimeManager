package net.vdcraft.arvdc.timemanager.cmdadmin;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.vdcraft.arvdc.timemanager.MainTM;
import net.vdcraft.arvdc.timemanager.mainclass.SqlHandler;

public class TmCheckSql extends MainTM {

    /**
     * CMD /tm sqlcheck
     */
    public static void cmdSqlcheck(CommandSender sender) {

	BukkitRunnable asyncCmdSqlcheck = new BukkitRunnable() {
	    @Override
	    public void run() {
		String isSslOn = " without";
		if (ssl.equalsIgnoreCase("true")) {
		    isSslOn = "";
		}
		// Start notifications
		if (sender instanceof Player) {
		    sender.sendMessage(prefixTMColor + " " + tryReachHostMsg + " \"§e" + host + "§r\" on port §e#" + port + "§r" + isSslOn + " using ssl."); // Player final msg (in case)
		}
		Bukkit.getLogger().info(prefixTM + " " + tryReachHostMsg + " \"" + host + "\" on port #" + port + isSslOn + " using ssl."); // Console final msg (always)
		// Test the connection
		boolean okOrNot = SqlHandler.connectionToHostIsAvailable(true);
		// Notifications
		if (sender instanceof Player) { // Console messages are displayed by 'SqlHandler.connectionToHostIsAvailable()'
		    if (okOrNot == true) { // If the connection is ok
			sender.sendMessage(prefixTMColor + " The mySQL host \"§e" + host + "§r\" " + connectionOkMsg + " §e#" + port + "§r" + isSslOn + " using ssl."); // Player final msg (in case)
			SqlHandler.closeConnection("Host"); // Stop the connection
		    } else { // If the connection is not ok
			sender.sendMessage(prefixTMColor + connectionFailMsg + " \"§e" + host + "§r\". " + checkLogMsg); // Player final msg (in case)
		    }
		}
	    }
	};

	asyncCmdSqlcheck.runTaskAsynchronously(MainTM.getInstance());

    }

};