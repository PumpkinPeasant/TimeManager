package net.vdcraft.arvdc.timemanager.mainclass;

import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.scheduler.BukkitScheduler;

import net.vdcraft.arvdc.timemanager.MainTM;

public class WorldSleepHandler implements Listener {

    /**
     * When a player try to sleep, authorize entering the bed but check if the time need to be spend until the dawn or not
     */

    // # 1. Listen to PlayerBedEnterEvent in relevant worlds
    @EventHandler
    public void whenPlayerTryToSleep(PlayerBedEnterEvent e) throws InterruptedException {
	// Get the event's world name
	Player p = e.getPlayer();
	World w = e.getBed().getWorld();
	String wn = w.getName();
	double speedModifier = MainTM.getInstance().getConfig().getDouble(MainTM.CF_WORLDSLIST + "." + wn + "." + MainTM.CF_SPEED); // Read config.yml to get the world's 'speed' value
	// Ignore: Nether and Ender worlds AND worlds with a fixed time (speed 0 or 24)
	if (!(wn.contains("_nether")) && !(wn.contains("_the_end")) && !(speedModifier == 24.00) && !(speedModifier == 0)) {
	    // Delay the doDaylightCycle gamerule change
	    sleepTicksCount(p, w, speedModifier);
	}
    }

    // # 2. Wait the nearly end of the sleep to adapt gamerule doDaylightCycle to permit or refuse the night spend until the dawn
    public static void sleepTicksCount(Player p, World w, double speedModifier) {
	BukkitScheduler firstSyncSheduler = MainTM.getInstance().getServer().getScheduler();
	firstSyncSheduler.scheduleSyncDelayedTask(MainTM.getInstance(), new Runnable() {
	    @SuppressWarnings("deprecation")
	    @Override
	    public void run() {
		// Check if the player is actually sleeping
		if (p.isSleeping() == true) {
		    int st = p.getSleepTicks();
		    // Wait just before the end of the sleep (= 100 ticks)
		    if (st <= 99) {
			sleepTicksCount(p, w, speedModifier);
			// Change the gamerule doDaylightCycle to true or false
		    } else {
			Boolean isSleepPermited = true;
			if (MainTM.getInstance().getConfig().getString(MainTM.CF_WORLDSLIST + "." + w.getName() + "." + MainTM.CF_SLEEP).equals("false")) {
			    isSleepPermited = false;
			}
			// Do something only if the value contradicts the current settings
			if ((isSleepPermited.equals(false) && speedModifier >= 1.0) || (isSleepPermited.equals(true) && speedModifier < 1.0)) {

			    if (MainTM.decimalOfMcVersion < 13.0) {
				w.setGameRuleValue("doDaylightCycle", isSleepPermited.toString());
			    } else {
				w.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, isSleepPermited);
			    }
			    restoreDayCycle(p, w.getName());
			}
		    }
		}
	    }
	}, 1L);
    }

    // # 3. Restore the doDaylightCycle gamerule to its default value
    public static void restoreDayCycle(Player p, String wn) {
	BukkitScheduler firstSyncSheduler = MainTM.getInstance().getServer().getScheduler();
	firstSyncSheduler.scheduleSyncDelayedTask(MainTM.getInstance(), new Runnable() {
	    @Override
	    public void run() {
		// This will check if the doDaylightCycle value needs to be restored
		WorldDayCycleHandler.doDaylightCheck(wn);
	    }
	}, 5L);
    }

};