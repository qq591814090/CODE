import java.sql.*;
import java.math.*;
import java.util.*;
import oracle.jdbc.driver.*;
public class test
{
	public static void main(String[] args) throws SQLException{
		java.util.Date date = new java.util.Date();
		System.out.println("---this is start of VarI---");
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}
		catch(ClassNotFoundException ex) {
			System.out.println("Failure");
			System.exit(1);
		}
		String URL = "jdbc:oracle:thin:@127.0.0.1:1521:ORCL";
		String USER = "hr";
		String PASS = "oracle";
		Connection conn = DriverManager.getConnection(URL, USER, PASS);
		Statement stmt = conn.createStatement();
		stmt.executeQuery(String.format("CREATE TABLE benchmark (\n theKey NUMBER PRIMARY KEY,\n columnA NUMBER, \ncolumnB NUMBER,\n filler CHAR(247) )"));

		int rMax = 50000;
		int lines = 5000000;
		int index = 0;
		String str ="a";
	  	int[] intarray = new int[lines+1];
                for(int j = 0; j<lines+1; j++) {
                        intarray[j] = j;
                }/*this can also genenrate other numbers instead of 1-5m. the different is you can generate a[i] between a[i-1] and a[i-1]+max_value/5m */
		/*here i just did this for convenience.*/
		
		Random randomGenerator = new Random();
		System.out.println(date.toString());
		for( int i = 1; i <= lines; i++) {
			stmt.executeQuery(String.format("insert into benchmark(thekey,COLUMNA,COLUMNB,filler) values (%d,%d,%d,'%s')",intarray[i],randomGenerator.nextInt(rMax)+1,randomGenerator.nextInt(rMax)+1,str));
			if (i*10/lines> index){
				index = i*10/lines;
				System.out.println(String.format("mission complete:%d0%%",index));
			}
		}
		date = new java.util.Date();
                System.out.println(date.toString());
                System.out.println("---this is end of VARI---");

		stmt.executeQuery(String.format("Drop table benchmark"));
		stmt.executeQuery(String.format("CREATE TABLE benchmark (\n theKey NUMBER PRIMARY KEY,\n columnA NUMBER, \ncolumnB NUMBER,\n filler CHAR(247) )"));
		/* ------Blow is shuffle algorithm here*/
                for(int  j = 1;j<lines+1;j++) {
                        int r = j + randomGenerator.nextInt(lines+1 - j);
                        int temp = intarray[j];
                        intarray[j] = intarray[r];
                        intarray[r] = temp;
                }
		index = 0;
		/* ------Above is shuffle algorithm here*/
		System.out.println("---this is start of VarII---");
                date = new java.util.Date();
                System.out.println(date.toString());

		for( int i = 1; i <= lines; i++) {
			stmt.executeQuery(String.format("insert into benchmark(thekey,COLUMNA,COLUMNB,filler) values (%d,%d,%d,'%s')",intarray[i],randomGenerator.nextInt(rMax)+1,randomGenerator.nextInt(rMax)+1,str));

                        if (i*10/lines> index){
                                index = i*10/lines;
                                System.out.println(String.format("mission complete:%d0%%",index));
                        }
                }
		date = new java.util.Date();
                System.out.println(date.toString());
		
		System.out.println("---this is end of VARII---");
		 stmt.executeQuery(String.format("Drop table benchmark"));
		stmt.close();
		conn.close();
	}
}	
