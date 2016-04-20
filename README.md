# kevscript
:warning: For now this is a WIP repository  

For the KevScript v5, look [here](https://github.com/dukeboard/kevoree/tree/master/kevoree-core/org.kevoree.kevscript)

# Architecture
The interpretation is done through a few steps :
 * parsing (antlr4)
 * step0/step1 : *language level validations*
   * Build a list of commands.
   * Everything is consistently typed
   * functions call are valids
   * no variables are used without declaration
   * ** step0 and step1 should be merged**
 * step2 : *registry validation*
   * Every declared instance is check against a registry and a CDN. Each of the instances should be found (using the defined TU/TD constraints) either in the current CDN state or in the registry.
   * Type consistency is ensured. For example a `add` action should not allow a node to be attached to a group.
 * step3 : *instances operation consistency*
   * Check if instances operations matches the TD definition. For example a set on an undefined library key should trigger an error.

# TODO TEST
 * the external context should only by accessible by the "main" script

# TODO
List of missing features :
 * allowing metas on any instance element.
 * testing more advances result chaining operations (eg u.a().b.d[0].d.e[fgh])
 * registry retrieving and error/validation management
 * KMF integration (and consistency validation steps);
 * runtime error reporting
 * ergonomic CLI interface
 * transpiling
 * subnodes integration (at step 3);
 * b64 DU integration ?