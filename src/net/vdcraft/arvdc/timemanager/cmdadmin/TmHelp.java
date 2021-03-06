package net.vdcraft.arvdc.timemanager.cmdadmin;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vdcraft.arvdc.timemanager.MainTM;

public class TmHelp extends MainTM {

    /**
     * Help messages (Always copy this content in the README.md)
     */
    private static String headerHelp = "§e---------§r Help: " + prefixTMColor + " §e---------";
    private static String helpHelpMsg = "§6/tm help [cmd] <subCmd>: §rHelp provides you the correct usage and a short description of targeted command or subcommand.";
    private static String reloadHelpMsg = "§6/tm reload [all|config|lang]: §rThis command allows you to reload datas from yaml files after manual modifications. All timers will be immediately resynchronized.";
    private static String resyncHelpMsg = "§6/tm resync [all|world]: §rThis command will re-synchronize a single or all worlds timers, based on the startup server's time, the elapsed time and the current speed modifier.";
    private static String checkconfigHelpMsg = "§6/tm checkconfig: §rAdmins and console can display a summary of the config.yml and lang.yml files.";
    private static String checkSqlHelpMsg = "§6/tm checksql: §rCheck the availability of the mySQL server according to the values provided in the config.yml file. This only checks the ip address and the correct port opening.";
    private static String checktimeHelpMsg = "§6/tm checktime [all|server|world]: §rAdmins and console can display a debug/managing message, who displays the startup server's time, the current server's time and the current time, start time and speed for a specific world (or for all of them).";
    private static String checkupdateHelpMsg = "§6/tm checkupdate [bukkit|spigot|github]: §rSearch if a newer version of the plugin exists on the chosen server. (MC 1.18.9+ only)";
    private static String setupdateSrcHelpMsg = "§6/tm set updatesrc [bukkit|spigot|github]: §rDefine the source server for the update search. (MC 1.18.9+ only)";
    private static String setDebugHelpMsg = "§6/tm set debugmode [true|false]: §rSet true to enable colored verbose messages in the console. Useful to understand some mechanisms of this plugin.";
    private static String setInitialTickHelpMsg = "§6/tm set multilang [tick|HH:mm:ss]: Modify the server's initial tick.";
    private static String setMultilangHelpMsg = "§6/tm set multilang [true|false]: §rSet true or false to use an automatic translation for the §o/now §rcommand.";
    private static String setDefLangHelpMsg = "§6/tm set deflang [lg_LG]: §rChoose the translation to use if player's locale doesn't exist in the lang.yml or when §o'multiLang'§r is false.";
    private static String setRefreshRateHelpMsg = "§e/tm set refreshrate [tick]: §rSet the delay (in ticks) before actualizing the speed stretch/expand effect. Must be an integer between §o" + refreshMin + "§r and §o" + refreshMax + "§r. Default value is §o" + defRefresh + " ticks§r, please note that a too small value can cause server lags.";
    private static String setSleepHelpMsg = "§6/tm set sleep [true|false] [all|world]: §rDefine if players can sleep until the next day in the specified world (or in all of them). By default, all worlds will start with parameter true, unless their timer is frozen or in real time who will be necessary false.";
    private static String setSpeedHelpMsg = "§6/tm set speed [multiplier] [all|world]: §rThe decimal number argument will multiply the world(s) speed. Use §o0.0§r to freeze time, numbers from §o0.1§r to §o0.9§r to slow time, §o1.0§o to get normal speed and numbers from §o1.1§r to " + speedMax + " to speedup time. Set this value to §o24.0§r or §orealtime§r to make the world time match the real speed time.";
    private static String setStartHelpMsg = "§6/tm set start [tick|daypart|HH:mm:ss] [all|world]: §rDefines the time at server startup for the specified world (or all of them). By default, all worlds will start at §otick #0§r. The timer(s) will be immediately resynchronized.";
    private static String setTimeHelpMsg = "§6/tm set time [tick|daypart|HH:mm:ss] [all|world]: §rSets current time for the specified world (or all of them). Consider using this instead of the vanilla §o/time§r command. The tab completion also provides handy presets like \"day\", \"noon\", \"night\", \"midnight\", etc.";
    private static String setSyncHelpMsg = "§6/tm set sync [true|false] [all|world]: §rDefine if the speed distortion method will increase/decrease the world's actual tick, or fit the theoretical tick value based on the server one. By default, all worlds will start with parameter false. Real time based worlds and frozen worlds do not use this option, on the other hand this will affect even the worlds with a normal speed.";   
    // Except this line, used when 'set' is used without additional argument
    private static String missingSetArgHelpMsg = "§e/tm help set [deflang|multilang|refreshrate|sleep|speed|start|sync|time]: §rThis command, use with arguments, permit to change plugin parameters.";

    /**
     * CMD /tm help [cmd]
     */
    public static boolean cmdHelp(CommandSender sender, String[] args) {
	int argsNb = args.length;
	String specificCmdMsg = "";
	// /tm help set [arg]
	if (argsNb >= 3) {
	    if (args[1].equalsIgnoreCase(CMD_SET)) {
		String subCmd = args[2].toLowerCase();
		// /tm help set debugMode
		if (subCmd.contains(CMD_SET_DEBUG)) {
		    specificCmdMsg = setDebugHelpMsg; // Help msg (in case of 2 args)
		}
		// /tm help set deflang
		if (subCmd.contains(CMD_SET_DEFLANG)) {
		    specificCmdMsg = setDefLangHelpMsg; // Help msg (in case of 2 args)
		}
		// /tm help set initialtick
		else if (subCmd.contains(CMD_SET_INITIALTICK)) {
		    specificCmdMsg = setInitialTickHelpMsg; // Help msg (in case of 2 args)
		}
		// /tm help set multilang
		else if (subCmd.contains(CMD_SET_MULTILANG)) {
		    specificCmdMsg = setMultilangHelpMsg; // Help msg (in case of 2 args)
		}
		// /tm help set refreshrate
		else if (subCmd.contains(CMD_SET_REFRESHRATE)) {
		    specificCmdMsg = setRefreshRateHelpMsg; // Help msg (in case of 2 args)
		}
		// /tm help set sleep
		else if (subCmd.contains(CMD_SET_SLEEP) || args[2].equalsIgnoreCase("sleepUntilDawn")) { // alias for v1.0 compatibility
		    specificCmdMsg = setSleepHelpMsg; // Help msg (in case of 2 args)
		}
		// /tm help set speed
		else if (subCmd.contains(CMD_SET_SPEED)) {
		    specificCmdMsg = setSpeedHelpMsg; // Help msg (in case of 2 args)
		}
		// /tm help set start
		else if (subCmd.contains(CMD_SET_START)) {
		    specificCmdMsg = setStartHelpMsg; // Help msg (in case of 2 args)
		}
		// /tm help set sync
		else if (subCmd.contains(CMD_SET_SYNC) || args[2].contains("synchro")) {// alias for commodity
		    specificCmdMsg = setSyncHelpMsg; // Help msg (in case of 2 args)
		}
		// /tm help set time
		else if (subCmd.contains(CMD_SET_TIME)) {
		    specificCmdMsg = setTimeHelpMsg; // Help msg (in case of 2 args)
		}
		// /tm help set updateMsgSrc
		else if ((subCmd.contains(CMD_SET_UPDATE))
			&& (MainTM.decimalOfMcVersion >= MainTM.requiredMcVersionForUpdate)) {
		    specificCmdMsg = setupdateSrcHelpMsg; // Help msg (in case of 2 args)
		}
	    }
	} else if (argsNb >= 2) {
	    String subCmd = args[1].toLowerCase();
	    // /tm help checkconfig
	    if (subCmd.contains(CMD_CHECKCONFIG)) {
		specificCmdMsg = checkconfigHelpMsg; // Help msg (in case of 1 arg)
	    }
	    // /tm help checksql
	    else if (subCmd.contains(CMD_CHECKSQL) || subCmd.contains("sqlcheck")) { // alias for v1.0 compatibility
		specificCmdMsg = checkSqlHelpMsg; // Help msg (in case of 1 arg)
	    }
	    // /tm help checktime
	    else if (subCmd.contains(CMD_CHECKTIME)) {
		specificCmdMsg = checktimeHelpMsg; // Help msg (in case of 1 arg)
	    }
	    // /tm help checkupdate
	    else if ((subCmd.contains(CMD_CHECKUPDATE))
		    && (MainTM.decimalOfMcVersion >= MainTM.requiredMcVersionForUpdate)) {
		specificCmdMsg = checkupdateHelpMsg; // Help msg (in case of 1 arg)
	    }
	    // /tm help reload
	    else if (subCmd.contains(CMD_RELOAD)) {
		specificCmdMsg = reloadHelpMsg; // Help msg (in case of 1 arg)
	    }
	    // /tm help resync
	    else if (subCmd.contains(CMD_RESYNC)) {
		specificCmdMsg = resyncHelpMsg; // Help msg (in case of 1 arg)
	    }
	    // /tm help set <null>
	    else if (subCmd.contains(CMD_SET)) {
		specificCmdMsg = missingSetArgHelpMsg; // Help msg (in case of 1 arg)
	    }
	    // Maybe someone could forget the 'set' part, so think of its place
	    else if (subCmd.contains(CMD_SET_DEBUG) || subCmd.contains(CMD_SET_DEFLANG) || subCmd.contains(CMD_SET_MULTILANG)
		    || subCmd.contains(CMD_SET_REFRESHRATE) || subCmd.contains(CMD_SET_SLEEP) || subCmd.contains(CMD_SET_SPEED)
		    || subCmd.contains(CMD_SET_START) || subCmd.contains(CMD_SET_SYNC) || subCmd.contains(CMD_SET_TIME) || subCmd.contains(CMD_SET_UPDATE)) {
		Bukkit.dispatchCommand(sender, CMD_TM + " " + CMD_HELP + " " + CMD_SET + " " + subCmd); // retry with correct arguments
		return true;
	    }
	}
	// Display Help header
	sender.sendMessage(headerHelp); // Final msg (always)
	// Display specific cmd msg
	if (!(specificCmdMsg.equals(""))) {
	    sender.sendMessage(specificCmdMsg);
	    return true;
	}
	// Else, display basic help msg and the list of cmds from plugin.yml
	sender.sendMessage(helpHelpMsg); // Final msg (always)
	return false;
    }

    /**
     * Display an error and its associated help message
     */
    public static void sendErrorMsg(CommandSender sender, String msgError, String cmdHelp) {
	MainTM.getInstance();
	if (sender instanceof Player) {
	    sender.sendMessage(prefixTMColor + " §c" + msgError); // Player error msg (in case is player)
	}
	Bukkit.getLogger().warning(prefixTM + " " + msgError); // Console error msg (always)
	Bukkit.dispatchCommand(sender, "tm help " + cmdHelp); // Sender help msg (always)
    }

};