# kevscript
:warning: For now this is a WIP repository  

For the KevScript v5, look [here](https://github.com/dukeboard/kevoree/tree/master/kevoree-core/org.kevoree.kevscript)

# TODO
List of missing features :
 * integration of https://github.com/dukeboard/kevoree/blob/new_grammar/kevoree-core/org.kevoree.kevscript/src/main/grammar/kevscript.waxeye
 * global variable injection. If a kevscript is run from the scope of a component, it should have access to its scope information such as the name of the current component.
 * dealing with DU ? we need more informations than just a version number (except if the version is incremental and managed by the registry as a reverse dictionary to the proper registry information, eg "org.kevoree.registr:kevoree-java-node:1.0.0-SNAPSHOT")
 * network conf ?
 * how to deal with repository configuration (maven, npm, nuget private repo & co) ?
 * allowing list definition in let elements ?
