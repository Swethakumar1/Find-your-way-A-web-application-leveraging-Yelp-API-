# Find-your-way-A-web-application-leveraging-Yelp-API-

This project is a multitier server application using Servlets as well as Enterprise
Java Beans (EJBs). The application developed helps a user to search for things to do anything like, by specifying
his/her preferences including distance, location and best reviewed places. Yelp API is used to generate response
and send to the servlet which in turn gives the results to the client or the user. 

External jars:
Scribe-1.2.1.jar
This is open source jar for the OAuth used for accessing data in yelp happens via OAuth. When I browsed
online, I found the scribe jar being recommended by many developers for accessing APIs of LinkedIn,
Facebook, yelp etc.
org.Json.jar
The response from yelp is a JSON object leading to the choice of selecting this option for parsing the Json data.
