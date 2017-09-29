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
public interface Player {
    public String getName();
    public int getPosition();
    public int getBalance();
    public boolean buy(Propriety propriety);
    public void pay(Player player, int value);
    public void pay(int value);
    public void move(int value);
    public boolean isPlaying();
    public void receive(int value);
    public final static String CAUTIOUS_PLAYER_NAME= "Cauteloso";
    public final static String IMPULSIVE_PLAYER_NAME= "Impulsivo";
    public final static String RANDOM_PLAYER_NAME= "Aleatório";
    public final static String DEMANDING_PLAYER_NAME= "Exigente";
}
