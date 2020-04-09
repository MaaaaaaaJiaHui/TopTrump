package game;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
public class gameCard {
	static Map <Integer,String[]> arrs=new HashMap<Integer,String[]>();
	static String attrs[];
	public static void txt2String(File file){
		//Map<String, String[][]> map = new HashMap<String, String[][]>();
        //StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            int i = 0;
            while((s = br.readLine())!=null){
            	if(i==0){
            		attrs = s.split("	");
            	}else{
            		String[] tmp = s.split("	");
            		//arrs[i-1] = new String[tmp.length];
            		//arrs[i-1] = tmp;
            		arrs.put(i-1, tmp);
            		//arrs[i-1] = "a";
            	}
                i++;
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        //map.put("attr", attrs);
        //map.put("arrs", arrs);
        //return result.toString();
        //return map;
    }
	public static void load(String deckfile) throws Exception{
		File file = new File(deckfile);
        txt2String(file);
		return;
	}
	public static void getCards(){//Map <Integer,String[]> 
		return ;
	}
	public static void getAttrs(){//String[] 
		//return attrs;
		return ;
	}
	public static String[] get(Object object){
		return arrs.get(object);
	}
	public static int size(){
		return arrs.size();
	}
	public static int keySize(){
		return arrs.keySet().size();
	}
	public static String attrGet(int index){
		return attrs[index];
	}
	
	public static int attrSize(){
		return attrs.length;
	}

}
