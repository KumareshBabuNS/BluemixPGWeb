<h1> BluemixPG*Web </h1>

![alt tag](https://dl.dropboxusercontent.com/u/15829935/bluemix-docs/images/bluemix-pg-web/PGWeb.png)

BluemixPG*Web is a browser based SQL tool for PostgreSQL which allows you to run SQL commands and view schema objects 
from a browser based interface. It includes the following capabilities

<ul>
  <li> Multiple Command SQL worksheet for DDL and DML </li>
  <li> Run Explain Plan across SQL Statements </li>
  <li> View/Run DDL command against Tables/Views [More Schema Object support Coming] </li>
  <li> Command History </li>
  <li> Auto Bind to ElephantSQL Service bound to Application within IBM Bluemix </li>
  <li> Manage JDBC Connections </li>
  <li> Load SQL File into SQL Worksheet from LOcal File System </li>
</ul>

<h2> Deploy to Bluemix </h2>

<a href="https://bluemix.net/deploy?repository=https://github.com/papicella/BluemixPGWeb" target="_blank">
<img src="http://bluemix.net/deploy/button.png" alt="Bluemix button" /></a>

Once deployed application on Bluemix will look as follows

![alt tag](https://dl.dropboxusercontent.com/u/15829935/bluemix-docs/images/bluemix-pg-web/img5.png)

<h2> Run Stand Alone Outside of Bluemix </h2>

- $ git clone https://github.com/papicella/BluemixPGWeb.git
- $ cd BluemixPGWeb
- $ mvn package
- Run as follows

```
pasapicella@192-168-1-5:~/temp/BluemixPGWeb$ java -jar ./target/demo-0.0.1-SNAPSHOT.jar

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.2.6.RELEASE)

..


2015-11-16 22:23:20.872  INFO 6633 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Registering beans for JMX exposure on startup
2015-11-16 22:23:20.936  INFO 6633 --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 8080 (http)
2015-11-16 22:23:20.938  INFO 6633 --- [           main] c.i.p.b.pgweb.BluemixPgWebApplication    : Started BluemixPgWebApplication in 3.479 seconds (JVM running for 3.813)

```

- Open a browser and enter http://localhost:8080/ to login

<h2>Screenshots</h2>

<b>Login Screen</b>

![alt tag](https://dl.dropboxusercontent.com/u/15829935/bluemix-docs/images/bluemix-pg-web/img1.png)

<b>Welcome Screen Once Logged In</b>

![alt tag](https://dl.dropboxusercontent.com/u/15829935/bluemix-docs/images/bluemix-pg-web/img2.png)

<b>Perform Table Actions</b>

![alt tag](https://dl.dropboxusercontent.com/u/15829935/bluemix-docs/images/bluemix-pg-web/img3.png)

<b>Multi Command SQL Worksheet</b>

![alt tag](https://dl.dropboxusercontent.com/u/15829935/bluemix-docs/images/bluemix-pg-web/img4.png)

<h2>Coming Soon</h2>

What's coming soon in Release 2

1. Support compose.io PostgreSQL service
2. All other schema objects like "Sequences, etc.."

<br />
![alt tag](https://dl.dropboxusercontent.com/u/15829935/bluemix-docs/images/bluemix-pg-web/PoweredByIBM.png)
<br />
<hr />
<i>
Pas Apicella [pasapi at au1.ibm.com] is a Bluemix Technical Specialist at IBM Australia
</i>