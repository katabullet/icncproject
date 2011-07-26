
If you wan't to use this module you need to build a jar file from the project.
You only need to add the classes, nothing else.

1.) Copy this jar and  commons-codec.jar  to the jboss lib folder,
	that is:   <JBOSS-HOME>/server/<FLAVOUR>/lib

2.) Open the file  <JBOSS-HOME>/server/<FLAVOUR>/conf/login-config.xml

and add the following snippet:

  <application-policy name="InetJaas">
                      <authentication>
                      <login-module code="at.inet.jaas.login.InetLoginModule" flag="required">
                      <module-option name="DataSourceJndi">java:omDS</module-option>
                      <module-option name="SelectStmnt">SELECT PASSWORD, SALT, VERSION, FAILED_LOGINS FROM USERS WHERE ID = ?</module-option>
                      <!-- Required for failed logins -->
                      <module-option name="UpdateStmnt">UPDATE USERS SET FAILED_LOGINS = ? WHERE ID = ?</module-option>
                      <module-option name="RoleStmnt">select a.id from PRINCIPAL a inner join USERPRINCIPAL b on (a.id=b.principal_id) inner join USERS c on (c.id = b.user_id) where c.id = ?</module-option>
                      <module-option name="AllowedFailedLogins">10</module-option>
                      </login-module>
                      <login-module code="org.jboss.security.ClientLoginModule" flag="required">
                      </login-module>
  </authentication>
  </application-policy>


You might have to adjust the jndi name for the datasource. Do not forget to set up the datasource as well ;-)

Restart JBoss.

Done



UNIT TESTING:

Test specific data is stored in the test src folder. Furthermore the jaas.config is required for testing.
You do not need and do not want't to package this stuff into the jar file.

jaas.config stores the content of the login-config.xml

A in memory db (derby) is used to perform the test. Additionally the datasource is bound in jndi.
The tomcat jars (catalina and tomcat-juli)  are required for the jndi bindings.
