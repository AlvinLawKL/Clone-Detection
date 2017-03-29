Clone detection for Android Application based on hashed class signatures.

# Version
* Version_1: Clone detection in memory
* Version_2: Clone detection with the use of database (mysql)
* Version_3: Switch to another database (PostgreSQL)
* Version_4: Improve clone detection by sorting class signatures within each class before hashing
* Version_5: Clone detection without replacing identifiers

# Classes
* Unzipper: Extract .dex files from APKs, as well as extract .class files from .jar files
* DexToJar: Convert .dex files into .jar files, require dex2jar to be installed
* ClassStructure: Print all class signatures of .class files into text files
* Hash: Hash the class signatures of each .class file
* ConnectDB: Insert hashed values into database
