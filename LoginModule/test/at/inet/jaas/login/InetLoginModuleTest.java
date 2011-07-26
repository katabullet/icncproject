package at.inet.jaas.login;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.InputStream;

import java.security.Principal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.HashMap;

import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;


import org.junit.BeforeClass;

import org.junit.Test;

import at.inet.helper.PasswordHelper;


public class InetLoginModuleTest {


    public static void importSQL(Connection conn, InputStream in) throws SQLException
    {
            Scanner s = new Scanner(in);
            s.useDelimiter("(;(\r)?\n)|(--\n)");
            Statement st = null;
            try
            {
                    st = conn.createStatement();
                    while (s.hasNext())
                    {
                            String line = s.next();
                            if (line.startsWith("/*!") && line.endsWith("*/"))
                            {
                                    int i = line.indexOf(' ');
                                    line = line.substring(i + 1, line.length() - " */".length());
                            }

                            if (line.trim().length() > 0)
                            {
                                    st.execute(line);
                            }
                    }
            }
            finally
            {
                    if (st != null) st.close();
            }
    }


    public static HashMap<String,String> fillOptionHashMap() {
        HashMap<String,String> hs = new HashMap<String,String>();
        hs.put(PasswordHelper.ALLOWEDFAILEDLOGINS, "-1");
        hs.put(InetLoginModule.JNDINAME, "java:/omDS");
        hs.put(PasswordHelper.SELECTSTMT,"SELECT CPWD, CSALT, NVERSION, NFAILEDLOGINS FROM MDUSER WHERE CUSERID = ?");
        hs.put(PasswordHelper.UPDATESTMNT, "UPDATE MDUSER SET NFAILEDLOGINS = ? WHERE CUSERID = ?");
        hs.put(InetLoginModule.ROLESTMNT, "select a.cprincipalid from MDPRINCIPAL a inner join MDUSERPRINCIPAL b on (a.cprincipalid=b.cprincipalid) inner join MDUSER c on (c.cuserid = b.cuserid) where c.cuserid = ?");
        return hs;
    }



    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    	System.setProperty("java.security.auth.login.config", "jaas.config");
        Connection connection = null;
        InitialContext ic  = null;
          try {
                // Create initial context
                System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                    "org.apache.naming.java.javaURLContextFactory");
                System.setProperty(Context.URL_PKG_PREFIXES,
                    "org.apache.naming");
                ic = new InitialContext();

                ic.createSubcontext("java:");
                // Construct DataSource

                org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource dataSource = new org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource();

                dataSource.setDatabaseName("memory:InetJaasTestBD");
                dataSource.setCreateDatabase ("create");

                /*
                OracleConnectionPoolDataSource ds = new OracleConnectionPoolDataSource();
                ds.setURL("jdbc:oracle:thin:@dbin92l:1539:idev");
                ds.setUser("mddev");
                ds.setPassword("havanna");

                */
                ic.bind("java:/omDS", dataSource);

                connection = dataSource.getConnection();
                FileInputStream fis = new FileInputStream("test/populateDerbyDB.sql");
                importSQL(connection,fis);
            } catch (NamingException ex) {
                Logger.getLogger(InetLoginModule.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (connection != null)
                    connection.close();
                if (ic != null)
                    ic.close();
            }

    }




    @Test
    public void testValidLoginLogout() {
        String name = "validUser";
        String password = "validPassword";
        LoginContext lc =null;
        try {
            lc = new LoginContext("InetJaas", new TestCallbackHandler(name, password));
            lc.login();
        } catch (LoginException e) {
            e.printStackTrace();
            fail("Login failed");
        }



        Set<Principal> mySet = lc.getSubject().getPrincipals();
        // That would be ourself (validUser), Authorizer1, Editor1, and Roles.
        // Roles is added for jboss compatibility
        assertSame("There are not four elements in the set", 4, mySet.size() );

        /*
        Iterator<Principal> it = mySet.iterator();
        while (it.hasNext() ) {
            System.out.println("Principal: " + ((Principal) it.next()).getName() );
        }*/


        assertTrue("Role Authorizer1 not assigned",mySet.contains(new InetPrincipal("Authorizer1")));
        assertTrue("Role Editor1 not assigned",mySet.contains(new InetPrincipal("Editor1")));
        assertTrue("Role validUser not assigned",mySet.contains(new InetPrincipal("validUser")));

        try {
            lc.logout();
        } catch (LoginException e) {
            e.printStackTrace();
            fail("Logout failed");
        }

    }

    @Test(expected=LoginException.class)
    public void testFailedLogin() throws LoginException {
        String name = "failedLogin";
        String password = "wrongPassword";
        LoginContext lc =null;
        lc = new LoginContext("InetJaas", new TestCallbackHandler(name, password));
        lc.login();
    }


    @Test
    public void testLockedUser() throws LoginException {
        String name = "lockedUser";
        String password = "validPassword";
        LoginContext lc =null;
    try {
        lc = new LoginContext("InetJaas", new TestCallbackHandler(name, password));
        lc.login();
        fail("Login succeeded account is locked");
    } catch (LoginException e) {
        assertEquals("Wrong exception thrown.",PasswordHelper.ACCOUNTLOCKEDERROR,e.getMessage());
    }
    }


    @Test
    public void testLockUser() {
        String name = "unlockeduser";
        String password = "invalidPassword";
        LoginContext lc =null;
        for (int i = 0; i <= 10 ; i++) {
            try {
                lc = new LoginContext("InetJaas", new TestCallbackHandler(name, password));
                lc.login();
                fail("Login succeeded but we used wrong password");
            } catch (LoginException e) {
            }
        }

        try {
            password = "Dummy";
            lc = new LoginContext("InetJaas", new TestCallbackHandler(name, password));
            lc.login();
            fail("Login succeeded but account should be locked by now!");
        } catch (LoginException e) {
            assertEquals("Wrong exception thrown.",PasswordHelper.ACCOUNTLOCKEDERROR,e.getMessage());
        }

    }


    @Test
    public void testLockUserUnlimited() {
        String name = "unlockeduser";
        String password = "invalidPassword";
        HashMap<String,String> hs = fillOptionHashMap();
        for (int i = 0; i <= 10 ; i++) {
            try {
                InetLoginModule ilm = new InetLoginModule();
                Subject s = new Subject();
                ilm.initialize(s, new TestCallbackHandler(name, password), null, hs);
                if (ilm.login()) {
                    fail("Login succeeded but we used wrong password");
                }
            } catch (LoginException e) {
            }
        }

        try {
            password = "Dummy";
            InetLoginModule ilm = new InetLoginModule();
            Subject s = new Subject();
            ilm.initialize(s, new TestCallbackHandler(name, password), null, hs);
            if (!ilm.login()) {
                fail("Login failed, but we should pass the login");
            }
            if (!ilm.commit()) {
            	fail("Commit phase failed");
            }
        } catch (LoginException e) {
            fail("Could not login but we should.");
        }

    }


    @Test
    public void testUnsupportedAlgorithmException() {
        String name = "unsupportedAlgo";
        String password = "transparent";
        try {
            LoginContext lc = new LoginContext("InetJaas", new TestCallbackHandler(name, password));
            lc.login();
            fail("Login succeeded but we do not have a supported algorithm!");
        } catch (LoginException e) {
            assertEquals("Wrong exception thrown.",PasswordHelper.UNSUPPORTEDALGORITHMERROR,e.getMessage());
        }

    }
}
