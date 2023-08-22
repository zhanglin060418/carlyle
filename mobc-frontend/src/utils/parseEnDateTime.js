export function parseEnDateTime(val) {
  //let now = new Date(val)
  let year = val.substring(0,4)    //年
  let month = val.substring(5,7);     //月
  let day = val.substring(8,10);           //日
  let hhmmss = val.substring(val.length-8,val.length); //时分秒
  let clock = day+"/"+month+"/"+year+" "+hhmmss;
  return clock;
}
