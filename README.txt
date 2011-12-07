Should work with any types of java IDEs
===================================
What you need to know:
===================================

- Source folders are core, extra & test

- The main class is xmet.client.Client

- Dependent jars are in the lib folder. Their package files can be found in the
 lib/external folder in most cases. They have been cached there instead of 
 mavening custom repositories.
 
- run with "-admin" switch to run the program in administrator mode

==============================
example setup instructions
==============================
Make sure you don't include generated project files in your commit.
 These will probably change every time someone opens the project and 
 with any luck, differ for machine to machine.

---------------------------
1. Eclipse
---------------------------
1. File->New->Java Project
2. Select  "Create project from existing source" and go select the client folder
3. Press Next
4. Press Finish
To Run the Project file
5. From the menu go Run->Run Configurations
6. Select Java application and press new
7. In the Main tab Browse and select your project name in the "Project:"
 and do the same to select xmet.client.Client in "Main Class:"
8. Press run and it should do the magic.

---------------------------
2. IntelliJ IDEA
---------------------------
1. File -> New Project
2. Select "Create Java Project from Existing Sources" and press Next
3. Browse and select the project folder in "Project Files Location"
4. Probably pick file based project file format. You'll get one less directory.
5. Go Next and then Next
6. Uncheck everything except for lib and then Next and then Next and then Finish
7. Probably cancel any dialog boxes about SVN integration
To Run the peoject file
8. Go Run->Edit Config in the menu
9. Press the + button and select Application
10. Put xmet.client.Client in the Main CLass or locate it via browsing for that matter
11. Under module of jdk or something, Select the project name.
12. Press ok
13. Press the run button. It should work.

---------------------------
3. NetJeans
---------------------------
1. Go File->New Project
2. Select Java
3. Select Java Project with Existing sources
4. Press Next
5. Go and select a project folder and maybe give the project a name
6. Go next. Press Add Folder for source package folders and select the src folder
7. Press next and then Finish
To Run the peoject file
8. From Menu, go Run->Set Project Configuration->Customize
9. Browse or type in xmet.client.Client in Main Class
10. For working directory, select the top folder (i.e. the highest level src folder.) 
11. In the left tree, go up to the "Libraries" node.
12. In there, go add jar/folder...browse to the lib folder of the client and select all the jar files
13. Press ok
14. Press run button. It should work.

---------------------------
4. Ant
---------------------------
1. Run "ant clean makeJar" - will compile and make client.jar file.
2. Run "java -jar client.jar" -  will run client.jar file