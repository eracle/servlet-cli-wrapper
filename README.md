servlet-cli-wrapper
===================

Java Servlet that wraps a generic cli bash command.

Introduction:
=============
Simple, modular, customizable Java Servlet that wraps a cli bash command.
It can be usefull in context of application architecture reengineering in which the internal system comunications between bash commands modules can be performed with a simple curl command.
For instance where some modules needs to be migrated to other platforms and other modules (more system dipendent) needs to temporary remain on the old system. 

Features:
=============
- Allows the copresence of different listening urls which are assigned different command as well;
- The war file don't needs to be recompiled in case of changing of the wrapped commands or adding of new ones;
- The log4j logging system is used for the sub process error handling and for others execution problem happened.

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
- Customize the web.xml file as preferred; (bash command to execute, http url_to_listen)
- Export as a war file (maybe named view.war) ;
- Deploying it on a servlet engine (I experienced with tomcat7);
- Run the servlet engine;
- Test it with curl:
	curl --data-binary @input_file http://localhost:8080/view/url_to_list/

- Enjoy.


Problems:
==============
With apache tomcat7 I observed an issue with the "system-wide" installation (which is the one installed by apt-get install ...):
The problem was related to the fact that tomcat7 daemon process, belongs to the tomcat7 linux user, has not the rights to execute some customized bash commands due to apache tomcat7 security restrictions.
The issue was solved by downloading the binary distribution from the ASF official web site and using jsvc apache utility to set it up to a system service daemon. Which jsvc allows to rewrite some daemon configuration constraint like the execution linux user.

Todo:
==============
- Extend the offered functionality to the http GET calls as well;
- extends the logger class with viewLogger implementation which in every log call made is show the specific command (the web.xml ones) that generated it;
- Mavenizing it.
