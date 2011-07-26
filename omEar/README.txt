Configuration:

Copy the complete config/mdm  folder to the jboss server conf dir (e.g D:/appl/aik/jboss-eap-5.0/jboss-as/server/all/conf). The configuration is picked up in that folder.

You should then have a subfolder mdm in the conf dir with all the content.

Comment out ALL OF THE CachedConnectionManager entries of JBoss 5.0.1 in conf/standardjboss.xml:
        <!-- <interceptor>org.jboss.resource.connectionmanager.CachedConnectionInterceptor</interceptor> -->


Configure CachedConnectionManager of JBoss 5.0.1 in the file deploy/jca-jboss-beans.xml as follows:


  <!-- CACHED CONNECTION MANAGER -->
  <bean name="CachedConnectionManager" class="org.jboss.resource.connectionmanager.CachedConnectionManager">

     <!-- Expose via JMX -->
     <annotation>@org.jboss.aop.microcontainer.aspects.jmx.JMX(name="jboss.jca:service=CachedConnectionManager", exposedInterface=org.jboss.resource.connectionmanager.CachedConnectionManagerMBean.class)</annotation>

     <!-- Whether to track unclosed connections and close them -->
     <property name="debug">false</property>

     <!-- Whether to throw an error for unclosed connections (true) or just log a warning (false) -->
     <property name="error">false</property>

     <!-- The transaction manager -->
     <property name="transactionManager"><inject bean="TransactionManager" property="transactionManager"/></property>

  </bean>

Configure in D:\appl\aik\jboss-eap-5.0\jboss-as\server\all\deploy\jbossweb.sar\META-INF\jboss-beans.xml as follows:
   <bean name="WebServer"
         class="org.jboss.web.tomcat.service.deployers.TomcatService">

      <annotation>@org.jboss.aop.microcontainer.aspects.jmx.JMX(name="jboss.web:service=WebServer", exposedInterface=org.jboss.web.tomcat.service.deployers.TomcatServiceMBean.class,registerDirectly=true)</annotation>

      <!-- Only needed if the org.jboss.web.tomcat.service.jca.CachedConnectionValve
           is enabled in the tomcat server.xml file.
      -->
      <!-- <depends>jboss.jca:service=CachedConnectionManager</depends> -->

      <!-- Transaction manager for unfinished transaction checking in the CachedConnectionValve -->
      <depends>jboss:service=TransactionManager</depends>

      <!-- Inject the TomcatDeployer -->
      <property name="tomcatDeployer"><inject bean="WarDeployer"/></property>
      <!-- Set the securityManagerService used to flush the auth cache on session expiration -->
      <property name="securityManagerService">
         <inject bean="jboss.security:service=JaasSecurityManager" />
      </property>
      <!--
         Do not configure other JMX attributes via this file.
         Use the WarDeployer bean in deployers/jboss-web.deployer/war-deployers-beans.xml
      -->

   </bean>

Configure in D:\appl\aik\jboss-eap-5.0\jboss-as\server\all\deploy\jbossweb.sar\server.xml as follows:
            <!-- <Valve className="org.jboss.web.tomcat.service.jca.CachedConnectionValve"
                cachedConnectionManagerObjectName="jboss.jca:service=CachedConnectionManager"
                transactionManagerObjectName="jboss:service=TransactionManager" /> -->

