package game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import game.gameLog;
public class gameRule {

	static int num = 0;
	static List indexes = new ArrayList();
	static int aiplayers = 0;
	static int allplayer = 0;
	static public ArrayList[] cards;
	static public List commonCards = new ArrayList();
	static int pernum,com;
	static int lastwin;
	static int isai=0;
	public static int end=0;
	static int draw=0;
	static boolean log = false;
	static Map <Integer,String[]> arrs=new HashMap<Integer,String[]>();
	static String attrs[];
	static int score[];
	static int maxmax = 0;
	/**
	 * @param args
	 */
	public static void init(int aiplayer,int numnum, boolean loglog) {
		// TODO Auto-generated method stub
		aiplayers = aiplayer;
		allplayer = aiplayers+1;
		cards = new ArrayList[allplayer];
		commonCards = new ArrayList();
		num = numnum;
		log = loglog;
	    com  = num%(allplayer);
	    pernum = (num-com)/allplayer;
		score = new int[allplayer];
	}
	public static ArrayList[] shuffle() throws IOException{
		for(int a=0;a<num;a++){
			indexes.add(a);
		}
        for (int i = 0; i < allplayer; i++) {
        	cards[i] = new ArrayList<Integer>();
        }
		Collections.shuffle(indexes);
		Collections.shuffle(indexes);
		if(log){
			gameLog.write("Shuffled cards\r\n");
			StringBuffer aBuffer = new StringBuffer("");
			for(int i =0; i<indexes.size();i++){
				aBuffer.append(gameCard.get(indexes.get(i))[0]+" ");
				if(i>0 && i%4==0){
					aBuffer.append("\r\n");
				}
			}
			gameLog.write(aBuffer.toString());
            gameLog.write("\r\n----------\r\n");
		}
		for(int i=0;i<allplayer;i++){
			for(int j=0;j<pernum;j++){
				int a;
				try{
					a = (Integer) indexes.get(1);
				}catch(Exception e){
					a = (Integer) indexes.get(0);
				}
				cards[i].add(a);
				//System.out.println(a);
				try{
					indexes.remove(1);
				}catch(Exception e){
					indexes.remove(0);
				}
			}
		}
		for(int i=0;i<com;i++){
			int a;
			try{
				a = (Integer) indexes.get(1);
			}catch(Exception e){
				a = (Integer) indexes.get(0);
			}
			commonCards.add(a);
			//System.out.println(a);
			try{
				indexes.remove(1);
			}catch(Exception e){
				indexes.remove(0);
			}
		}
		return cards;
	}
	public static int getrand(int ch){
		int rand = 0;
		int human = 0;//,bk=0;
		List alive = new ArrayList();/*
		if(cards[0].size()>0){
			String[] datas = gameCard.get(cards[0].get(0));
			if(Integer.parseInt(datas[ch]) == maxmax) return 0;
		}*/
		for(int i=0;i<allplayer;i++){
			if(cards[i].size()>0){
				String[] data = gameCard.get(cards[i].get(0));
				if(Integer.parseInt(data[ch]) == maxmax){
					if(i == 0){
						human = 1;
						break;
					}
					alive.add(i);
				}
				//bk = i;
			}else{
				//
			}
		}
		if(human == 1) return 0;
		Collections.shuffle(alive);
		Collections.shuffle(alive);
		//if(alive.){
			rand = (Integer) alive.get(0);
		//}catch(Exception e){
		//	rand = bk;
		//}
		/*while(rand>0){
			rand = (int) Math.floor(Math.random()*players.size());
		}*/
		return rand;
	}

	public static int getWinner(int choose){
		// return 666 eq has a drawn
		int winner = 666;
		int max = 0;
		int token=0;
		for(int i=0;i<allplayer;i++){
			if(cards[i].size()>0){
				String[] data = gameCard.get(cards[i].get(0));
				int value = Integer.parseInt(data[choose]);
				//System.out.println("The value is:"+value+", the max is:"+max+",the cond is "+(value>max));
				if(value == max){
					winner = 666;
					isai = i>0?1:0;
				}
				if(value > max){
					winner = i;
					max = value;
				}
				token++;
			}
		}
		maxmax = max;
		end = token==1?1:0;
		return winner;
	}

	public static int showChoose(int nowround){
		int choose = 1;
		String[] data = gameCard.get(cards[nowround].get(0));
		int max = 0;
		choose = 1;
		for(int i=1;i<data.length;i++){
			if(Integer.parseInt(data[i])>=max){
				max = Integer.parseInt(data[i]);
				choose = i;
			}	
		}
		choose = choose>gameCard.attrSize()?1:choose;
		return choose;	
	}
}
