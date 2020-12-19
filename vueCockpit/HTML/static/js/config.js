//基本配置文件
const CONFIG = {
    //服务器地址
    SERVER : "http://screen.yidun-yun.com/apis",
    //正式
   


};

 var ctxPath=CONFIG.SERVER;

var t;
var hh;
function lunbo(obj, sobj, height) {
    hh = 0;
    clearInterval(t);
    function tableLunbo1() {
        var he1 = $(obj).find(sobj).length * height;

        var h1 = he1 - $(obj).height();
        hh += 1;
        if (hh >= h1) {
            hh = 0
        }
        $(obj).scrollTop(hh);
    }
    t = setInterval(tableLunbo1, 30);

    $(obj).mouseenter(function () {
        clearInterval(t)
    });
    $(obj).mouseleave(function () {
        clearInterval(t);
        t = setInterval(tableLunbo1, 30)
    })
}



var docEl2 = window.document.documentElement;
var clientWidth2 = docEl2.clientWidth;
function clearCache() {
  fetch(ctxPath + '/jingJiMuDuController/clearCache.action', {method: 'delete'}).then(res => res.json()).then(data => {
      if (data.success) {console.log(data.message)}
  })
}

function getDataStr(data,name) {
  let str=[]
  for (let i = 0; i < data.length; i++) {
    str.push(data[i][name])
  }
  return str
}

function getLegendData(array, key) {
  var resObj = {};
  for (var i = 0; i < array.length; i++) {
      resObj[array[i][key]] = array[i];
  }
  return resObj
}

function getAllData(array, key) {
  var size = 0;
  for (let i in array) {
      size += Number(array[i][key])
  }
  return size
}
