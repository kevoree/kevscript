# V5 Translator
## Presentation
This module offers a tool to convert v6 kevscript to v5 kevscript.


## Example
**V6 : **
```

    add web : JavascriptNode/nodeVersion
    add web.chart1: Chart/2
    add web.chart2: Chart/2
    add web.chart3: Chart/2
    attach group web
    return web
}

function initEdison(nodeName, group, nodeVersion) {
    let edison=@nodeName
    add edison : JavascriptNode/nodeVersion
    add edison.lcd : eu_heads.HeadsLCDDisplayComp/2
    add edison.led : eu_heads.HeadsDigitalWriteComp/2
    add edison.noise : eu_heads.HeadsAnalogSensorComp/2
    add edison.light : eu_heads.HeadsAnalogSensorComp/2
    set edison.lcd.test = 'false'
    set edison.led.pin = '7'
    set edison.led.test = 'false'
    set edison.light.pin = '2'
    set edison.light.test = 'false'
    set edison.noise.test = 'false'
    set edison.temp.pin = '1'
    set edison.temp.test = 'false'
    attach group edison
}

function bindByChan(port0, port1, chanName, host, uuid) {
    let chan=@chanName
    add chan:  RemoteWSChan/5
    set chan.host = host
    set chan.uuid = uuid
    bind chan port0 port1
}

add mainGroup: RemoteWSGroup
set mainGroup.host = confs.group.host
set mainGroup.path = confs.group.path
set mainGroup.answerPull/edison2 = "false"
set mainGroup.answerPull/edison3 = "false"
set mainGroup.answerPull/edison4 = "false"
set mainGroup.answerPull/edison5 = "false"

for (nodeName in ["edison1", "edison2", "edison3", "edison4", "edison5"]) {
    initEdison(nodeName, mainGroup, confs.nodes.javascript.version)
}

for(elem in [{nodeName : "web1", target:edison1}, {nodeName : "web2", target:edison2}, {nodeName : "web3", target:edison3}]) {
    let node = initWebNode(elem.nodeName, mainGroup, confs.nodes.javascript.version)
    bindByChan(node.chart2.input, elem.target.temp.out, "chan", conf.chan.host, "edison1Temp")
    bindByChan(node.chart3.input, elem.target.light.out, "chan1", conf.chan.host, "edison1Light")
    bindByChan(node.chart1.input, elem.target.noise.out, "chan2", conf.chan.host, "edison1Noise")
}
```

**V5**

```
TODO
```