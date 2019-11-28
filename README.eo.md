# Esperanto-Analiziloj
[**Read in English**](README.md)

Esperanto-Analiziloj konsistas el du analiziloj por la lingvo Esperanto: Ilo por analizi apartajn vortojn kaj ilo por analizilo la strukturon de unu frazo.

Esperanto uzas la vortkreadan gramatikon de ĝermanaj lingvoj. Tio signifas, ke vortoj povas esti kreata per kunmetado de aliaj vortoj, per almetado de prefiksoj kaj sufikoj, kaj per aldonado de finaĵoj. La vort-analizilo provas trovi ĉiujn senchavajn manierojn, kiel la komponantoj konsistigas la donitan vorton. Plue, ĝi determinos kelkajn trajtojn de la vorto. Ekzemple, ĉu ĝi estas substantivo, havas la akuzativon, estas plurala aŭ kaze de verbo la transitivecon.

La fraz-analizilo penas analizi la gramatikon de la tajpita frazo kaj provizas bazan gramatikan strukturon. Ĝi nun estas limigita de frazoj, kiuj konsistas al nur unu propozicio (do nur unu predikato aŭ ĉefverbo). Ĝi ne subtenas ian ajn subordigitan propozicion.

## Pakado
Por paki la projekton uzu Maven kaj plenumu la jenan komandon de la radika dosierujo de la projekto:
```
mvn package
```
Se ial vi bezonas reiniciati la tutan datumbazon el la provizitaj XML-dosieroj de [Reta-Vortaro](http://www.reta-vortaro.de/revo/), vi plenumu la jenajn komandojn el la radika dosierujo.
```
mvn compile
mvn dependency:copy-dependencies
```
Fine, startigu la `place-database-in-resources.bat`-dosieron aŭ rekte plenumu la komandon, kiujn ĝi enhavas:
```
java -cp target/dependency/*;target/classes nl.sogyo.esperanto.persistence.DatabaseResourcePlacer
```
Nun vi povas ordinare paki per la jena komando:
```
mvn package
```

## Funkciigi
Pakinte la projekton, vi funkciigu ĝin per la startigo de la `Esperanto-analiziloj-#.##.jar`-dosiero de la komandilo:
```
java -jar Esperanto-analiziloj-#.##.jar
```
Ĉi tie `#.##` estu anstataŭigita per la versinumero. Poste la aplikaĵo estos alirebla ĉe
```
http://localhost:8090
```

## Problemsolvado
Se la aplikaĵo ne startas, dum ĝi antaŭe sukcese startis, povas esti, ke Jetty estas ankoraŭ ligita al la pordo. Por perforte ĉesigi Jetty, malfermu la komandilon kaj plenumu
```
netstat -ano | findstr 8090
```
Se neniu rezultas, tiam Jetty ne ligiĝas al la pordo. Iu ajn rezulto havos PID (procezan identigilon). Trovu ĉi tiun numeron kaj tiam plenumu
If no result is found, Jetty is not bound to the port. Any result will have a PID. Find this number and then execute
```
taskkill /pid <PID> /f
```
kie <PID> estu anstataŭigita per la trovita PID.

##Pri ĉi tiu projekto
Ĉi tiu projekto estas rezulto de traniĝado ĉe [Sogyo Information Engineering B.V.](https://www.sogyo.nl/) en Nederlando.