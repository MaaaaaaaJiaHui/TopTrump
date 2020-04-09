package game;

import java.sql.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.StringUtils;
public class gameData {

    static final String JDBC_DRIVER = "org.postgresql.Driver";  
    static final String DB_URL = "jdbc:postgresql://52.24.215.108:5432/CT";
 // username and password of database
    static final String USER = "CT";
    static final String PASS = "CT";
	/**
	 * @param args
	 */
    public static int[] get(){
    	Connection conn = null;
        Statement stmt = null;
        //winner = winner==666?0:winner;
        int total = 0;
        int human = 0;
        int ai = 0;
        int draws = 0;
        int longest = 0;
        try{

            Class.forName(JDBC_DRIVER);
        

            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            ResultSet rs;
            String sql;
            stmt = conn.createStatement();

            
            sql = "SELECT count(id) as total FROM gamedatas";
            rs = stmt.executeQuery(sql);

            while(rs.next()){

                total  = rs.getInt("total");
                rs.close();
                break;
            }
            sql = "SELECT max(rounds) as rounds FROM gamedatas";
            rs = stmt.executeQuery(sql);

            while(rs.next()){

            	longest  = rs.getInt("rounds");
                rs.close();
                break;
            }
            sql = "SELECT sum(player0) as human FROM gamedatas";
            rs = stmt.executeQuery(sql);

            while(rs.next()){

            	human  = rs.getInt("human");
                rs.close();
                break;
            }
            sql = "SELECT sum(player1)+sum(player2)+sum(player3)+sum(player4) as ai FROM gamedatas";
            rs = stmt.executeQuery(sql);

            while(rs.next()){

            	ai  = rs.getInt("ai");
                rs.close();
                break;
            }
            sql = "SELECT sum(draws)/count(id) as draws FROM gamedatas";
            rs = stmt.executeQuery(sql);

            while(rs.next()){

            	draws  = (int) Math.floor(rs.getInt("draws"));
                rs.close();
                break;
            }
            
            stmt.close();
            conn.close();
        }catch(SQLException se){

            se.printStackTrace();
        }catch(Exception e){

            e.printStackTrace();
        }finally{

            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        int data[] = new int[5];
        data[0]=total;
        data[1]=human;
        data[2]=ai;
        data[3]=draws;
        data[4]=longest;//[,human,ai,draws,longest]
        return data;
    }
	public static void update(boolean c,int[] score,int draw,int rounds,int winner) {
		// TODO Auto-generated method stub
		Connection conn = null;
        Statement stmt = null;
        winner = winner==666?0:winner;
        try{

            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            ResultSet rs;
            String sql;
            stmt = conn.createStatement();
            if(!c){
            
            sql = "SELECT id FROM gamedatas order by id desc limit 1";
            rs = stmt.executeQuery(sql);

            while(rs.next()){

                int id  = rs.getInt("id");
                //String name = rs.getString("name");
                //String url = rs.getString("url");
                String str = "";
                String value[] = new String[5];
                for(int i=0;i<score.length;i++){
                	value[i] = "player"+i+"="+score[i];
                }
                for(int i=score.length;i<5;i++){
                	value[i] = "player"+i+"="+0;
                }
                str = StringUtils.join(value,",");
                sql = "update gamedatas set rounds='"+rounds+"',draws='"+draw+"',winner='"+winner+"',"+str+" where id='"+id+"'";
                stmt.executeUpdate(sql);

                rs.close();
                break;
            }

            }else{
            	String str;
            	String valuea[] = new String[5];
            	String value[] = new String[5];
                for(int i=0;i<score.length;i++){
                	valuea[i] = "player"+i;//+"="+score[i];
                	value[i] = score[i]+"";
                }
                for(int j=score.length;j<5;j++){
                	valuea[j] = "player"+j;
                	value[j] = 0+"";
                }
                String stra = StringUtils.join(valuea,",");
                str = StringUtils.join(value,",");
                sql = "insert into gamedatas (rounds,draws,winner,"+stra+") VALUES ("+rounds+","+draw+",'"+winner+"',"+str+")";
                //System.out.println(sql);
            	stmt.executeUpdate(sql);
            }
            stmt.close();
            conn.close();
        }catch(SQLException se){

            se.printStackTrace();
        }catch(Exception e){

            e.printStackTrace();
        }finally{

            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        //System.out.println("Goodbye!");
	}

}
