servlet-cli-wrapper
===================

Java Servlet that wraps a generic cli bash command.

Introduction:
=============
Simple, modular, customizable Java Servlet that wraps a cli bash command.
It can be usefull in context of application architecture reengineering in which the system comunications between modules can be performed with a simple curl command.
For instance where some modules needs to be migrated to other platforms and other modules (more system dipendent) needs to temporary remain on the old system. 

Features:
=============
- Allows the copresence of many listening command to different urls;
- The war file don't needs to be recompiled in case of changing of the wrapped command;


Implementation details:
=============
- The bash command is passed once for all through the web.xml configuration file to the servlet engine at deployment time; 
- It reads the content of the http post and passes it to the standard input of the process instantiated by the command;
- Returns the stdout of the command in the http post response;
- It uses Commons IO - Apache Commons for the io comunications with the sub process created.


Use:
=============
- Clone the repository;
- Import into Eclipse in a new dynamic web project;
- Download from the web the Commons IO - Apache Commons library and importing it on the web project;
- Customize the web.xml file as preferred; (bash command to execute, http url to listen)
- Export as a war file;
- Deploying it on a servlet engine (I experienced with tomcat7);
- Run the servlet engine;
- Test it with curl:
    curl --data "input text" localhost:8080/url_to_listen/

- Enjoy.


Problems:
==============
With apache tomcat7 I observed a problem with the "system-wide" installation (which is the one installed by apt-get install ...):
The problem was related to the fact that tomcat7 daemon process, belongs to the tomcat7 linux user, has not the rights to execute some customized bash commands due to apache tomcat7 security restrictions.
The issue was solved by downloading the binary distribution from the ASF official web site and using jsvc apache utility to set it up to a system service daemon. While jsvc allows to rewrite some daemon configuration constraint like the linux user.

Todo:
==============
- A better implementation of the log system, maybe using log4j;
- Extend the offered functionality to the http get calls as well;
- Mavenizing it.
