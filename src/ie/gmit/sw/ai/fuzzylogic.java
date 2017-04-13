package ie.gmit.sw.ai;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
public class fuzzylogic {

       public double fight(double weaponStrenght, double playerHealth, double spider ){  	   
       
    	
    	// Load from 'FCL' file
        String fileName = "fcl/gameLogic.fcl";
        
        FIS fis = FIS.load(fileName,true);

        FunctionBlock fb = fis.getFunctionBlock("gameLogic");

        // Set inputs
        fis.setVariable("weapon",weaponStrenght ); //Apply a value to a variable
        fis.setVariable("playerLife",playerHealth ); //Apply a value to a variable

        // Evaluate
        fis.evaluate();
        Double damage = fis.getVariable("damage").getValue();
        System.out.println("Dame: "+damage);
		double newPlayerHealth=playerHealth-damage;
		System.out.println("new Player Health: " + newPlayerHealth);
		return 0;
       } 
}