# TrustlessFileServer
Simple implementation of file server with merkle tree
* prerequisite: jdk 11, gradle 7.0
* build project: gradle build
* run tests: gradle test --rerun-tasks
* run: java -jar build/libs/TrustlessFileServer-1.0-SNAPSHOT.jar --filePath="icons_rgb_circle.png"

# Sample request hashes:
curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X GET http://localhost:8080/hashes
# Response:
[{"hash":"f7a6d7b7f4e7d8252039c5518ff5e05fe25625be50b5fc9ae94edd217a44ab0e","pieces":17}]

# Sample request piece:
curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X GET http://localhost:8080/piece/f7a6d7b7f4e7d8252039c5518ff5e05fe25625be50b5fc9ae94edd217a44ab0e/8
# Response:
{"content":
"1wSDXYz+dPEXQP9oAYKE7Tz5ttGgCYkD3ile/OXpP4AAAPTqv+BlsRiHgknDtgQv/orRny7+AhAAgB7a+tKLxbYEp8bkJiY7bdm/L7n35ek/QN/NOQMAGYi+8c17X7AQLf8MUxOjP83+B+jzn71XLs+ZAgQZiKkxO7QCtffz27kjyYu/zP0HGAwtQJCJGA36zFtvWIgWSrWF646n/wACAFBzIfnqL7qTZGiXFC/+uvPpZ0Z/AvTfpW4AmL9yedpaQD5iKpDRoO0q/lMc/an9B2Ag5roBwDpAXuI8wLOnTlqItgSABEd/xsVf97/+xocLMCACAGQoxkkaDdqGz2m8e3YjNXc+1fsPMCDfLQ0As9YD8vLM735jNGjDpdj7Hxd/Gf0JMDAzSwOAUaCQoWdPn3QeoKHi4i+jPwGogxYgyPkPgOefK3YYDdpITyfYouXiL4CBe6AFyG3AkKl4ymw0aLPErkyK7T93P/+imL923QcMMDhagIAFMRo0Wk5ohij+Y1pTam5+OOXDBWgALUBAt9jcaTRoY6Q4oenu+S9d/AUweHNLA8C09YC8xbjJ7a93LMSARTuWi78AqMP8lcsPBACAYvuvj3VnzzM42xK8+CtGf9676KgZQFMsBoA5SwGEnSffNhp0QOJehrikLTV6/wEa4cIDAWBxOwAg2k/iUDD9t83oTwD68b/1S/71VcsBhK0vvZjkGMomi10XF38BUKO5lQKABk3gB8+89Ua3JYX+SPHpf7jj6T9AowMAwA9iNOgOrUACwEaK/08/M/oToDm+WykATFsXYKkYDRo7AdQr1Yu/tP8ANMrMSgEA4CHbXv2F0aB1B4AER3/euzhb3P/6Gx8uQHOsuAPgDACwomdPnzQatCYRrmKnJTV3PtX7D9Ak81cur7gD8J2lAVYS7SnPnjppIWqQYu9/XPxl9CdAc9kBAFYlLqhKdVLNoDz10590R66mRu8/QONcWDEAzF+5bAcAeKxnfvcbo0F76GkXfwEwAMsPAc9aEuBxYjSo8wAbF2uY4mVrdz//opi/dt0HDNAs00v/j83L/p92AViXexdniv+z76VsC7mRf/mnYuj557J4v3FgdcdbbxTX3nnPF38DUh39efPDKR8uQPM8UOMPPS4dAE8WTzu/f/NEVu95+PDLxdZDB3z4G5DieYq757908RdAM808LgDYAYB1iHnnNz86k9V73vn7t7uHWFm7CE8p7hg5/AvQWHOPCwAmAcE6RetDXH6Ui2hfifMArN22V9Mc/RmtgAA0z/yVy48NAHOWCNbv+jvvdaeg5CJGg25/veODX4OYohTrlmIABqCRHno6OfS4dACsTfQ/53Y4dvuvj3Vvs2V1thn9CQ==",
"proof":
["6a10a0b8c1bd3651cba6e5604b31df595e965be137650d296c05afc1084cfe1f",
 "956bf86d100b2f49a8d057ebafa85b8db89a0f19d5627a1226fea1cb3e23d3f3",
 "04284ddea22b003e6098e7dd1a421a565380d11530a35f2e711a8dd2b9b5e7f8",
 "c66a821b749e0576e54b89dbac8f71211a508f7916e3d6235900372bed6c6c22",
 "6afb77a17fd0b9b42e1e3c3762d9823797a776090d525d66672fb83c25ea9778"]
}
