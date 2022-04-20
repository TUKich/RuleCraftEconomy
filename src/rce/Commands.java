package rce;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import javax.naming.InvalidNameException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.command.NationCommand;
import com.palmergames.bukkit.towny.command.TownCommand;
import com.palmergames.bukkit.towny.event.PreNewTownEvent;
import com.palmergames.bukkit.towny.event.TownPreClaimEvent;
import com.palmergames.bukkit.towny.exceptions.AlreadyRegisteredException;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Coord;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownBlockOwner;
import com.palmergames.bukkit.towny.object.TownyWorld;
import com.palmergames.bukkit.towny.object.WorldCoord;
import com.palmergames.bukkit.towny.permissions.PermissionNodes;
import com.palmergames.bukkit.towny.tasks.TownClaim;
import com.palmergames.bukkit.towny.utils.AreaSelectionUtil;
import com.palmergames.bukkit.towny.utils.OutpostUtil;
import com.palmergames.bukkit.towny.utils.ResidentUtil;
import com.palmergames.bukkit.util.BukkitTools;
import com.palmergames.bukkit.util.NameValidation;
import com.palmergames.util.StringMgmt;


public class Commands implements CommandExecutor {

	private static Towny towny_plugin;
	
	private static Economy plugin;
	public Commands (Economy plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		String updated_tnp = plugin.getConfig().getString("commands.updated_tnp"); 
		String updated_rm = plugin.getConfig().getString("commands.updated_rm");
		//String invalid_arguments = plugin.getConfig().getString("commands.invalid_arguments");
		String updated_ef = plugin.getConfig().getString("commands.updated_ef");
		String ef_have_cooldown = plugin.getConfig().getString("commands.ef_have_cooldown");
		String this_ef_is_now = plugin.getConfig().getString("commands.this_ef_is_now");
		String setting_of_ef_is_limited = plugin.getConfig().getString("commands.setting_of_ef_is_limited");
		String town_dont_have_rm = plugin.getConfig().getString("commands.town_dont_have_rm");
		String war_declared = plugin.getConfig().getString("commands.war_declared");
		String nbal = plugin.getConfig().getString("commands.nbal");
		
		String dont_have_permission = plugin.getConfig().getString("commands_errors.dont_have_permission");
		String this_town_is_not_exists = plugin.getConfig().getString("commands_errors.this_town_is_not_exists");
		String player_is_not_in_the_town = plugin.getConfig().getString("commands_errors.player_is_not_in_the_town");
		String arg_must_be_int = plugin.getConfig().getString("commands_errors.arg_must_be_int");
		String you_have_already_declared_war = plugin.getConfig().getString("commands_errors.you_have_already_declared_war");
		String this_nation_is_not_exists = plugin.getConfig().getString("commands_errors.this_nation_is_not_exists");
		String you_cant_declare_war_to_you = plugin.getConfig().getString("commands_errors.you_cant_declare_war_to_you");
		String player_is_not_in_the_nation = plugin.getConfig().getString("commands_errors.player_is_not_in_the_nation");
		String your_nation_is_not_in_war = plugin.getConfig().getString("commands_errors.your_nation_is_not_in_war");
		String this_nation_is_not_in_war = plugin.getConfig().getString("commands_errors.this_nation_is_not_in_war");
		String this_nations_is_not_in_war = plugin.getConfig().getString("commands_errors.this_nations_is_not_in_war");
		String one_of_this_nations_is_not_exists = plugin.getConfig().getString("commands_errors.one_of_this_nations_is_not_exists");
		String now_nowar_time = plugin.getConfig().getString("commands_errors.now_nowar_time");
		String toutpost_dont_have_recourñes = plugin.getConfig().getString("commands_errors.toutpost_dont_have_recourñes");
		String nsettax_greater_max_tax = plugin.getConfig().getString("commands_errors.nsettax_greater_max_tax");
		String value_less_zero = plugin.getConfig().getString("commands_errors.value_less_zero");
		
		String tbal_invalid_arguments = plugin.getConfig().getString("commands_errors.tbal_invalid_arguments");
		String trmadd_invalid_arguments = plugin.getConfig().getString("commands_errors.trmadd_invalid_arguments");
		String tnpadd_invalid_arguments = plugin.getConfig().getString("commands_errors.tnpadd_invalid_arguments");
		String tsetef_invalid_arguments = plugin.getConfig().getString("commands_errors.tsetef_invalid_arguments");
		String tef_invalid_arguments = plugin.getConfig().getString("commands_errors.tef_invalid_arguments");
		String nrmadd_invalid_arguments  = plugin.getConfig().getString("commands_errors.tef_invalid_arguments");
		String declare_war_invalid_arguments  = plugin.getConfig().getString("commands_errors.declare_war_invalid_arguments");
		String warinfo_invalid_arguments  = plugin.getConfig().getString("commands_errors.warinfo_invalid_arguments");
		String nsettax_invalid_arguments  = plugin.getConfig().getString("commands_errors.nsettax_invalid_arguments");

		updated_tnp = updated_tnp.replace("&", "\u00a7");
		updated_rm = updated_rm.replace("&", "\u00a7");
		//invalid_arguments = invalid_arguments.replace("&", "\u00a7");
		updated_ef = updated_ef.replace("&", "\u00a7");
		ef_have_cooldown = ef_have_cooldown.replace("&", "\u00a7");
		this_ef_is_now = this_ef_is_now.replace("&", "\u00a7");
		setting_of_ef_is_limited = setting_of_ef_is_limited.replace("&", "\u00a7");
		town_dont_have_rm = town_dont_have_rm.replace("&", "\u00a7");
		war_declared = war_declared.replace("&", "\u00a7");
		nbal = nbal.replace("&", "\u00a7");

		dont_have_permission = dont_have_permission.replace("&", "\u00a7");
		this_town_is_not_exists = this_town_is_not_exists.replace("&", "\u00a7");
		player_is_not_in_the_town = player_is_not_in_the_town.replace("&", "\u00a7");
		arg_must_be_int = arg_must_be_int.replace("&", "\u00a7");
		you_have_already_declared_war = you_have_already_declared_war.replace("&", "\u00a7");
		this_nation_is_not_exists = this_nation_is_not_exists.replace("&", "\u00a7");
		you_cant_declare_war_to_you = you_cant_declare_war_to_you.replace("&", "\u00a7");
		player_is_not_in_the_nation = player_is_not_in_the_nation.replace("&", "\u00a7");
		your_nation_is_not_in_war = your_nation_is_not_in_war.replace("&", "\u00a7");
		this_nation_is_not_in_war = this_nation_is_not_in_war.replace("&", "\u00a7");
		this_nations_is_not_in_war = this_nations_is_not_in_war.replace("&", "\u00a7");
		one_of_this_nations_is_not_exists = one_of_this_nations_is_not_exists.replace("&", "\u00a7");
		now_nowar_time = now_nowar_time.replace("&", "\u00a7");
		toutpost_dont_have_recourñes = toutpost_dont_have_recourñes.replace("&", "\u00a7");
		nsettax_greater_max_tax = nsettax_greater_max_tax.replace("&", "\u00a7");
		value_less_zero = value_less_zero.replace("&", "\u00a7");
		
		tbal_invalid_arguments = tbal_invalid_arguments.replace("&", "\u00a7");
		trmadd_invalid_arguments = trmadd_invalid_arguments.replace("&", "\u00a7");
		tnpadd_invalid_arguments = tnpadd_invalid_arguments.replace("&", "\u00a7");
		tsetef_invalid_arguments = tsetef_invalid_arguments.replace("&", "\u00a7");
		tef_invalid_arguments = tef_invalid_arguments.replace("&", "\u00a7");
		nrmadd_invalid_arguments = nrmadd_invalid_arguments.replace("&", "\u00a7");
		declare_war_invalid_arguments = declare_war_invalid_arguments.replace("&", "\u00a7");
		warinfo_invalid_arguments = warinfo_invalid_arguments.replace("&", "\u00a7");
		nsettax_invalid_arguments = nsettax_invalid_arguments.replace("&", "\u00a7");
		
		Player p = (Player) sender;
		Resident res;

		File towns =  new File(plugin.getDataFolder() + "/towns.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(towns);

		File nations =  new File(plugin.getDataFolder() + "/nations.yml");
		FileConfiguration nationsConfig = YamlConfiguration.loadConfiguration(nations);
				
		switch (label) {
			
			case ("nsettax"):
	
				if (p.hasPermission("rce.nsettax")) {
					if (args.length == 1) {
						try {
							res = TownyUniverse.getInstance().getDataSource().getResident(p.getName());
							if (res.hasNation()) {
								int n_max_tax = (int) ((res.getTown().getNation().getNumTownblocks() * plugin.getRM_Ncost())/4);
								if (Integer.parseInt(args[0].trim())>=0) {	
									if (Integer.parseInt(args[0].trim()) <= n_max_tax) {
										nationsConfig.set("nations." + res.getTown().getNation().getName() + ".tax", Integer.parseInt(args[0].trim()));

										nationsConfig.save(nations);
									}
									else {
										nsettax_greater_max_tax = nsettax_greater_max_tax.replace("$max_tax", String.valueOf(n_max_tax));
										sender.sendMessage(nsettax_greater_max_tax);
									}
								}
								else sender.sendMessage(value_less_zero);
							}
							else p.sendMessage(player_is_not_in_the_nation);
						} catch (NotRegisteredException e) {
							p.sendMessage("Error with resident or town or nation!");
							plugin.getLogger().info("Error with resident or town or nation!");
						} catch (IOException e) {
							e.printStackTrace();
						}
						catch(NumberFormatException e) {
							p.sendMessage(arg_must_be_int);
						}
					}
					else p.sendMessage(nsettax_invalid_arguments);
				}
				else sender.sendMessage(dont_have_permission);
				break;
				
			case ("put_towns"):
				if (p.hasPermission("rce.put_towns")) {
					try {
						for (Town town : TownyUniverse.getInstance().getDataSource().getTowns()) {
							if (!customConfig.contains("towns." + town.getName())) {
								customConfig.createSection("towns." + town.getName() + ".tnp");
								customConfig.createSection("towns." + town.getName() + ".rm");
								customConfig.createSection("towns." + town.getName() + ".ef");
								customConfig.createSection("towns." + town.getName() + ".ef_cd");
								customConfig.set("towns." + town.getName() + ".tnp", 0);
								customConfig.set("towns." + town.getName() + ".rm", 0);
								customConfig.set("towns." + town.getName() + ".ef", 3);
								customConfig.set("towns." + town.getName() + ".ef_cd", false);
							}
						}
						customConfig.save(towns);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				else sender.sendMessage(dont_have_permission);
				break;
				
			case ("put_nations"):
				if (p.hasPermission("rce.put_nations")) {
					try {
						for (Nation nation : TownyUniverse.getInstance().getDataSource().getNations()) {
							if (!nationsConfig.contains("nations." + nation.getName())) {
								nationsConfig.createSection("nations." + nation.getName() + ".rm");
								nationsConfig.createSection("nations." + nation.getName() + ".tax");
								nationsConfig.set("nations." + nation.getName() + ".rm", 0);
								nationsConfig.set("nations." + nation.getName() + ".tax", 0);
							}
						}
						nationsConfig.save(nations);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				else sender.sendMessage(dont_have_permission);
				break;
				
			case ("toggle_war"):

				if (p.hasPermission("rce.toggle_war")) {
					if (args.length == 0) {
						boolean choice = TownyAPI.getInstance().isWarTime();
						
						if (!choice) {
							TownyUniverse.getInstance().startWarEvent();
							TownyMessaging.sendMsg(sender, TownySettings.getLangString("msg_war_started"));
						} else {
							//WarEndsEvent();
							TownyUniverse.getInstance().endWarEvent();
							TownyMessaging.sendMsg(sender, TownySettings.getLangString("msg_war_ended"));
						}
					}
					else if (args.length >= 2) {
						if (customConfig.contains("towns." + args[0])) {
							try {
								plugin.addTNP(args[0], Integer.parseInt(args[1].trim()));
	
								updated_tnp = updated_tnp.replace("$town", args[0]);
								updated_tnp = updated_tnp.replace("$tnp", String.valueOf(plugin.getTNP(args[0])));
									
								p.sendMessage(updated_tnp);
							}
							catch (NumberFormatException e) {
								p.sendMessage(arg_must_be_int);
							}
						}
						else {
							this_town_is_not_exists = this_town_is_not_exists.replace("$town", args[0]);
							p.sendMessage(this_town_is_not_exists);
						}
					}
					else p.sendMessage(tnpadd_invalid_arguments);
				}
				else sender.sendMessage(dont_have_permission);
				break;
				
			case ("tbal"):
				if (p.hasPermission("rce.tbal")) {
					if (args.length >= 1) {
						if (customConfig.contains("towns." + args[0])) {
							TownBalanceInfo (p, args[0]);
						}
						else {
							this_town_is_not_exists = this_town_is_not_exists.replace("$town", args[0]);
							p.sendMessage(this_town_is_not_exists);
						}
					}
					else if (args.length == 0) {
						try {
							res = TownyUniverse.getInstance().getDataSource().getResident(p.getName());
							if (res.hasTown()) {
								TownBalanceInfo (p, res.getTown().getName());
							}
							else p.sendMessage(player_is_not_in_the_town);
						} catch (NotRegisteredException e) {
							p.sendMessage("Error with resident or town!");
							plugin.getLogger().info("Error with resident or town!");
						}
					}
					else p.sendMessage(tbal_invalid_arguments);
				}
				else sender.sendMessage(dont_have_permission);
				break;
			
			case ("nbal"):
				if (p.hasPermission("rce.nbal")) {
					if (args.length >= 1) {
						if (nationsConfig.contains("nations." + args[0])) {
							try {
								int nrm_cost = (int) (TownyUniverse.getInstance().getDataSource().getNation(args[0]).getNumTownblocks() * plugin.getRM_Ncost());
							
								nbal = nbal.replace("$nation", args[0]);
								nbal = nbal.replace("$rm", String.valueOf(plugin.getRM_N(args[0])));
								nbal = nbal.replace("$cost", String.valueOf(nrm_cost));
								nbal = nbal.replace("$tax", String.valueOf(plugin.getTax_N(args[0])));
								p.sendMessage(nbal);
							} catch (NotRegisteredException e) {
								e.printStackTrace();
							}
						}
						else {
							this_nation_is_not_exists = this_nation_is_not_exists.replace("$nation", args[0]);
							p.sendMessage(this_nation_is_not_exists);
						}
					}
					else if (args.length == 0) {
						try {
							res = TownyUniverse.getInstance().getDataSource().getResident(p.getName());
							if (res.hasNation()) {
								int nrm_cost = (int) (res.getTown().getNation().getNumTownblocks() * plugin.getRM_Ncost());
								
								nbal = nbal.replace("$nation", res.getTown().getNation().getName());
								nbal = nbal.replace("$rm", String.valueOf(plugin.getRM_N(res.getTown().getNation().getName())));
								nbal = nbal.replace("$cost", String.valueOf(nrm_cost));
								nbal = nbal.replace("$tax", String.valueOf(plugin.getTax_N(res.getTown().getNation().getName())));
								p.sendMessage(nbal);
							}
							else p.sendMessage(player_is_not_in_the_nation);
						} catch (NotRegisteredException e) {
							p.sendMessage("Error with resident or town!");
							plugin.getLogger().info("Error with resident or town!");
						}
					}
					else p.sendMessage(tbal_invalid_arguments);
				}
				else sender.sendMessage(dont_have_permission);
				break;
						
			case ("trmadd"):
				
				if (p.hasPermission("rce.trmadd")) {
					if (args.length == 1) {
						try {
							res = TownyUniverse.getInstance().getDataSource().getResident(p.getName());
							if (res.hasTown()) {
								try {
									plugin.addRM(res.getTown().getName(), Integer.parseInt(args[0].trim()));
	
									updated_rm = updated_rm.replace("$town", res.getTown().getName());
									updated_rm = updated_rm.replace("$rm", String.valueOf(plugin.getRM(res.getTown().getName())));
										
									p.sendMessage(updated_rm);
								}
								catch (NumberFormatException e) {
									p.sendMessage(arg_must_be_int);
								}
								
							}
							else p.sendMessage(player_is_not_in_the_town);
						} catch (NotRegisteredException e) {
							p.sendMessage("Error with resident or town!");
							plugin.getLogger().info("Error with resident or town!");
						}
					}
					else if (args.length >= 2) {
						if (customConfig.contains("towns." + args[0])) {
							try {
								plugin.addRM(args[0], Integer.parseInt(args[1].trim()));
	
								updated_rm = updated_rm.replace("$town", args[0]);
								updated_rm = updated_rm.replace("$rm", String.valueOf(plugin.getRM(args[0])));
									
								p.sendMessage(updated_rm);
							}
							catch(NumberFormatException e) {
								p.sendMessage(arg_must_be_int);
							}
						}	
						else {
							this_town_is_not_exists = this_town_is_not_exists.replace("$town", args[0]);
							p.sendMessage(this_town_is_not_exists);
						}
					}
					else p.sendMessage(trmadd_invalid_arguments);
				}
				else sender.sendMessage(dont_have_permission);
			
				break;
				
			case ("tnpadd"):

				if (p.hasPermission("rce.tnpadd")) {
					if (args.length == 1) {
						try {
							res = TownyUniverse.getInstance().getDataSource().getResident(p.getName());
							if (res.hasTown()) {
								try {
									plugin.addTNP(res.getTown().getName(), Integer.parseInt(args[0].trim()));
	
									updated_tnp = updated_tnp.replace("$town", res.getTown().getName());
									updated_tnp = updated_tnp.replace("$tnp", String.valueOf(plugin.getTNP(res.getTown().getName())));
										
									p.sendMessage(updated_tnp);
								}
								catch (NumberFormatException e) {
									p.sendMessage(arg_must_be_int);
								}
							}
							else p.sendMessage(player_is_not_in_the_town);
						} catch (NotRegisteredException e) {
							p.sendMessage("Error with resident or town!");
							plugin.getLogger().info("Error with resident or town!");
						}
					}
					else if (args.length >= 2) {
						if (customConfig.contains("towns." + args[0])) {
							try {
								plugin.addTNP(args[0], Integer.parseInt(args[1].trim()));
	
								updated_tnp = updated_tnp.replace("$town", args[0]);
								updated_tnp = updated_tnp.replace("$tnp", String.valueOf(plugin.getTNP(args[0])));
									
								p.sendMessage(updated_tnp);
							}
							catch (NumberFormatException e) {
								p.sendMessage(arg_must_be_int);
							}
						}
						else {
							this_town_is_not_exists = this_town_is_not_exists.replace("$town", args[0]);
							p.sendMessage(this_town_is_not_exists);
						}
					}
					else p.sendMessage(tnpadd_invalid_arguments);
				}
				else sender.sendMessage(dont_have_permission);
				break;
	
			case ("tsetef"):

				if (p.hasPermission("rce.tsetef")) {
					if (args.length == 1) {
						try {
							if (Integer.parseInt(args[0].trim())>0 && Integer.parseInt(args[0].trim())<6) {
								try {
									res = TownyUniverse.getInstance().getDataSource().getResident(p.getName());
									if (res.hasTown()) {
										try {
											Resident res_i;
											Town town = res.getTown();
											List<Resident> res_list = new ArrayList<>(ResidentUtil.getOnlineResidentsViewable((Player) sender, town));
											ListIterator<Resident> resItr = res_list.listIterator();
											while (resItr.hasNext()) {
												res_i = resItr.next();
												if (Bukkit.getPlayer(res_i.getName()).isOnline()) {
													plugin.removeEFeffect(town, Bukkit.getPlayer(res_i.getName()));
												}
											}
											
											plugin.setEF(res.getTown().getName(), Integer.parseInt(args[0].trim()));
											
											ListIterator<Resident> resItr1 = res_list.listIterator();
											while (resItr1.hasNext()) {
												res_i = resItr1.next();
												if (Bukkit.getPlayer(res_i.getName()).isOnline()) {
													plugin.addEFeffect(town, Bukkit.getPlayer(res_i.getName()));
												}
											}
											
										
											updated_ef = updated_ef.replace("$town", res.getTown().getName());
											updated_ef = updated_ef.replace("$ef", plugin.getConfig().getString("ef.ef_" + String.valueOf(plugin.getEF(res.getTown().getName()))));
											
											p.sendMessage(updated_ef);
										}
										catch (NumberFormatException e) {
											p.sendMessage(arg_must_be_int);
										}
										
									}
									else p.sendMessage(player_is_not_in_the_town);
								} catch (NotRegisteredException e) {
									p.sendMessage("Error with resident or town!");
									plugin.getLogger().info("Error with resident or town!");
								}
							}
							else p.sendMessage(tsetef_invalid_arguments);
						} catch (NumberFormatException e) {
							p.sendMessage(arg_must_be_int);
						}
					}
					else if (args.length >= 2) {
						if (customConfig.contains("towns." + args[0])) {
							if (Integer.parseInt(args[1].trim())>0 && Integer.parseInt(args[1].trim())<6) {
								try {
									Resident res_i;
									Town town = TownyUniverse.getInstance().getDataSource().getTown(args[0]);
									List<Resident> res_list = new ArrayList<>(ResidentUtil.getOnlineResidentsViewable((Player) sender, town));
									ListIterator<Resident> resItr = res_list.listIterator();
									while (resItr.hasNext()) {
										res_i = resItr.next();
										if (Bukkit.getPlayer(res_i.getName()).isOnline()) {
											plugin.removeEFeffect(town, Bukkit.getPlayer(res_i.getName()));
										}
									}
									
									plugin.setEF(args[0], Integer.parseInt(args[1].trim()));

									ListIterator<Resident> resItr1 = res_list.listIterator();
									while (resItr1.hasNext()) {
										res_i = resItr1.next();
										if (Bukkit.getPlayer(res_i.getName()).isOnline()) {
											plugin.addEFeffect(town, Bukkit.getPlayer(res_i.getName()));
										}
									}
		
									updated_ef = updated_ef.replace("$town", args[0]);
									updated_ef = updated_ef.replace("$ef", plugin.getConfig().getString("ef.ef_" + String.valueOf(plugin.getEF(args[0]))));
										
									p.sendMessage(updated_ef);
								} catch(NumberFormatException e) {
									p.sendMessage(arg_must_be_int);
								} catch (NotRegisteredException e) {
									p.sendMessage("Error with town!");
									plugin.getLogger().info("Error with town!");
								}
							}
							else p.sendMessage(tsetef_invalid_arguments);
						}	
						else {
							this_town_is_not_exists = this_town_is_not_exists.replace("$town", args[0]);
							p.sendMessage(this_town_is_not_exists);
						}
					}
					else p.sendMessage(tsetef_invalid_arguments);
				}
				else sender.sendMessage(dont_have_permission);
			
				break;
				
			case ("tef"):

				if (p.hasPermission("rce.tef")) {
					if (args.length >= 1) {
						if (Integer.parseInt(args[0].trim())>0 && Integer.parseInt(args[0].trim())<6) {
							try {
								res = TownyUniverse.getInstance().getDataSource().getResident(p.getName());
								if (res.hasTown()) {
									try {
										if (plugin.getEFCD(res.getTown().getName())) {
											p.sendMessage(ef_have_cooldown);
										}
										else {
											switch (plugin.getEF(res.getTown().getName())) {
												case 1:
													if (Integer.parseInt(args[0]) == 2) {
														Resident res_i;
														Town town = res.getTown();
														List<Resident> res_list = new ArrayList<>(ResidentUtil.getOnlineResidentsViewable((Player) sender, town));
														ListIterator<Resident> resItr = res_list.listIterator();
														while (resItr.hasNext()) {
															res_i = resItr.next();
															if (Bukkit.getPlayer(res_i.getName()).isOnline()) {
																plugin.removeEFeffect(town, Bukkit.getPlayer(res_i.getName()));
															}
														}
														
														plugin.setEF(res.getTown().getName(), Integer.parseInt(args[0].trim()));
														plugin.setEFCD(res.getTown().getName(), true);
														
														ListIterator<Resident> resItr1 = res_list.listIterator();
														while (resItr1.hasNext()) {
															res_i = resItr1.next();
															if (Bukkit.getPlayer(res_i.getName()).isOnline()) {
																plugin.addEFeffect(town, Bukkit.getPlayer(res_i.getName()));
															}
														}
														
														updated_ef = updated_ef.replace("$town", res.getTown().getName());
														updated_ef = updated_ef.replace("$ef", plugin.getConfig().getString("ef.ef_" + String.valueOf(plugin.getEF(res.getTown().getName()))));
														
														p.sendMessage(updated_ef);
													}
													else if (Integer.parseInt(args[0]) == 1) {
														p.sendMessage(this_ef_is_now);
													}
													else {
														p.sendMessage(setting_of_ef_is_limited);
													}
													break;

												case 2:
													if (Integer.parseInt(args[0]) == 3 || Integer.parseInt(args[0]) == 1) {
														Resident res_i;
														Town town = res.getTown();
														List<Resident> res_list = new ArrayList<>(ResidentUtil.getOnlineResidentsViewable((Player) sender, town));
														ListIterator<Resident> resItr = res_list.listIterator();
														while (resItr.hasNext()) {
															res_i = resItr.next();
															if (Bukkit.getPlayer(res_i.getName()).isOnline()) {
																plugin.removeEFeffect(town, Bukkit.getPlayer(res_i.getName()));
															}
														}
														
														plugin.setEF(res.getTown().getName(), Integer.parseInt(args[0].trim()));
														plugin.setEFCD(res.getTown().getName(), true);
														
														ListIterator<Resident> resItr1 = res_list.listIterator();
														while (resItr1.hasNext()) {
															res_i = resItr1.next();
															if (Bukkit.getPlayer(res_i.getName()).isOnline()) {
																plugin.addEFeffect(town, Bukkit.getPlayer(res_i.getName()));
															}
														}
														
														updated_ef = updated_ef.replace("$town", res.getTown().getName());
														updated_ef = updated_ef.replace("$ef", plugin.getConfig().getString("ef.ef_" + String.valueOf(plugin.getEF(res.getTown().getName()))));
														
														p.sendMessage(updated_ef);
													}
													else if (Integer.parseInt(args[0]) == 2) {
														p.sendMessage(this_ef_is_now);
													}
													else {
														p.sendMessage(setting_of_ef_is_limited);
													}
													break;

												case 3:
													if (Integer.parseInt(args[0]) == 4 || Integer.parseInt(args[0]) == 2) {
														Resident res_i;
														Town town = res.getTown();
														List<Resident> res_list = new ArrayList<>(ResidentUtil.getOnlineResidentsViewable((Player) sender, town));
														ListIterator<Resident> resItr = res_list.listIterator();
														while (resItr.hasNext()) {
															res_i = resItr.next();
															if (Bukkit.getPlayer(res_i.getName()).isOnline()) {
																plugin.removeEFeffect(town, Bukkit.getPlayer(res_i.getName()));
															}
														}
														
														plugin.setEF(res.getTown().getName(), Integer.parseInt(args[0].trim()));
														plugin.setEFCD(res.getTown().getName(), true);
														
														ListIterator<Resident> resItr1 = res_list.listIterator();
														while (resItr1.hasNext()) {
															res_i = resItr1.next();
															if (Bukkit.getPlayer(res_i.getName()).isOnline()) {
																plugin.addEFeffect(town, Bukkit.getPlayer(res_i.getName()));
															}
														}
														
														updated_ef = updated_ef.replace("$town", res.getTown().getName());
														updated_ef = updated_ef.replace("$ef", plugin.getConfig().getString("ef.ef_" + String.valueOf(plugin.getEF(res.getTown().getName()))));
														
														p.sendMessage(updated_ef);
													}
													else if (Integer.parseInt(args[0]) == 3) {
														p.sendMessage(this_ef_is_now);
													}
													else {
														p.sendMessage(setting_of_ef_is_limited);
													}
													break;

												case 4:
													if (Integer.parseInt(args[0]) == 5 || Integer.parseInt(args[0]) == 3) {
														Resident res_i;
														Town town = res.getTown();
														List<Resident> res_list = new ArrayList<>(ResidentUtil.getOnlineResidentsViewable((Player) sender, town));
														ListIterator<Resident> resItr = res_list.listIterator();
														while (resItr.hasNext()) {
															res_i = resItr.next();
															if (Bukkit.getPlayer(res_i.getName()).isOnline()) {
																plugin.removeEFeffect(town, Bukkit.getPlayer(res_i.getName()));
															}
														}
														
														plugin.setEF(res.getTown().getName(), Integer.parseInt(args[0].trim()));
														plugin.setEFCD(res.getTown().getName(), true);
														
														ListIterator<Resident> resItr1 = res_list.listIterator();
														while (resItr1.hasNext()) {
															res_i = resItr1.next();
															if (Bukkit.getPlayer(res_i.getName()).isOnline()) {
																plugin.addEFeffect(town, Bukkit.getPlayer(res_i.getName()));
															}
														}
														
														updated_ef = updated_ef.replace("$town", res.getTown().getName());
														updated_ef = updated_ef.replace("$ef", plugin.getConfig().getString("ef.ef_" + String.valueOf(plugin.getEF(res.getTown().getName()))));
														
														p.sendMessage(updated_ef);
													}
													else if (Integer.parseInt(args[0]) == 4) {
														p.sendMessage(this_ef_is_now);
													}
													else {
														p.sendMessage(setting_of_ef_is_limited);
													}
													break;

												case 5:
													if (Integer.parseInt(args[0]) == 4) {
														Resident res_i;
														Town town = res.getTown();
														List<Resident> res_list = new ArrayList<>(ResidentUtil.getOnlineResidentsViewable((Player) sender, town));
														ListIterator<Resident> resItr = res_list.listIterator();
														while (resItr.hasNext()) {
															res_i = resItr.next();
															if (Bukkit.getPlayer(res_i.getName()).isOnline()) {
																plugin.removeEFeffect(town, Bukkit.getPlayer(res_i.getName()));
															}
														}
														
														plugin.setEF(res.getTown().getName(), Integer.parseInt(args[0].trim()));
														plugin.setEFCD(res.getTown().getName(), true);
														
														ListIterator<Resident> resItr1 = res_list.listIterator();
														while (resItr1.hasNext()) {
															res_i = resItr1.next();
															if (Bukkit.getPlayer(res_i.getName()).isOnline()) {
																plugin.addEFeffect(town, Bukkit.getPlayer(res_i.getName()));
															}
														}
														
														updated_ef = updated_ef.replace("$town", res.getTown().getName());
														updated_ef = updated_ef.replace("$ef", plugin.getConfig().getString("ef.ef_" + String.valueOf(plugin.getEF(res.getTown().getName()))));
														
														p.sendMessage(updated_ef);
													}
													else if (Integer.parseInt(args[0]) == 5) {
														p.sendMessage(this_ef_is_now);
													}
													else {
														p.sendMessage(setting_of_ef_is_limited);
													}
													break;
											}
										}
									}
									catch (NumberFormatException e) {
										p.sendMessage(arg_must_be_int);
									}
									
								}
								else p.sendMessage(player_is_not_in_the_town);
							} catch (NotRegisteredException e) {
								p.sendMessage("Error with resident or town!");
								plugin.getLogger().info("Error with resident or town!");
							}
						}
						else p.sendMessage(tef_invalid_arguments);
					}
					else p.sendMessage(tef_invalid_arguments);
				}
				else sender.sendMessage(dont_have_permission);
			
				break;
			
			case ("nrmadd"):
				
				if (p.hasPermission("rce.nrmadd")) {
					if (args.length == 1) {
						try {
							res = TownyUniverse.getInstance().getDataSource().getResident(p.getName());
							if (res.hasNation()) {
								if (Integer.parseInt(args[0].trim())>=0) {	
									try {
										if (plugin.getRM(res.getTown().getName()) >= Integer.parseInt(args[0].trim())) {
											plugin.addRM_N(res.getTown().getNation().getName(), Integer.parseInt(args[0].trim()));
											plugin.addRM(res.getTown().getName(), -Integer.parseInt(args[0].trim()));
	
											updated_rm = updated_rm.replace("$town", res.getTown().getName());
											updated_rm = updated_rm.replace("$rm", String.valueOf(plugin.getRM(res.getTown().getName())));
											updated_rm = updated_rm.replace("$nation", res.getTown().getNation().getName());
											updated_rm = updated_rm.replace("$rm", String.valueOf(plugin.getRM_N(res.getTown().getNation().getName())));
												
											p.sendMessage(updated_rm);
										}
										else {
											town_dont_have_rm = town_dont_have_rm.replace("$rm", args[0]);
											p.sendMessage(town_dont_have_rm);
										}
									}
									catch (NumberFormatException e) {
										p.sendMessage(arg_must_be_int);
									}
								}
								else p.sendMessage(value_less_zero);
							}
							else p.sendMessage(player_is_not_in_the_town);
						} catch (NotRegisteredException e) {
							p.sendMessage("Error with resident or town or nation!");
							plugin.getLogger().info("Error with resident or town or nation!");
						}
					}
					else p.sendMessage(nrmadd_invalid_arguments);
				}
				else sender.sendMessage(dont_have_permission);
			
				break;
			
			case ("declarewar"):
				
				if (p.hasPermission("rce.declarewar")) {
					if (args.length == 2) {
						try {
							res = TownyUniverse.getInstance().getDataSource().getResident(p.getName());
							if (res.hasNation()) {
								if (!res.getTown().getNation().getName().equals(args[0])) {
									if (TownyUniverse.getInstance().getDataSource().getNations().contains(TownyUniverse.getInstance().getDataSource().getNation(args[0]))) {
										if (plugin.hasDeclaredWar(res.getTown().getNation())) {
											if (args[1].equals("destroy_alliance")) {
												plugin.declareWar(res.getTown().getNation(), TownyUniverse.getInstance().getDataSource().getNation(args[0]), 1);
												try {
													res.getTown().getNation().addEnemy(TownyUniverse.getInstance().getDataSource().getNation(args[0]));
													TownyUniverse.getInstance().getDataSource().getNation(args[0]).addEnemy(res.getTown().getNation());
													TownyUniverse.getInstance().getDataSource().saveNations();
												} catch (AlreadyRegisteredException e) {
													p.sendMessage("Error with adding enemy of nation!");
													plugin.getLogger().info("Error with adding enemy of nation!");
												}
												war_declared = war_declared.replace("$attacker", res.getTown().getNation().getName());
												war_declared = war_declared.replace("$defender", TownyUniverse.getInstance().getDataSource().getNation(args[0]).getName());
												war_declared = war_declared.replace("$casus_beli", plugin.getConfig().getString("casus_beli.destroy_alliance"));
												for(Player player : Bukkit.getOnlinePlayers()) {
													player.sendMessage(war_declared);
												}
											}
											/*else if (args[1].equals("take_rm")) {
												plugin.declareWar(res.getTown().getNation(), TownyUniverse.getInstance().getDataSource().getNation(args[0]), 2);
												try {
													res.getTown().getNation().addEnemy(TownyUniverse.getInstance().getDataSource().getNation(args[0]));
													TownyUniverse.getInstance().getDataSource().getNation(args[0]).addEnemy(res.getTown().getNation());
												} catch (AlreadyRegisteredException e) {
													//
												}
												for(Player player : Bukkit.getOnlinePlayers()) {
													player.sendMessage("Nation " + res.getTown().getNation().getName() + " declared war on " + TownyUniverse.getInstance().getDataSource().getNation(args[0]).getName());
												}
											}*/
											else if (args[1].equals("conquer")) {
												
												float townblockRate = ((float) TownyUniverse.getInstance().getDataSource().getNation(args[0]).getNumTownblocks()) / ((float) res.getTown().getNation().getNumTownblocks());
												if (townblockRate < 0.5) {
													p.sendMessage("Your nation is too big compared to another nation!");
												}
												else if (townblockRate > 2) {
													p.sendMessage("Your nation is too small compared to another nation!");
												}
												else {
													plugin.declareWar(res.getTown().getNation(), TownyUniverse.getInstance().getDataSource().getNation(args[0]), 2);
													try {
														res.getTown().getNation().addEnemy(TownyUniverse.getInstance().getDataSource().getNation(args[0]));
														TownyUniverse.getInstance().getDataSource().getNation(args[0]).addEnemy(res.getTown().getNation());
														TownyUniverse.getInstance().getDataSource().saveNations();
													} catch (AlreadyRegisteredException e) {
														p.sendMessage("Error with adding enemy of nation!");
														plugin.getLogger().info("Error with adding enemy of nation!");
													}
													war_declared = war_declared.replace("$attacker", res.getTown().getNation().getName());
													war_declared = war_declared.replace("$defender", TownyUniverse.getInstance().getDataSource().getNation(args[0]).getName());
													war_declared = war_declared.replace("$casus_beli", plugin.getConfig().getString("casus_beli.destroy_alliance"));
													for(Player player : Bukkit.getOnlinePlayers()) {
														player.sendMessage(war_declared);
													}
												}
											}
											else p.sendMessage(declare_war_invalid_arguments);
										}
										else p.sendMessage(you_have_already_declared_war);
									}
									else {
										this_nation_is_not_exists = this_nation_is_not_exists.replace("$nation", args[0]);
										p.sendMessage(this_nation_is_not_exists);
									}
								}
								else p.sendMessage(you_cant_declare_war_to_you);
							}
							else p.sendMessage(player_is_not_in_the_nation);
						} catch (NotRegisteredException e) {
							p.sendMessage("Error with resident or town or nation!");
							plugin.getLogger().info("Error with resident or town or nation!");
						}
					}
					else p.sendMessage(declare_war_invalid_arguments);
				}
				else sender.sendMessage(dont_have_permission);
			
				break;
			
			case ("warinfo"):
				
				if (p.hasPermission("rce.nrmadd")) {
					if (TownyAPI.getInstance().isWarTime()) {
						if (args.length == 0) {
							try {
								File wars =  new File(plugin.getDataFolder() + "/wars.yml");
								FileConfiguration warsConfig = YamlConfiguration.loadConfiguration(wars);
								
								res = TownyUniverse.getInstance().getDataSource().getResident(p.getName());
								if (res.hasNation()) {
									Nation nation = res.getTown().getNation();
									for (String war : warsConfig.getConfigurationSection("wars").getKeys(false)) {
										if (warsConfig.getString("wars." + war + ".attacker").equals(nation.getName()) || warsConfig.getString("wars." + war + ".defender").equals(nation.getName())) {
											WarInfoMessage (p, war);
										}
										else p.sendMessage(your_nation_is_not_in_war);
									}
									
								}
								else p.sendMessage(player_is_not_in_the_nation);
							} catch (NotRegisteredException e) {
								p.sendMessage("Error with resident or town or nation!");
								plugin.getLogger().info("Error with resident or town or nation!");
							}
						}
						else if (args.length == 1) {
							try {
								File wars =  new File(plugin.getDataFolder() + "/wars.yml");
								FileConfiguration warsConfig = YamlConfiguration.loadConfiguration(wars);
								
								Nation nation = TownyUniverse.getInstance().getDataSource().getNation(args[0]);
								
								for (String war : warsConfig.getConfigurationSection("wars").getKeys(false)) {
									if (warsConfig.getString("wars." + war + ".attacker").equals(nation.getName()) || warsConfig.getString("wars." + war + ".defender").equals(nation.getName())) {
										WarInfoMessage (p, war);
									}
									else p.sendMessage(this_nation_is_not_in_war);
								}
								//else p.sendMessage(player_is_not_in_the_town);
							} catch (NotRegisteredException e) {
								this_nation_is_not_exists = this_nation_is_not_exists.replace("$nation", args[0]);
								p.sendMessage(this_nation_is_not_exists);
							}
						}
						else if (args.length == 2) {
							try {
								File wars =  new File(plugin.getDataFolder() + "/wars.yml");
								FileConfiguration warsConfig = YamlConfiguration.loadConfiguration(wars);
								
								Nation nation1 = TownyUniverse.getInstance().getDataSource().getNation(args[0]);
								Nation nation2 = TownyUniverse.getInstance().getDataSource().getNation(args[1]);
								
								for (String war : warsConfig.getConfigurationSection("wars").getKeys(false)) {
									if (warsConfig.getString("wars." + war + ".attacker").equals(nation1.getName()) && warsConfig.getString("wars." + war + ".defender").equals(nation2.getName())) {
										WarInfoMessage (p, war);
									}
									else if (warsConfig.getString("wars." + war + ".attacker").equals(nation2.getName()) && warsConfig.getString("wars." + war + ".defender").equals(nation1.getName())) {
										WarInfoMessage (p, war);
									}
									else p.sendMessage(this_nations_is_not_in_war);
								}
								//else p.sendMessage(player_is_not_in_the_town);
							} catch (NotRegisteredException e) {
								p.sendMessage(one_of_this_nations_is_not_exists);
							}
						}
						else p.sendMessage(warinfo_invalid_arguments);
					}
					else p.sendMessage(now_nowar_time);
				}
				else sender.sendMessage(dont_have_permission);
			
				break;
			
			case ("tnew"):
				
				if (p.hasPermission("rce.tnew")) {

					try {
						if (args.length == 0) {
							p.sendMessage(TownySettings.getLangString("msg_specify_name"));
						} else if (args.length >= 1) {
							String[] newSplit = args;
							String townName = String.join("_", newSplit);
							newTown(p, townName, p.getName(), false);			
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else sender.sendMessage(dont_have_permission);
			
				break;
				
			case ("nnew"):
				
				if (p.hasPermission("rce.nnew")) {
					
					
					TownyUniverse townyUniverse = TownyUniverse.getInstance();
					try {
						Resident resident = townyUniverse.getDataSource().getResident(p.getName());
						if (resident.hasTown()) {
					        if ((TownySettings.getNumResidentsCreateNation() > 0) && (resident.getTown().getNumResidents() < TownySettings.getNumResidentsCreateNation())) {
					          TownyMessaging.sendErrorMsg(p, String.format(TownySettings.getLangString("msg_err_not_enough_residents_new_nation")));
					          return true;
					        }
		
							if (!townyUniverse.getPermissionSource().testPermission(p, PermissionNodes.TOWNY_COMMAND_NATION_NEW.getNode()))
								//throw new TownyException(TownySettings.getNotPermToNewNationLine());
		
							if (args.length == 0)
								TownyMessaging.sendErrorMsg(p, TownySettings.getLangString("msg_specify_nation_name"));
							else if (args.length >= 1) {
		
								if (!resident.isMayor() && !resident.getTown().hasAssistant(resident))
									throw new TownyException(TownySettings.getLangString("msg_peasant_right"));
								
								String[] newSplit = args;
								String nationName = String.join("_", newSplit);
								newNation(p, nationName, resident.getTown().getName(), false);
		
							}
						}
						else p.sendMessage(player_is_not_in_the_town);
					} catch (NotRegisteredException e) {
						e.printStackTrace();
					} catch (TownyException e) {
						e.printStackTrace();
					} catch (InvalidNameException e) {
						// TODO Àâòîìàòè÷åñêè ñîçäàííûé áëîê catch
						e.printStackTrace();
					}
				}
				else sender.sendMessage(dont_have_permission);
			
				break;
				
			case ("toutpost"):
				
				if (p.hasPermission("rce.toutpost")) {
					
					TownyUniverse townyUniverse = TownyUniverse.getInstance();
					
					Resident resident;
					Town town;
					TownyWorld world;
					try {
						toutpost_dont_have_recourñes = toutpost_dont_have_recourñes.replace("$diamond", plugin.getConfig().getString("costs.toutpost_emerald"));
						
						resident = townyUniverse.getDataSource().getResident(p.getName());
						town = resident.getTown();
						world = townyUniverse.getDataSource().getWorld(p.getWorld().getName());
						
						double blockCost = 0;
						List<WorldCoord> selection;
						boolean attachedToEdge = true, outpost = false;
						boolean isAdmin = townyUniverse.getPermissionSource().isTownyAdmin(p);
						Coord key = Coord.parseCoord(p.getLocation());
						
						if (TownyAPI.getInstance().isWarTime()) {
							throw new TownyException(TownySettings.getLangString("msg_war_cannot_do"));
						}
						
						if (TownySettings.isAllowingOutposts()) {
							//if (!townyUniverse.getPermissionSource().testPermission(p, PermissionNodes.TOWNY_COMMAND_TOWN_CLAIM_OUTPOST.getNode()))
							//	throw new TownyException(TownySettings.getLangString("msg_err_command_disable"));
							
							// Run various tests required by configuration/permissions through Util.
							OutpostUtil.OutpostTests(town, resident, world, key, isAdmin, false);
							
							if (world.hasTownBlock(key))
								throw new TownyException(String.format(TownySettings.getLangString("msg_already_claimed_1"), key));
	
							
							selection = AreaSelectionUtil.selectWorldCoordArea(town, new WorldCoord(world.getName(), key), new String[0]);
							attachedToEdge = false;
							outpost = true;
						} else throw new TownyException(TownySettings.getLangString("msg_outpost_disable"));
						if ((world.getMinDistanceFromOtherTownsPlots(key, town) < TownySettings.getMinDistanceFromTownPlotblocks()))
							throw new TownyException(String.format(TownySettings.getLangString("msg_too_close2"), TownySettings.getLangString("townblock")));

						if(!town.hasHomeBlock() && world.getMinDistanceFromOtherTowns(key) < TownySettings.getMinDistanceFromTownHomeblocks())
							throw new TownyException(String.format(TownySettings.getLangString("msg_too_close2"), TownySettings.getLangString("homeblock")));

						TownyMessaging.sendDebugMsg("townClaim: Pre-Filter Selection ["+selection.size()+"] " + Arrays.toString(selection.toArray(new WorldCoord[0])));
						selection = AreaSelectionUtil.filterOutTownOwnedBlocks(selection);
						selection = AreaSelectionUtil.filterInvalidProximityTownBlocks(selection, town);
						
						TownyMessaging.sendDebugMsg("townClaim: Post-Filter Selection ["+selection.size()+"] " + Arrays.toString(selection.toArray(new WorldCoord[0])));
						checkIfSelectionIsValid(town, selection, attachedToEdge, blockCost, false);
										
						//Check if other plugins have a problem with claiming this area
						int blockedClaims = 0;

						for(WorldCoord coord : selection){
							//Use the user's current world
							TownPreClaimEvent preClaimEvent = new TownPreClaimEvent(town, new TownBlock(coord.getX(), coord.getZ(), world), p, isAdmin, false);
							BukkitTools.getPluginManager().callEvent(preClaimEvent);
							if(preClaimEvent.isCancelled())
								blockedClaims++;
						}

						if(blockedClaims > 0){
							throw new TownyException(String.format(TownySettings.getLangString("msg_claim_error"), blockedClaims, selection.size()));
						}
						
						if (p.getInventory().contains(Material.DIAMOND, plugin.getConfig().getInt("costs.toutpost_emerald"))) {
							outpostRemoveItems(p);
							new TownClaim(towny_plugin, p, town, selection, outpost, true, false).start();
						}
						else p.sendMessage(toutpost_dont_have_recourñes);
					} catch (TownyException x) {
						TownyMessaging.sendErrorMsg(p, x.getMessage());
						break;
					}
				}
				else sender.sendMessage(dont_have_permission);
			
				break;
			
		}
		
		return true;
	}
	
	public static void checkIfSelectionIsValid(TownBlockOwner owner, List<WorldCoord> selection, boolean attachedToEdge, double blockCost, boolean force) throws TownyException {

		if (force)
			return;
		Town town = (Town) owner;

		if (owner instanceof Town) {
			// Town town = (Town)owner;
			int available = TownySettings.getMaxTownBlocks(town) - town.getTownBlocks().size();
			TownyMessaging.sendDebugMsg("Claim Check Available: " + available);
			TownyMessaging.sendDebugMsg("Claim Selection Size: " + selection.size());
			if (available - selection.size() < 0)
				throw new TownyException(TownySettings.getLangString("msg_err_not_enough_blocks"));
		}
		
		if (attachedToEdge && !isEdgeBlock(owner, selection) && !town.getTownBlocks().isEmpty()) {
			if (selection.size() == 0)
				throw new TownyException(TownySettings.getLangString("msg_already_claimed_2"));
			else
				throw new TownyException(TownySettings.getLangString("msg_err_not_attached_edge"));
		}
	}
	
	public static boolean isEdgeBlock(TownBlockOwner owner, List<WorldCoord> worldCoords) {


		for (WorldCoord worldCoord : worldCoords)
			if (isEdgeBlock(owner, worldCoord))
				return true;
		return false;
	}
	
	public static boolean isEdgeBlock(TownBlockOwner owner, WorldCoord worldCoord) {

		int[][] offset = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
		for (int i = 0; i < 4; i++)
			try {
				TownBlock edgeTownBlock = worldCoord.getTownyWorld().getTownBlock(new Coord(worldCoord.getX() + offset[i][0], worldCoord.getZ() + offset[i][1]));
				if (edgeTownBlock.isOwner(owner)) {
					//TownyMessaging.sendDebugMsg("[Towny] Debug: isEdgeBlock(" + worldCoord.toString() + ") = True.");
					return true;
				}
			} catch (NotRegisteredException e) {
			}
		//TownyMessaging.sendDebugMsg("[Towny] Debug: isEdgeBlock(" + worldCoord.toString() + ") = False.");
		return false;
	}
	
	public static void newNation(Player player, String name, String capitalName, boolean noCharge) throws InvalidNameException {

		com.palmergames.bukkit.towny.TownyUniverse universe = com.palmergames.bukkit.towny.TownyUniverse.getInstance();
		try {
			

			String nnew_dont_have_recourñes = plugin.getConfig().getString("commands_errors.nnew_dont_have_recourñes");
			nnew_dont_have_recourñes = nnew_dont_have_recourñes.replace("&", "\u00a7");
			nnew_dont_have_recourñes = nnew_dont_have_recourñes.replace("$gold_ingot", plugin.getConfig().getString("costs.nnew_gold_ingot"));
			
			Town town = universe.getDataSource().getTown(capitalName);
			if (town.hasNation())
				throw new TownyException(TownySettings.getLangString("msg_err_already_nation"));

			// Check the name is valid and doesn't already exist.
			String filteredName;
			filteredName = NameValidation.checkAndFilterName(name);

			if ((filteredName == null) || universe.getDataSource().hasNation(filteredName))
				throw new TownyException(String.format(TownySettings.getLangString("msg_err_invalid_name"), name));

			//if (!noCharge && TownySettings.isUsingEconomy() && !town.getAccount().pay(TownySettings.getNewNationPrice(), "New Nation Cost"))
			//	throw new TownyException(String.format(TownySettings.getLangString("msg_no_funds_new_nation2"), TownySettings.getNewNationPrice()));
			
			if (player.getInventory().contains(Material.GOLD_INGOT, plugin.getConfig().getInt("costs.nnew_gold_ingot"))) {
				NationCommand.newNation(name, town);
				newNationRemoveItems(player);
				TownyMessaging.sendGlobalMessage(String.format(TownySettings.getLangString("msg_new_nation"), player.getName(), StringMgmt.remUnderscore(name)));
			}
			else player.sendMessage(nnew_dont_have_recourñes);
		} catch (TownyException x) {
			TownyMessaging.sendErrorMsg(player, x.getMessage());
		}
	}
	
	public static void newTown(Player player, String name, String mayorName, boolean noCharge) throws InvalidNameException {
		TownyUniverse townyUniverse = TownyUniverse.getInstance();

		PreNewTownEvent preEvent = new PreNewTownEvent(player, name);
		Bukkit.getPluginManager().callEvent(preEvent);
		
		String tnew_dont_have_recourñes = plugin.getConfig().getString("commands_errors.tnew_dont_have_recourñes");
		tnew_dont_have_recourñes = tnew_dont_have_recourñes.replace("&", "\u00a7");
		tnew_dont_have_recourñes = tnew_dont_have_recourñes.replace("$cobblestone", plugin.getConfig().getString("costs.tnew_cobblestone"));
		tnew_dont_have_recourñes = tnew_dont_have_recourñes.replace("$iron_ingot", plugin.getConfig().getString("costs.tnew_iron_ingot"));
		tnew_dont_have_recourñes = tnew_dont_have_recourñes.replace("$diamond", plugin.getConfig().getString("costs.tnew_diamond"));
		
		if (preEvent.isCancelled()) {
			TownyMessaging.sendErrorMsg(player, preEvent.getCancelMessage());
			return;
		}

		try {
			if (TownyAPI.getInstance().isWarTime())
				throw new TownyException(TownySettings.getLangString("msg_war_cannot_do"));

			if (TownySettings.hasTownLimit() && townyUniverse.getDataSource().getTowns().size() >= TownySettings.getTownLimit())
				throw new TownyException(TownySettings.getLangString("msg_err_universe_limit"));

			// Check the name is valid and doesn't already exist.
			String filteredName;
			filteredName = NameValidation.checkAndFilterName(name);

			if ((filteredName == null) || townyUniverse.getDataSource().hasTown(filteredName))
				throw new TownyException(String.format(TownySettings.getLangString("msg_err_invalid_name"), name));

			Resident resident = townyUniverse.getDataSource().getResident(mayorName);
			if (resident.hasTown())
				throw new TownyException(String.format(TownySettings.getLangString("msg_err_already_res"), resident.getName()));

			TownyWorld world = townyUniverse.getDataSource().getWorld(player.getWorld().getName());

			if (!world.isUsingTowny())
				throw new TownyException(TownySettings.getLangString("msg_set_use_towny_off"));

			if (!world.isClaimable())
				throw new TownyException(TownySettings.getLangString("msg_not_claimable"));

			Coord key = Coord.parseCoord(player);

			if (world.hasTownBlock(key))
				throw new TownyException(String.format(TownySettings.getLangString("msg_already_claimed_1"), key));
			
			if ((world.getMinDistanceFromOtherTownsPlots(key) < TownySettings.getMinDistanceFromTownPlotblocks()))
				throw new TownyException(String.format(TownySettings.getLangString("msg_too_close2"), TownySettings.getLangString("townblock")));

			if (world.getMinDistanceFromOtherTowns(key) < TownySettings.getMinDistanceFromTownHomeblocks())
				throw new TownyException(String.format(TownySettings.getLangString("msg_too_close2"), TownySettings.getLangString("homeblock")));

			if (TownySettings.getMaxDistanceBetweenHomeblocks() > 0)
				if ((world.getMinDistanceFromOtherTowns(key) > TownySettings.getMaxDistanceBetweenHomeblocks()) && world.hasTowns())
					throw new TownyException(TownySettings.getLangString("msg_too_far"));
			
			//if (!noCharge && TownySettings.isUsingEconomy() && !resident.getAccount().pay(TownySettings.getNewTownPrice(), "New Town Cost"))
			//	throw new TownyException(String.format(TownySettings.getLangString("msg_no_funds_new_town2"), (resident.getName().equals(player.getName()) ? TownySettings.getLangString("msg_you") : resident.getName()), TownySettings.getNewTownPrice()));
			
			if(player.getInventory().contains(Material.COBBLESTONE, plugin.getConfig().getInt("costs.tnew_cobblestone")) 
			&& player.getInventory().contains(Material.IRON_INGOT, plugin.getConfig().getInt("costs.tnew_iron_ingot")) 
			&& player.getInventory().contains(Material.DIAMOND, plugin.getConfig().getInt("costs.tnew_diamond"))) {
				TownCommand.newTown(world, name, resident, key, player.getLocation(), player);
				newTownRemoveItems(player);
				TownyMessaging.sendGlobalMessage(String.format(TownySettings.getLangString("msg_new_town"), player.getName(), StringMgmt.remUnderscore(name)));
			}
			else player.sendMessage(tnew_dont_have_recourñes);
		//} catch (EconomyException x) {
		//	TownyMessaging.sendErrorMsg(player, "No valid economy found, your server admin might need to install Vault.jar or set using_economy: false in the Towny config.yml");
		} catch (TownyException x) {
			TownyMessaging.sendErrorMsg(player, x.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void  newTownRemoveItems(Player p) {
		ItemStack is = new ItemStack(Material.COBBLESTONE, 1);
		ItemStack is2 = new ItemStack(Material.IRON_INGOT, 1);
		ItemStack is3 = new ItemStack(Material.DIAMOND, 1);
		//ItemStack is3 = new ItemStack(Material.WHEAT, 1);
		for (int s = 0; s < plugin.getConfig().getInt("costs.tnew_cobblestone"); s++) {
	        p.getInventory().removeItem(is);
		}
		for (int s = 0; s < plugin.getConfig().getInt("costs.tnew_iron_ingot"); s++) {
	        p.getInventory().removeItem(is2);
		}
		for (int s = 0; s < plugin.getConfig().getInt("costs.tnew_diamond"); s++) {
	        p.getInventory().removeItem(is3);
		}
    }
	public static void  newNationRemoveItems(Player p) {
		ItemStack is = new ItemStack(Material.GOLD_INGOT, 1);
		//ItemStack is3 = new ItemStack(Material.WHEAT, 1);
		for (int s = 0; s < plugin.getConfig().getInt("costs.nnew_gold_ingot"); s++) {
	        p.getInventory().removeItem(is);
		}
    }
	public static void  outpostRemoveItems(Player p) {
		ItemStack is = new ItemStack(Material.DIAMOND, 1);
		//ItemStack is3 = new ItemStack(Material.WHEAT, 1);
		for (int s = 0; s < plugin.getConfig().getInt("costs.toutpost_emerald"); s++) {
	        p.getInventory().removeItem(is);
		}
    }
	
	private void TownBalanceInfo (Player p, String town) {
		String tbal_open_string  = plugin.getConfig().getString("tbal.tbal_open_string");
		String tbal_bank_of_town_name  = plugin.getConfig().getString("tbal.tbal_bank_of_town_name");
		String tbal_bank_of_town_values  = plugin.getConfig().getString("tbal.tbal_bank_of_town_values");
		String tbal_cost_of_town_name  = plugin.getConfig().getString("tbal.tbal_cost_of_town_name");
		String tbal_cost_of_town_values  = plugin.getConfig().getString("tbal.tbal_cost_of_town_values");
		String tbal_nation_tax  = plugin.getConfig().getString("tbal.tbal_nation_tax");
		String tbal_ef_of_town  = plugin.getConfig().getString("tbal.tbal_ef_of_town");
		String tbal_close_string  = plugin.getConfig().getString("tbal.tbal_close_string");
		
		tbal_open_string = tbal_open_string.replace("&", "\u00a7");
		tbal_bank_of_town_name = tbal_bank_of_town_name.replace("&", "\u00a7");
		tbal_bank_of_town_values = tbal_bank_of_town_values.replace("&", "\u00a7");
		tbal_cost_of_town_name = tbal_cost_of_town_name.replace("&", "\u00a7");
		tbal_cost_of_town_values = tbal_cost_of_town_values.replace("&", "\u00a7");
		tbal_nation_tax = tbal_nation_tax.replace("&", "\u00a7");
		tbal_ef_of_town = tbal_ef_of_town.replace("&", "\u00a7");
		tbal_close_string = tbal_close_string.replace("&", "\u00a7");
		
		float rm_fm_k = 1, tnp_fm_k = 1;
		
		switch (plugin.getEF(town)) {
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
		tbal_open_string = tbal_open_string.replace("$town", town);
		tbal_bank_of_town_values = tbal_bank_of_town_values.replace("$tnp", String.valueOf(plugin.getTNP(town)));
		tbal_bank_of_town_values = tbal_bank_of_town_values.replace("$rm", String.valueOf(plugin.getRM(town)));
	
		try {
			tbal_cost_of_town_values = tbal_cost_of_town_values.replace("$tnp_cost", String.valueOf((TownyUniverse.getInstance().getDataSource().getTown(town).getResidents().size() * tnp_fm_k * plugin.getTNPcost())));
			tbal_cost_of_town_values = tbal_cost_of_town_values.replace("$rm_cost", String.valueOf((TownyUniverse.getInstance().getDataSource().getTown(town).getTownBlocks().size() * rm_fm_k * plugin.getRMcost())));
		} catch (NotRegisteredException e) {
			e.printStackTrace();
		}
		
		tbal_ef_of_town = tbal_ef_of_town.replace("$ef", plugin.getConfig().getString("ef.ef_" + String.valueOf(plugin.getEF(town))));
		
		p.sendMessage(tbal_open_string);
		p.sendMessage(tbal_bank_of_town_name);
		p.sendMessage(tbal_bank_of_town_values);
		p.sendMessage(tbal_cost_of_town_name);
		p.sendMessage(tbal_cost_of_town_values);
		try {
			Town town_1 = TownyUniverse.getInstance().getDataSource().getTown(town);
			if (town_1.hasNation()) {
				tbal_nation_tax = tbal_nation_tax.replace("$tax", String.valueOf(plugin.getTax_N(town_1.getNation().getName())));
				p.sendMessage(tbal_nation_tax);
			}
		} catch (NotRegisteredException e) {
			e.printStackTrace();
		}
		p.sendMessage(tbal_ef_of_town);
		p.sendMessage(tbal_close_string);
	}
	private void WarInfoMessage(Player p, String war) {
		
		String warinfo_open_string_war  = plugin.getConfig().getString("warinfo.warinfo_open_string_war");
		String warinfo_open_string_truce  = plugin.getConfig().getString("warinfo.warinfo_open_string_truce");
		String warinfo_attacker_name  = plugin.getConfig().getString("warinfo.warinfo_attacker_name");
		String warinfo_defender_name  = plugin.getConfig().getString("warinfo.warinfo_defender_name");
		String warinfo_chunks  = plugin.getConfig().getString("warinfo.warinfo_chunks");
		String warinfo_score  = plugin.getConfig().getString("warinfo.warinfo_score");
		String warinfo_casus_beli  = plugin.getConfig().getString("warinfo.warinfo_casus_beli");
		String warinfo_winner  = plugin.getConfig().getString("warinfo.warinfo_winner");
		String warinfo_close_string  = plugin.getConfig().getString("warinfo.warinfo_close_string");
		
		warinfo_open_string_war = warinfo_open_string_war.replace("&", "\u00a7");
		warinfo_open_string_truce = warinfo_open_string_truce.replace("&", "\u00a7");
		warinfo_attacker_name = warinfo_attacker_name.replace("&", "\u00a7");
		warinfo_defender_name = warinfo_defender_name.replace("&", "\u00a7");
		warinfo_chunks = warinfo_chunks.replace("&", "\u00a7");
		warinfo_score = warinfo_score.replace("&", "\u00a7");
		warinfo_casus_beli = warinfo_casus_beli.replace("&", "\u00a7");
		warinfo_winner = warinfo_winner.replace("&", "\u00a7");
		warinfo_close_string = warinfo_close_string.replace("&", "\u00a7");
		
		File wars =  new File(plugin.getDataFolder() + "/wars.yml");
		FileConfiguration warsConfig = YamlConfiguration.loadConfiguration(wars);
		
		String attacker = warsConfig.getString("wars." + war + ".attacker");
		String defender = warsConfig.getString("wars." + war + ".defender");
		float attacker_chunk_percent = ((float) warsConfig.getInt("wars." + war + "." + attacker + ".chunks_now")) / ((float) warsConfig.getInt("wars." + war + "." + attacker + ".chunks_on_start"));
		float defender_chunk_percent = ((float) warsConfig.getInt("wars." + war + "." + defender + ".chunks_now")) / ((float) warsConfig.getInt("wars." + war + "." + defender + ".chunks_on_start"));
		
		attacker_chunk_percent = attacker_chunk_percent * 100;
		defender_chunk_percent = defender_chunk_percent * 100;
		
		float attacker_score_percent =  ((float) warsConfig.getInt("wars." + war + "." + attacker + ".score_now")) / ((float) warsConfig.getInt("wars." + war + "." + attacker + ".score_on_start"));
		float defender_score_percent =  ((float) warsConfig.getInt("wars." + war + "." + defender + ".score_now")) / ((float) warsConfig.getInt("wars." + war + "." + defender + ".score_on_start"));
		
		attacker_score_percent = attacker_score_percent * 100;
		defender_score_percent = defender_score_percent * 100;
		
		if (warsConfig.getString("wars." + war + ".winner").equals("none")) {
			p.sendMessage(warinfo_open_string_war);
		}
		else {
			p.sendMessage(warinfo_open_string_truce);
		}
		warinfo_attacker_name = warinfo_attacker_name.replace("$nation", attacker);
		p.sendMessage(warinfo_attacker_name);
		warinfo_chunks = warinfo_chunks.replace("$chunks_now", String.valueOf(warsConfig.getInt("wars." + war + "." + attacker + ".chunks_now")));
		warinfo_chunks = warinfo_chunks.replace("$chunks_on_start", String.valueOf(warsConfig.getInt("wars." + war + "." + attacker + ".chunks_on_start")));
		warinfo_chunks = warinfo_chunks.replace("$chunk_percent", String.valueOf((int) attacker_chunk_percent));
		p.sendMessage(warinfo_chunks);
		warinfo_score = warinfo_score.replace("$score_now", String.valueOf(warsConfig.getInt("wars." + war + "." + attacker + ".score_now")));
		warinfo_score = warinfo_score.replace("$score_on_start", String.valueOf(warsConfig.getInt("wars." + war + "." + attacker + ".score_on_start")));
		warinfo_score = warinfo_score.replace("$score_percent", String.valueOf((int) attacker_score_percent));
		p.sendMessage(warinfo_score);

		warinfo_chunks  = plugin.getConfig().getString("warinfo.warinfo_chunks");
		warinfo_score  = plugin.getConfig().getString("warinfo.warinfo_score");
		warinfo_chunks = warinfo_chunks.replace("&", "\u00a7");
		warinfo_score = warinfo_score.replace("&", "\u00a7");
		
		warinfo_defender_name = warinfo_defender_name.replace("$nation", defender);
		p.sendMessage(warinfo_defender_name);
		warinfo_chunks = warinfo_chunks.replace("$chunks_now", String.valueOf(warsConfig.getInt("wars." + war + "." + defender + ".chunks_now")));
		warinfo_chunks = warinfo_chunks.replace("$chunks_on_start", String.valueOf(warsConfig.getInt("wars." + war + "." + defender + ".chunks_on_start")));
		warinfo_chunks = warinfo_chunks.replace("$chunk_percent", String.valueOf((int) defender_chunk_percent));
		p.sendMessage(warinfo_chunks);
		warinfo_score = warinfo_score.replace("$score_now", String.valueOf(warsConfig.getInt("wars." + war + "." + defender + ".score_now")));
		warinfo_score = warinfo_score.replace("$score_on_start", String.valueOf(warsConfig.getInt("wars." + war + "." + defender + ".score_on_start")));
		warinfo_score = warinfo_score.replace("$score_percent", String.valueOf((int) defender_score_percent));
		p.sendMessage(warinfo_score);
		
		switch(warsConfig.getInt("wars." + war + ".casus_beli")) {
			case 1:
				warinfo_casus_beli = warinfo_casus_beli.replace("$casus_beli", plugin.getConfig().getString("casus_beli.destroy_alliance"));
				break;
			case 2:
				warinfo_casus_beli = warinfo_casus_beli.replace("$casus_beli", plugin.getConfig().getString("casus_beli.conquer"));
				break;
		}
		p.sendMessage(warinfo_casus_beli);
		
		if (warsConfig.getString("wars." + war + ".winner").equals("none")) {
			warinfo_winner = warinfo_winner.replace("$winner", plugin.getConfig().getString("warinfo.no_winner"));
		}
		else {
			warinfo_winner = warinfo_winner.replace("$winner", warsConfig.getString("wars." + war + ".winner"));
		}

		p.sendMessage(warinfo_winner);

		p.sendMessage(warinfo_close_string);
	}
	
	/*	@SuppressWarnings("deprecation")
	 * 	public void WarEndsEvent () {
		
		
		
		String war_ends_1_no_winner = plugin.getConfig().getString("events.war_ends_1_no_winner");
		String war_ends_1_attacker_winner = plugin.getConfig().getString("events.war_ends_1_attacker_winner");
		String war_ends_1_defender_winner = plugin.getConfig().getString("events.war_ends_1_defender_winner");

		String war_ends_2_no_winner = plugin.getConfig().getString("events.war_ends_2_no_winner");
		String war_ends_2_attacker_winner = plugin.getConfig().getString("events.war_ends_2_attacker_winner");
		String war_ends_2_defender_winner = plugin.getConfig().getString("events.war_ends_2_defender_winner");
		
		war_ends_1_no_winner = war_ends_1_no_winner.replace("&", "\u00a7");
		war_ends_1_attacker_winner = war_ends_1_attacker_winner.replace("&", "\u00a7");
		war_ends_1_defender_winner = war_ends_1_defender_winner.replace("&", "\u00a7");

		war_ends_2_no_winner = war_ends_2_no_winner.replace("&", "\u00a7");
		war_ends_2_attacker_winner = war_ends_2_attacker_winner.replace("&", "\u00a7");
		war_ends_2_defender_winner = war_ends_2_defender_winner.replace("&", "\u00a7");
		
		Town town;

		File wars =  new File(plugin.getDataFolder() + "/wars.yml");
		FileConfiguration warsConfig = YamlConfiguration.loadConfiguration(wars);
		for (String war : warsConfig.getConfigurationSection("wars").getKeys(false)) {
			// plugin.addRM_N(warsConfig.getString("wars." + war + ".attacker"), 10);
			//plugin.getLogger().info(war);
			if (warsConfig.getInt("wars." + war + ".casus_beli") != 2) {
				for(String chunk : warsConfig.getConfigurationSection("wars." + war + ".chunks").getKeys(true)) {
					for (TownBlock townblock : TownyUniverse.getInstance().getDataSource().getAllTownBlocks()) {
						if ((townblock.getX() == warsConfig.getInt("wars." + war + ".chunks." + chunk + ".x"))&&(townblock.getZ() == warsConfig.getInt("wars." + war + ".chunks." + chunk + ".z"))) {
							try {
								town = TownyUniverse.getInstance().getDataSource().getTown(warsConfig.getString("wars." + war + ".chunks." + chunk + ".owner"));
								if (!town.hasTownBlock(townblock)) {
									townblock.setTown(town);
									TownyUniverse.getInstance().getDataSource().saveTownBlock(townblock);
								}
								
							} catch (NotRegisteredException e2) {
								e2.printStackTrace();
							}
						}
					}
				}
			}
			if (warsConfig.getInt("wars." + war + ".casus_beli") == 1) {
				if (warsConfig.getString("wars." + war + ".winner").equals("none")) {
					
					
					
					String attacker = warsConfig.getString("wars." + war + ".attacker");
					String defender = warsConfig.getString("wars." + war + ".defender");
					
					float attacker_chunk_percent =  ((float) warsConfig.getInt("wars." + war + "." + attacker + ".chunks_now")) / ((float) warsConfig.getInt("wars." + war + "." + attacker + ".chunks_on_start"));
					float defender_chunk_percent =  ((float) warsConfig.getInt("wars." + war + "." + defender + ".chunks_now")) / ((float) warsConfig.getInt("wars." + war + "." + defender + ".chunks_on_start"));

					float attacker_score_percent =  ((float) warsConfig.getInt("wars." + war + "." + attacker + ".score_now")) / ((float) warsConfig.getInt("wars." + war + "." + attacker + ".score_on_start"));
					float defender_score_percent =  ((float) warsConfig.getInt("wars." + war + "." + defender + ".score_now")) / ((float) warsConfig.getInt("wars." + war + "." + defender + ".score_on_start"));
					if (warsConfig.getString("wars." + war + ".winner").equals("none")) {
						if ((attacker_chunk_percent >= 0.7) && (defender_chunk_percent >= 0.7)) {
							
							// No winner
							
							try {
								
								war_ends_1_no_winner = plugin.getConfig().getString("events.war_ends_1_no_winner");
								war_ends_1_no_winner = war_ends_1_no_winner.replace("&", "\u00a7");
								
								Nation attackerNation = TownyUniverse.getInstance().getDataSource().getNation(attacker);
								Nation defenderNation = TownyUniverse.getInstance().getDataSource().getNation(defender);
								
								attackerNation.removeEnemy(defenderNation);
								defenderNation.removeEnemy(attackerNation);
								
								war_ends_1_no_winner = war_ends_1_no_winner.replace("$attacker", attacker);
								war_ends_1_no_winner = war_ends_1_no_winner.replace("$defender", defender);
								
								for(Player p : Bukkit.getOnlinePlayers()) {
									Resident res;
									try {
										res = TownyUniverse.getInstance().getDataSource().getResident(p.getName());
										if (res.hasNation()) {
											if (res.getTown().getNation().equals(attackerNation) || res.getTown().getNation().equals(defenderNation)) {		
												p.sendMessage(war_ends_1_no_winner);
											}
										}
									} catch (NotRegisteredException e1) {
										e1.printStackTrace();
									}
								}
							} catch (NotRegisteredException e1) {
								e1.printStackTrace();
							}
							
						}
						else if ((attacker_chunk_percent >= 0.7) && (defender_chunk_percent < 0.7)) {
							
							// Attacker winner
							
							warsConfig.set("wars." + war + ".winner", attacker);
							warsConfig.set("wars." + war + ".loser", defender);
							
							try {
								
								war_ends_1_attacker_winner = plugin.getConfig().getString("events.war_ends_1_attacker_winner");
								war_ends_1_attacker_winner = war_ends_1_attacker_winner.replace("&", "\u00a7");
								
								Nation LoserNation = TownyUniverse.getInstance().getDataSource().getNation(defender);
								Nation WinnerNation = TownyUniverse.getInstance().getDataSource().getNation(attacker);
								
								LoserNation.removeEnemy(WinnerNation);
								WinnerNation.removeEnemy(LoserNation);
	
								TownyUniverse.getInstance().getDataSource().saveNation(WinnerNation);
								TownyUniverse.getInstance().getDataSource().saveNation(LoserNation);
								
								TownyUniverse.getInstance().getDataSource().removeNation(LoserNation);
								TownyUniverse.getInstance().getDataSource().saveNation(WinnerNation);
								
								//TownyUniverse.getInstance().getDataSource().saveNation(LoserNation);
								
								war_ends_1_attacker_winner = war_ends_1_attacker_winner.replace("$attacker", attacker);
								war_ends_1_attacker_winner = war_ends_1_attacker_winner.replace("$defender", defender);
								
								for(Player p : Bukkit.getOnlinePlayers()) {
									p.sendMessage(war_ends_1_attacker_winner);
								}
								
							} catch (NotRegisteredException e1) {
								e1.printStackTrace();
							}
						}
						else if ((attacker_chunk_percent < 0.7) && (defender_chunk_percent >= 0.7)) {
							
							// Defender winner
							
							warsConfig.set("wars." + war + ".winner", defender);
							warsConfig.set("wars." + war + ".loser", attacker);
							
							try {

								war_ends_1_defender_winner = plugin.getConfig().getString("events.war_ends_1_defender_winner");
								war_ends_1_defender_winner = war_ends_1_defender_winner.replace("&", "\u00a7");
								
								Nation LoserNation = TownyUniverse.getInstance().getDataSource().getNation(attacker);
								Nation WinnerNation = TownyUniverse.getInstance().getDataSource().getNation(defender);
								
								LoserNation.removeEnemy(WinnerNation);
								WinnerNation.removeEnemy(LoserNation);
	
								TownyUniverse.getInstance().getDataSource().saveNation(WinnerNation);
								TownyUniverse.getInstance().getDataSource().saveNation(LoserNation);
								
								TownyUniverse.getInstance().getDataSource().removeNation(LoserNation);
								TownyUniverse.getInstance().getDataSource().saveNation(WinnerNation);
								
								//TownyUniverse.getInstance().getDataSource().saveNation(LoserNation);
								
								war_ends_1_defender_winner = war_ends_1_defender_winner.replace("$attacker", attacker);
								war_ends_1_defender_winner = war_ends_1_defender_winner.replace("$defender", defender);
								
								for(Player p : Bukkit.getOnlinePlayers()) {
									p.sendMessage(war_ends_1_defender_winner);
								}
							} catch (NotRegisteredException e1) {
								e1.printStackTrace();
							}
						}
						else if ((attacker_chunk_percent < 0.7) && (defender_chunk_percent < 0.7)) {
							if (attacker_score_percent > defender_score_percent) {
								
								// Attacker winner
								
								warsConfig.set("wars." + war + ".winner", attacker);
								warsConfig.set("wars." + war + ".loser", defender);
								
								try {

									war_ends_1_attacker_winner = plugin.getConfig().getString("events.war_ends_1_attacker_winner");
									war_ends_1_attacker_winner = war_ends_1_attacker_winner.replace("&", "\u00a7");
									
									Nation LoserNation = TownyUniverse.getInstance().getDataSource().getNation(defender);
									Nation WinnerNation = TownyUniverse.getInstance().getDataSource().getNation(attacker);
									
									LoserNation.removeEnemy(WinnerNation);
									WinnerNation.removeEnemy(LoserNation);
	
									TownyUniverse.getInstance().getDataSource().saveNation(WinnerNation);
									TownyUniverse.getInstance().getDataSource().saveNation(LoserNation);
									
									TownyUniverse.getInstance().getDataSource().removeNation(LoserNation);
									TownyUniverse.getInstance().getDataSource().saveNation(WinnerNation);
									
									//TownyUniverse.getInstance().getDataSource().saveNation(LoserNation);
									
									war_ends_1_attacker_winner = war_ends_1_attacker_winner.replace("$attacker", attacker);
									war_ends_1_attacker_winner = war_ends_1_attacker_winner.replace("$defender", defender);
									
									for(Player p : Bukkit.getOnlinePlayers()) {
										p.sendMessage(war_ends_1_attacker_winner);
									}
									
								} catch (NotRegisteredException e1) {
									e1.printStackTrace();
								}
							}
							else if (attacker_score_percent < defender_score_percent){
								
								// Defender winner
								
								warsConfig.set("wars." + war + ".winner", defender);
								warsConfig.set("wars." + war + ".loser", attacker);
								
								try {

									war_ends_1_defender_winner = plugin.getConfig().getString("events.war_ends_1_defender_winner");
									war_ends_1_defender_winner = war_ends_1_defender_winner.replace("&", "\u00a7");
									
									Nation LoserNation = TownyUniverse.getInstance().getDataSource().getNation(attacker);
									Nation WinnerNation = TownyUniverse.getInstance().getDataSource().getNation(defender);
									
									LoserNation.removeEnemy(WinnerNation);
									WinnerNation.removeEnemy(LoserNation);
	
									TownyUniverse.getInstance().getDataSource().saveNation(WinnerNation);
									TownyUniverse.getInstance().getDataSource().saveNation(LoserNation);
									
									TownyUniverse.getInstance().getDataSource().removeNation(LoserNation);
									TownyUniverse.getInstance().getDataSource().saveNation(WinnerNation);
									
									//TownyUniverse.getInstance().getDataSource().saveNation(LoserNation);
									
									war_ends_1_defender_winner = war_ends_1_defender_winner.replace("$attacker", attacker);
									war_ends_1_defender_winner = war_ends_1_defender_winner.replace("$defender", defender);
									
									for(Player p : Bukkit.getOnlinePlayers()) {
										p.sendMessage(war_ends_1_defender_winner);
									}
								} catch (NotRegisteredException e1) {
									e1.printStackTrace();
								}
							}
							else {
								
								// No winner
								
								try {
									
									war_ends_1_no_winner = plugin.getConfig().getString("events.war_ends_1_no_winner");
									war_ends_1_no_winner = war_ends_1_no_winner.replace("&", "\u00a7");
									
									Nation attackerNation = TownyUniverse.getInstance().getDataSource().getNation(attacker);
									Nation defenderNation = TownyUniverse.getInstance().getDataSource().getNation(defender);
									
									attackerNation.removeEnemy(defenderNation);
									defenderNation.removeEnemy(attackerNation);
									
									war_ends_1_no_winner = war_ends_1_no_winner.replace("$attacker", attacker);
									war_ends_1_no_winner = war_ends_1_no_winner.replace("$defender", defender);
									
									for(Player p : Bukkit.getOnlinePlayers()) {
										Resident res;
										try {
											res = TownyUniverse.getInstance().getDataSource().getResident(p.getName());
											if (res.hasNation()) {
												if (res.getTown().getNation().equals(attackerNation) || res.getTown().getNation().equals(defenderNation)) {		
													p.sendMessage(war_ends_1_no_winner);
												}
											}
										} catch (NotRegisteredException e1) {
											e1.printStackTrace();
										}
									}
								} catch (NotRegisteredException e1) {
									e1.printStackTrace();
								}
							}
						}
					}
				}
				else {
					
					try {
						
						war_ends_1_defender_winner = plugin.getConfig().getString("events.war_ends_1_defender_winner");
						war_ends_1_defender_winner = war_ends_1_defender_winner.replace("&", "\u00a7");
						
						String loser = warsConfig.getString("wars." + war + ".loser");
						String winner = warsConfig.getString("wars." + war + ".winner");
						
						Nation LoserNation = TownyUniverse.getInstance().getDataSource().getNation(loser);
						Nation WinnerNation = TownyUniverse.getInstance().getDataSource().getNation(winner);
						
						LoserNation.removeEnemy(WinnerNation);
						WinnerNation.removeEnemy(LoserNation);

						//TownyUniverse.getInstance().getDataSource().saveNation(WinnerNation);
						//TownyUniverse.getInstance().getDataSource().saveNation(LoserNation);
						
						TownyUniverse.getInstance().getDataSource().removeNation(LoserNation);
						TownyUniverse.getInstance().getDataSource().saveNation(WinnerNation);
						
						//TownyUniverse.getInstance().getDataSource().saveNation(LoserNation);
						
						war_ends_1_defender_winner = war_ends_1_defender_winner.replace("$attacker", loser);
						war_ends_1_defender_winner = war_ends_1_defender_winner.replace("$defender", winner);
						
						for(Player p : Bukkit.getOnlinePlayers()) {
							p.sendMessage(war_ends_1_defender_winner);
						}
					} catch (NotRegisteredException e1) {
						e1.printStackTrace();
					}
				}
			}
			else if (warsConfig.getInt("wars." + war + ".casus_beli") == 2) {
				if (warsConfig.getString("wars." + war + ".winner").equals("none")) {
					if (warsConfig.getString("wars." + war + ".winner").equals("none")) {
						String attacker = warsConfig.getString("wars." + war + ".attacker");
						String defender = warsConfig.getString("wars." + war + ".defender");
						
						float attacker_chunk_percent =  ((float) warsConfig.getInt("wars." + war + "." + attacker + ".chunks_now")) / ((float) warsConfig.getInt("wars." + war + "." + attacker + ".chunks_on_start"));
						float defender_chunk_percent =  ((float) warsConfig.getInt("wars." + war + "." + defender + ".chunks_now")) / ((float) warsConfig.getInt("wars." + war + "." + defender + ".chunks_on_start"));
	
						float attacker_score_percent =  ((float) warsConfig.getInt("wars." + war + "." + attacker + ".score_now")) / ((float) warsConfig.getInt("wars." + war + "." + attacker + ".score_on_start"));
						float defender_score_percent =  ((float) warsConfig.getInt("wars." + war + "." + defender + ".score_now")) / ((float) warsConfig.getInt("wars." + war + "." + defender + ".score_on_start"));
						
						if ((attacker_chunk_percent >= 0.7) && (defender_chunk_percent >= 0.7)) {
							
							// No winner, chunks backs for all
							
							for(String chunk : warsConfig.getConfigurationSection("wars." + war + ".chunks").getKeys(true)) {
								for (TownBlock townblock : TownyUniverse.getInstance().getDataSource().getAllTownBlocks()) {
									if ((townblock.getX() == warsConfig.getInt("wars." + war + ".chunks." + chunk + ".x"))&&(townblock.getZ() == warsConfig.getInt("wars." + war + ".chunks." + chunk + ".z"))) {
										try {
											town = TownyUniverse.getInstance().getDataSource().getTown(warsConfig.getString("wars." + war + ".chunks." + chunk + ".owner"));
											if (!town.hasTownBlock(townblock)) {
												townblock.setTown(town);
												TownyUniverse.getInstance().getDataSource().saveTownBlock(townblock);
											}
										} catch (NotRegisteredException e2) {
											e2.printStackTrace();
										}
									}
								}
							}
							try {
								
								war_ends_2_no_winner = plugin.getConfig().getString("events.war_ends_2_no_winner");
								war_ends_2_no_winner = war_ends_2_no_winner.replace("&", "\u00a7");
								
								Nation attackerNation = TownyUniverse.getInstance().getDataSource().getNation(attacker);
								Nation defenderNation = TownyUniverse.getInstance().getDataSource().getNation(defender);
								
								attackerNation.removeEnemy(defenderNation);
								defenderNation.removeEnemy(attackerNation);
								
								war_ends_2_no_winner = war_ends_2_no_winner.replace("$attacker", attacker);
								war_ends_2_no_winner = war_ends_2_no_winner.replace("$defender", defender);
								
								for(Player p : Bukkit.getOnlinePlayers()) {
									Resident res;
									try {
										res = TownyUniverse.getInstance().getDataSource().getResident(p.getName());
										if (res.hasNation()) {
											if (res.getTown().getNation().equals(attackerNation) || res.getTown().getNation().equals(defenderNation)) {		
												p.sendMessage(war_ends_2_no_winner);
											}
										}
									} catch (NotRegisteredException e1) {
										e1.printStackTrace();
									}
								}
							} catch (NotRegisteredException e1) {
								e1.printStackTrace();
							}
						}
						else if ((attacker_chunk_percent >= 0.7) && (defender_chunk_percent < 0.7)) {
							
							// Attacker winner, chunks backs for attacker
							
							warsConfig.set("wars." + war + ".winner", attacker);
							warsConfig.set("wars." + war + ".loser", defender);
							
							try {
								
								war_ends_2_attacker_winner = plugin.getConfig().getString("events.war_ends_2_attacker_winner");
								war_ends_2_attacker_winner = war_ends_2_attacker_winner.replace("&", "\u00a7");
								
								Nation LoserNation = TownyUniverse.getInstance().getDataSource().getNation(defender);
								Nation WinnerNation = TownyUniverse.getInstance().getDataSource().getNation(attacker);
								
								LoserNation.removeEnemy(WinnerNation);
								WinnerNation.removeEnemy(LoserNation);
								
								TownyUniverse.getInstance().getDataSource().saveNation(LoserNation);
								TownyUniverse.getInstance().getDataSource().saveNation(WinnerNation);
								
								for(String chunk : warsConfig.getConfigurationSection("wars." + war + ".chunks").getKeys(true)) {
									for (TownBlock townblock : TownyUniverse.getInstance().getDataSource().getAllTownBlocks()) {
										if ((townblock.getX() == warsConfig.getInt("wars." + war + ".chunks." + chunk + ".x"))&&(townblock.getZ() == warsConfig.getInt("wars." + war + ".chunks." + chunk + ".z"))) {
											try {
												town = TownyUniverse.getInstance().getDataSource().getTown(warsConfig.getString("wars." + war + ".chunks." + chunk + ".owner"));
												if (!town.hasTownBlock(townblock) && town.getNation().getName().equals(attacker)) {
													townblock.setTown(town);
													TownyUniverse.getInstance().getDataSource().saveTownBlock(townblock);
												}
											} catch (NotRegisteredException e2) {
												e2.printStackTrace();
											}
										}
									}
								}
								
								war_ends_2_attacker_winner = war_ends_2_attacker_winner.replace("$attacker", attacker);
								war_ends_2_attacker_winner = war_ends_2_attacker_winner.replace("$defender", defender);
								
								for(Player p : Bukkit.getOnlinePlayers()) {
									p.sendMessage(war_ends_2_attacker_winner);
								}
								
							} catch (NotRegisteredException e1) {
								e1.printStackTrace();
							}
						}
						else if ((attacker_chunk_percent < 0.7) && (defender_chunk_percent >= 0.7)) {
							
							// Defender winner, chunks backs for defender
							
							warsConfig.set("wars." + war + ".winner", defender);
							warsConfig.set("wars." + war + ".loser", attacker);
							
							try {
								
								war_ends_2_defender_winner = plugin.getConfig().getString("events.war_ends_2_defender_winner");
								war_ends_2_defender_winner = war_ends_2_defender_winner.replace("&", "\u00a7");
								
								Nation LoserNation = TownyUniverse.getInstance().getDataSource().getNation(attacker);
								Nation WinnerNation = TownyUniverse.getInstance().getDataSource().getNation(defender);
								
								LoserNation.removeEnemy(WinnerNation);
								WinnerNation.removeEnemy(LoserNation);
								
								TownyUniverse.getInstance().getDataSource().saveNation(LoserNation);
								TownyUniverse.getInstance().getDataSource().saveNation(WinnerNation);
								
								for(String chunk : warsConfig.getConfigurationSection("wars." + war + ".chunks").getKeys(true)) {
									for (TownBlock townblock : TownyUniverse.getInstance().getDataSource().getAllTownBlocks()) {
										if ((townblock.getX() == warsConfig.getInt("wars." + war + ".chunks." + chunk + ".x"))&&(townblock.getZ() == warsConfig.getInt("wars." + war + ".chunks." + chunk + ".z"))) {
											try {
												town = TownyUniverse.getInstance().getDataSource().getTown(warsConfig.getString("wars." + war + ".chunks." + chunk + ".owner"));
												if (!town.hasTownBlock(townblock) && town.getNation().getName().equals(defender)) {
													townblock.setTown(town);
													TownyUniverse.getInstance().getDataSource().saveTownBlock(townblock);
												}
											} catch (NotRegisteredException e2) {
												e2.printStackTrace();
											}
										}
									}
								}
								
								war_ends_2_defender_winner = war_ends_2_defender_winner.replace("$attacker", attacker);
								war_ends_2_defender_winner = war_ends_2_defender_winner.replace("$defender", defender);
								
								for(Player p : Bukkit.getOnlinePlayers()) {
									p.sendMessage(war_ends_2_defender_winner);
								}
								
							} catch (NotRegisteredException e1) {
								e1.printStackTrace();
							}
						}
						else if ((attacker_chunk_percent < 0.7) && (defender_chunk_percent < 0.7)) {
							if (attacker_score_percent > defender_score_percent) {
								
								// Attacker winner, chunks backs for attacker
								
								warsConfig.set("wars." + war + ".winner", attacker);
								warsConfig.set("wars." + war + ".loser", defender);
								
								try {
									
									war_ends_2_attacker_winner = plugin.getConfig().getString("events.war_ends_2_attacker_winner");
									war_ends_2_attacker_winner = war_ends_2_attacker_winner.replace("&", "\u00a7");
									
									Nation LoserNation = TownyUniverse.getInstance().getDataSource().getNation(defender);
									Nation WinnerNation = TownyUniverse.getInstance().getDataSource().getNation(attacker);
									
									LoserNation.removeEnemy(WinnerNation);
									WinnerNation.removeEnemy(LoserNation);
									
									TownyUniverse.getInstance().getDataSource().saveNation(LoserNation);
									TownyUniverse.getInstance().getDataSource().saveNation(WinnerNation);
									
									for(String chunk : warsConfig.getConfigurationSection("wars." + war + ".chunks").getKeys(true)) {
										for (TownBlock townblock : TownyUniverse.getInstance().getDataSource().getAllTownBlocks()) {
											if ((townblock.getX() == warsConfig.getInt("wars." + war + ".chunks." + chunk + ".x"))&&(townblock.getZ() == warsConfig.getInt("wars." + war + ".chunks." + chunk + ".z"))) {
												try {
													town = TownyUniverse.getInstance().getDataSource().getTown(warsConfig.getString("wars." + war + ".chunks." + chunk + ".owner"));
													if (!town.hasTownBlock(townblock) && town.getNation().getName().equals(attacker)) {
														townblock.setTown(town);
														TownyUniverse.getInstance().getDataSource().saveTownBlock(townblock);
													}
												} catch (NotRegisteredException e2) {
													e2.printStackTrace();
												}
											}
										}
									}
									
									war_ends_2_attacker_winner = war_ends_2_attacker_winner.replace("$attacker", attacker);
									war_ends_2_attacker_winner = war_ends_2_attacker_winner.replace("$defender", defender);
									
									for(Player p : Bukkit.getOnlinePlayers()) {
										p.sendMessage(war_ends_2_attacker_winner);
									}
									
								} catch (NotRegisteredException e1) {
									e1.printStackTrace();
								}
							}
							else if (attacker_score_percent < defender_score_percent){
								
								// Defender winner, chunks backs for defender
								
								warsConfig.set("wars." + war + ".winner", defender);
								warsConfig.set("wars." + war + ".loser", attacker);
								
								try {
									
									war_ends_2_defender_winner = plugin.getConfig().getString("events.war_ends_2_defender_winner");
									war_ends_2_defender_winner = war_ends_2_defender_winner.replace("&", "\u00a7");
									
									Nation LoserNation = TownyUniverse.getInstance().getDataSource().getNation(attacker);
									Nation WinnerNation = TownyUniverse.getInstance().getDataSource().getNation(defender);
									
									LoserNation.removeEnemy(WinnerNation);
									WinnerNation.removeEnemy(LoserNation);
									
									TownyUniverse.getInstance().getDataSource().saveNation(LoserNation);
									TownyUniverse.getInstance().getDataSource().saveNation(WinnerNation);
									
									for(String chunk : warsConfig.getConfigurationSection("wars." + war + ".chunks").getKeys(true)) {
										for (TownBlock townblock : TownyUniverse.getInstance().getDataSource().getAllTownBlocks()) {
											if ((townblock.getX() == warsConfig.getInt("wars." + war + ".chunks." + chunk + ".x"))&&(townblock.getZ() == warsConfig.getInt("wars." + war + ".chunks." + chunk + ".z"))) {
												try {
													town = TownyUniverse.getInstance().getDataSource().getTown(warsConfig.getString("wars." + war + ".chunks." + chunk + ".owner"));
													if (!town.hasTownBlock(townblock) && town.getNation().getName().equals(defender)) {
														townblock.setTown(town);
														TownyUniverse.getInstance().getDataSource().saveTownBlock(townblock);
													}
												} catch (NotRegisteredException e2) {
													e2.printStackTrace();
												}
											}
										}
									}
									
									war_ends_2_defender_winner = war_ends_2_defender_winner.replace("$attacker", attacker);
									war_ends_2_defender_winner = war_ends_2_defender_winner.replace("$defender", defender);
									
									for(Player p : Bukkit.getOnlinePlayers()) {
										p.sendMessage(war_ends_2_defender_winner);
									}
									
								} catch (NotRegisteredException e1) {
									e1.printStackTrace();
								}
							}
							else {
								
								// No winner, chunks backs for all
								
								for(String chunk : warsConfig.getConfigurationSection("wars." + war + ".chunks").getKeys(true)) {
									for (TownBlock townblock : TownyUniverse.getInstance().getDataSource().getAllTownBlocks()) {
										if ((townblock.getX() == warsConfig.getInt("wars." + war + ".chunks." + chunk + ".x"))&&(townblock.getZ() == warsConfig.getInt("wars." + war + ".chunks." + chunk + ".z"))) {
											try {
												town = TownyUniverse.getInstance().getDataSource().getTown(warsConfig.getString("wars." + war + ".chunks." + chunk + ".owner"));
												if (!town.hasTownBlock(townblock)) {
													townblock.setTown(town);
													TownyUniverse.getInstance().getDataSource().saveTownBlock(townblock);
												}
											} catch (NotRegisteredException e2) {
												e2.printStackTrace();
											}
										}
									}
								}
								try {
									
									war_ends_2_no_winner = plugin.getConfig().getString("events.war_ends_2_no_winner");
									war_ends_2_no_winner = war_ends_2_no_winner.replace("&", "\u00a7");
									
									Nation attackerNation = TownyUniverse.getInstance().getDataSource().getNation(attacker);
									Nation defenderNation = TownyUniverse.getInstance().getDataSource().getNation(defender);
									
									attackerNation.removeEnemy(defenderNation);
									defenderNation.removeEnemy(attackerNation);
									
									war_ends_2_no_winner = war_ends_2_no_winner.replace("$attacker", attacker);
									war_ends_2_no_winner = war_ends_2_no_winner.replace("$defender", defender);
									
									for(Player p : Bukkit.getOnlinePlayers()) {
										Resident res;
										try {
											res = TownyUniverse.getInstance().getDataSource().getResident(p.getName());
											if (res.hasNation()) {
												if (res.getTown().getNation().equals(attackerNation) || res.getTown().getNation().equals(defenderNation)) {		
													p.sendMessage(war_ends_2_no_winner);
												}
											}
										} catch (NotRegisteredException e1) {
											e1.printStackTrace();
										}
									}
								} catch (NotRegisteredException e1) {
									e1.printStackTrace();
								}
							}
						}
						
					}
				}
				else {
					String loser = warsConfig.getString("wars." + war + ".loser");
					String winner = warsConfig.getString("wars." + war + ".winner");
					
					try {
						Nation LoserNation = TownyUniverse.getInstance().getDataSource().getNation(loser);
						Nation WinnerNation = TownyUniverse.getInstance().getDataSource().getNation(winner);
						
						LoserNation.removeEnemy(WinnerNation);
						WinnerNation.removeEnemy(LoserNation);
	
						war_ends_2_no_winner = plugin.getConfig().getString("events.war_ends_2_no_winner");
						war_ends_2_no_winner = war_ends_2_no_winner.replace("&", "\u00a7");
						
						for(String chunk : warsConfig.getConfigurationSection("wars." + war + ".chunks").getKeys(true)) {
							for (TownBlock townblock : TownyUniverse.getInstance().getDataSource().getAllTownBlocks()) {
								if ((townblock.getX() == warsConfig.getInt("wars." + war + ".chunks." + chunk + ".x"))&&(townblock.getZ() == warsConfig.getInt("wars." + war + ".chunks." + chunk + ".z"))) {
									try {
										town = TownyUniverse.getInstance().getDataSource().getTown(warsConfig.getString("wars." + war + ".chunks." + chunk + ".owner"));
										if (!town.hasTownBlock(townblock) && town.getNation().getName().equals(winner)) {
											townblock.setTown(town);
											TownyUniverse.getInstance().getDataSource().saveTownBlock(townblock);
										}
									} catch (NotRegisteredException e2) {
										e2.printStackTrace();
									}
								}
							}
						}
						
						//TownyUniverse.getInstance().getDataSource().saveNation(LoserNation);
						
						war_ends_2_defender_winner = war_ends_2_defender_winner.replace("$attacker", loser);
						war_ends_2_defender_winner = war_ends_2_defender_winner.replace("$defender", winner);
						
						for(Player p : Bukkit.getOnlinePlayers()) {
							p.sendMessage(war_ends_2_defender_winner);
						}
					} catch (NotRegisteredException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		warsConfig.set("wars", null);
		
		try {
			warsConfig.save(wars);
		} catch (IOException e3) {
			e3.printStackTrace();
		}*/
	
	}


