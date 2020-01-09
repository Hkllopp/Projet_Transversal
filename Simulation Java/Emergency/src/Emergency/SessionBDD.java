package Emergency;

import java.sql.Connection;
import java.sql.DriverManager;

public class SessionBDD {
	
	 private String url;
	 private String user;
	 private String passwd;
	 private Connection connect;
	 
	 public SessionBDD(String url, String user, String passwd)
	 {
		 this.url = url;
		 this.user = user;
		 this.passwd = passwd;
		 try 
		 { 	
			 Class.forName("org.postgresql.Driver");
		     System.out.println("Driver O.K.");
			 this.connect = DriverManager.getConnection(this.url, this.user, this.passwd);
			 System.out.println("Connection BDD effective ! ");
			 
		 } catch (Exception e) 
		 {
		      e.printStackTrace();
		 }
	 }
	 
	 public Connection getConnect()
	 {
		 return this.connect;
	 }
}
