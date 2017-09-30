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
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Player;
import model.Propriety;

/**
 *
 * @author lsitec205.ferreira
 */
public class Bankrupt {

    public static ArrayList<Propriety> BOARD;
    private final static int NUM_INTERACTIONS = 300;
    private static LinkedList<Integer> numRounds;
    private static float numTimeOuts = 0;
    private static float numVictoryCautious = 0;
    private static float numVictoryDemanding = 0;
    private static float numVictoryImpulsive = 0;
    private static float numVictoryRandom = 0;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        try {
            numRounds = new LinkedList<>();
            readData("gameConfig.txt");
            for(int i = 0; i < NUM_INTERACTIONS; i++){
                Game game = new Game(BOARD);
                game.start();
                saveResults(game);
            }
            printResults();
        } catch (IOException ex) {
            Logger.getLogger(Bankrupt.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    //Load board from file
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

    //When the game ends, store important data
    private static void saveResults(Game game) {
        if (game.getNumRounds()>= game.ROUND_LIMIT){
            numTimeOuts ++;
        }
        numRounds.add(game.getNumRounds());
        String winnerName;
        winnerName = game.getWinner().getName();
        switch(winnerName){
            case Player.CAUTIOUS_PLAYER_NAME:
                numVictoryCautious++;
                break;
            case Player.DEMANDING_PLAYER_NAME:
                numVictoryDemanding++;
                break;
            case Player.IMPULSIVE_PLAYER_NAME:
                numVictoryImpulsive++;
                break;
            case Player.RANDOM_PLAYER_NAME:
                numVictoryRandom++;
                break;
        }
           
    }

    private static void printResults() {
        System.out.println("-------------------------------------------");
        System.out.println("Partidas que terminam por timeout " + numTimeOuts);
        float sum = 0;
        for(int i : numRounds)
            sum += i;
        System.out.println("Média de turnos por partida "+ (sum/(float)numRounds.size()));
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
}
