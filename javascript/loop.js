var crontime = 1000
var cronhandle = setTimeout(fnmaincron, 100)


async function fnmaincron() {
  clearTimeout(cronhandle)
  const event = new Date();
  console.log(event.toString());
  cronhandle = setTimeout(fnmaincron, crontime)
}
