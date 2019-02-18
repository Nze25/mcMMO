package com.gmail.nossr50.commands.skills;

import com.gmail.nossr50.core.data.UserManager;
import com.gmail.nossr50.core.locale.LocaleLoader;
import com.gmail.nossr50.core.skills.PrimarySkillType;
import com.gmail.nossr50.core.skills.SubSkillType;
import com.gmail.nossr50.core.util.Permissions;
import com.gmail.nossr50.core.util.TextComponentFactory;
import com.gmail.nossr50.core.util.skills.RankUtils;
import com.gmail.nossr50.core.util.skills.SkillActivationType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class UnarmedCommand extends SkillCommand {
    private String berserkLength;
    private String berserkLengthEndurance;
    private String deflectChance;
    private String deflectChanceLucky;
    private String disarmChance;
    private String disarmChanceLucky;
    private String ironGripChance;
    private String ironGripChanceLucky;
    private double ironArmBonus;

    private boolean canBerserk;
    private boolean canDisarm;
    private boolean canIronArm;
    private boolean canDeflect;
    private boolean canIronGrip;

    public UnarmedCommand() {
        super(PrimarySkillType.UNARMED);
    }

    @Override
    protected void dataCalculations(Player player, float skillValue) {
        // UNARMED_ARROW_DEFLECT
        if (canDeflect) {
            String[] deflectStrings = getAbilityDisplayValues(SkillActivationType.RANDOM_LINEAR_100_SCALE_WITH_CAP, player, SubSkillType.UNARMED_ARROW_DEFLECT);
            ;
            deflectChance = deflectStrings[0];
            deflectChanceLucky = deflectStrings[1];
        }

        // BERSERK
        if (canBerserk) {
            String[] berserkStrings = calculateLengthDisplayValues(player, skillValue);
            berserkLength = berserkStrings[0];
            berserkLengthEndurance = berserkStrings[1];
        }

        // UNARMED_DISARM
        if (canDisarm) {
            String[] disarmStrings = getAbilityDisplayValues(SkillActivationType.RANDOM_LINEAR_100_SCALE_WITH_CAP, player, SubSkillType.UNARMED_DISARM);
            ;
            disarmChance = disarmStrings[0];
            disarmChanceLucky = disarmStrings[1];
        }

        // IRON ARM
        if (canIronArm) {
            ironArmBonus = UserManager.getPlayer(player).getUnarmedManager().getIronArmDamage();
        }

        // IRON GRIP
        if (canIronGrip) {
            String[] ironGripStrings = getAbilityDisplayValues(SkillActivationType.RANDOM_LINEAR_100_SCALE_WITH_CAP, player, SubSkillType.UNARMED_IRON_GRIP);
            ironGripChance = ironGripStrings[0];
            ironGripChanceLucky = ironGripStrings[1];
        }
    }

    @Override
    protected void permissionsCheck(Player player) {
        canBerserk = RankUtils.hasUnlockedSubskill(player, SubSkillType.UNARMED_BERSERK) && Permissions.berserk(player);
        canIronArm = canUseSubskill(player, SubSkillType.UNARMED_IRON_ARM_STYLE);
        canDeflect = canUseSubskill(player, SubSkillType.UNARMED_ARROW_DEFLECT);
        canDisarm = canUseSubskill(player, SubSkillType.UNARMED_DISARM);
        canIronGrip = canUseSubskill(player, SubSkillType.UNARMED_IRON_GRIP);
        // TODO: Apparently we forgot about block cracker?
    }

    @Override
    protected List<String> statsDisplay(Player player, float skillValue, boolean hasEndurance, boolean isLucky) {
        List<String> messages = new ArrayList<String>();

        if (canDeflect) {
            messages.add(getStatMessage(SubSkillType.UNARMED_ARROW_DEFLECT, deflectChance)
                    + (isLucky ? LocaleLoader.getString("Perks.Lucky.Bonus", deflectChanceLucky) : ""));
            //messages.add(LocaleLoader.getString("Unarmed.Ability.Chance.ArrowDeflect", deflectChance) + (isLucky ? LocaleLoader.getString("Perks.Lucky.Bonus", deflectChanceLucky) : ""));
        }

        if (canBerserk) {
            messages.add(getStatMessage(SubSkillType.UNARMED_BERSERK, berserkLength)
                    + (hasEndurance ? LocaleLoader.getString("Perks.ActivationTime.Bonus", berserkLengthEndurance) : ""));
            //messages.add(LocaleLoader.getString("Unarmed.Ability.Berserk.Length", berserkLength) + (hasEndurance ? LocaleLoader.getString("Perks.ActivationTime.Bonus", berserkLengthEndurance) : ""));
        }

        if (canDisarm) {
            messages.add(getStatMessage(SubSkillType.UNARMED_DISARM, disarmChance)
                    + (isLucky ? LocaleLoader.getString("Perks.Lucky.Bonus", disarmChanceLucky) : ""));
            //messages.add(LocaleLoader.getString("Unarmed.Ability.Chance.Disarm", disarmChance) + (isLucky ? LocaleLoader.getString("Perks.Lucky.Bonus", disarmChanceLucky) : ""));
        }

        if (canIronArm) {
            messages.add(LocaleLoader.getString("Ability.Generic.Template", LocaleLoader.getString("Unarmed.Ability.Bonus.0"), LocaleLoader.getString("Unarmed.Ability.Bonus.1", ironArmBonus)));
        }

        if (canIronGrip) {
            messages.add(getStatMessage(SubSkillType.UNARMED_IRON_GRIP, ironGripChance)
                    + (isLucky ? LocaleLoader.getString("Perks.Lucky.Bonus", ironGripChanceLucky) : ""));
            //messages.add(LocaleLoader.getString("Unarmed.Ability.Chance.IronGrip", ironGripChance) + (isLucky ? LocaleLoader.getString("Perks.Lucky.Bonus", ironGripChanceLucky) : ""));
        }

        return messages;
    }

    @Override
    protected List<TextComponent> getTextComponents(Player player) {
        List<TextComponent> textComponents = new ArrayList<>();

        TextComponentFactory.getSubSkillTextComponents(player, textComponents, PrimarySkillType.UNARMED);

        return textComponents;
    }
}