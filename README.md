# Alfresco -> CoreNLP PoC

This project is a first crack at using Stanford CoreNLP to enhance search in Alfresco.  Right now it only does one thing.  It extracts named entities from a body of text.  It takes the "person" entities and saves them as metadata so they are searchable as people.  There is currently no UI, I have been testing this using the Javascript Console extension to call the action.  To get this running:

1.  Grab the source, build the jars.  I haven't checked in the whole All-in-one project, so you may need to tweak things to get it to build.

2.  Grab a copy of CoreNLP 3.7.0.  Start the server as defined in the docs.

3.  Configure the Alfresco CoreNLP integration to point to your CoreNLP server.  This is currently done in the context files.

4.  Grab a copy of the models for your language from the CoreNLP site and put them on a classpath where Alfresco can find them

I've written a brief introductory post on this project, and will continue to write about it when (if) it expands into something useful.  Right now it's just a toy project for me to explore some stuff around CoreNLP.
   
  
 
