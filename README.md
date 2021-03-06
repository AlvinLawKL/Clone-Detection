Clone detection software for Android Applications, hashing class signatures from APKs and store them into database (Software Bertillonage).

* Code: https://github.com/AlvinLawKL/Clone-Detection

# Usage
* Create database and table with the following columns (Name, Type) in PostgreSQL:
    * path, varchar(500)
    * hash, varchar(500)
    * app, varchar(500)
* Change the following variables in Processor.java
    * dirPath: Absolute file path of folder that contain APKs.
    * destPath: Absolute file path of output folder. Must not be the same as dirPath.
    * d2jLocation: Absolute file path of dex2jar tool.
    * javapLocation: Absolute file path of javap command.
    * databaseName: The name of database created above.
* Compile: javac Processor.java Version_"?"/\*.java ANTLR/\*.java
* Run: java Processor

# Version
* Version_1: Clone detection in memory, replace identifiers
* Version_2: Clone detection with the use of database (mysql), replace identifiers
* Version_3: Switch to another database (PostgreSQL), replace identifiers
* Version_4: Improve clone detection by sorting within each class signature, replace identifiers
* Version_5: Clone detection without replacing identifiers

# Classes
* Processor: Main file to use other class files
* Unzipper: Extract .dex files from APKs, as well as extract .class files from .jar files
* DexToJar: Convert .dex files into .jar files, require the tool dex2jar
* ClassStructure: Output all class signatures of .class files into text files
* Hash: Hash the class signatures of each .class file
* ReplaceIdent: Replace the identifers in class signatures, require files in ANTLR folder
* ConnectDB: Insert hash values into database