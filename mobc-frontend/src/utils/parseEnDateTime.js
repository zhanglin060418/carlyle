export function parseEnDateTime(val) {
  if (val == null)
    return
  const now = new Date(val)
  var year = now.getFullYear();       //年
  var month = now.getMonth() + 1;     //月
  var day = now.getDate();            //日
  var hh = now.getHours();            //时
  var mm = now.getMinutes();          //分
  var ss = now.getSeconds();
  var clock = '';
  if(day < 10)
    clock += "0";
  clock += day + "/";
  if(month < 10)
    clock += "0";
  clock += month + "/";
  clock += year + " "
  if(hh < 10)
    clock += "0";
  clock += hh + ":";
  if (mm < 10) clock += '0';
  clock += mm + ":";
  clock += ss;
  return(clock);
}
