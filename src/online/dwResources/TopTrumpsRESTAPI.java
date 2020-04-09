package online.dwResources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;

import online.configuration.TopTrumpsJSONConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import game.gameData;
import game.gameCard;
import game.gameRule;
@Path("/toptrumps") // Resources specified here should be hosted at http://localhost:7777/toptrumps
@Produces(MediaType.APPLICATION_JSON) // This resource returns JSON content
@Consumes(MediaType.APPLICATION_JSON) // This resource can take JSON content as input
/**
 * This is a Dropwizard Resource that specifies what to provide when a user
 * requests a particular URL. In this case, the URLs are associated to the
 * different REST API methods that you will need to expose the game commands
 * to the Web page.
 * 
 * Below are provided some sample methods that illustrate how to create
 * REST API methods in Dropwizard. You will need to replace these with
 * methods that allow a TopTrumps game to be controled from a Web page.
 */
public class TopTrumpsRESTAPI {

	//static Map <Integer,String[]> arrs=new HashMap<Integer,String[]>();
	//static String realcard[][];
	//static String attrs[];
	static int num = 0;
	static int nowround;
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
	static String deckfile;
	static int ainum;
	static int round=1;
	static int score[];
	static int attr_index=0;
	static String now_arr[];
	static int mychoose = 1;
	static int draw = 0;
	/** A Jackson Object writer. It allows us to turn Java objects
	 * into JSON strings easily. */
	ObjectWriter oWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
	//static TopTrumpsJSONConfiguration conf;
	
	public static int showChoose(int nowround){
		int dchoose = 1;
		if(nowround==0){
			dchoose = mychoose;
			//me
			//print("It is your turn to select a category, the categories are:");
			//for(int i=1;i<gameCard.attrSize();i++){
				//print("   "+i+": "+attrs[i]);
			//}
			//System.out.print("Enter the number for your attribute: ");
			//choose = sc.nextInt();
		}else{
			dchoose = gameRule.showChoose(nowround);
			
		}
		//dchoose = dchoose>gameCard.attrSize()?1:dchoose;
		return dchoose;
	}
	public static void newgame() throws IOException{
		draw = 0;
		round=1;
		//attr_index=0;
		allplayer = aiplayers+1;
		score = new int[allplayer];
		gameRule.end = 0;
			players = new HashMap<Integer,String>();
			players.put(0,"You");
			score[0]=0;
			for(int pa=1;pa<=aiplayers;pa++){
				players.put(pa,"AI Players "+pa);
				score[pa]=0; //all players never have won
			}
			nowround  = (int) Math.floor(Math.random()*players.size());
			// 
			gameRule.init(aiplayers, num, false);
			gameRule.commonCards = new ArrayList();
			gameRule.shuffle(); // get new cardscards = 
	}
	/**
	 * Contructor method for the REST API. This is called first. It provides
	 * a TopTrumpsJSONConfiguration from which you can get the location of
	 * the deck file and the number of AI players.
	 * @param conf
	 */
	public TopTrumpsRESTAPI(TopTrumpsJSONConfiguration conf) {
		// ----------------------------------------------------
		// Add relevant initalization here
		// ----------------------------------------------------
		//conf = conf;
		deckfile = conf.getDeckFile();
		aiplayers = conf.getNumAIPlayers();
		allplayer = aiplayers+1;
		try {
			gameCard.load(deckfile);
			gameCard.getCards();
			gameCard.getAttrs();
			num=0;
			num = gameCard.keySize();
		    com  = num%(allplayer);
		    pernum = (num-com)/allplayer;
		    newgame();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Where is StarCitizenDeck.txt?");
			e.printStackTrace();
		}
	}
	
	// ----------------------------------------------------
	// Add relevant API methods here
	// ----------------------------------------------------

	@GET
	@Path("/game/attr")
	public String attr() throws IOException {
		
		List<String> listOfAttrs = new ArrayList<String>();
		for(int i=1;i<gameCard.attrSize();i++){
			listOfAttrs.add(gameCard.attrGet(i));
		}
		// We can turn arbatory Java objects directly into JSON strings using
		// Jackson seralization, assuming that the Java objects are not too complex.
		String listAsJSONString = oWriter.writeValueAsString(listOfAttrs);
		return listAsJSONString;
	}

	@GET
	@Path("/game/new")
	public String game(@QueryParam("aiplayer") String aiplayer) throws IOException {
		
		aiplayers = Integer.parseInt(aiplayer)>0?Integer.parseInt(aiplayer):aiplayers;
	    newgame();

		List<String> listOfAttrs = new ArrayList<String>();
		for(int i=1;i<gameCard.attrSize();i++){
			listOfAttrs.add(gameCard.attrGet(i));
		}
		// We can turn arbatory Java objects directly into JSON strings using
		// Jackson seralization, assuming that the Java objects are not too complex.
		String listAsJSONString = oWriter.writeValueAsString(listOfAttrs);
		return listAsJSONString;
	    
	}
	@GET
	@Path("/game/round")
	public String round() throws IOException {

		Map <String,String> datas=new HashMap<String,String>();
		//List<String> listOfAttrs = new ArrayList<String>();
		/*for(int i=0;i<gameCard.attrSize();i++){
			listOfAttrs.add(attrs[i]);
		}*/
		String value[] = new String[gameCard.attrSize()-1];
		String nowcardname = "";
		if(gameRule.cards[0].size()>0){
			for(int i=1;i<gameCard.attrSize();i++){
				value[i-1]=gameCard.get(gameRule.cards[0].get(0))[i];
			}
			nowcardname = gameCard.get(gameRule.cards[0].get(0))[0];
		}
		datas.put("aiplayers",String.valueOf(aiplayers));
		datas.put("round",String.valueOf(round));
		datas.put("nowround",String.valueOf(nowround));
		datas.put("nowcard",StringUtils.join(value,"#"));
		datas.put("nowcardname",nowcardname);
		datas.put("havecard",String.valueOf(gameRule.cards[0].size()));
		// We can turn arbatory Java objects directly into JSON strings using
		// Jackson seralization, assuming that the Java objects are not too complex.
		String MapAsJSONString = oWriter.writeValueAsString(datas);
		return MapAsJSONString;
	}	

	
	@GET
	@Path("/game/score")
	public String score() throws IOException {
		List<String> datas = new ArrayList<String>();
		//datas.put("score",);
		for(int i =0;i<score.length;i++){
			datas.add(score[i]+"");
		}
		String jsons = oWriter.writeValueAsString(datas);
		return jsons;
	}
	@POST
	@Path("/game/round")
	public String cate(@QueryParam("cate") String mychooses) throws IOException {
		mychoose = mychooses==""||mychooses=="0"?1:Integer.parseInt(mychooses);
		Map <String,String> datas=new HashMap<String,String>();
		int choose = showChoose(nowround);
		int winner = gameRule.getWinner(choose);
		String value[] = new String[gameCard.attrSize()];
		if(winner <666){
			lastwin = (Integer) gameRule.cards[winner].get(0);
			score[winner]++;
			nowround = winner;
		}else{
			nowround = gameRule.getrand(choose);
		}
		for(int ii=0;ii<players.size();ii++){
			value = new String[gameCard.attrSize()];
			if(gameRule.cards[ii].size()>0){
				for(int ij=0;ij<gameCard.attrSize();ij++){
					//print("   > "+attrs[ij]+": "+gameCard.get(lastwin)[ij]+" "+(ij==choose?"<--":""));
					value[ij] = gameCard.get(gameRule.cards[ii].get(0))[ij];
				}
			}
			datas.put("player"+ii,StringUtils.join(value,"#"));
		}
//AI Players 
		for(int ci = 0;ci<allplayer;ci++){
			if(gameRule.cards[ci].size()>0){
				gameRule.commonCards.add(gameRule.cards[ci].get(0));
				gameRule.cards[ci].remove(0);
			}else{
				continue;
			}
		}
		if(winner == 666){
			draw++;//isai==1?:(cards[0].size()>0?0:gameRule.getrand());
			//commonCards.add("");
			datas.put("status","draw");
			
			//print("Round "+round+": This round was a Draw, common pile now has "+commonCards.size()+" cards");
			//print(commonCards.toString());
		}else{
			datas.put("status",String.valueOf(winner));
			for(int ci=0;ci<gameRule.commonCards.size();ci++){
				gameRule.cards[winner].add(gameRule.commonCards.get(ci));
			}
			gameRule.commonCards = new ArrayList();
			/*
			for(int ci = 0;ci<commonCards.size();ci++){
				commonCards.remove(0);
			}*/
			//print("Round "+round+": Player "+players.get(winner)+" won this round");
			//print("The winning card was '"+gameCard.get(lastwin)[0]+"':");
			/*for(int ij=0;ij<gameCard.attrSize();ij++){
				//print("   > "+attrs[ij]+": "+gameCard.get(lastwin)[ij]+" "+(ij==choose?"<--":""));
				value[ij] = gameCard.get(cards[ij].get(0))[ij];
			}
			datas.put("winner",StringUtils.join(value,"#"));*/
		}
		
		round++;

		datas.put("common",StringUtils.join(gameRule.commonCards.size()));
		int[] playerscards = new int[players.size()];
		for(int ij=0;ij<players.size();ij++){
			//print("   > "+attrs[ij]+": "+gameCard.get(lastwin)[ij]+" "+(ij==choose?"<--":""));
			playerscards[ij] = gameRule.cards[ij].size();
		}
		datas.put("choose",choose+"");
		datas.put("playercards",oWriter.writeValueAsString(playerscards));
		if(gameRule.cards[0].size()==0){
			datas.put("lose","1");
		}else{
			datas.put("lose","0");
		}
		if(gameRule.end==1){
			datas.put("end","1");
			//if(round==1){
			//	gamePlay.update(true, score, draw, round, winner);
			//}else{
				gameData.update(true, score, draw, round, winner);
			//}
		}else{
			datas.put("end","0");
		}
		String listAsJSONString = oWriter.writeValueAsString(datas);
		return listAsJSONString;
	}
	
	@GET
	@Path("/game/stats")
	/**
	 * Here is an example of a simple REST get request that returns a String.
	 * We also illustrate here how we can convert Java objects to JSON strings.
	 * @return - List of words as JSON
	 * @throws IOException
	 */
	public String stats() throws IOException {
		int[] data = game.gameData.get();
		List<String> listOfWords = new ArrayList<String>();
		for(int i=0;i<data.length;i++){
			listOfWords.add(data[i]+"");
		}
		String listAsJSONString = oWriter.writeValueAsString(listOfWords);
		return listAsJSONString;
	}
	@GET
	@Path("/helloJSONList")
	/**
	 * Here is an example of a simple REST get request that returns a String.
	 * We also illustrate here how we can convert Java objects to JSON strings.
	 * @return - List of words as JSON
	 * @throws IOException
	 */
	public String helloJSONList() throws IOException {
		
		List<String> listOfWords = new ArrayList<String>();
		listOfWords.add("Hello");
		listOfWords.add("World!");
		listOfWords.add(deckfile);
		for(int i=0;i<gameRule.cards[0].size();i++){
			listOfWords.add(gameCard.get(gameRule.cards[0].get(i))[0]);
		}
		//System.out.println(listOfWords.add(gameCard.get(cards[0].get(0))[0]));
		// We can turn arbatory Java objects directly into JSON strings using
		// Jackson seralization, assuming that the Java objects are not too complex.
		String listAsJSONString = oWriter.writeValueAsString(listOfWords);
		
		return listAsJSONString;
	}
	
	@GET
	@Path("/helloWord")
	/**
	 * Here is an example of how to read parameters provided in an HTML Get request.
	 * @param Word - A word
	 * @return - A String
	 * @throws IOException
	 */
	public String helloWord(@QueryParam("Word") String Word) throws IOException {
		return "Hello "+Word;
	}
	
}
