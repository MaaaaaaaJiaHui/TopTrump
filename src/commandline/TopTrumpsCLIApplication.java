package commandline;

import java.io.*;
import java.math.*;
import java.util.*;

import game.gameCard;
import game.gameData;
import game.gameLog;
import game.gameRule;
//import pk1.loadCard;
/**
 * Top Trumps command line application
 */
public class TopTrumpsCLIApplication {

	//static Map <Integer,String[]> arrs=new HashMap<Integer,String[]>();
	static String realcard[][];
	//static String attrs[];
	static int num = 0;
	static int aiplayers = 4;
	static int allplayer = aiplayers+1;
	//static int cards[][];
	//static ArrayList[] cards;// = new ArrayList[allplayer];
	static Map <Integer,String> players=new HashMap<Integer,String>();
	//static List commonCards = new ArrayList();
	//static List indexes = new ArrayList();
	static int com;
	static int pernum;
	static int lastwin;
	static int isai=0;
	//static int end=0;
	static int draw=0;
	static boolean log = false;
	/*
	public static int getRand(int leng){
		return (int) Math.floor(Math.random()*leng);
	}*/
	public static void menu(){

		System.out.println("Do you want to see past results or play a game?");
		System.out.println("   1: Print Game Statistics");
		System.out.println("   2: Play game");
	}
	public static void print(String str){
		System.out.println(str);
	}
	public static int showChoose(Scanner sc, int nowround){
		int choose = 1;
		if(nowround==0){
			//me
			print("It is your turn to select a category, the categories are:");
			for(int i=1;i<gameCard.attrSize();i++){
				print("   "+i+": "+gameCard.attrGet(i));
			}
			System.out.print("Enter the number for your attribute: ");
			choose = sc.nextInt();
		}else{
			choose = gameRule.showChoose(nowround);
		}
		choose = choose>gameCard.attrSize()?1:choose;
		return choose;
	}
	/*  
	public static int getrand(){
		int rand = 0;
		while(rand>0){
			rand = (int) Math.floor(Math.random()*players.size());
		}
		return rand;
	}*/
	public static void commonLog() throws IOException{
		
		gameLog.write("Common cards have:\r\n");
		//StringBuffer aBuffer = new StringBuffer("");
		for(int i =0; i<gameRule.commonCards.size();i++){
			//aBuffer.append(gameCard.get(gameRule.commonCards.get(i))[0]+" ");
			gameLog.write(gameCard.get(gameRule.commonCards.get(i))[0]);
			if(i%4==0){
				gameLog.write("\r\n");
			//	aBuffer.append("\r\n");
			}
		}
		//gameLog.write(aBuffer.toString());
		gameLog.write("\r\n----------\r\n");
	}
	public static void recoedLog(String user, String[] card) throws IOException{
		String str = "";
		for(int i=1;i<gameCard.attrSize();i++){
			str+=gameCard.attrGet(i)+": "+card[i]+"\r\n";
		}
		gameLog.write(user+"'s Card used: "+card[0]+str);
		gameLog.write("\r\n----------\r\n");
	}
	/**
	 * This main method is called by TopTrumps.java when the user specifies that they want to run in
	 * command line mode. The contents of args[0] is whether we should write game logs to a file.
 	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		boolean writeGameLogsToFile = false; // Should we write game logs to file?
		if (args[0].equalsIgnoreCase("true")) writeGameLogsToFile=true; // Command line selection
		
		log = writeGameLogsToFile;
		// State
		boolean userWantsToQuit = false; // flag to check whether the user wants to quit the application
		/*	
		// Loop until the user wants to exit the game
		while (!userWantsToQuit) {

			// ----------------------------------------------------
			// Add your game logic here based on the requirements
			// ----------------------------------------------------
			
			userWantsToQuit=true; // use this when the user wants to exit the game
			
		}
		*/
		try {

			gameCard.load("StarCitizenDeck.txt");
			//arrs = 
			gameCard.getCards();
			//attrs = 
			gameCard.getAttrs();
			if(writeGameLogsToFile){
				gameLog.init();
				gameLog.write("All cards\r\n");
			    StringBuffer sBuffer = new StringBuffer("");
				for(int i=0;i<gameCard.size();i++){
					sBuffer.append(gameCard.get(i)[0]+" ");
					if(i%4==0 && i!=0){
						sBuffer.append("\r\n");
					}
					
				}
				gameLog.write(sBuffer.toString());
				gameLog.write("\r\n----------\r\n");
			}
			
			num=0;
			num = gameCard.keySize();
			/*
			for(String[] v:arrs){
				if(v[0] == null){
					break;
				}
				indexes.add(num);
				num++;
				for(String vv:v){
					System.out.println(vv);
				}
			}
			realcard = new String[num][gameCard.attrSize()];
			num=0;
		    //  indexes = new int[num];
			for(String[] v:arrs){
				if(v[0] == null){
					break;
				}
				realcard[num]=v;
				num++;
			}*/
		    gameRule.init(aiplayers,num,log);
		    //cards = new int[allplayer][num];
			//System.out.println("Total"+num);
			//System.out.println("Common pil"+com);
			//System.out.println("nums:"+gameCard.attrSize());
		    /*
			for(int i=0;i<allplayer;i++){
				for(int j=0;j<cards[i].size();j++){
					String a = cards[i].get(j).toString();
					//System.out.println(gameCard.get(a)[0]);
				}
			}*/
		//System.out.println("--------------------");
		//System.out.println("--- Top Trumps   ---");
		//System.out.println("--------------------");
		int round=1;
		int nowround;
		int score[];
		int attr_index=0;
		int winner = 0;
		String now_arr[];
		score = new int[allplayer];
		while(true){
			menu();
			Scanner sc = new Scanner(System.in);
			int opt = sc.nextInt();
			if( opt == 1 ){
				int[] data = game.gameData.get();
				//print(" static ");
				print("Number of Games: "+data[0]);
				print("Number of Human wins: "+data[1]);
				print("Number of AI Wins: "+data[2]);
				print("Average Draws per game: "+data[3]);
				print("Longest Game: "+data[4]);
				continue;
			}else{
				gameRule.end = 0;
				draw = 0;
				players = new HashMap<Integer,String>();
				players.put(0,"You");
				score[0]=0;
				round=1;
				for(int pa=1;pa<=aiplayers;pa++){
					players.put(pa,"AI Players "+pa);
					score[pa]=0; //all players never have won
				}
				nowround = (int) Math.floor(Math.random()*players.size());
				gameRule.init(aiplayers, num, log);
				gameRule.commonCards = new ArrayList();
				print("Game Start");
				// 
				//gameRule.cards = 
			    gameRule.shuffle(); // get new cards
				if(writeGameLogsToFile){
					StringBuffer aBuffer = new StringBuffer("");
		            for(int i=0;i<players.size();i++){
		            	gameLog.write(players.get(i)+"\r\n");
	            		aBuffer = new StringBuffer("");
		            	for(int j=0;j<gameRule.cards[i].size();j++){
							aBuffer.append(gameCard.get(gameRule.cards[i].get(j))[0]+" ");
		            		if(j%4==0){
								aBuffer.append("\r\n");
							}
		            	}
		    			byte[] bytesArray;// = aBuffer.toString().getBytes();
		    			gameLog.write(aBuffer.toString());
		            	gameLog.write("\r\n----------\r\n");
		            }
				}
				while(gameRule.end!=1){
					print("Round "+round);
					print("Round "+round+": Players have drawn their cards");
					if(gameRule.cards[0].size()==0){
						print("You have Lost!");
					}else{
						now_arr = gameCard.get(gameRule.cards[0].get(0));
						print("You drew '"+now_arr[0]+"':");
						for(attr_index=1;attr_index<gameCard.attrSize();attr_index++){
							print("   > "+gameCard.attrGet(attr_index)+": "+now_arr[attr_index]);
						}
						print("There are '"+(gameRule.cards[0].size()-1)+" cards in your deck");
					}
					if(writeGameLogsToFile){
			            for(int i=0;i<players.size();i++){
			            	if(gameRule.cards[i].size()>0){
			            		gameLog.write(players.get(i)+"'s Crad Playing:"+gameCard.get(gameRule.cards[i].get(0))[0]+"\r\n");
			            		gameLog.write("\r\n----------\r\n");
			            	}
			            }
					}
					/*
					for(int i=0;i < gameRule.cards.length;i++){
						//for(int j=0;j<cards[nowround].size())
						print("this is "+i+", he has "+(gameRule.cards[i].size()));
					}*/
					//print("now round is"+nowround);
					//print("size is :"+gameRule.cards[nowround].size());
					int choose = showChoose(sc,nowround);
					if(writeGameLogsToFile){
			            if(gameRule.cards[nowround].size()>0){
			            	gameLog.write((players.get(nowround)+"'s category selected: "+gameCard.attrGet(choose)+"\r\n"
				            			+"corresponding values: "+gameCard.get(gameRule.cards[nowround].get(0))[choose]+"\r\n"));
			            	gameLog.write("\r\n----------\r\n");
			            	}
			            
					}
					winner = gameRule.getWinner(choose);
					if(winner <666){
						lastwin = (Integer) gameRule.cards[winner].get(0);
						score[winner]++;
					}else{
						nowround = gameRule.getrand(choose);
					}
					for(int ci = 0;ci<allplayer;ci++){
						if(gameRule.cards[ci].size()>0){
							gameRule.commonCards.add(gameRule.cards[ci].get(0));
							if(writeGameLogsToFile){
								recoedLog(players.get(ci),gameCard.get(gameRule.cards[ci].get(0)));
							}
							gameRule.cards[ci].remove(0);
						}else{
							continue;
						}
					}
					if(winner == 666){
						draw++;//isai==1?:(cards[0].size()>0?0:gameRule.getrand());
						//commonCards.add("");
						print("Round "+round+": This round was a Draw, common pile now has "+gameRule.commonCards.size()+" cards");
						//print(commonCards.toString());
					}else{
						nowround = winner;
						for(int ci=0;ci<gameRule.commonCards.size();ci++){
							gameRule.cards[winner].add(gameRule.commonCards.get(ci));
						}
						gameRule.commonCards = new ArrayList();
						/*
						for(int ci = 0;ci<commonCards.size();ci++){
							commonCards.remove(0);
						}*/
						print("Round "+round+": Player "+players.get(winner)+" won this round");
						print("The winning card was '"+gameCard.get(lastwin)[0]+"':");
						for(int ij=1;ij<gameCard.attrSize();ij++){
							print("   > "+gameCard.attrGet(ij)+": "+gameCard.get(lastwin)[ij]+" "+(ij==choose?"<--":""));
						}
					}

					if(writeGameLogsToFile){
						commonLog();
					}
					//if(round==1){
					//	gamePlay.update(true, score, draw, round, winner);
					//}else{
					//	gamePlay.update(false, score, draw, round, winner);
					//}
					round++;
					//break;
				}
				print("Game End");
				print("The overall winnder was "+players.get(winner));
				gameData.update(true, score, draw, round, winner);
				if(writeGameLogsToFile){
					StringBuffer aBuffer = new StringBuffer("");
					gameLog.write("The last winner is: "+players.get(winner));
					gameLog.write("\r\n----------\r\n");
				}
				print("Scores:");
				for(int i=0;i<score.length;i++){
					print("   "+players.get(i)+": "+score[i]);
				}
				continue;
			}
		}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Where is StarCitizenDeck.txt?");
			e.printStackTrace();
		}

	}

}
