# kevscript
:warning: For now this is a WIP repository  

For the KevScript v5, look [here](https://github.com/dukeboard/kevoree/tree/master/kevoree-core/org.kevoree.kevscript)

# TODO
List of missing features :
 * b64 DU integration ?


## node name definition

either variable name and node is autodefined or @"string" and

```
let node1="anotherNode0"
add node0, @node1: JavaNode
```

Will create to nodes named node0 and anotherNode0 but respectively referenced as node0 and node1 in the kevscript context.

## special methods definition

If a method starts with @ it is a special method of the language. For example ```@random()``` will return a random string.

# DISCUSSED FEATURES
 * Is `let` the right keyword for variable declaration ? (what about `var`, `const` or just `$`)


