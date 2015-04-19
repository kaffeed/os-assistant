/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package os.assistant;

/**
 *
 * @author Daniel
 */
public class SkillStats implements Comparable<String> {
    public int XPGained;
    public int XPRemaining;
    public int ActionsLeft;
    public String skillName;

    @Override
    public int compareTo(String o) {
        return skillName.compareTo(o);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof String)
            return skillName.equals(obj);
        return false;
    }

    @Override
    public int hashCode() {
        return skillName.hashCode(); 
    }
    
    
    
    
}
