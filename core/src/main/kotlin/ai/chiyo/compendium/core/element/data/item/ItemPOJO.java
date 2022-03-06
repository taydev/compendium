package ai.chiyo.compendium.core.element.data.item;

import java.util.ArrayList;

public class ItemPOJO {

  public class ReqAttuneTag{
    @JsonProperty("class")
    public String myclass;
    public boolean spellcasting;
  }

  public class Equal{
    public String fly;
  }

  public class ModifySpeed{
    public Equal equal;
  }

  public class Item{
    public String name;
    public String source;
    public int page;
    public String rarity;
    public Object reqAttune;
    public ArrayList<ReqAttuneTag> reqAttuneTags;
    public boolean wondrous;
    public String bonusSpellAttack;
    public String bonusSpellSaveDc;
    public ArrayList<String> focus;
    public ArrayList<Object> entries;
    public double weight;
    public String baseItem;
    public String type;
    public String weaponCategory;
    public ArrayList<String> property;
    public String dmg1;
    public String dmgType;
    public String bonusWeapon;
    public boolean weapon;
    public boolean grantsProficiency;
    public String tier;
    public ArrayList<String> lootTables;
    public boolean srd;
    public boolean basicRules;
    public int value;
    public String recharge;
    public int charges;
    public boolean tattoo;
    public ArrayList<String> resist;
    public String detail1;
    public boolean hasRefs;
    public int crew;
    public int vehAc;
    public int vehHp;
    public int vehSpeed;
    public int capPassenger;
    public int capCargo;
    public ArrayList<String> conditionImmune;
    public String dmg2;
    public ArrayList<String> attachedSpells;
    public boolean hasFluffImages;
    public Copy _copy;
    public ArrayList<AdditionalSource> additionalSources;
    public ArrayList<Object> additionalEntries;
    public ModifySpeed modifySpeed;
  }


  public class ItemGroup{
    public String name;
    public String source;
    public int page;
    public String rarity;
    public Object reqAttune;
    public boolean wondrous;
    public boolean tattoo;
    public ArrayList<Object> entries;
    public ArrayList<String> items;
    public String type;
    public String scfType;
    public ArrayList<String> focus;
    public String tier;
    public boolean hasFluffImages;
    public String baseItem;
    public boolean curse;
    public double weight;
    public int ac;
    public String strength;
    public boolean stealth;
    public ArrayList<String> lootTables;
    public boolean srd;
    public boolean basicRules;
    public ArrayList<String> attachedSpells;
    public ArrayList<ReqAttuneTag> reqAttuneTags;
    public String dmgType;
    public int charges;
    public String bonusAc;
    public ModifySpeed modifySpeed;
    public ArrayList<OtherSource> otherSources;
    public String weaponCategory;
    public ArrayList<String> property;
    public String dmg1;
    public String dmg2;
  }



}
