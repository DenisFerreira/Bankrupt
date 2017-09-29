/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankrupt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.CautiousPlayer;
import model.DemandingPlayer;
import model.ImpulsivePlayer;
import model.Player;
import model.Propriety;
import model.RandomPlayer;

/**
 *
 * @author lsitec205.ferreira
 */
public class Bankrupt {

    public static ArrayList<Propriety> BOARD;
    private final static int NUM_INTERACTIONS = 1;
    private static LinkedList<Integer> numRounds;
    private static int numTimeOuts = 0;
    private static int numVictoryCautious = 0;
    private static int numVictoryDemanding = 0;
    private static int numVictoryImpulsive = 0;
    private static int numVictoryRandom = 0;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        try {
            numRounds = new LinkedList<>();
            readData("/home/lsitec205.ferreira/Downloads/gameConfig.txt");
            //for(int i = 0; i < NUM_INTERACTIONS; i++){
                Game game = new Game(BOARD);
                game.start();
                saveResults(game);
            //}
            //printResults();
        } catch (IOException ex) {
            Logger.getLogger(Bankrupt.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    private static void readData(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));

            String sCurrentLine;
            BOARD = new ArrayList<>();

            while ((sCurrentLine = br.readLine()) != null) {
                    Scanner scanner = new Scanner(sCurrentLine);
                    Propriety mPropriety = new Propriety();
                    mPropriety.setValue(scanner.nextInt());
                    mPropriety.setRent(scanner.nextInt());
                    mPropriety.setPosition(BOARD.size());
                    BOARD.add(mPropriety);
            }

    }   

    private static void saveResults(Game game) {
        if (game.getNumRounds()>= game.ROUND_LIMIT){
            numTimeOuts ++;
        }else
        numRounds.add(game.getNumRounds());
        String winnerName;
        winnerName = game.getWinner().getName();
        switch(winnerName){
            case Player.CAUTIOUS_PLAYER_NAME:
                numVictoryCautious++;
            case Player.DEMANDING_PLAYER_NAME:
                numVictoryDemanding++;
            case Player.IMPULSIVE_PLAYER_NAME:
                numVictoryImpulsive++;
            case Player.RANDOM_PLAYER_NAME:
                numVictoryRandom++;
        }
           
    }

    private static void printResults() {
        System.out.println("Partidas que terminam por timeout " + numTimeOuts);
        int sum = 0;
        for(int i : numRounds)
            sum += i;
        System.out.println("Média de turnos por partida "+ (sum/numRounds.size()));
        System.out.println("Porcentagem vitórias "+ Player.CAUTIOUS_PLAYER_NAME + 
                ": "+ ((numVictoryCautious/NUM_INTERACTIONS)*100)+ "%");
        System.out.println("Porcentagem vitórias "+ Player.DEMANDING_PLAYER_NAME + 
                ": "+ ((numVictoryDemanding/NUM_INTERACTIONS)*100)+ "%");
        System.out.println("Porcentagem vitórias "+ Player.IMPULSIVE_PLAYER_NAME + 
                ": "+ ((numVictoryImpulsive/NUM_INTERACTIONS)*100)+ "%");
        System.out.println("Porcentagem vitórias "+ Player.RANDOM_PLAYER_NAME + 
                ": "+ ((numVictoryRandom/NUM_INTERACTIONS)*100)+ "%");
        
        System.out.print("Comportamento que mais vence é do ");
        if(numVictoryCautious > numVictoryDemanding && 
                numVictoryCautious > numVictoryImpulsive &&
                numVictoryCautious > numVictoryRandom) {
            System.out.println(Player.CAUTIOUS_PLAYER_NAME);
        }else if(numVictoryDemanding > numVictoryCautious && 
                numVictoryDemanding > numVictoryImpulsive &&
                numVictoryDemanding > numVictoryRandom) {
            System.out.println(Player.DEMANDING_PLAYER_NAME);
        }else if(numVictoryImpulsive > numVictoryCautious && 
                numVictoryImpulsive > numVictoryDemanding &&
                numVictoryImpulsive > numVictoryRandom) {
            System.out.println(Player.IMPULSIVE_PLAYER_NAME);
        }else if(numVictoryRandom > numVictoryCautious && 
                numVictoryRandom > numVictoryImpulsive &&
                numVictoryRandom > numVictoryDemanding) {
            System.out.println(Player.RANDOM_PLAYER_NAME);
        }
        
    }

    private static class Game {
        private ArrayList<Propriety> mBoard;
        private ArrayList<Player> mPlayers;
        private Iterator<Player> mIterator;
        private int round = 0;
        public final int ROUND_LIMIT = 1000;
        private Player mWinner;
        
        public int getNumRounds(){
            return round;
        }
        public Player getWinner(){
            return mWinner;
        }
        
        public Game(ArrayList board) {
            mBoard = board;
        }
        private void start() {
            initGame();
            Player mPlayer, owner;
            Propriety mPropriety;
            while(!gameEnded()){
                mPlayer = getNextPlayer();
                mPlayer.move(rollDice());
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
            
        

        private void initGame() {
            //SortPlayers
            mPlayers = new ArrayList<>();
            mPlayers.add(new CautiousPlayer());
            mPlayers.add(new DemandingPlayer());
            mPlayers.add(new ImpulsivePlayer());
            mPlayers.add(new RandomPlayer());
            Collections.shuffle(mPlayers);
            mIterator = mPlayers.iterator();
        }

        private boolean gameEnded() {
            round ++;
            if (round >= ROUND_LIMIT){
                System.out.println("Game ended by round limit");
                return true;
            }
            int numPlayers= 0;
            for(Player player: mPlayers)
                if (player.isPlaying()) numPlayers++;
            return numPlayers < 2;
        }

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
                    " with balance of "+ winner.getBalance() +
                    " and " + proprietiesValue + " in Proprieties");
            mWinner = winner;
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
    
}
