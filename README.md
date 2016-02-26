# kevscript
:warning: For now this is a WIP repository  

For the KevScript v5, look [here](https://github.com/dukeboard/kevoree/tree/master/kevoree-core/org.kevoree.kevscript)

# TODO
List of missing features :
 * integration of https://github.com/dukeboard/kevoree/blob/new_grammar/kevoree-core/org.kevoree.kevscript/src/main/grammar/kevscript.waxeye
 * global variable injection. If a kevscript is run from the scope of a component, it should have access to its scope information such as the name of the current component.
 * dealing with DU ? we need more information than just a version number (except if the version is incremental and managed by the registry as a reverse dictionary to the proper registry information, eg "org.kevoree.registr:kevoree-java-node:1.0.0-SNAPSHOT")
 ```kevs
 var versionSpecs = {
    java: {
      name: 'groupId:artifactId',
      version: '1.2.3'
    },
    javascript: {
      name: 'module-name',
      version: '1.2.5-rc1'
    }
 }
 add myChan: MyChanType/42 versionSpecs // using TypeDefinition v42 but forcing usage of DeployUnits 1.2.3 for Java and 1.2.5-rc1 for JavaScript (other platforms will get the latest release available)
 ```
 
 * network conf ?
 * how to deal with repository configuration (maven, npm, nuget private repo & co) ?
   * **@manuelleduc**: to me it is node related, not kevscript related
   * **@maxleiko**: One might want to dinamically add an external repository @runtime so we have to be able to express it in Kevscript
 * allowing list definition in let elements ? did not found a usecase yet
