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
public abstract class DefaultPlayer implements Player{

    private int mPosition = 0;
    private int mCoin = 300;
    
    @Override
    public int getBalance() {
        return mCoin;
    }
    
    @Override
    public void pay(int value) {
        mCoin -= value;
    }
    
    @Override
    public int getPosition() {
        return mPosition;
    }

    @Override
    public void pay(Player player, int value) {
        mCoin = mCoin - value;
        player.receive(value);
    }

    @Override
    public void move(int value) {
        mPosition += value;
        // Se deu uma volta no tabuleiro recebe $100
        if(mPosition >= 20) {
            mPosition = mPosition%20;
            receive(100);
        }
        
    }

    @Override
    public void receive(int value) {
        mCoin += value;
    }
    
    @Override
    public boolean isPlaying(){
        return getBalance()>0;
    }
    
}
