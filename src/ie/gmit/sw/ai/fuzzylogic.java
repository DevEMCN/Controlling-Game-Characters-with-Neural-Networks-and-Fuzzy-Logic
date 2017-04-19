package ie.gmit.sw.ai;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
public class fuzzylogic {

       public double fight(double weaponStrenght, double playerHealth, double spiderStrenght ){  	   
       double NewPlayerHealth=100;
    	System.out.println(spiderStrenght);
    	// Load from 'FCL' file
        String fileName = "fcl/gameLogic.fcl";
        
        FIS fis = FIS.load(fileName,true);

        FunctionBlock fb = fis.getFunctionBlock("gameLogic");

        // Set inputs
        fis.setVariable("spider",spiderStrenght ); //Apply a value to a variable
        fis.setVariable("weapon",weaponStrenght ); //Apply a value to a variable
        // Evaluate
        fis.evaluate();
        Double damage = fis.getVariable("damage").getValue();
        System.out.println("Dame: "+damage);
        NewPlayerHealth=NewPlayerHealth-damage;
		System.out.println("new Player Health: " + NewPlayerHealth);
		return 0;
       } 
}