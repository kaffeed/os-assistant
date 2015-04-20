/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package os.assistant;

import java.awt.List;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Get's RS Player Information
 *
 * @author Daniel
 */
public class Player {

    private Object handle;
    private int[] previous_skills;
    private int[] skills;
    private ArrayList<SkillStats> skillsStats;
    private String[] skill_names = new String[]
    {    
        "Attack",
        "Defence",
        "Strength",
        "Hitpoints",
        "Ranged",
        "Prayer",
        "Magic",
        "Cooking",
        "Woodcutting",
        "Fletching",
        "Fishing",
        "Firemaking",
        "Crafting",
        "Smithing",
        "Mining",
        "Herblore",
        "Agility",
        "Thieving",
        "Slayer",
        "Farming",
        "Runecraft",
        "Hunter",
        "Construction",
    };

    public Player(Object handle) {
        skillsStats = new ArrayList<>();
        try {
            this.handle = handle;
            Field ht = handle.getClass().getDeclaredField("ht");
            ht.setAccessible(true);
            skills = (int[]) ht.get(handle);
            previous_skills = Arrays.copyOf(skills, skills.length);
        } catch (Exception e) {
            skills = new int[28];
            this.handle = null;
        }
    }

    public int getXPOf(String skillName) {

        for (int i = 0; i < skill_names.length; i++) {
            if (skillName.equals(skill_names[i])) {
                return skills[i];
            }
        }
        return 0;
    }

    private boolean alreadyInList(int index) {
        for (SkillStats stat : skillsStats) {
            if (stat.skillName.equals(skill_names[index])) {
                return true;
            }
        }
        return false;
    }

    private int indexOf(String s) {
        int index = 0;
        for (SkillStats stat : skillsStats) {
            if (stat.skillName.equals(s)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    // If zero this means at the current tick no exp was gained from skill X.
    public int getXPDropOf(int i) {

        int diff = skills[i] - previous_skills[i];
        if (!alreadyInList(i)) {
            SkillStats stat = new SkillStats();
            stat.skillName = skill_names[i];
            stat.XPGained = diff;
            stat.XPRemaining = getNumberOfActionsToNextLevel(xPToLevel(getXPOf(skill_names[i])), getXPOf(skill_names[i]), 1);
            if (diff > 0) {
                stat.ActionsLeft = stat.XPRemaining / diff;
            }
            if (diff > 0 && previous_skills[i] > 0) {
                skillsStats.add(stat);
            }

        } else {
            SkillStats stat = skillsStats.get(indexOf(skill_names[i]));
            stat.XPGained += getXPDropOf(skill_names[i]);
            stat.XPRemaining = getNumberOfActionsToNextLevel(xPToLevel(getXPOf(skill_names[i])), getXPOf(skill_names[i]), 1);
            if (diff > 0) {
                stat.ActionsLeft = stat.XPRemaining / diff;
            }

        }

        return diff;
    }

    public int getXPDropOf(String skillName) {

        for (int i = 0; i < skill_names.length; i++) {
            if (skillName.equals(skill_names[i])) {
                int diff = skills[i] - previous_skills[i];
                if (!alreadyInList(i)) {
                    SkillStats stat = new SkillStats();
                    stat.skillName = skill_names[i];
                    stat.XPGained = diff;
                    stat.XPRemaining = getNumberOfActionsToNextLevel(xPToLevel(getXPOf(skillName)), getXPOf(skillName), 1);
                    if (diff > 0) {
                        stat.ActionsLeft = stat.XPRemaining / diff;
                    }
                    if (diff > 0 && previous_skills[i] > 0) {
                        skillsStats.add(stat);
                    }
                } else {
                    int index;
                    if ((index = indexOf(skillName)) > 0) {
                        SkillStats stat = skillsStats.get(index);
                        stat.XPGained += getXPOf(skillName);
                        stat.XPRemaining = getNumberOfActionsToNextLevel(xPToLevel(getXPOf(skillName)), getXPOf(skillName), 1);
                        if (diff > 0) {
                            stat.ActionsLeft = stat.XPRemaining / diff;
                        }
                    }

                }

                return diff;
            }
        }
        return 0;
    }

    // Returns current skilled being trained and important information
    public java.util.List<SkillStats> getActiveSkills() {
        return (java.util.List<SkillStats>) skillsStats;
    }

    public void updatePlayerInformation() {
        try {
            Field ht = handle.getClass().getDeclaredField("ht");
            ht.setAccessible(true);
            previous_skills = Arrays.copyOf(skills, skills.length);
            skills = (int[]) ht.get(handle);
        } catch (Exception e) {
            skills = new int[28];
        }
    }

    public static int experienceToNextLevel(int level) {
        return (int)(75 * Math.pow(2, level/7.0));
    }

    public static int xPToLevel(int xp) {
        for (int i = 1; i < 99; i++) {
            int x = experienceAtLevel(i);
            int y = experienceAtLevel(i+1);
            if (x >= xp && y > xp) {
                return i;
            }
        }
        return 0;
    }

    public static int experienceAtLevel(int level) {
        int xp = 0;
        for (int i = 1; i <= level; i++) {
            xp += experienceToNextLevel(i);
        }
        return xp;
    }

    public static int getNumberOfActionsToNextLevel(int level, int totalXP, int experienceGained) {
        return (int) ((experienceAtLevel(level + 1) - totalXP) / (double) experienceGained);
    }
    
    //TEST
    public static void main(String[] args) {
        System.out.println(experienceAtLevel(77+1) - 1516955);
    }
}
