/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author lsitec205.ferreira
 */
public class DemandingPlayer extends DefaultPlayer{

    @Override
    public String getName() {
        return DEMANDING_PLAYER_NAME;
    }

    @Override
    public boolean buy(Propriety propriety) {
        if(getBalance() >= propriety.getValue())
            if(propriety.getRent() > 50){
                pay(propriety.getValue());
                propriety.setOwner(this);
                return true;
            }
        return false;
    }
    
}
