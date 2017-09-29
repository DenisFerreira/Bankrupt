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
public class ImpulsivePlayer extends DefaultPlayer{

    @Override
    public boolean buy(Propriety propriety) {
        if(getBalance() >= propriety.getValue()){
            pay(propriety.getValue());
            propriety.setOwner(this);
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return IMPULSIVE_PLAYER_NAME;    
    }
    
}
