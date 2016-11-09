/**
 * 
 */
package com.coolisland.trackmystocks;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.coolisland.trackmystocks.database.CheckDatabaseConnection;

/**
 * @author Grasshopper
 *
 */
public class DatabaseChecks {
	static boolean databaseAvailable = false;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	
	@Test
	public void databaseAvailable() {
		// Verfiy database is running
		CheckDatabaseConnection dbAvailable = new CheckDatabaseConnection();
		boolean databaseAvailable = dbAvailable.testConnection();
		
		assertTrue("Database is not available. Tests will fail", databaseAvailable);
	}
}
