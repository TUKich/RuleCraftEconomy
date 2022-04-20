package rce;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.event.DeleteTownEvent;
import com.palmergames.bukkit.towny.event.NewNationEvent;
import com.palmergames.bukkit.towny.event.NewTownEvent;
import com.palmergames.bukkit.towny.event.PreDeleteNationEvent;
import com.palmergames.bukkit.towny.event.PreDeleteTownEvent;
import com.palmergames.bukkit.towny.event.PreNewDayEvent;
import com.palmergames.bukkit.towny.event.RenameNationEvent;
import com.palmergames.bukkit.towny.event.RenameTownEvent;
import com.palmergames.bukkit.towny.event.TownAddResidentEvent;
import com.palmergames.bukkit.towny.event.TownRemoveResidentEvent;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.utils.ResidentUtil;

public class events implements Listener {
	
	private Economy plugin;
	
	public events(Economy plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onNationRename (RenameNationEvent e) {
		
		int rm = 0;
		int tax = 0;
		
		File nations =  new File(plugin.getDataFolder() + "/nations.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(nations);
		rm = plugin.getRM_N(e.getOldName());
		tax = customConfig.getInt("nations." + e.getNation().getName() + ".tax");
		customConfig.set("nations." + e.getOldName(), null);
		customConfig.createSection("nations." + e.getNation().getName() + ".rm");
		customConfig.createSection("nations." + e.getNation().getName() + ".tax");
		customConfig.set("nations." + e.getNation().getName() + ".rm", rm);
		customConfig.set("nations." + e.getNation().getName() + ".tax", tax);
		try {
			customConfig.save(nations);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	@EventHandler
	public void onNationCreation (NewNationEvent e) {
		File nations =  new File(plugin.getDataFolder() + "/nations.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(nations);
		customConfig.createSection("nations." + e.getNation().getName() + ".rm");
		customConfig.createSection("nations." + e.getNation().getName() + ".tax");
		customConfig.set("nations." + e.getNation().getName() + ".rm", 0);
		customConfig.set("nations." + e.getNation().getName() + ".tax", 0);
		try {
			customConfig.save(nations);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		Town town = e.getNation().getCapital();
		
		List<Resident> res_list = new ArrayList<>(ResidentUtil.getOnlineResidentsViewable(null, town));
		ListIterator<Resident> resItr = res_list.listIterator();
		while (resItr.hasNext()) {
			Resident res = resItr.next();
			plugin.removeDebuff(Bukkit.getPlayer(res.getName()));
		}
		
		
	}
	
	@EventHandler
	public void onNationDeleting (PreDeleteNationEvent e) {
		
		File nations =  new File(plugin.getDataFolder() + "/nations.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(nations);
		
		customConfig.set("nations." + e.getNationName(), null);
		
		try {
			customConfig.save(nations);
			
			List<Town> town_list = new ArrayList<>(TownyUniverse.getInstance().getDataSource().getNation(e.getNationName()).getTowns());
			ListIterator<Town> townItr = town_list.listIterator();
			while (townItr.hasNext()) {
				Town town = townItr.next();

				for (Resident res : ResidentUtil.getOnlineResidentsViewable(null, town)) {
					plugin.removeDebuff(Bukkit.getPlayer(res.getName()));
					if(town.getResidents().size() >= plugin.getNoNationDebuffNum()) {
						plugin.addDebuff(Bukkit.getPlayer(res.getName()));
					}
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (NotRegisteredException e1) {
			plugin.getLogger().info("Error with town or nation!");
		}
	}
	
	@EventHandler
	public void onTownRename (RenameTownEvent e) {
		
		int rm = 0, ef = 3, tnp = 0;
		boolean ef_cd = false;
		
		File towns =  new File(plugin.getDataFolder() + "/towns.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(towns);
		tnp = plugin.getTNP(e.getOldName());
		rm = plugin.getRM(e.getOldName());
		ef = plugin.getEF(e.getOldName());
		ef_cd = plugin.getEFCD(e.getOldName());
		customConfig.set("towns." + e.getOldName(), null);
		customConfig.createSection("towns." + e.getTown().getName() + ".tnp");
		customConfig.createSection("towns." + e.getTown().getName() + ".rm");
		customConfig.createSection("towns." + e.getTown().getName() + ".ef");
		customConfig.createSection("towns." + e.getTown().getName() + ".ef_cd");
		customConfig.set("towns." + e.getTown().getName() + ".tnp", tnp);
		customConfig.set("towns." + e.getTown().getName() + ".rm", rm);
		customConfig.set("towns." + e.getTown().getName() + ".ef", ef);
		customConfig.set("towns." + e.getTown().getName() + ".ef_cd", ef_cd);
		try {
			customConfig.save(towns);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	@EventHandler
	public void onTownCreation (NewTownEvent e) {
		File towns =  new File(plugin.getDataFolder() + "/towns.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(towns);
		customConfig.createSection("towns." + e.getTown().getName() + ".tnp");
		customConfig.createSection("towns." + e.getTown().getName() + ".rm");
		customConfig.createSection("towns." + e.getTown().getName() + ".ef");
		customConfig.createSection("towns." + e.getTown().getName() + ".ef_cd");
		customConfig.set("towns." + e.getTown().getName() + ".tnp", 0);
		customConfig.set("towns." + e.getTown().getName() + ".rm", 0);
		customConfig.set("towns." + e.getTown().getName() + ".ef", 3);
		customConfig.set("towns." + e.getTown().getName() + ".ef_cd", false);
		try {
			customConfig.save(towns);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}
	
	@EventHandler
	public void onTownDeleting (PreDeleteTownEvent e) {
		Resident res;
		Town town = e.getTown();
		
		List<Resident> res_list = new ArrayList<>(ResidentUtil.getOnlineResidentsViewable(null, town));
		ListIterator<Resident> resItr = res_list.listIterator();
		while (resItr.hasNext()) {
			res = resItr.next();
			plugin.removeEFeffect(town, Bukkit.getPlayer(res.getName()));
			plugin.removeDebuff(Bukkit.getPlayer(res.getName()));
		}
		
		File towns =  new File(plugin.getDataFolder() + "/towns.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(towns);
		
		customConfig.set("towns." + e.getTown().getName(), null);
		
		try {
			customConfig.save(towns);
		} catch (IOException e1) {
			plugin.getLogger().info("Error with deleting town!");
		}
	}
	
	@EventHandler
	public void onTownDeleted (DeleteTownEvent e) {
		Resident res;
		Town town;
		try {
			town = TownyUniverse.getInstance().getDataSource().getTown(e.getTownName());
			
			List<Resident> res_list = new ArrayList<>(ResidentUtil.getOnlineResidentsViewable(null, town));
			ListIterator<Resident> resItr = res_list.listIterator();
			while (resItr.hasNext()) {
				res = resItr.next();
				plugin.removeEFeffect(town, Bukkit.getPlayer(res.getName()));
				plugin.removeDebuff(Bukkit.getPlayer(res.getName()));
			}
			
			File towns =  new File(plugin.getDataFolder() + "/towns.yml");
			FileConfiguration customConfig = YamlConfiguration.loadConfiguration(towns);
			
			customConfig.set("towns." + town.getName(), null);
			
			customConfig.save(towns);
		} catch (IOException e1) {
			plugin.getLogger().info("Error with deleting town!");
		} catch (NotRegisteredException e2) {
			plugin.getLogger().info("Error with deleting town!");
		}
	}

	@EventHandler
	public void onNewDay (PreNewDayEvent e) {
		
		//long start = System.currentTimeMillis();
		
		String town_costs_collected = plugin.getConfig().getString("events.town_costs_collected");
		String town_deleted = plugin.getConfig().getString("events.town_deleted");
		String nation_costs_collected = plugin.getConfig().getString("events.nation_costs_collected");
		String nation_deleted = plugin.getConfig().getString("events.nation_deleted");
		String nation_tax_collected = plugin.getConfig().getString("events.nation_tax_collected");
		
		town_costs_collected = town_costs_collected.replace("&", "\u00a7");
		town_deleted = town_deleted.replace("&", "\u00a7");
		nation_costs_collected = nation_costs_collected.replace("&", "\u00a7");
		nation_deleted = nation_deleted.replace("&", "\u00a7");
		nation_tax_collected  = nation_tax_collected.replace("&", "\u00a7");
		
		TownyUniverse townyUniverse = TownyUniverse.getInstance();
		List<Town> towns_list = new ArrayList<>(townyUniverse.getDataSource().getTowns());
		ListIterator<Town> townItr = towns_list.listIterator();

		List<Nation> nations_list = new ArrayList<>(townyUniverse.getDataSource().getNations());
		ListIterator<Nation> nationItr = nations_list.listIterator();
		
		int rm_now = 0;
		int tnp_now = 0;
		int rm_cost = 0;
		int tnp_cost = 0;
		float rm_fm_k = 1;
		float tnp_fm_k = 1;

		int nrm_now = 0;
		int nrm_cost = 0;
		
		while (townItr.hasNext()) {
			Town town = townItr.next();
			
			if (plugin.getEFCD(town.getName())) {
				plugin.setEFCD(town.getName(), false);
			}
			if (townyUniverse.getDataSource().hasTown(town.getName())) {
				tnp_now = plugin.getTNP(town.getName());
				rm_now = plugin.getRM(town.getName());

				if( rm_now > 0 || tnp_now > 0 ) {
					switch (plugin.getEF(town.getName())) {
						case 1:
							rm_fm_k = rm_fm_k * ((float) plugin.getConfig().getInt("ef.ef_1_rm_k") / 100);
							tnp_fm_k = tnp_fm_k * ((float) plugin.getConfig().getInt("ef.ef_1_tnp_k") / 100);
							break;
						case 2:
							rm_fm_k = rm_fm_k * ((float) plugin.getConfig().getInt("ef.ef_2_rm_k") / 100);
							tnp_fm_k = tnp_fm_k * ((float) plugin.getConfig().getInt("ef.ef_2_tnp_k") / 100);
							break;
						case 3:
							rm_fm_k = rm_fm_k * ((float) plugin.getConfig().getInt("ef.ef_3_rm_k") / 100);
							tnp_fm_k = tnp_fm_k * ((float) plugin.getConfig().getInt("ef.ef_3_tnp_k") / 100);
							break;
						case 4:
							rm_fm_k = rm_fm_k * ((float) plugin.getConfig().getInt("ef.ef_4_rm_k") / 100);
							tnp_fm_k = tnp_fm_k * ((float) plugin.getConfig().getInt("ef.ef_4_tnp_k") / 100);
							break;
						case 5:
							rm_fm_k = rm_fm_k * ((float) plugin.getConfig().getInt("ef.ef_5_rm_k") / 100);
							tnp_fm_k = tnp_fm_k * ((float) plugin.getConfig().getInt("ef.ef_5_tnp_k") / 100);
							break;
							
					}
					
					rm_cost = (int) (town.getTownBlocks().size() * rm_fm_k * plugin.getRMcost());
					tnp_cost = (int) (town.getNumResidents() * tnp_fm_k * plugin.getTNPcost());
					
					rm_fm_k = 1;
					tnp_fm_k = 1;
					
					plugin.addRM(town.getName(), -rm_cost);
					plugin.addTNP(town.getName(), -tnp_cost);

					town_costs_collected = town_costs_collected.replace("$rm_now", String.valueOf(plugin.getRM(town.getName())));
					town_costs_collected = town_costs_collected.replace("$tnp_now", String.valueOf(plugin.getTNP(town.getName())));
					town_costs_collected = town_costs_collected.replace("$rm", String.valueOf(rm_cost));
					town_costs_collected = town_costs_collected.replace("$tnp", String.valueOf(tnp_cost));
							
					for(Player p : Bukkit.getOnlinePlayers()) {
						Resident res;
						try {
							res = townyUniverse.getDataSource().getResident(p.getName());
							if (res.hasTown()) {
								if (res.getTown().equals(town)) {		
									p.sendMessage(town_costs_collected);
								}
							}
						} catch (NotRegisteredException e1) {
							e1.printStackTrace();
						}
					}
					town_costs_collected = plugin.getConfig().getString("events.town_costs_collected");
					town_costs_collected = town_costs_collected.replace("&", "\u00a7");
				}
				
				tnp_now = plugin.getTNP(town.getName());
				rm_now = plugin.getRM(town.getName());
				
				if( rm_now <= 0 || tnp_now <= 0 ) {
					townyUniverse.getDataSource().removeTown(town);
					townyUniverse.getDataSource().saveTowns();
					
					town_deleted = town_deleted.replace("$town", town.getName());
					
					for(Player p : Bukkit.getOnlinePlayers()) {
						p.sendMessage(town_deleted);
					}
				}
				town_deleted = plugin.getConfig().getString("events.town_deleted");
				town_deleted = town_deleted.replace("&", "\u00a7");
			}
		}
		while (nationItr.hasNext()) {
			Nation nation = nationItr.next();
			
			if (townyUniverse.getDataSource().hasNation(nation.getName())) {
				nrm_now = plugin.getRM_N(nation.getName());
				
				while (townItr.hasNext()) {
					Town town = townItr.next();

					if (townyUniverse.getDataSource().hasTown(town.getName())) {
						rm_now = plugin.getRM(town.getName());
						if (town.hasNation()) {
							try {
								if (town.getNation().equals(nation)) {
									if (plugin.getRM(town.getName()) < plugin.getTax_N(nation.getName())) {
										plugin.addRM_N(nation.getName(), plugin.getRM(town.getName()));
										plugin.addRM(town.getName(), -plugin.getRM(town.getName()));
										
										nation_tax_collected = nation_tax_collected.replace("$rm_collected", String.valueOf(plugin.getRM(town.getName())));
										nation_tax_collected = nation_tax_collected.replace("$town", town.getName());
										
										for(Player p : Bukkit.getOnlinePlayers()) {
											Resident res;
											try {
												res = townyUniverse.getDataSource().getResident(p.getName());
												if (res.hasNation()) {
													if (res.getTown().getNation().equals(nation)) {		
														p.sendMessage(nation_tax_collected);
													}
												}
											} catch (NotRegisteredException e1) {
												e1.printStackTrace();
											}
										}
										nation_tax_collected = plugin.getConfig().getString("events.nation_tax_collected");
										nation_tax_collected = nation_tax_collected.replace("&", "\u00a7");
									}
									else {
										plugin.addRM_N(nation.getName(), plugin.getTax_N(nation.getName()));
										plugin.addRM(town.getName(), -plugin.getTax_N(nation.getName()));
										
										nation_tax_collected = nation_tax_collected.replace("$rm_collected", String.valueOf(plugin.getTax_N(nation.getName())));
										nation_tax_collected = nation_tax_collected.replace("$town", town.getName());
										
										for(Player p : Bukkit.getOnlinePlayers()) {
											Resident res;
											try {
												res = townyUniverse.getDataSource().getResident(p.getName());
												if (res.hasNation()) {
													if (res.getTown().getNation().equals(nation)) {		
														p.sendMessage(nation_tax_collected);
													}
												}
											} catch (NotRegisteredException e1) {
												e1.printStackTrace();
											}
										}
										nation_tax_collected = plugin.getConfig().getString("events.nation_tax_collected");
										nation_tax_collected = nation_tax_collected.replace("&", "\u00a7");
									}
								}
							} catch (NotRegisteredException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
				
				if( nrm_now > 0 ) {
					nrm_cost = (int) (nation.getNumTownblocks() * plugin.getRM_Ncost());
					
					plugin.addRM_N(nation.getName(), -nrm_cost);

					nation_costs_collected = nation_costs_collected.replace("$rm_now", String.valueOf(plugin.getRM_N(nation.getName())));
					nation_costs_collected = nation_costs_collected.replace("$rm", String.valueOf(nrm_cost));
					
					for(Player p : Bukkit.getOnlinePlayers()) {
						Resident res;
						try {
							res = townyUniverse.getDataSource().getResident(p.getName());
							if (res.hasNation()) {
								if (res.getTown().getNation().equals(nation)) {		
									p.sendMessage(nation_costs_collected);
								}
							}
						} catch (NotRegisteredException e1) {
							e1.printStackTrace();
						}
					}
					nation_costs_collected = plugin.getConfig().getString("events.nation_costs_collected");
					nation_costs_collected = nation_costs_collected.replace("&", "\u00a7");
				}
				if( nrm_now <= 0 ) {
					townyUniverse.getDataSource().removeNation(nation);
					townyUniverse.getDataSource().saveTowns();
					
					nation_deleted = nation_deleted.replace("$nation", nation.getName());
					
					for(Player p : Bukkit.getOnlinePlayers()) {
						p.sendMessage(nation_deleted);
					}
				}
				nation_deleted = plugin.getConfig().getString("events.nation_deleted");
				nation_deleted = nation_deleted.replace("&", "\u00a7");
			}
		}
		
		
		/*File towns =  new File(plugin.getDataFolder() + "/towns.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(towns);
		customConfig.createSection("towns." + e.getTown().getName() + ".rm");
		customConfig.createSection("towns." + e.getTown().getName() + ".ef");
		customConfig.set("towns." + e.getTown().getName() + ".rm", "0");
		customConfig.set("towns." + e.getTown().getName() + ".ef", "0");
		try {
			customConfig.save(towns);
		} catch (IOException e1) {
			e1.printStackTrace();
		}*/
		
	}

	@EventHandler
	public void onJoin (PlayerJoinEvent e) {
		
		String your_town_will_be_deleted_next_day = plugin.getConfig().getString("events.your_town_will_be_deleted_next_day");
		String your_nation_will_be_deleted_next_day = plugin.getConfig().getString("events.your_nation_will_be_deleted_next_day");
		
		your_town_will_be_deleted_next_day = your_town_will_be_deleted_next_day.replace("&", "\u00a7");
		your_nation_will_be_deleted_next_day = your_nation_will_be_deleted_next_day.replace("&", "\u00a7");
		
		Town town;
		Resident res;
		Player p = e.getPlayer();
		try {
			res = TownyUniverse.getInstance().getDataSource().getResident(p.getName());
			
			if (res.hasTown()) {
				town = res.getTown();
				plugin.addEFeffect(town, e.getPlayer());
				
				plugin.removeDebuff(Bukkit.getPlayer(res.getName()));
				if(town.getResidents().size() >= plugin.getNoNationDebuffNum()) {
					if (!town.hasNation()) {
						plugin.addDebuff(Bukkit.getPlayer(res.getName()));
					}
				}
				
				float tnp_now = plugin.getTNP(town.getName());
				float rm_now = plugin.getRM(town.getName());
				float rm_fm_k = 1;
				float tnp_fm_k = 1;

				switch (plugin.getEF(town.getName())) {
					case 1:
						rm_fm_k = rm_fm_k * ((float) plugin.getConfig().getInt("ef.ef_1_rm_k") / 100);
						tnp_fm_k = tnp_fm_k * ((float) plugin.getConfig().getInt("ef.ef_1_tnp_k") / 100);
						break;
					case 2:
						rm_fm_k = rm_fm_k * ((float) plugin.getConfig().getInt("ef.ef_2_rm_k") / 100);
						tnp_fm_k = tnp_fm_k * ((float) plugin.getConfig().getInt("ef.ef_2_tnp_k") / 100);
						break;
					case 3:
						rm_fm_k = rm_fm_k * ((float) plugin.getConfig().getInt("ef.ef_3_rm_k") / 100);
						tnp_fm_k = tnp_fm_k * ((float) plugin.getConfig().getInt("ef.ef_3_tnp_k") / 100);
						break;
					case 4:
						rm_fm_k = rm_fm_k * ((float) plugin.getConfig().getInt("ef.ef_4_rm_k") / 100);
						tnp_fm_k = tnp_fm_k * ((float) plugin.getConfig().getInt("ef.ef_4_tnp_k") / 100);
						break;
					case 5:
						rm_fm_k = rm_fm_k * ((float) plugin.getConfig().getInt("ef.ef_5_rm_k") / 100);
						tnp_fm_k = tnp_fm_k * ((float) plugin.getConfig().getInt("ef.ef_5_tnp_k") / 100);
						break;	
				}
				
				int rm_cost = (int) (town.getTownBlocks().size() * rm_fm_k * plugin.getRMcost());
				int tnp_cost = (int) (town.getNumResidents() * tnp_fm_k * plugin.getTNPcost());
				
				if (rm_cost > rm_now || tnp_cost > tnp_now ) {
					p.sendMessage(your_town_will_be_deleted_next_day);
				}
				
				if (res.hasNation()) {
					int nrm_cost = (int) (town.getNation().getNumTownblocks() * plugin.getRM_Ncost());
					int nrm_now = plugin.getRM_N(town.getNation().getName());
					if (nrm_cost > nrm_now) {
						p.sendMessage(your_nation_will_be_deleted_next_day);
					}
				}
			}
		} catch (NotRegisteredException e1) {
			p.sendMessage("Error!");
		}
	}
	
	@EventHandler
	public void onLeave (PlayerQuitEvent e) {
		Town town;
		Resident res;
		Player p = e.getPlayer();
		try {
			res = TownyUniverse.getInstance().getDataSource().getResident(p.getName());
			if (res.hasTown()) {
				town = res.getTown();
				plugin.removeEFeffect(town, e.getPlayer());
			}
			plugin.removeDebuff(Bukkit.getPlayer(res.getName()));
		} catch (NotRegisteredException e1) {
			p.sendMessage("Error!");
		}
		plugin.removeDebuff(e.getPlayer());
	}

	@EventHandler
	public void onMilkDrink (PlayerItemConsumeEvent e) {
		
		if (e.getItem().getType().equals(Material.MILK_BUCKET)) {
			e.setCancelled(true);
			e.getPlayer().sendMessage("Milk is out of law.");
		}
	}

	@EventHandler
	public void onTownJoin (TownAddResidentEvent e) {
		Town town;
		Resident res;
		town = e.getTown();
		
		List<Resident> res_list = new ArrayList<>(ResidentUtil.getOnlineResidentsViewable(null, town));
		ListIterator<Resident> resItr = res_list.listIterator();
		while (resItr.hasNext()) {
			res = resItr.next();
			if (Bukkit.getPlayer(res.getName()).isOnline()) {
				plugin.removeEFeffect(town, Bukkit.getPlayer(res.getName()));
				plugin.addEFeffect(town, Bukkit.getPlayer(res.getName()));
			}
			plugin.removeDebuff(Bukkit.getPlayer(res.getName()));
			if(town.getResidents().size() >= plugin.getNoNationDebuffNum()) {
				if (!town.hasNation()) {
					plugin.addDebuff(Bukkit.getPlayer(res.getName()));
					plugin.getLogger().info("Debuff added on resident join");
				}
			}
		}
	}

	@EventHandler
	public void onTownLeave (TownRemoveResidentEvent e) {
		Town town;
		
		town = e.getTown();
		plugin.removeEFeffect(town, Bukkit.getPlayer(e.getResident().getName()));
		plugin.removeDebuff(Bukkit.getPlayer(e.getResident().getName()));
		
		List<Resident> res_list = new ArrayList<>(ResidentUtil.getOnlineResidentsViewable(null, town));
		ListIterator<Resident> resItr = res_list.listIterator();
		while (resItr.hasNext()) {
			Resident res = resItr.next();
			if(town.getResidents().size() >= plugin.getNoNationDebuffNum()) {
				if (!town.hasNation()) {
					plugin.addDebuff(Bukkit.getPlayer(res.getName()));
					plugin.getLogger().info("Debuff added on resident leave");
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerRespawn (PlayerRespawnEvent e) {
		Town town;
		Resident res;
		Player p = e.getPlayer();
		try {
			res = TownyUniverse.getInstance().getDataSource().getResident(p.getName());
			if (res.hasTown()) {
				town = res.getTown();
				plugin.addEFeffect(town, e.getPlayer());
				
				if(town.getResidents().size() >= plugin.getNoNationDebuffNum()) {
					if (!town.hasNation()) {
						plugin.addDebuff(Bukkit.getPlayer(res.getName()));
					}
				}
			}
		} catch (NotRegisteredException e1) {
			p.sendMessage("Error!");
		}
	}
	
	/*@EventHandler
	public void onWarStart (EventWarStartEvent e) {
		
		String war_declared = plugin.getConfig().getString("events.war_declared");
		
		war_declared = war_declared.replace("&", "\u00a7");
		
		Nation nation1, nation2;
		List<Nation> nation_list = e.getWarringNations();
		ListIterator<Nation> nationItr1 = nation_list.listIterator();
		ListIterator<Nation> nationItr2 = nation_list.listIterator();
		int townblock_num = 0;
		
		while (nationItr1.hasNext()) {
			nation1 = nationItr1.next();
			
			while (nationItr2.hasNext()) {
				nation2 = nationItr2.next();
				
				if (nation1.hasEnemy(nation2) && nation2.hasEnemy(nation1) && (nation1!=nation2) && (plugin.hasDeclaredWar(nation1) || plugin.hasDeclaredWar(nation2))) {
					
					war_declared = plugin.getConfig().getString("events.war_declared");
					war_declared = war_declared.replace("&", "\u00a7");
					
					String n1 = nation1.getName();
					String n2 = nation2.getName();
					File wars =  new File(plugin.getDataFolder() + "/wars.yml");
					FileConfiguration customConfig = YamlConfiguration.loadConfiguration(wars);
					customConfig.createSection("wars.war_" + plugin.getAttacker(n1, n2) +".attacker" );
					customConfig.createSection("wars.war_" + plugin.getAttacker(n1, n2) +".defender" );
					customConfig.createSection("wars.war_" + plugin.getAttacker(n1, n2) + "." + nation1.getName() + ".name" );
					customConfig.createSection("wars.war_" + plugin.getAttacker(n1, n2) + "." + nation1.getName() + ".chunks_on_start" );
					customConfig.createSection("wars.war_" + plugin.getAttacker(n1, n2) + "." + nation1.getName() + ".chunks_now" );
					customConfig.createSection("wars.war_" + plugin.getAttacker(n1, n2) + "." + nation1.getName() + ".score_on_start" );
					customConfig.createSection("wars.war_" + plugin.getAttacker(n1, n2) + "." + nation1.getName() + ".score_now" );
					customConfig.createSection("wars.war_" + plugin.getAttacker(n1, n2) + "." + nation2.getName() + ".name" );
					customConfig.createSection("wars.war_" + plugin.getAttacker(n1, n2) + "." + nation2.getName() + ".chunks_on_start" );
					customConfig.createSection("wars.war_" + plugin.getAttacker(n1, n2) + "." + nation2.getName() + ".chunks_now" );
					customConfig.createSection("wars.war_" + plugin.getAttacker(n1, n2) + "." + nation2.getName() + ".score_on_start" );
					customConfig.createSection("wars.war_" + plugin.getAttacker(n1, n2) + "." + nation2.getName() + ".score_now" );
					try {
						customConfig.createSection("wars.war_" + plugin.getAttacker(n1, n2) + ".casus_beli" );
						customConfig.createSection("wars.war_" + plugin.getAttacker(n1, n2) + ".winner" );
						customConfig.createSection("wars.war_" + plugin.getAttacker(n1, n2) + ".loser" );
						customConfig.set("wars.war_" + plugin.getAttacker(n1, n2) + ".casus_beli", plugin.getCasusBeli(TownyUniverse.getInstance().getDataSource().getNation(plugin.getAttacker(n1, n2))));
						customConfig.set("wars.war_" + plugin.getAttacker(n1, n2) + ".winner", "none");
						customConfig.set("wars.war_" + plugin.getAttacker(n1, n2) + ".loser", "none");
					} catch (NotRegisteredException e2) {
						e2.printStackTrace();
					}
					
					int n1_score =  (int) (plugin.getBaseScore() * (nation1.getNumResidents()/plugin.getPlayerScoreCoef()));
					int n2_score =  (int) (plugin.getBaseScore() * (nation2.getNumResidents()/plugin.getPlayerScoreCoef()));

					customConfig.set("wars.war_" + plugin.getAttacker(n1, n2) +".attacker", plugin.getAttacker(n1, n2));
					customConfig.set("wars.war_" + plugin.getAttacker(n1, n2) +".defender", plugin.getDefender(n1, n2));
					customConfig.set("wars.war_" + plugin.getAttacker(n1, n2) + "." + nation1.getName() + ".name", nation1.getName());
					customConfig.set("wars.war_" + plugin.getAttacker(n1, n2) + "." + nation1.getName() + ".chunks_on_start", nation1.getNumTownblocks());
					customConfig.set("wars.war_" + plugin.getAttacker(n1, n2) + "." + nation1.getName() + ".chunks_now", nation1.getNumTownblocks());
					customConfig.set("wars.war_" + plugin.getAttacker(n1, n2) + "." + nation1.getName() + ".score_on_start", n1_score);
					customConfig.set("wars.war_" + plugin.getAttacker(n1, n2) + "." + nation1.getName() + ".score_now", n1_score);
					customConfig.set("wars.war_" + plugin.getAttacker(n1, n2) + "." + nation2.getName() + ".name", nation2.getName());
					customConfig.set("wars.war_" + plugin.getAttacker(n1, n2) + "." + nation2.getName() + ".chunks_on_start", nation2.getNumTownblocks());
					customConfig.set("wars.war_" + plugin.getAttacker(n1, n2) + "." + nation2.getName() + ".chunks_now", nation2.getNumTownblocks());
					customConfig.set("wars.war_" + plugin.getAttacker(n1, n2) + "." + nation2.getName() + ".score_on_start", n2_score);
					customConfig.set("wars.war_" + plugin.getAttacker(n1, n2) + "." + nation2.getName() + ".score_now", n2_score);

					List<String> towns = new ArrayList<String>();
					for (Town town_itr : nation1.getTowns()) {
						towns.add(town_itr.getName());
					}
					for (Town town_itr : nation2.getTowns()) {
						towns.add(town_itr.getName());
					}
					ListIterator<String> townItr = towns.listIterator();

					while (townItr.hasNext()) {
						String town = townItr.next();
						
						List<TownBlock> townblocks;
						try {
							townblocks = TownyUniverse.getInstance().getDataSource().getTown(town).getTownBlocks();
						
							ListIterator<TownBlock> townblockItr = townblocks.listIterator();
	
							while (townblockItr.hasNext()) {
								TownBlock townblock = townblockItr.next();
	
								townblock_num++;
								
								customConfig.createSection("wars.war_" + plugin.getAttacker(n1, n2) + ".chunks." + "chunk_" + String.valueOf(townblock_num) + ".x");
								customConfig.createSection("wars.war_" + plugin.getAttacker(n1, n2) + ".chunks." + "chunk_" + String.valueOf(townblock_num) + ".z");
								customConfig.createSection("wars.war_" + plugin.getAttacker(n1, n2) + ".chunks." + "chunk_" + String.valueOf(townblock_num) + ".owner");
								customConfig.set("wars.war_" + plugin.getAttacker(n1, n2) + ".chunks." + "chunk_" + String.valueOf(townblock_num) + ".x", townblock.getX());
								customConfig.set("wars.war_" + plugin.getAttacker(n1, n2) + ".chunks." + "chunk_" + String.valueOf(townblock_num) + ".z", townblock.getZ());
								customConfig.set("wars.war_" + plugin.getAttacker(n1, n2) + ".chunks." + "chunk_" + String.valueOf(townblock_num) + ".owner", town);
								
							}
						} catch (NotRegisteredException e1) {
							e1.printStackTrace();
						}
					}
						
					try {
						customConfig.save(wars);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					war_declared = war_declared.replace("$attacker", customConfig.getString("wars.war_" + plugin.getAttacker(n1, n2) +".attacker"));
					war_declared = war_declared.replace("$defender", customConfig.getString("wars.war_" + plugin.getAttacker(n1, n2) +".defender"));
					
					try {
						switch(plugin.getCasusBeli(TownyUniverse.getInstance().getDataSource().getNation(plugin.getAttacker(n1, n2)))) {
							case 1:
								war_declared = war_declared.replace("$casus_beli", plugin.getConfig().getString("casus_beli.destroy_alliance"));
								break;
							case 2:
								war_declared = war_declared.replace("$casus_beli", plugin.getConfig().getString("casus_beli.conquer"));
								break;
						}
					} catch (NotRegisteredException e1) {
						e1.printStackTrace();
					}
					
					for(Player p : Bukkit.getOnlinePlayers()) {
						p.sendMessage(war_declared);
					}
				}
			}

			nationItr2 = nationItr1;
		}

		plugin.clearDWconfig();
	}*/

	//@EventHandler
	
	
	/*@EventHandler
	public void onCellWonEvent(PlotAttackedEvent event) {

		//if (event.isCancelled())
		//	return;
		
		//File wars =  new File(plugin.getDataFolder() + "/wars.yml");
		//FileConfiguration warsConfig = YamlConfiguration.loadConfiguration(wars);
		
		String pre_owner;
		
		
		String war_chunk_percent_defender = plugin.getConfig().getString("events.war_chunk_percent_defender");
		String war_truce_no_chunks = plugin.getConfig().getString("events.war_truce_no_chunks");
		
		war_chunk_percent_defender = war_chunk_percent_defender.replace("&", "\u00a7");
		war_truce_no_chunks = war_truce_no_chunks.replace("&", "\u00a7");
		
		if (event.getHP() == 0) {
			plugin.getLogger().info("Chunk catched!");
		
			//Player p = Bukkit.getPlayer(e);
			//for(Player player : Bukkit.getOnlinePlayers()) {
			//	player.sendMessage("It's works!");
			//}
			
			try {
				
				File wars =  new File(plugin.getDataFolder() + "/wars.yml");
				FileConfiguration warsConfig = YamlConfiguration.loadConfiguration(wars);
				
				int chunks_temp = 0;

				float townblockPercent = 0;
				boolean warEnded = false;
				String Loser = "";
				String Winner = "";
				
				for (String war : warsConfig.getConfigurationSection("wars").getKeys(false)) {
						for(String chunk : warsConfig.getConfigurationSection("wars." + war + ".chunks").getKeys(true)) {
							for (TownBlock townblock : TownyUniverse.getInstance().getDataSource().getAllTownBlocks()) {
								if ((townblock.getX() == warsConfig.getInt("wars." + war + ".chunks." + chunk + ".x"))&&(townblock.getZ() == warsConfig.getInt("wars." + war + ".chunks." + chunk + ".z"))) {
									if (townblock.getTown().getName().equals(warsConfig.getString("wars." + war + ".chunks." + chunk + ".owner"))) {
										warsConfig.set("wars." + war + "." + townblock.getTown().getNation().getName() + ".chunks_now", 0);
									}
								}
							}
						}
						for(String chunk : warsConfig.getConfigurationSection("wars." + war + ".chunks").getKeys(true)) {
							for (TownBlock townblock : TownyUniverse.getInstance().getDataSource().getAllTownBlocks()) {
								if ((townblock.getX() == warsConfig.getInt("wars." + war + ".chunks." + chunk + ".x"))&&(townblock.getZ() == warsConfig.getInt("wars." + war + ".chunks." + chunk + ".z"))) {
									if (townblock.getTown().getName().equals(warsConfig.getString("wars." + war + ".chunks." + chunk + ".owner"))) {
										chunks_temp = warsConfig.getInt("wars." + war + "." + townblock.getTown().getNation().getName() + ".chunks_now") + 1;
										warsConfig.set("wars." + war + "." + townblock.getTown().getNation().getName() + ".chunks_now", chunks_temp);
										chunks_temp = 0;
										//plugin.getLogger().info("Chunk catched!");
									}
								}
							}
						}
						for(String chunk : warsConfig.getConfigurationSection("wars." + war + ".chunks").getKeys(true)) {
							for (TownBlock townblock : TownyUniverse.getInstance().getDataSource().getAllTownBlocks()) {
								if ((townblock.getX() == warsConfig.getInt("wars." + war + ".chunks." + chunk + ".x"))&&(townblock.getZ() == warsConfig.getInt("wars." + war + ".chunks." + chunk + ".z"))) {
									if (townblock.getTown().getName().equals(warsConfig.getString("wars." + war + ".chunks." + chunk + ".owner"))) {
										townblockPercent = ( (float) warsConfig.getInt("wars." + war + "." + townblock.getTown().getNation().getName() + ".chunks_now")) / ( (float) warsConfig.getInt("wars." + war + "." + townblock.getTown().getNation().getName() + ".chunks_on_start"));
										if (townblockPercent < (((float) plugin.getConfig().getInt("costs.auto_lose_chunk_percent")) / (float) 100)) {
											warEnded = true;
											Loser = warsConfig.getString("wars." + war + "." + townblock.getTown().getNation().getName() + ".name");
											//warsConfig.set("wars." + war + ".winner", townblockPercent);
										}
										
										//townblockPercent = townblockPercent * 100;
										
										
										townblockPercent = 0;
									}
								}
							}
						}
						if (warsConfig.getString("wars." + war + ".attacker").equals(event.getTownBlock().getTown().getNation().getName()) && !warEnded) {

							Nation attacker = TownyUniverse.getInstance().getDataSource().getNation(warsConfig.getString("wars." + war + ".attacker"));
							Nation defender = TownyUniverse.getInstance().getDataSource().getNation(warsConfig.getString("wars." + war + ".defender"));
							
							pre_owner = warsConfig.getString("wars." + war + ".defender");
							
							townblockPercent = ( (float) warsConfig.getInt("wars." + war + "." + pre_owner + ".chunks_now")) / ( (float) warsConfig.getInt("wars." + war + "." + pre_owner + ".chunks_on_start"));

							townblockPercent = townblockPercent * 100;
							
							war_chunk_percent_defender = war_chunk_percent_defender.replace("$chunk_percent", String.valueOf((int) townblockPercent));
							war_chunk_percent_defender = war_chunk_percent_defender.replace("$defender", pre_owner);
										
							for(Player player : Bukkit.getOnlinePlayers()) {
								Resident res;
								try {
									res = TownyUniverse.getInstance().getDataSource().getResident(player.getName());
									if (res.hasNation()) {
										if (res.getTown().getNation().equals(attacker) || res.getTown().getNation().equals(defender)) {		
											player.sendMessage(war_chunk_percent_defender);
										}
									}
								} catch (NotRegisteredException e1) {
									e1.printStackTrace();
								}
							}
							townblockPercent = 0;
							//war_chunk_percent_defender = plugin.getConfig().getString("events.war_chunk_percent_defender");
							//war_chunk_percent_defender = war_chunk_percent_defender.replace("&", "\u00a7");
						}
						else if (warsConfig.getString("wars." + war + ".defender").equals(event.getTownBlock().getTown().getNation().getName()) && !warEnded) {
							
							Nation attacker = TownyUniverse.getInstance().getDataSource().getNation(warsConfig.getString("wars." + war + ".attacker"));
							Nation defender = TownyUniverse.getInstance().getDataSource().getNation(warsConfig.getString("wars." + war + ".defender"));
							
							pre_owner = warsConfig.getString("wars." + war + ".attacker");
							
							townblockPercent = ( (float) warsConfig.getInt("wars." + war + "." + pre_owner + ".chunks_now")) / ( (float) warsConfig.getInt("wars." + war + "." + pre_owner + ".chunks_on_start"));

							townblockPercent = townblockPercent * 100;
							
							war_chunk_percent_defender = war_chunk_percent_defender.replace("$chunk_percent", String.valueOf((int) townblockPercent));
							war_chunk_percent_defender = war_chunk_percent_defender.replace("$defender", pre_owner);
										
							for(Player player : Bukkit.getOnlinePlayers()) {
								Resident res;
								try {
									res = TownyUniverse.getInstance().getDataSource().getResident(player.getName());
									if (res.hasNation()) {
										if (res.getTown().getNation().equals(attacker) || res.getTown().getNation().equals(defender)) {		
											player.sendMessage(war_chunk_percent_defender);
										}
									}
								} catch (NotRegisteredException e1) {
									e1.printStackTrace();
								}
							}
							townblockPercent = 0;
							//war_chunk_percent_defender = plugin.getConfig().getString("events.war_chunk_percent_defender");
							//war_chunk_percent_defender = war_chunk_percent_defender.replace("&", "\u00a7");
						}
						if (warEnded) {
							if (warsConfig.getString("wars." + war + ".attacker").equals(Loser)) {
								Winner = warsConfig.getString("wars." + war + ".defender");
							}
							else if (warsConfig.getString("wars." + war + ".defender").equals(Loser)) {
								Winner = warsConfig.getString("wars." + war + ".attacker");
							}
							warsConfig.set("wars." + war + ".winner", Winner);
							warsConfig.set("wars." + war + ".loser", Loser);
							
							Nation LoserNation = TownyUniverse.getInstance().getDataSource().getNation(Loser);
							Nation WinnerNation = TownyUniverse.getInstance().getDataSource().getNation(Winner);
							
							LoserNation.removeEnemy(WinnerNation);
							WinnerNation.removeEnemy(LoserNation);
							
							TownyUniverse.getInstance().getDataSource().saveNation(LoserNation);
							TownyUniverse.getInstance().getDataSource().saveNation(WinnerNation);
							
							war_truce_no_chunks = war_truce_no_chunks.replace("$loser", Loser);
							war_truce_no_chunks = war_truce_no_chunks.replace("$winner", Winner);
							
							for(Player pl : Bukkit.getOnlinePlayers()) {
								pl.sendMessage(war_truce_no_chunks);
							}
							//war_truce_no_chunks = plugin.getConfig().getString("events.war_truce_no_chunks");
							//war_truce_no_chunks = war_truce_no_chunks.replace("&", "\u00a7");
						}
				}

				//warsConfig.set("wars", null);
				
				warsConfig.save(wars);
			} catch (NotRegisteredException e) {
				e.printStackTrace();
			} catch (IOException e3) {
				e3.printStackTrace();
			}
		}
	
	}*/

	/*@EventHandler
	public void onPlayerDeath(EntityDeathEvent event) {
		
		
		
		String war_score_defender = plugin.getConfig().getString("events.war_score_defender");
		String war_truce_no_score = plugin.getConfig().getString("events.war_truce_no_score");
		
		war_score_defender = war_score_defender.replace("&", "\u00a7");
		war_truce_no_score = war_truce_no_score.replace("&", "\u00a7");
		
		Entity defenderEntity = event.getEntity();
		TownyUniverse townyUniverse = TownyUniverse.getInstance();
		if (!TownyAPI.getInstance().isTownyWorld(event.getEntity().getWorld()))
			return;
		
		try {
			if (defenderEntity instanceof Player) {
				Player defenderPlayer = (Player) defenderEntity;
				Resident defenderResident;
				
					defenderResident = townyUniverse.getDataSource().getResident(defenderPlayer.getName());
				
				// Killed by another entity?			
				if (defenderEntity.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
	
					EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) defenderEntity.getLastDamageCause();
	
					Entity attackerEntity = damageEvent.getDamager();
					Player attackerPlayer = null;
					Resident attackerResident = null;
	
					if (attackerEntity instanceof Projectile) { // Killed by projectile, try to narrow the true source of the kill.
						Projectile projectile = (Projectile) attackerEntity;
						if (projectile.getShooter() instanceof Player) { // Player shot a projectile.
							attackerPlayer = (Player) projectile.getShooter();
							attackerResident = townyUniverse.getDataSource().getResident(attackerPlayer.getName());
						} else { // Something else shot a projectile.
							try {
								attackerEntity = (Entity) projectile.getShooter(); // Mob shot a projectile.
							} catch (Exception e2) { // This would be a dispenser kill, should count as environmental death.
							}
						}
	
					} else if (attackerEntity instanceof Player) {
						// This was a player kill
						attackerPlayer = (Player) attackerEntity;
						try {
							attackerResident = townyUniverse.getDataSource().getResident(attackerPlayer.getName());
						} catch (NotRegisteredException e) {
						}
					}
					
					if (attackerPlayer != null) {
						if (TownyAPI.getInstance().isWarTime()) {
	
							File wars =  new File(plugin.getDataFolder() + "/wars.yml");
							FileConfiguration warsConfig = YamlConfiguration.loadConfiguration(wars);
							
							int score_temp = 0;
							
							Nation defenderNation = defenderResident.getTown().getNation();
							Nation attackerNation = attackerResident.getTown().getNation();
							
							for (String war : warsConfig.getConfigurationSection("wars").getKeys(false)) {
								if (warsConfig.getString("wars." + war + ".winner").equals("none")) {
									if (warsConfig.contains("wars." + war + "." + attackerNation.getName()) && warsConfig.contains("wars." + war + "." + defenderNation.getName())) {
										score_temp = warsConfig.getInt("wars." + war + "." + defenderNation.getName() + ".score_now") - 1;
										warsConfig.set("wars." + war + "." + defenderNation.getName() + ".score_now", score_temp);
										if (score_temp == 0) {
											warsConfig.set("wars." + war + ".winner", attackerNation.getName());
											warsConfig.set("wars." + war + ".loser", attackerNation.getName());
											defenderNation.removeEnemy(attackerNation);
											attackerNation.removeEnemy(defenderNation);
											
											TownyUniverse.getInstance().getDataSource().saveNation(defenderNation);
											TownyUniverse.getInstance().getDataSource().saveNation(attackerNation);
											
											war_truce_no_score = war_truce_no_score.replace("$loser", defenderNation.getName());
											war_truce_no_score = war_truce_no_score.replace("$winner", attackerNation.getName());
											
											for(Player player : Bukkit.getOnlinePlayers()) {
												player.sendMessage(war_truce_no_score);
											}
										}
										else {
											war_score_defender = war_score_defender.replace("$score", String.valueOf(warsConfig.getInt("wars." + war + "." + defenderNation.getName() + ".score_now")));
											war_score_defender = war_score_defender.replace("$defender", defenderNation.getName());
											
											for(Player player : Bukkit.getOnlinePlayers()) {
												Resident res;
												try {
													res = TownyUniverse.getInstance().getDataSource().getResident(player.getName());
													if (res.hasNation()) {
														if (res.getTown().getNation().equals(attackerNation) || res.getTown().getNation().equals(defenderNation)) {		
															player.sendMessage(war_score_defender);
														}
													}
												} catch (NotRegisteredException e1) {
													e1.printStackTrace();
												}
											}
										}
										score_temp = 0;
									}
								}
							}
							warsConfig.save(wars);
						}
					}
				}
			}
		} catch (NotRegisteredException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} //catch (TownyException e3) {
		//	e3.printStackTrace();
		//}
		
	}*/
	
}
