Quake JSON Parser Project
--
## Rationale
1. **Identify the structure of the log file:** The first step in parsing the log file is to understand its structure. We assume that the log file has entries for each kill, the player responsible, the player who was killed, and the cause of the kill. 
2. **Set up the data structures:** You'll need to define a few classes or structures to store the parsed information. 
3. **Parse the log file:** Read the log line by line, and parse the lines to extract the relevant information. 
4. **Convert parsed data to JSON:** Once the data is parsed, a JSON library (in this case, Gson) is used to convert your data structures to JSON format.
