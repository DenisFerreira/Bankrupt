/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.math.MathContext;
import java.util.Random;

/**
 *
 * @author lsitec205.ferreira
 */
public class RandomPlayer extends DefaultPlayer{

    @Override
    public String getName() {
        return RANDOM_PLAYER_NAME;
    }

    @Override
    public boolean buy(Propriety propriety) {
        Random r = new Random();
        if((getBalance() >= propriety.getValue()) && r.nextBoolean()){
            pay(propriety.getValue());
            propriety.setOwner(this);
            return true;
        }
        return false;
    }
    
}
