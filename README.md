# Esperanto-Analiziloj
Esperanto-Analiziloj consists of two analyzation tools for the language Esperanto: A tool to analyze individual words and a tool to analyze the structure of a single sentence.

Esperanto uses the word-building grammar from germanic languages. That means that words can be made by sticking other words together, by attaching prefixes and suffixes, and adding endings. The word analyzer tries to find all plausible ways as what individual components constitute a given word. Moreover, it will determine some properties of the word. For instance, whether it is a substantive, has the accusative case, is plural, or in case of a verb determine the transitivity.

The sentence analyzer makes an effort to parse a typed sentence and provide a basic grammatical analysis. It is currently limited to sentences that have only one clause (so one predicate or main verb). It does not support any kind of subordinate clauses.

## Packaging
[**Legu en Esperanto**](README.eo.md)

To package the project use Maven by running the following command from the root directory of the project:
```
mvn package
```
If, for some reason, you need to reinitialize the entire database from the provided XML files from [Reta-Vortaro](http://www.reta-vortaro.de/revo/) you should run the following commands from the root directory.
```
mvn compile
mvn dependency:copy-dependencies
```
Finally execute the `place-database-in-resources.bat` file, or directly run the command it contains:
```
java -cp target/dependency/*;target/classes nl.sogyo.esperanto.persistence.DatabaseResourcePlacer
```
Now you can package as normal by executing
```
mvn package
```

## Running
After having build the project, you can run it by executing the `Esperanto-analiziloj-#.##.jar` file from the command line:
```
java -jar Esperanto-analiziloj-#.##.jar
```
Here `#.##` should be replaced by the version number. Then the application is available at
```
http://localhost:8090
```

## Troubleshooting
If the application won't start after having previously started successfully, it might be because Jetty is still bound to the port. To force Jetty to stop open the command line and run
```
netstat -ano | findstr 8090
```
If no result is found, Jetty is not bound to the port. Any result will have a PID. Find this number and then execute
```
taskkill /pid <PID> /f
```
where <PID> is to be replaced by the found PID.

##About this project
This project is a result of a traineeship at [Sogyo Information Engineering B.V.](https://www.sogyo.nl/) in the Netherlands.