/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankrupt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import model.CautiousPlayer;
import model.DemandingPlayer;
import model.ImpulsivePlayer;
import model.Player;
import model.Propriety;
import model.RandomPlayer;

/**
 *
 * @author Denis
 */
public class Game {
        private final ArrayList<Propriety> mBoard;
        private ArrayList<Player> mPlayers;
        private Iterator<Player> mIterator;
        private int round = 0;
        public final int ROUND_LIMIT = 1000;
        private Player mWinner;
        
        public Game(ArrayList board) {
            mBoard = board;
        }
        
        public int getNumRounds(){
            return round;
        }
        public Player getWinner(){
            return mWinner;
        }
        
        //Game Logic loop
        public void start() {
            initGame();
            Player mPlayer, owner;
            Propriety mPropriety;
            while(!gameEnded()){
                mPlayer = getNextPlayer();
                mPlayer.move(rollDice());
                System.out.println(mPlayer.getName() + " moved to " + mPlayer.getPosition());
                mPropriety = mBoard.get(mPlayer.getPosition());
                owner = mPropriety.getOwner();
                if(owner == null){
                    if(mPlayer.buy(mPropriety))
                        System.out.println(mPlayer.getName() + " bought propriety #"+ mPropriety.getPosition());
                }else if(owner != mPlayer){
                    mPlayer.pay(owner, mPropriety.getRent());
                    System.out.println(mPlayer.getName() + " paid rent $"
                            +mPropriety.getRent()+" propriety #"
                            + mPropriety.getPosition()+" to "+owner.getName());
                }
                if(!mPlayer.isPlaying())
                    doBankrupt(mPlayer);
            }
            printResults();
        }
            
        //Reset all proprieties
        private void initBoard(ArrayList<Propriety> board) {
            for(Propriety propriety: board) 
                propriety.setOwner(null);
        }

        //instantiate the players 
        private void initGame() {
            //SortPlayers
            mPlayers = new ArrayList<>();
            mPlayers.add(new CautiousPlayer());
            mPlayers.add(new DemandingPlayer());
            mPlayers.add(new ImpulsivePlayer());
            mPlayers.add(new RandomPlayer());
            Collections.shuffle(mPlayers);
            mIterator = mPlayers.iterator();
            initBoard(mBoard);
        }
        
        //Verify if the game has ended by round limit or by elimination of other players
        private boolean gameEnded() {
            round++;
            if (round >= ROUND_LIMIT){
                System.out.println("Game ended by round limit");
                return true;
            }
            int numPlayers= 0;
            for(Player player: mPlayers)
                if (player.isPlaying()) numPlayers++;
            return numPlayers < 2;
        }

        //Return the next valid player
        //Requires that at least one player is playing in this moment,
        //gameEnded() ensures this requirement
        private Player getNextPlayer() {
            Player player = null;
            do{
                if(!mIterator.hasNext())
                    mIterator = mPlayers.iterator();
                player = mIterator.next();  
            }
            while(!player.isPlaying());
            return player;
            
        }

        private int rollDice() {
            int result = Math.abs(new Random().nextInt());
            result = (result % 6) + 1;
            return result;
        }

        private void printResults() {
            System.out.println("-------------------------------------------");
            Player winner = mPlayers.get(0);
            for(Player player : mPlayers){
                System.out.println(player.getName() + " balance "+ player.getBalance());
                if(player.getBalance()> winner.getBalance())
                    winner = player;
            }
            int proprietiesValue = 0;
            for(Propriety propriety : mBoard){
                if(propriety.getOwner()==winner)
                    proprietiesValue += propriety.getValue();
            }
            System.out.println("The winner is "+ winner.getName() + 
                    " after "+round+" rounds, with balance of "+ winner.getBalance() +
                    " and " + proprietiesValue + " in Proprieties");
            mWinner = winner;
            System.out.println("-------------------------------------------");
        }

        private void doBankrupt(Player mPlayer) {
            for(Propriety propriety : mBoard){
                if(propriety.getOwner() == mPlayer){
                    propriety.setOwner(null);
                    System.out.println("propriety #"+ propriety.getPosition()+" is available");
                }
            }
            System.out.println("Player "+mPlayer.getName() + " out of the game");
        }
    }
    
