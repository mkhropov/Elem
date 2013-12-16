/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package creature;

/**
 *
 * @author Михаил
 */

import world.*;
import java.util.Stack;


public class SmartElem extends Elem implements Worker {
    
    public SmartElem(World w, Block b){
        super(w, b);
    }
    
    @Override
    public void iterate(){
        Block t;
        System.out.printf("iterating\n");
        if (path == null)
            return;
        if (path.size() > 0){
            t = path.pop();
            if (canMove(b, t)){
                move(t);
                if (path.size()==0)
                    System.out.printf("Path walked succesfully\n");
            } else {
                System.out.printf("Incorrect path!\n");
                path.clear();
            }
        }
    }
}
