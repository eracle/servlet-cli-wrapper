servlet-cli-wrapper
===================

Java Servlet that wraps a generic cli command.

Simple, modular, customizable Java Servlet that wraps a cli bash command.

Features:
=============
- The command is passed through the web.xml file, so it don't needs to be recompiled in case of changing of the wrapped command.
- It reads the content of the http post and passes it to the standard input of the process instantiated by the cmd.
- Returns the stdout of the command in the http post response.
- It uses Commons IO - Apache Commons for the io comunications with the sub process created.


Use:
=============
- clone the repository;
- import in eclipse in a new dynamic web project;
- downloading from the web the Commons IO - Apache Commons library and importing it on the web project;
- changing the web.xml file as preferred; (cmd, url to listen)
- export as a war file;
- deploying it on a serlet engine (tomcat7 in my case);
- run the serlet engine;
- test it with curl --data "process input text" localhost:8080/url_to_listen/

enjoy

