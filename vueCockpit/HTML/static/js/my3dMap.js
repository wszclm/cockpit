var my3dMap = {
    // ---------私有变量---------
    _config: null,
    _viewer: null,
    _mapLayer: null,
    queryhandler: null,
    _myArcGisMapServerImageryLayers: {},

    // ---------公有事件---------
    // 点查事件
    identifyEvent: new Cesium.Event(),

    // ---------公有方法---------
    // 初始化
    init: function (config) {
        var me = this;
        me._config = config;

        // 创建Viewer
        me._createViewer();

        // 绑定事件
        me._bindEvents();

        // 创建临时层
        if (me._mapLayer == null) {
            me._mapLayer = new Cesium.CustomDataSource;
            me._viewer.dataSources.add(me._mapLayer);
        }
    },

    // 根据地块编号定位
    locateByBlockId: function (blockId) {
        var me = this;

        var where = me._config.fieldBlockId + " = '" + blockId + "'";

        Cesium.Resource.post({
            url: my3dConfig.industrialLandUrl + "/0/query",
            data: Cesium.objectToQuery({
                where: where,
                outFields: '*',
                f: 'json'
            }),
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            responseType: 'json'
        }).then(function (body) {
            // 高亮显示选中要素
            me._zoomToMap(body.features, false);

            // 输出变量
            //console.log(body.features, JSON.stringify(body.features));
        }).otherwise(function (error) {
            // an error occurred
            console.error(error);
        });
    },

    // 根据企业名称定位
    locateByCompanyName: function (blockId) {
        var me = this;

        var where = me._config.fieldCompanyName + " like '%" + blockId + "%'";

        Cesium.Resource.post({
            url: my3dConfig.industrialLandUrl + "/0/query",
            data: Cesium.objectToQuery({
                where: where,
                outFields: '*',
                f: 'json'
            }),
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            responseType: 'json'
        }).then(function (body) {
            // 高亮显示选中要素
            me._zoomToMap(body.features, false);

            // 输出变量
            //console.log(body.features, JSON.stringify(body.features));
        }).otherwise(function (error) {
            // an error occurred
            console.error(error);
        });
    },

    // 切换图层可见性
    toggleLayerVisibility: function (treeNode) {
        var me = this;

        me._toggleDataVisibility(treeNode);
    },

    // 根据分类显示地块
    showBlockByClass: function (values) {
        var me = this;
        var viewer = me._viewer;
        var layer = me._myArcGisMapServerImageryLayers[me._config.industrialLandName];

        if (!layer) {
            return;
        }

        // 获取原图层的相关属性
        var index = layer._layerIndex;
        var node = layer.node;

        // 移除原图层
        viewer.imageryLayers.remove(layer);

        // 获取图层条件
        var showLayers = '-1';
        var condition = "1=2";
        if (values.length > 0) {
            showLayers = 0;
            condition = "现状使 in (" + values.map(
                function (item) {
                    return "'" + item + "'";
                }
            ).join(",") + ")";
        }

        // 重新添加新图层
        var arcGisMapServerImageryProvider = new MyArcGisMapServerImageryProvider({
            url: node.dataUrl,
            layers: showLayers,
            layerDefs: {
                "0": condition
            }
        });
        layer = viewer.imageryLayers.addImageryProvider(arcGisMapServerImageryProvider, index);
        me._myArcGisMapServerImageryLayers[node.name] = layer;
        layer.node = node;
        me._setLayerShow(layer);
    },

    // 飞到新地点
    flyTo: function (lng, lat, height) {
        var me = this;
        var viewer = me._viewer;

        viewer.camera.flyTo({
            destination: new Cesium.Cartesian3.fromDegrees(lng, lat, height || 500),
            duration: 1
        });
    },

    // ---------私有---------
    // 创建Viewer
    _createViewer: function () {
        var me = this;
        var config = me._config;

        // 加载球
        var viewer = me._viewer = new Cesium.Viewer("map", {
            imageryProvider: false, // 是否显示影像底图
            fullscreenButton: false, // 是否显示全屏控件
            geocoder: false, // 是否显示地名查找控件
            homeButton: false, // 是否显示Home控件
            navigationHelpButton: false, // 是否显示帮助信息控件
            sceneModePicker: false, // 是否显示投影方式控件
            baseLayerPicker: false, // 是否显示图层选择控件
            timeline: false, // 是否显示时间线控件
            animation: false, // 是否显示左下角的动画控件
            infoBox: false, // 是否显示信息框
            selectionIndicator: false
        });

        // 隐藏版权信息
        viewer._cesiumWidget._creditContainer.style.display = "none";

        // 加载数据
        me._loadData();

        // 初始位置
        viewer.camera.setView({
            destination: Cesium.Cartesian3.fromDegrees.apply(Cesium.Cartesian3, config.defaultPosition)
        });
    },

    // 加载数据
    _loadData: function () {
        var me = this;
        var config = me._config;
        var zNodes = me._config.toc;

        me._loadDataRecursively(zNodes);
    },

    // 递归加载数据
    _loadDataRecursively: function (nodes) {
        var me = this;

        for (var i = nodes.length - 1; i >= 0; i--) {
            var node = nodes[i];

            if (node.children) {
                me._loadDataRecursively(node.children);
            }
            else {
                if (node.dataType) {
                    me._loadDataByDataType(node);
                }
            }
        }
    },

    // 根据数据类型加载数据
    _loadDataByDataType: function (node) {
        var me = this;
        var viewer = me._viewer;
        var url = node.dataUrl;

        switch (node.dataType) {
            case 'CesiumTerrainProvider':
                var terrainProvider = new Cesium.CesiumTerrainProvider({
                    url: url
                });
                viewer.scene.terrainProvider = terrainProvider;
                break;
            case 'WebMapTileServiceImageryProvider':
                var imageryProvider = new Cesium.WebMapTileServiceImageryProvider({
                    url: url,
                    layer: '影像',
                    style: 'default',
                    tileMatrixSetID: 'EPSG:4490',
                    format: 'image/png',
                    tilingScheme: new Cesium.GeographicTilingScheme(),
                    maximumLevel: 17,
                    tileMatrixLabels: ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17']
                });
                var layer = viewer.imageryLayers.addImageryProvider(imageryProvider);
                node.dataId = me._guid();
                layer.node = node;
                break;
            case 'SingleTileImageryProvider':
                var singleTileImageryProvider = new Cesium.SingleTileImageryProvider({
                    url: url
                });
                var layer = viewer.imageryLayers.addImageryProvider(singleTileImageryProvider);
                node.dataId = me._guid();
                layer.node = node;
                break;
            case 'MyArcGisMapServerImageryProvider':
                var arcGisMapServerImageryProvider = new MyArcGisMapServerImageryProvider({
                    url: url
                });
                var layer = viewer.imageryLayers.addImageryProvider(arcGisMapServerImageryProvider);
                me._myArcGisMapServerImageryLayers[node.name] = layer;
                node.dataId = me._guid();
                layer.node = node;
                break;
            case 'ArcGisMapServerAnnProvider':
                // 标注层
                var lablayer = new Cesium.CustomDataSource;
                viewer.dataSources.add(lablayer);
                node.dataId = me._guid();
                lablayer.node = node;

                // 需标注的要素层
                var laburllayer = null;
                viewer.imageryLayers._layers.some(function (layer) {
                    if (layer.node != null) {
                        if (layer.node.dataUrl == url) {
                            laburllayer = layer;
                            return true;
                        }
                    }
                });
                me._AddLabel(lablayer, laburllayer, url);
                break;
            case 'Cesium3DTileset':
                var cesium3DTileset = new Cesium.Cesium3DTileset({
                    url: url
                });
                viewer.scene.primitives.add(cesium3DTileset);
                node.dataId = me._guid();
                cesium3DTileset.node = node;
                break;
        }
    },

    // 切换数据可见性
    _toggleDataVisibility: function (treeNode) {
        var me = this;
        var viewer = me._viewer;
        var checked = treeNode.checked;
        var dataId = treeNode.dataId;

        switch (treeNode.dataType) {
            case 'CesiumTerrainProvider':
                if (checked) {
                    me._loadDataByDataType(treeNode);
                }
                else {
                    viewer.scene.terrainProvider = new Cesium.EllipsoidTerrainProvider({});
                }
                break;
            case 'WebMapTileServiceImageryProvider':
                viewer.imageryLayers._layers.some(function (layer) {
                    if (layer.node.dataId == dataId) {
                        layer.node.checked = checked;
                        me._setLayerShow(layer);
                        return true;
                    }
                });
                break;
            case 'SingleTileImageryProvider':
                viewer.imageryLayers._layers.some(function (layer) {
                    if (layer.node.dataId == dataId) {
                        layer.node.checked = checked;
                        me._setLayerShow(layer);
                        return true;
                    }
                });
                break;
            case 'MyArcGisMapServerImageryProvider':
                viewer.imageryLayers._layers.some(function (layer) {
                    if (layer.node.dataId == dataId) {
                        layer.node.checked = checked;
                        me._setLayerShow(layer);
                        return true;
                    }
                });
                break;
            case 'ArcGisMapServerAnnProvider':
                viewer.dataSources._dataSources.some(function (layer) {
                    if (layer.node.dataId == dataId) {
                        layer.node.checked = checked;
                        me._setLayerShow(layer);
                        return true;
                    }
                });
                break;
            case 'Cesium3DTileset':
                viewer.scene.primitives._primitives.some(function (layer) {
                    if (layer.node.dataId == dataId) {
                        layer.node.checked = checked;
                        me._setLayerShow(layer);
                        return true;
                    }
                });
                break;
        }
    },

    // 设置图层的可见性
    _setLayerShow: function (layer) {
        if (Cesium.defined(layer.node) && Cesium.defined(layer.node.checked)) {
            if (layer.node.checked) { // 已勾选
                var show = true;
                if (Cesium.defined(layer.node.minHeight) && height > layer.node.minHeight) {
                    show = false;
                }

                if (Cesium.defined(layer.node.maxHeight) && height < layer.node.maxHeight) {
                    show = false;
                }
                layer.show = show;
            }
            else { // 未勾选
                layer.show = false;
            }
        }
    },

    // 绑定事件
    _bindEvents: function () {
        var me = this;
        var viewer = me._viewer;

        // 绑定地图缩放事件
        me._bindCameraMoveEndEvent();

        // 绑定点查事件
        me._bindIdentifyEvent();
        //绑定量算事件
        me._bindMeasureEvent();
    },

    // 绑定地图缩放事件
    _bindCameraMoveEndEvent: function () {
        var me = this;
        var viewer = me._viewer;

        // 监听地图缩放，控制地图内容是否显示
        viewer.scene.camera.moveEnd.addEventListener(function () {

            // 获取当前相机高度
            height = Math.ceil(viewer.scene.camera.positionCartographic.height);

            viewer.imageryLayers._layers.forEach(function (layer) {
                me._setLayerShow(layer);
            });

            viewer.scene.primitives._primitives.forEach(function (layer) {
                me._setLayerShow(layer);
            });

            viewer.dataSources._dataSources.forEach(function (layer) {
                me._setLayerShow(layer);
            });
        });
    },

    // 绑定点查事件
    _bindIdentifyEvent: function () {
        var me = this;
        var viewer = me._viewer;

        me.queryhandler = new Cesium.ScreenSpaceEventHandler(viewer.scene.canvas);
        me.queryhandler.setInputAction(
          pickImageryLayerFeature,
          Cesium.ScreenSpaceEventType.LEFT_CLICK
        );

        function pickImageryLayerFeature(e) {
            var windowPosition = e.position;

            if (!Cesium.defined(viewer.scene.globe)) {
                return;
            }

            var scene = viewer.scene;
            var pickRay = scene.camera.getPickRay(windowPosition);
            var imageryLayerFeaturePromise = scene.imageryLayers.pickImageryLayerFeatures(
              pickRay,
              scene
            );
            if (!Cesium.defined(imageryLayerFeaturePromise)) {
                return;
            }

            Cesium.when(
              imageryLayerFeaturePromise,
              function (features) {

                  if (!Cesium.defined(features) || features.length === 0) {
                      return;
                  }

                  // 选择第一条记录
                  var feature = features[0];

                  // 高亮显示选中要素
                  me._zoomToMap(feature.data, false).then(function () {
                      var viewModel = me._viewer.selectionIndicator.viewModel;
                      var x = viewModel._screenPositionX;
                      var y = viewModel._screenPositionY;

                      // 触发点查事件
                      me.identifyEvent.raiseEvent(feature, x, y);
                  });
              },
              function () {

              }
            );
        }
    },
    // 绑定量算事件
    _bindMeasureEvent: function () {
        var me = this;
        var viewer = me._viewer;
        $('#btn_measure_length').bind('click', function () {
            if (me.queryhandler != null) {
                me.queryhandler.destroy();
            }
            measureLineSpace(viewer);
        });
        $('#btn_measure_Height').bind('click', function () {
            if (me.queryhandler != null) {
                me.queryhandler.destroy();
            }
            measureHeight(viewer);
        });


        $('#btn_measure_area').bind('click', function () {
            if (me.queryhandler != null) {
                me.queryhandler.destroy();
            }
            measureAreaSpace(viewer);
        });
        $('#btn_measure_clear').bind('click', function () {
            clearDrawingBoard();
            me._bindIdentifyEvent();
        });

    },
    // 生成唯一值
    // RFC4122, version 4 ID
    _guid: function () {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
    },

    // 高亮显示选中要素
    _zoomToMap: function (esriFeatureOrFeatures, onlyseeline) {
        var me = this;
        me._mapLayer.entities.removeAll();

        if (esriFeatureOrFeatures instanceof Array) {
            // 要素数组
            var result = [];
            esriFeatureOrFeatures.forEach(function (esriFeature) {
                var entities = me.__zoomToMapByFeature(esriFeature, onlyseeline);
                result = result.concat(entities);
            });
            return me._viewer.flyTo(result);
        }
        else {
            // 单个要素
            var result = me.__zoomToMapByFeature(esriFeatureOrFeatures, onlyseeline);
            return me._viewer.flyTo(result);
        }
    },

    // 高亮显示选中单个要素
    __zoomToMapByFeature: function (esriFeature, onlyseeline) {
        var me = this;

        var entities = [];
        for (var a = 0; a < esriFeature.geometry.rings.length; a++) {
            var maximumHeights = [];
            var minimumHeights = [];
            var points = [];
            for (var k = 0; k < esriFeature.geometry.rings[a].length; k++) {
                points.push(esriFeature.geometry.rings[a][k][0]);
                points.push(esriFeature.geometry.rings[a][k][1]);
            }
            var ev = null;
            if (!onlyseeline) {
                for (var k = 0; k < points.length / 2; k++) {
                    maximumHeights.push(80);
                    minimumHeights.push(1);
                }
                var flog = true;
                var x = 1;
                ev = me._mapLayer.entities.add({
                    name: "动态范围",
                    wall: {
                        positions: Cesium.Cartesian3.fromDegreesArray(points),
                        maximumHeights: maximumHeights,
                        minimumHeights: minimumHeights,
                        clampToGround: true,
                        material: new Cesium.ColorMaterialProperty(new Cesium.CallbackProperty(function () {
                            if (flog) {
                                x = x - 0.02;
                                if (x <= 0) {
                                    flog = false;
                                }
                            } else {
                                x = x + 0.02;
                                if (x >= 1) {
                                    flog = true;
                                }
                            }
                            return Cesium.Color.RED.withAlpha(x);
                        }, false))
                    }
                });
            } else {
                ev = me._mapLayer.entities.add({
                    name: "界线范围",
                    polyline: {
                        positions: Cesium.Cartesian3.fromDegreesArray(points),
                        clampToGround: true,
                        material: Cesium.Color.RED,
                        width: 5
                    }
                });
            }

            entities.push(ev);
        }

        return entities;
    },

    _AddLabel: function (lablayer, laburllayer, dataurl) {
        var me = this;
        var datatype = "json"; //注记加载类别
        if (laburllayer == "") {
            datatype = "json";

        }
        else {
            datatype = "layer";
        }
        me.initLabel(lablayer, dataurl, datatype);
    },

    initLabel: function (lablayer, dataurl, pdatype) {
        var me = this;

        if (pdatype == "json") {
            $.ajax({
                type: "GET",
                url: dataurl,
                dataType: "json",
                success: function (data) {
                    floors = data.RECORDS;
                    $.each(floors, function (i, floor) {
                        var qyname = floor.name;
                        var lntlats = floor.lnglats;
                        var pstrs = lntlats.split(";");
                        if (pstrs.length >= 1) {
                            var ppoints = pstrs[0].split(",");
                            if (ppoints.length >= 2) {
                                var labx = ppoints[0];
                                var laby = ppoints[1];
                                me.makelabelbyvalue(lablayer, qyname, labx, laby);
                            }

                        }
                    });
                }
            });

        }
        else {
            $.ajax({
                url: dataurl + "/0/query?f=json&text=&geometry=&geometryType=esriGeometryPoint&inSR=&spatialRel=esriSpatialRelIntersects&relationParam=&objectIds=&time=&returnCountOnly=false&returnIdsOnly=false&returnGeometry=true&maxAllowableOffset=&outSR=&outFields=*",
                type: "POST", dataType: 'json', data: { where: "1=1" },
                success: function (dkdata) {
                    if (dkdata.features) {
                        for (var k = 0; k < dkdata.features.length; k++) {
                            me.makelabelbyvalue(lablayer, dkdata.features[k].attributes["企业名称（"], dkdata.features[k].attributes["lon"], dkdata.features[k].attributes["lat"]);
                        }
                    }

                }
            });
        }
    },

    makelabelbyvalue: function (lablayer, qyname, labx, laby) {
        var me = this;

        var name = qyname;
        if (!name) name = "标签";
        var description = "";
        if (!description) description = "";
        var height = 80;
        if (!height) height = 0;
        var fheight = 0;
        var icon = "static/images/marker/mark1.png";
        if (icon == null) icon = "static/images/marker/mark1.png";
        if (!fheight) fheight = 0;
        var action = "[拉线标注]";
        if (!action) action = "[拉线标注]";
        var alpha = 1;
        if (!alpha) alpha = 1;
        var color = "#FFFF00";
        if (!color) color = "#FFFF00";
        //color = color.replace(/^\s+|\s+$/gm, '');
        var labelcolor = "#FFFF00";
        if (!labelcolor) labelcolor = "#FFFF00";
        labelcolor = labelcolor.replace(/^\s+|\s+$/gm, '');
        var font = "15px 黑体";
        if (!font) font = "15px 黑体";
        var near = 20;
        if (!near) near = 20;
        var far = 10000;
        if (!far) far = 10000;
        var x = labx;
        if (!x) x = 0;
        var y = laby;
        if (!y) y = 0;
        var scale = 1.0;
        if (!scale) scale = 1.0;
        var heading = 0;
        if (!heading) heading = 0;
        var pitch = -45;
        if (!pitch) pitch = -45;
        var distance = 8000;
        if (!distance) distance = 8000;

        var featcolor1 = Cesium.Color.fromCssColorString(color).withAlpha(1);
        var featcolor2 = Cesium.Color.fromCssColorString(color).withAlpha(alpha);
        var labelfeatcolor = Cesium.Color.fromCssColorString(labelcolor).withAlpha(1);
        var baseheight = me.getGlobeHeight(x, y);
        var maximumHeights = [];
        var minimumHeights = [];

        minimumHeights.push(baseheight + height);
        maximumHeights.push(baseheight + height + fheight);

        var points = [];
        points.push(x);
        points.push(y);

        if (action.indexOf("[标注]") >= 0) {
            lablayer.entities.add({
                description: description,
                position: Cesium.Cartesian3.fromDegrees(x, y, baseheight + height + 0.1),
                label: {
                    text: name,
                    font: font,
                    fillColor: labelfeatcolor,
                    outlineColor: Cesium.Color.BLACK,
                    outlineWidth: 4,
                    style: Cesium.LabelStyle.FILL_AND_OUTLINE,
                    scale: scale,
                    translucencyByDistance: new Cesium.NearFarScalar(near, 1, far, 0),
                    horizontalOrigin: Cesium.HorizontalOrigin.CENTER,
                    verticalOrigin: Cesium.VerticalOrigin.BOTTOM
                }
            });
        }
        else if (action.indexOf("[拉线标注]") >= 0) {
            if (icon != "") {
                lablayer.entities.add({
                    description: description,
                    position: Cesium.Cartesian3.fromDegrees(x, y, baseheight + height + fheight + 10),
                    show: true,
                    billboard: {
                        image: icon,
                        horizontalOrigin: Cesium.HorizontalOrigin.CENTER,
                        verticalOrigin: Cesium.VerticalOrigin.BOTTOM,
                        scale: 1,
                        scaleByDistance: new Cesium.NearFarScalar(0, 1, far, 0)
                    },
                    label: {
                        text: name,
                        font: font,
                        fillColor: labelfeatcolor,
                        outlineColor: Cesium.Color.BLACK,
                        outlineWidth: 4,
                        style: Cesium.LabelStyle.FILL_AND_OUTLINE,
                        scale: 1,
                        translucencyByDistance: new Cesium.NearFarScalar(near, 1, far, 0),
                        horizontalOrigin: Cesium.HorizontalOrigin.CENTER,
                        verticalOrigin: Cesium.VerticalOrigin.TOP
                    },
                    polyline: {
                        positions: Cesium.Cartesian3.fromDegreesArrayHeights([x, y, baseheight, x, y, baseheight + height + fheight + 10]),
                        width: 1.5,
                        material: labelfeatcolor,
                        translucencyByDistance: new Cesium.NearFarScalar(near, 1, far, 0)
                    }
                });
            }
            else {

            }
        } else {
            lablayer.entities.add({
                description: description,
                position: Cesium.Cartesian3.fromDegrees(x, y, baseheight + height + fheight),
                label: {
                    text: name,
                    font: font,
                    fillColor: labelfeatcolor,
                    outlineColor: Cesium.Color.BLACK,
                    outlineWidth: 4,
                    style: Cesium.LabelStyle.FILL_AND_OUTLINE,
                    scale: scale,
                    translucencyByDistance: new Cesium.NearFarScalar(near, 0.9, far, 0),
                    horizontalOrigin: Cesium.HorizontalOrigin.CENTER,
                    verticalOrigin: Cesium.VerticalOrigin.BOTTOM
                },
                polyline: {
                    positions: Cesium.Cartesian3.fromDegreesArrayHeights([x, y, baseheight, x, y, baseheight + height + fheight]),
                    width: 1.5,
                    material: color,
                    translucencyByDistance: new Cesium.NearFarScalar(near, 1, far, 0)
                }
            });
        }
    },

    //获取对应位置地表高度
    getGlobeHeight: function (longitude, latitude) {
        var me = this;
        var viewer = me._viewer;

        var pgeo = new Cesium.Cartographic(longitude, latitude);
        var height = viewer.scene.globe.getHeight(pgeo); //和ellipsoid.cartesianToCartographic(cartesian)取得的效果一样！！
        if (!height) height = 0;
        return height;
    }
}

function MyArcGisMapServerImageryProvider(options) {
    Cesium.ArcGisMapServerImageryProvider.call(this, options);
    this.layerDefs = options.layerDefs;
}
MyArcGisMapServerImageryProvider.prototype = Object.create(Cesium.ArcGisMapServerImageryProvider.prototype);
MyArcGisMapServerImageryProvider.prototype.constructor = MyArcGisMapServerImageryProvider;
MyArcGisMapServerImageryProvider.prototype.requestImage = function (
  x,
  y,
  level,
  request
) {

    //>>includeStart('debug', pragmas.debug);
    if (!this._ready) {
        throw new DeveloperError(
          "requestImage must not be called before the imagery provider is ready."
        );
    }
    //>>includeEnd('debug');

    return Cesium.ImageryProvider.loadImage(
      this,
      buildImageResource(this, x, y, level, request)
    );

    function buildImageResource(imageryProvider, x, y, level, request) {
        var resource;
        if (imageryProvider._useTiles) {
            resource = imageryProvider._resource.getDerivedResource({
                url: "tile/" + level + "/" + y + "/" + x,
                request: request,
            });
        } else {
            var nativeRectangle = imageryProvider._tilingScheme.tileXYToNativeRectangle(
              x,
              y,
              level
            );
            var bbox =
              nativeRectangle.west +
              "," +
              nativeRectangle.south +
              "," +
              nativeRectangle.east +
              "," +
              nativeRectangle.north;

            var query = {
                bbox: bbox,
                size: imageryProvider._tileWidth + "," + imageryProvider._tileHeight,
                format: "png32",
                transparent: true,
                f: "image",
            };

            if (
              imageryProvider._tilingScheme.projection instanceof Cesium.GeographicProjection
            ) {
                query.bboxSR = 4326;
                query.imageSR = 4326;
            } else {
                query.bboxSR = 3857;
                query.imageSR = 3857;
            }
            if (imageryProvider.layers) {
                query.layers = "show:" + imageryProvider.layers;
            }
            if (imageryProvider.layerDefs) {
                query.layerDefs = JSON.stringify(imageryProvider.layerDefs);
            }

            resource = imageryProvider._resource.getDerivedResource({
                url: "export",
                request: request,
                queryParameters: query,
            });
        }

        return resource;
    }
};