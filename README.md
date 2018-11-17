# gitUpdateListener

This program checks if a certain branch on git is behind and in case it is, it updates (pulls) it. 
After pulling it can run a bash script (copy files and restart server, whatever you want).

In the end a specified list of emailaddresses get a notification about the update

It runs by using your local ssh key, so pls make sure you provided your SSH key to (github, gitlab, ...)
currently only the user key in home/usr/.ssh/.id_rsa.pub is used <br> 
You have to setup a config file and provide its path as command line argument like so:

```
java -jar executable.jar PATH-TO-CONFIG
```

You can see an example of the config in the 

```
src/main/resources/config_example.properties
```

[x] setup Maven <br>
[x] setup git with gitignore <br>
[x] open Repo <br>
[x] check for updates on branch <br>
[x] execute bash script <br>
[x] add email notification <br>


I am glad to help if any problems occurr
