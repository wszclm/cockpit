var mapUrl='my3dUrl'
// 'http://103.219.33.242:6080'
var my3dConfig = {

    // 图层树节点
    toc: [{
        name: "专题",
        open: true,
        nocheck: true,
        children: [{
            name: "工业用地注记",
            checked: true,
            dataType: "ArcGisMapServerAnnProvider",
            dataUrl: 'http://103.219.33.242:6080/arcgis/rest/services/TL/tlqy/MapServer',
            minHeight: 2000
        }, {
            name: "工业地块倾斜模型1-2",
            checked: true,
            dataType: "Cesium3DTileset",
            dataUrl: 'http://103.219.33.242:8088/3ddata/Qu1_2_50M_OSGB1/tileset.json',
            minHeight: 2000
        }, {
            name: "工业地块倾斜模型3",
            checked: true,
            dataType: "Cesium3DTileset",
            dataUrl: 'http://103.219.33.242:8088/3ddata/Qu3_50M_OSGB1/tileset.json',
            minHeight: 2000
        }, {
            name: "工业地块倾斜模型1-2bu",
            checked: true,
            dataType: "Cesium3DTileset",
            dataUrl: 'http://103.219.33.242:8088/3ddata/Qu1_2_Bu_OSGB/tileset.json',
            minHeight: 2000
        }, {
            name: "工业用地矢量面",
            checked: true,
            dataType: "MyArcGisMapServerImageryProvider",
            dataUrl: 'http://103.219.33.242:6080/arcgis/rest/services/TL/tlqy/MapServer'
        }]
    }, {
        name: "底图",
        open: true,
        nocheck: true,
        children: [{
            name: "桐庐影像",
            checked: true,
            dataType: "WebMapTileServiceImageryProvider",
            dataUrl: 'http://103.219.33.242:6080/arcgis/rest/services/TL/TLIMAGE/MapServer/WMTS?'
        }, {
            name: "全球影像图片",
            checked: true,
            dataType: "SingleTileImageryProvider",
            dataUrl: 'static/images/globeTif/Earth.jpg'
        }, {
            name: "地形",
            checked: true,
            dataType: "CesiumTerrainProvider",
            dataUrl: 'http://103.219.33.242:8088/3ddata/terrain'
        }]
    }],

    // 工业用地名称
    industrialLandName: '工业用地矢量面',

    // 工业用地查询服务地址
    industrialLandUrl: 'my3dUrl/arcgis/rest/services/TL/tlqy/MapServer',

    // 地块编号字段名
    fieldBlockId: '地块编号',

    // 企业名称字段名
    fieldCompanyName: '企业名称（',

    // 默认位置
    defaultPosition: [119.73, 29.818, 20000]
};