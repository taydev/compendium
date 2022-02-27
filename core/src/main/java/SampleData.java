import java.util.ArrayList;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */
public class Hd{
  public int number;
  public int faces;
}

public class Choose{
  public ArrayList<String> from;
  public int count;
}

public class Skill{
  public Choose choose;
  public int any;
}

public class StartingProficiencies{
  public ArrayList<Object> armor;
  public ArrayList<Object> weapons;
  public ArrayList<String> tools;
  public ArrayList<Skill> skills;
}

public class DefaultData{
  public ArrayList<Object> a;
  public ArrayList<Object> b;
  public ArrayList<Object> _;
  public ArrayList<Object> c;
}

public class StartingEquipment{
  public boolean additionalFromBackground;
  @JsonProperty("default")
  public ArrayList<String> mydefault;
  public ArrayList<DefaultData> defaultData;
  public String goldAlternative;
}

public class ClassTableGroup{
  public ArrayList<String> colLabels;
  public ArrayList<ArrayList<Object>> rows;
  public String title;
  public ArrayList<ArrayList<int>> rowsSpellProgression;
}

public class OptionalfeatureProgression{
  public String name;
  public ArrayList<String> featureType;
  public Object progression;
  public Required required;
}

public class Or{
  public int str;
  public int dex;
}

public class Requirements{
  @JsonProperty("int")
  public int myint;
  public int str;
  public int cha;
  public int wis;
  public ArrayList<Or> or;
  public int dex;
  public ArrayList<String> entries;
}

public class ProficienciesGained{
  public ArrayList<Object> armor;
  public ArrayList<String> tools;
  public ArrayList<String> weapons;
  public ArrayList<Skill> skills;
}

public class Multiclassing{
  public Requirements requirements;
  public ProficienciesGained proficienciesGained;
}

public class Fluff{
  public String name;
  public int page;
  public String source;
  public String type;
  public ArrayList<Object> entries;
}

public class OtherSource{
  public String source;
  public int page;
}

public class 10{
public String choose;
    }

public class 14{
public String choose;
    }

public class 18{
public String choose;
    }

public class Known{
  @JsonProperty("10")
  public ArrayList<_10> _10;
  @JsonProperty("14")
  public ArrayList<_14> _14;
  @JsonProperty("18")
  public ArrayList<_18> _18;
  @JsonProperty("6")
  public ArrayList<_6> _6;
  @JsonProperty("3")
  public ArrayList<String> _3;
  @JsonProperty("1")
  public Object _1;
  @JsonProperty("2")
  public ArrayList<String> _2;
  @JsonProperty("5")
  public ArrayList<String> _5;
  @JsonProperty("7")
  public ArrayList<String> _7;
  @JsonProperty("9")
  public ArrayList<String> _9;
  @JsonProperty("13")
  public ArrayList<String> _13;
  @JsonProperty("17")
  public ArrayList<String> _17;
}

public class AdditionalSpell{
  public String name;
  public Known known;
  public Prepared prepared;
  public Innate innate;
  public Expanded expanded;
}

public class _11{
  @JsonProperty("6")
  public int _6;
}

public class _13{
  @JsonProperty("7")
  public int _7;
}

public class _15{
  @JsonProperty("8")
  public int _8;
}

public class _17{
  @JsonProperty("9")
  public int _9;
}

public class SpellsKnownProgressionFixedByLevel{
  @JsonProperty("11")
  public _11 _11;
  @JsonProperty("13")
  public _13 _13;
  @JsonProperty("15")
  public _15 _15;
  @JsonProperty("17")
  public _17 _17;
}

public class Class{
  public String name;
  public String source;
  public int page;
  public boolean isReprinted;
  public Hd hd;
  public ArrayList<String> proficiency;
  public String spellcastingAbility;
  public String casterProgression;
  public ArrayList<int> spellsKnownProgression;
  public StartingProficiencies startingProficiencies;
  public StartingEquipment startingEquipment;
  public ArrayList<ClassTableGroup> classTableGroups;
  public ArrayList<Object> classFeatures;
  public String subclassTitle;
  public String preparedSpells;
  public ArrayList<int> cantripProgression;
  public ArrayList<OptionalfeatureProgression> optionalfeatureProgression;
  public Multiclassing multiclassing;
  public ArrayList<Fluff> fluff;
  public ArrayList<OtherSource> otherSources;
  public boolean srd;
  public ArrayList<AdditionalSpell> additionalSpells;
  public boolean basicRules;
  public Requirements requirements;
  public boolean isSidekick;
  public SpellsKnownProgressionFixedByLevel spellsKnownProgressionFixedByLevel;
  public ArrayList<int> spellsKnownProgressionFixed;
  public boolean spellsKnownProgressionFixedAllowLowerLevel;
}

public class Prepared{
  @JsonProperty("3")
  public ArrayList<String> _3;
  @JsonProperty("5")
  public ArrayList<String> _5;
  @JsonProperty("9")
  public ArrayList<String> _9;
  @JsonProperty("13")
  public ArrayList<String> _13;
  @JsonProperty("17")
  public ArrayList<String> _17;
  @JsonProperty("1")
  public ArrayList<String> _1;
  @JsonProperty("7")
  public ArrayList<String> _7;
  @JsonProperty("2")
  public ArrayList<String> _2;
}

public class _3{
  public ArrayList<String> ritual;
}

public class Innate{
  @JsonProperty("3")
  public _3 _3;
  @JsonProperty("10")
  public Object _10;
  @JsonProperty("6")
  public ArrayList<String> _6;
}

public class 6{
public String choose;
    }

public class Expanded{
  public ArrayList<String> s1;
  public ArrayList<String> s2;
  public ArrayList<String> s3;
  public ArrayList<String> s4;
  public ArrayList<String> s5;
  @JsonProperty("9")
  public ArrayList<String> _9;
}

public class Preserve{
  public boolean page;
}

public class Copy{
  public String name;
  public String shortName;
  public String source;
  public String className;
  public String classSource;
  public Preserve _preserve;
}

public class Progression{
  @JsonProperty("3")
  public int _3;
  @JsonProperty("7")
  public int _7;
  @JsonProperty("10")
  public int _10;
  @JsonProperty("15")
  public int _15;
  @JsonProperty("18")
  public int _18;
  @JsonProperty("6")
  public int _6;
  @JsonProperty("11")
  public int _11;
  @JsonProperty("17")
  public int _17;
}

public class Required{
  @JsonProperty("3")
  public ArrayList<String> _3;
}

public class Subclass2{
  public String name;
  public String source;
}

public class SubclassTableGroup{
  public ArrayList<Subclass> subclasses;
  public ArrayList<String> colLabels;
  public ArrayList<ArrayList<int>> rows;
  public String title;
  public ArrayList<ArrayList<int>> rowsSpellProgression;
}

public class Subclass{
  public String name;
  public String shortName;
  public String source;
  public String className;
  public String classSource;
  public int page;
  public ArrayList<String> subclassFeatures;
  public boolean isReprinted;
  public ArrayList<AdditionalSpell> additionalSpells;
  public ArrayList<OtherSource> otherSources;
  public boolean srd;
  public String spellcastingAbility;
  public Copy _copy;
  public boolean basicRules;
  public ArrayList<OptionalfeatureProgression> optionalfeatureProgression;
  public String casterProgression;
  public ArrayList<int> cantripProgression;
  public ArrayList<int> spellsKnownProgression;
  public ArrayList<SubclassTableGroup> subclassTableGroups;
}

public class Consumes{
  public String name;
  public int amount;
}

public class ClassFeature{
  public String name;
  public String source;
  public int page;
  public ArrayList<OtherSource> otherSources;
  public String className;
  public String classSource;
  public int level;
  public ArrayList<Object> entries;
  public int header;
  public boolean isClassFeatureVariant;
  public boolean srd;
  public boolean basicRules;
  public Consumes consumes;
  public String type;
}

public class SubclassFeature{
  public String name;
  public String source;
  public int page;
  public ArrayList<OtherSource> otherSources;
  public String className;
  public String classSource;
  public String subclassShortName;
  public String subclassSource;
  public int level;
  public ArrayList<Object> entries;
  public int header;
  public String type;
  public boolean srd;
  public Consumes consumes;
  public boolean isClassFeatureVariant;
  public boolean basicRules;
  public boolean isGainAtNextFeatureLevel;
}

public class Dependencies{
  public ArrayList<String> subclass;
}

public class Meta{
  public Dependencies dependencies;
}

public class Root{
  @JsonProperty("class")
  public ArrayList<Class> myclass;
  public ArrayList<Subclass> subclass;
  public ArrayList<ClassFeature> classFeature;
  public ArrayList<SubclassFeature> subclassFeature;
  public Meta _meta;
}

