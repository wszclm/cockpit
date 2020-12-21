var map = new AMap.Map('map', {
    center: [119.602017, 29.830088],
    layers: [new AMap.TileLayer.Satellite()],
    showIndoorMap:false,
    showLabel:false,
    resizeEnable: true,
    rotateEnable:true,
    pitchEnable:true,
    zoom: 16.77,
    pitch:39.473,
    rotation:0.658,
    viewMode:'3D',//开启3D视图,默认为关闭
    // buildingAnimation:true,//楼块出现是否带动画
    // expandZoomRange:true,
});
map.on('complete', function () {
    setTimeout(() => {
        init();
    }, 100)
});
map.on('click', function (e) {
    console.log(e.lnglat.getLng() + ',' + e.lnglat.getLat())

});

var infoWindow;
function init() {
    fetch('./../HTML/static/file/1639.geojson').then(res => res.json()).then(geojson => {
        var geoJson = new AMap.GeoJSON({
            geoJSON: geojson.features,   // GeoJSON对象
            getPolygon: function (geojson, lnglats) {//还可以自定义getMarker和getPolyline
                return new AMap.Polygon({
                    path: lnglats,
                    name: geojson.properties.name,
                    strokeColor: "#de0000", //线颜色
                    strokeOpacity: 1, //线透明度
                    strokeWeight: 2,    //线宽
                    fillColor: "#00ddff", //填充色
                    fillOpacity: 0.1//填充透明度
                });
            },


        });
        geoJson.on('click', function (e) {
            //构建信息窗体中显示的内容
            var info = [];
            info.push("<div><div><img style=\"float:left;\" src=\" https://webapi.amap.com/images/autonavi.png \"/></div> ");
            info.push(`<div style="padding:0px 0px 0px 4px;"><b>${e.target.w.name||''}</b>`);
            info.push("地址 :xxx路</div></div>");
            infoWindow = new AMap.InfoWindow({
                content: info.join("<br/>")  //使用默认信息窗体框样式，显示信息内容
            });
            // infoWindow.on('open',showInfoOpen)
            // infoWindow.on('close',showInfoClose)
            infoWindow.open(map, [e.lnglat.lng,e.lnglat.lat]);

        })
        map.add(geoJson);

    })
}
// function closeInfo() {
//     infoWindow.close();
// }
