package cs623project;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sample of JDBC for MySQL ACID is implemented
 */

public class cs623project {

	public static void main(String args[]) throws SQLException, IOException, 
	ClassNotFoundException {

		// Load the MySQL driver
		Class.forName("com.mysql.cj.jdbc.Driver");
		// Old driver
		// Class.forName("com.mysql.jdbc.Driver");

		// Connect to the default database with credentials
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cs623project", "root", "root");

		// For atomicity
		conn.setAutoCommit(false);

		// For isolation
		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

		Statement stmt1 = null;
		try {
			// Create statement object
			stmt1 = conn.createStatement();
			
			// Maybe a table student1 exist, maybe not
			// create table student(id integer, name varchar(10), primary key(Id))
			// Either the 2 following inserts are executed, or none of them are. This is
			// atomicity.
			
			
			// Creating PRODUCT TABLE
			stmt1.execute("CREATE TABLE `product` (\r\n"
					+ "  `proid` varchar(45) NOT NULL,\r\n"
					+ "  `pname` varchar(45) DEFAULT NULL,\r\n"
					+ "  `price` varchar(45) DEFAULT NULL,\r\n"
					+ "  PRIMARY KEY (`proid`)\r\n"
					+ ")");
			
			//Inserting into PRODUCT
			stmt1.execute("INSERT INTO `product` VALUES ('p1','tape','2.5'),('p2','tv','250'),('p3','vcr','80');");
			
			
			//Creating DEPOT TABLE
			stmt1.execute("CREATE TABLE `depot` (\r\n"
					+ "  `depid` varchar(45) NOT NULL,\r\n"
					+ "  `addr` varchar(45) DEFAULT NULL,\r\n"
					+ "  `volume` varchar(45) DEFAULT NULL,\r\n"
					+ "  PRIMARY KEY (`depid`)\r\n"
					+ ") ");
			
			//Inserting into DEPOT
			stmt1.execute("\r\n"
					+ "INSERT INTO `depot` VALUES ('d1','new york','9000'),('d2','syracuse','6000'),('d4','new york','2000');");
			
			//Creating STOCK TABLE with CONSTRAIN
			stmt1.execute("CREATE TABLE `stock` (\r\n"
					+ "  `proid` varchar(45) NOT NULL,\r\n"
					+ "  `depid` varchar(45) NOT NULL,\r\n"
					+ "  `quantity` int DEFAULT NULL,\r\n"
					+ "  PRIMARY KEY (`proid`,`depid`),\r\n"
					+ "  KEY `FK_Depid` (`depid`),\r\n"
					+ "  CONSTRAINT `FK_Depid` FOREIGN KEY (`depid`) REFERENCES `depot` (`depid`),\r\n"
					+ "  CONSTRAINT `FK_Proid` FOREIGN KEY (`proid`) REFERENCES `product` (`proid`)\r\n"
					+ ")");
			
			//Inserting into STOCK TABLE
			stmt1.execute("INSERT INTO `stock` VALUES ('p1','d1',1000),('p1','d2',-100),('p1','d4',1200),('p2','d1',-400),('p2','d2',2000),('p2','d4',1500),('p3','d1',3000),('p3','d4',2000);\r\n"
					+ "");
			//Demonstration of ACID PROPERTY
			
			//Inserting into PRODUCT TABLE
			stmt1.executeUpdate("insert into product values ('p100','cd',5)");
			//Inserting stock for the PRODUCT
			stmt1.executeUpdate("insert into stock values ('p100','d2',50)");
			
			
		} catch (SQLException e) {
			
			System.out.println(e);
			// For atomicity
			conn.rollback();
			stmt1.close();
			conn.close();
			return;
		}
		conn.commit();
		stmt1.close();
		conn.close();
	}
}
