package rce;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;



public class Economy extends JavaPlugin {

	Logger log = getLogger();

	public void onEnable(){
		log.info("Your plugin has been enabled!");
		
		File config = new File(this.getDataFolder() + "/config.yml");
		if (!config.exists()) {
			getLogger().info("Creating new config file...");
			getConfig().options().copyDefaults(true);
			saveDefaultConfig();
		}

		this.getCommand("toggle_war").setExecutor(new Commands(this));
		this.getCommand("put_towns").setExecutor(new Commands(this));
		this.getCommand("put_nations").setExecutor(new Commands(this));
		
		this.getCommand("tbal").setExecutor(new Commands(this));
		this.getCommand("trmadd").setExecutor(new Commands(this));
		this.getCommand("tnpadd").setExecutor(new Commands(this));
		this.getCommand("tsetef").setExecutor(new Commands(this));
		this.getCommand("tef").setExecutor(new Commands(this));
		
		this.getCommand("nrmadd").setExecutor(new Commands(this));
		this.getCommand("tnew").setExecutor(new Commands(this));
		this.getCommand("nnew").setExecutor(new Commands(this));
		this.getCommand("toutpost").setExecutor(new Commands(this));
		this.getCommand("nbal").setExecutor(new Commands(this));
		this.getCommand("nsettax").setExecutor(new Commands(this));

		getServer().getPluginManager().registerEvents(new events(this), this);

	}
	
	public void onDisable(){
		log.info("Your plugin has been disabled.");
	}

	public void addTNP(String town, int tnp){

		File towns =  new File(getDataFolder() + "/towns.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(towns);
		
		int tnp_temp = 0;

		tnp_temp = customConfig.getInt("towns." + town + ".tnp") + tnp;
		customConfig.set("towns." + town + ".tnp", tnp_temp);
		try {
			customConfig.save(towns);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void addRM(String town, int rm){

		File towns =  new File(getDataFolder() + "/towns.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(towns);
		
		int rm_temp = 0;

		rm_temp = customConfig.getInt("towns." + town + ".rm") + rm;
		customConfig.set("towns." + town + ".rm", rm_temp);
		try {
			customConfig.save(towns);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public int getTNP(String town){
		File towns =  new File(getDataFolder() + "/towns.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(towns);
		
		return customConfig.getInt("towns." + town + ".tnp");
		
	}
	
	public int getRM(String town){
		File towns =  new File(getDataFolder() + "/towns.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(towns);
		
		return customConfig.getInt("towns." + town + ".rm");
		
	}

	public void setEF(String town, int ef){
		File towns =  new File(getDataFolder() + "/towns.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(towns);

		customConfig.set("towns." + town + ".ef", ef);
		try {
			customConfig.save(towns);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}

	public int getEF(String town){
		File towns =  new File(getDataFolder() + "/towns.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(towns);
		
		return customConfig.getInt("towns." + town + ".ef");
		
	}

	public int getTNPcost(){
		
		return getConfig().getInt("costs.tnp");
		
	}

	public int getRMcost(){
		
		return getConfig().getInt("costs.rm");
		
	}
	
	public float getPlayerScoreCoef(){
		
		return getConfig().getInt("costs.p_score_coef");
		
	}
	
	public float getBaseScore(){
		
		return getConfig().getInt("costs.base_score");
		
	}
	
	public int getNoNationDebuffNum(){
		
		return getConfig().getInt("costs.no_nation_debuff_num");
		
	}

	public boolean getEFCD(String town){
		File towns =  new File(getDataFolder() + "/towns.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(towns);
		
		return customConfig.getBoolean("towns." + town + ".ef_cd");
		
	}

	public void setEFCD(String town, boolean cd){
		File towns =  new File(getDataFolder() + "/towns.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(towns);
		
		customConfig.set("towns." + town + ".ef_cd", cd);
		try {
			customConfig.save(towns);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}
	
	/*public void addDebuff (Player p) {
		if(p.isOnline()) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 0));
			getLogger().info("Debuffs added");
		}
	}
	
	public void removeDebuff (Player p) {
		if(p.isOnline()) {
			if (p.hasPotionEffect(PotionEffectType.BLINDNESS)) {
				p.removePotionEffect(PotionEffectType.BLINDNESS);
				getLogger().info("Debuffs removed");
			}
		}
	}*/
	
	public void addEFeffect (Town town, Player p) {
		if(p.isOnline()) {
				switch (getEF(town.getName())) {
					case 1:
						p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0));
						p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, 0));
						p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));
						p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 0));
						break;
					case 2:
						p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, 0));
						break;
					case 3:
						break;
					case 4:
						p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1));
						break;
					case 5:
						p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1));
						p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0));
						p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, Integer.MAX_VALUE, 0));
						break;
				}
		}
	}
	
	public void removeEFeffect (Town town, Player p) {
		if(p.isOnline()) {
				switch (getEF(town.getName())) {
					case 1:
						if (p.hasPotionEffect(PotionEffectType.WEAKNESS) && p.hasPotionEffect(PotionEffectType.SLOW_DIGGING) &&
							p.hasPotionEffect(PotionEffectType.REGENERATION) && p.hasPotionEffect(PotionEffectType.SATURATION)) {
							p.removePotionEffect(PotionEffectType.WEAKNESS);
							p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
							p.removePotionEffect(PotionEffectType.REGENERATION);
							p.removePotionEffect(PotionEffectType.SATURATION);
						}
						break;
					case 2:
						if (p.hasPotionEffect(PotionEffectType.SLOW_DIGGING))
							p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
						break;
					case 3:
						break;
					case 4:
						if (p.hasPotionEffect(PotionEffectType.FAST_DIGGING))
							p.removePotionEffect(PotionEffectType.FAST_DIGGING);
						break;
					case 5:
						if (p.hasPotionEffect(PotionEffectType.FAST_DIGGING) && p.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE) && p.hasPotionEffect(PotionEffectType.HUNGER)) {
							p.removePotionEffect(PotionEffectType.FAST_DIGGING);
							p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
							p.removePotionEffect(PotionEffectType.HUNGER);
						}
						break;
				}
		}
	}

	public void addRM_N(String nation, int rm){

		File nations =  new File(getDataFolder() + "/nations.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(nations);
		
		int rm_temp = 0;

		rm_temp = customConfig.getInt("nations." + nation + ".rm") + rm;
		customConfig.set("nations." + nation + ".rm", rm_temp);
		try {
			customConfig.save(nations);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public int getRM_Ncost(){
		
		return getConfig().getInt("costs.nation_rm");
		
	}

	public int getRM_N(String nation){
		File nations =  new File(getDataFolder() + "/nations.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(nations);
		
		return customConfig.getInt("nations." + nation + ".rm");
		
	}
	
	public int getTax_N(String nation){
		File nations =  new File(getDataFolder() + "/nations.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(nations);
		
		return customConfig.getInt("nations." + nation + ".tax");
		
	}

	public void declareWar(Nation attacker, Nation defender, int casus_beli){
		
		File wars =  new File(getDataFolder() + "/war_declarations.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(wars);

		customConfig.createSection("war_declarations.declaration_from_" + attacker.getName() + ".attacker");
		customConfig.createSection("war_declarations.declaration_from_" + attacker.getName() + ".defender");
		customConfig.createSection("war_declarations.declaration_from_" + attacker.getName() + ".casus_beli");

		customConfig.set("war_declarations.declaration_from_" + attacker.getName() + ".attacker", attacker.getName());
		customConfig.set("war_declarations.declaration_from_" + attacker.getName() + ".defender", defender.getName());
		customConfig.set("war_declarations.declaration_from_" + attacker.getName() + ".casus_beli", casus_beli);
		
		try {
			customConfig.save(wars);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public boolean hasDeclaredWar(Nation attacker){
		
		File wars =  new File(getDataFolder() + "/war_declarations.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(wars);
		
		if (customConfig.contains("war_declarations.declaration_from_" + attacker.getName())) return false;
		else return true;
		
	}
	public int getCasusBeli(Nation attacker){
		
		File wars =  new File(getDataFolder() + "/war_declarations.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(wars);
		
		return customConfig.getInt("war_declarations.declaration_from_" + attacker.getName() + ".casus_beli");
		
	}
	public void clearDWconfig(){
		
		File wars =  new File(getDataFolder() + "/war_declarations.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(wars);
		
		customConfig.set("war_declarations", null);
		
		try {
			customConfig.save(wars);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}
	public String getAttacker(String n1, String n2){
		
		File wars =  new File(getDataFolder() + "/war_declarations.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(wars);
		
		for (String declaration : customConfig.getConfigurationSection("war_declarations").getKeys(true)) {
			if (customConfig.getString("war_declarations." + declaration + ".attacker").equals(n1) && customConfig.getString("war_declarations." + declaration + ".defender").equals(n2)) {
				return n1;
			}
			else if (customConfig.getString("war_declarations." + declaration + ".attacker").equals(n2) && customConfig.getString("war_declarations." + declaration + ".defender").equals(n1)) {
				return n2;
			}
		}
		return null;
		
	}
	public String getDefender(String n1, String n2){
		
		File wars =  new File(getDataFolder() + "/war_declarations.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(wars);
		
		for (String declaration : customConfig.getConfigurationSection("war_declarations").getKeys(true)) {
			if (customConfig.getString("war_declarations." + declaration + ".attacker").equals(n1) && customConfig.getString("war_declarations." + declaration + ".defender").equals(n2)) {
				return n2;
			}
			else if (customConfig.getString("war_declarations." + declaration + ".attacker").equals(n2) && customConfig.getString("war_declarations." + declaration + ".defender").equals(n1)) {
				return n1;
			}
		}
		return null;
		
	}
	public boolean hasWar(Nation attacker){
		
		File wars =  new File(getDataFolder() + "/wars.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(wars);
		
		if (customConfig.contains("wars.war_" + attacker.getName())) return false;
		else return true;
		
	}
	public String getAttackerOfWar(String n1, String n2){
		
		File wars =  new File(getDataFolder() + "/war_declarations.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(wars);
		
		if (customConfig.contains("wars.war_" + n1)) {
			if (customConfig.contains("wars.war_" + n1 + "." + n2))
			return n1;
		}
		else if (customConfig.contains("wars.war_" + n2)) {
			if (customConfig.contains("wars.war_" + n2 + "." + n1)){
				return n2;
			}
		}
		return null;
		
	}
	public int getCasusBeliOfWar(Nation attacker){
		
		File wars =  new File(getDataFolder() + "/wars.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(wars);
		
		return customConfig.getInt("wars.war_" + attacker.getName() + ".casus_beli");
		
	}
	public String getOwnerOfCatchedChunk(String attacker, int x, int z){
		
		File wars =  new File(getDataFolder() + "/wars.yml");
		FileConfiguration customConfig = YamlConfiguration.loadConfiguration(wars);
		
		for(String chunk_list : customConfig.getConfigurationSection("wars.war_" + attacker + ".chunks").getKeys(false)) {
			if ((customConfig.getInt("chunks." + chunk_list + ".x") == x)&&(customConfig.getInt("chunks." + chunk_list + ".z") == z)) {
				return customConfig.getString("chunks." + chunk_list + ".owner");
			}
		}
		return null;
		
	}
}