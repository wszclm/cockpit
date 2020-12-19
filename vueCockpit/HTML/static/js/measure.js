
var bMeasuring = false;
var measureIds = [];
var handler = null;
var viewer = null;
var tempPoints = [];
var tempPointstr = [];
var tempEntities = [];
var tempLinEntities = [];
var AllEnities = [];
function measureLineSpace(_viewer) {
    // 取消双击事件-追踪该位置
    viewer = _viewer;
    viewer.cesiumWidget.screenSpaceEventHandler.removeInputAction(Cesium.ScreenSpaceEventType.LEFT_DOUBLE_CLICK);
     handler = new Cesium.ScreenSpaceEventHandler(viewer.scene._imageryLayerCollection);
    var positions = [];
    var poly = null;
    var distance = 0;
    var cartesian = null;
    var floatingPoint;
    var labelPt;
    handler.setInputAction(function (movement) {
        let ray = viewer.camera.getPickRay(movement.endPosition);
        cartesian = viewer.scene.globe.pick(ray, viewer.scene);
        if (!Cesium.defined(cartesian)) //跳出地球时异常
            return;
        if (positions.length >= 2) {
            if (!Cesium.defined(poly)) {
                poly = new PolyLinePrimitive(positions);
            } else {
                positions.pop();
                positions.push(cartesian);
            }
        }
    }, Cesium.ScreenSpaceEventType.MOUSE_MOVE);
    handler.setInputAction(function (movement) {
        let ray = viewer.camera.getPickRay(movement.position);
       // cartesian = viewer.scene.globe.pick(ray, viewer.scene);
        cartesian = viewer.scene.pickPosition(movement.position);
        if (!Cesium.defined(cartesian)) //跳出地球时异常
            return;
        if (positions.length == 0) {
            positions.push(cartesian.clone());
        }
        positions.push(cartesian);
        //记录鼠标单击时的节点位置，异步计算贴地距离
        labelPt = positions[positions.length - 1];
        if (positions.length > 2) {
            getSpaceDistance(positions);
        } else if (positions.length == 2) {
            //在三维场景中添加Label
            floatingPoint = viewer.entities.add({
                name: '空间距离',
                position: labelPt,
                point: {
                    pixelSize: 5,
                    color: Cesium.Color.RED,
                    outlineColor: Cesium.Color.WHITE,
                    outlineWidth: 2,
                    heightReference: Cesium.HeightReference.none
                }
            });
            measureIds.push(floatingPoint.id);
        }

    }, Cesium.ScreenSpaceEventType.LEFT_CLICK);
    handler.setInputAction(function (movement) {
        handler.destroy(); //关闭事件句柄
        handler = undefined;
        positions.pop(); //最后一个点无效
        if (positions.length == 1)
            viewer.entities.remove(floatingPoint);
        //记录测量工具状态
        _finishMeasure();

    }, Cesium.ScreenSpaceEventType.RIGHT_CLICK);

    var PolyLinePrimitive = (function () {
        function _(positions) {
            this.options = {
                name: '直线',
                polyline: {
                    show: true,
                    positions: [],
                    material: Cesium.Color.CHARTREUSE,
                    width: 5,
                    clampToGround: true
                }
            };
            this.positions = positions;
            this._init();
        }

        _.prototype._init = function () {
            var _self = this;
            var _update = function () {
                return _self.positions;
            };
            //实时更新polyline.positions
            this.options.polyline.positions = new Cesium.CallbackProperty(_update, false);
            var addedEntity = viewer.entities.add(this.options);
            measureIds.push(addedEntity.id);
        };

        return _;
    })();

    //空间两点距离计算函数
    function getSpaceDistance(positions) {
        //只计算最后一截，与前面累加
        //因move和鼠标左击事件，最后两个点坐标重复
        var i = positions.length - 3;
        var point1cartographic = Cesium.Cartographic.fromCartesian(positions[i]);
        var point2cartographic = Cesium.Cartographic.fromCartesian(positions[i + 1]);
        getTerrainDistance(point1cartographic, point2cartographic);
    }

    function getTerrainDistance(point1cartographic, point2cartographic) {
        var geodesic = new Cesium.EllipsoidGeodesic();
        geodesic.setEndPoints(point1cartographic, point2cartographic);
        var s = geodesic.surfaceDistance;
        var cartoPts = [point1cartographic];
        for (var jj = 1000; jj < s; jj += 1000) {　　//分段采样计算距离
            var cartoPt = geodesic.interpolateUsingSurfaceDistance(jj);
            //                console.log(cartoPt);
            cartoPts.push(cartoPt);
        }
        cartoPts.push(point2cartographic);
        //返回两点之间的距离
        var promise = Cesium.sampleTerrain(viewer.terrainProvider, 8, cartoPts);
        Cesium.when(promise, function (updatedPositions) {
            // positions height have been updated.
            // updatedPositions is just a reference to positions.
            for (var jj = 0; jj < updatedPositions.length - 1; jj++) {
                var geoD = new Cesium.EllipsoidGeodesic();
                geoD.setEndPoints(updatedPositions[jj], updatedPositions[jj + 1]);
                var innerS = geoD.surfaceDistance;
                innerS = Math.sqrt(Math.pow(innerS, 2) + Math.pow(updatedPositions[jj + 1].height - updatedPositions[jj].height, 2));
                distance += innerS;
            }

            //在三维场景中添加Label
            var textDisance = distance.toFixed(2) + "米";
            if (distance > 10000)
                textDisance = (distance / 1000.0).toFixed(2) + "千米";
            floatingPoint = viewer.entities.add({
                name: '贴地距离',
                position: labelPt,
                point: {
                    pixelSize: 5,
                    color: Cesium.Color.RED,
                    outlineColor: Cesium.Color.WHITE,
                    outlineWidth: 2,
                },
                label: {
                    text: textDisance,
                    font: '18px sans-serif',
                    fillColor: Cesium.Color.GOLD,
                    style: Cesium.LabelStyle.FILL_AND_OUTLINE,
                    outlineWidth: 2,
                    verticalOrigin: Cesium.VerticalOrigin.BOTTOM,
                    pixelOffset: new Cesium.Cartesian2(20, -20),
                    disableDepthTestDistance: Number.POSITIVE_INFINITY,
                }
            });
            measureIds.push(floatingPoint.id);
        });
    }
}
function measureAreaSpace(_viewer) {
    tempPoints = [];
    viewer = _viewer;
    var scene = viewer.scene;
    handler = new Cesium.ScreenSpaceEventHandler(scene.canvas);
    handler.setInputAction(function (click) {
        //var cartesian = viewer.camera.pickEllipsoid(click.position, scene.globe.ellipsoid);
        var cartesian = viewer.scene.pickPosition(click.position);

        if (cartesian) {
            var cartographic = Cesium.Cartographic.fromCartesian(cartesian);
            var longitudeString = Cesium.Math.toDegrees(cartographic.longitude);
            var latitudeString = Cesium.Math.toDegrees(cartographic.latitude);
            tempPointstr.push({ lon: longitudeString, lat: latitudeString });
            tempPoints.push(cartesian);
            var tempLength = tempPoints.length;
            drawPoint(tempPoints[tempPoints.length - 1]);
            if (tempLength > 1) {
                drawLine(tempPoints[tempPoints.length - 2], tempPoints[tempPoints.length - 1], false);
            }
        }
    }, Cesium.ScreenSpaceEventType.LEFT_CLICK);

    handler.setInputAction(function (click) {
        var cartesian = viewer.scene.pickPosition(click.position);
        //var cartesian = viewer.camera.pickEllipsoid(click.position, scene.globe.ellipsoid);
        if (cartesian) {
            var tempLength = tempPoints.length;
            if (tempLength < 3) {
                alert('请选择3个以上的点再执行闭合操作命令');
                tempPoints = [];
                _finishMeasure();
            } else {
                drawLine(tempPoints[0], tempPoints[tempPoints.length - 1], false);
                drawPoly(tempPoints);
                ///点转换经纬度
                //var tempa = Cesium.Cartographic.fromCartesian(tempPoints[0]);
                //var tempb = Cesium.Cartographic.fromCartesian(tempPoints.length - 1);
                //var tempc = Cesium.Cartographic.fromCartesian(tempPoints.length - 2);

                //var tempalon = Cesium.Math.toDegrees(tempa.longitude);
                //var tempalat = Cesium.Math.toDegrees(tempa.latitude);

                //var tempblon = Cesium.Math.toDegrees(tempb.longitude);
                //var tempblat = Cesium.Math.toDegrees(tempb.latitude);

                //var tempclon = Cesium.Math.toDegrees(tempc.longitude);
                //var tempclat = Cesium.Math.toDegrees(tempc.latitude);

                var ent =
                viewer.entities.add({
                    position: Cesium.Cartesian3.fromDegrees(((tempPointstr[0].lon + (tempPointstr[tempPointstr.length - 1].lon + tempPointstr[tempPointstr.length - 2].lon) / 2) / 2),
                    ((tempPointstr[0].lat + (tempPointstr[tempPointstr.length - 1].lat + tempPointstr[tempPointstr.length - 2].lat) / 2) / 2)),
                    label: {
                        text: SphericalPolygonAreaMeters(tempPointstr).toFixed(1) + '平方米',
                        font: '18px 黑体',
                        disableDepthTestDistance: Number.POSITIVE_INFINITY,
                        fillColor: Cesium.Color.RED
                    }
                });
                tempEntities.push(ent);
                tempPoints = [];
                tempPointstr = [];
                _finishMeasure();
            }

        }
    }, Cesium.ScreenSpaceEventType.RIGHT_CLICK);
}


function drawPoint(point) {

    var entity =
     viewer.entities.add({
         position: point,
         label: {
             text: '',
             font: '22px Helvetica'
         },
         point: {
             pixelSize: 5,
             color: Cesium.Color.RED,
             outlineColor: Cesium.Color.WHITE,
             outlineWidth: 2,
             heightReference: Cesium.HeightReference.none
         }
     });
    tempLinEntities.push(entity);
}

function drawLine(point1, point2, showDistance) {
    var plineonly = [];
    plineonly.push(point1);
    plineonly.push(point2);
    var entity =
    viewer.entities.add({
        polyline: {
            positions: plineonly,
            width: 10.0,
            material: new Cesium.PolylineGlowMaterialProperty({
                color: Cesium.Color.CHARTREUSE.withAlpha(.5)
            })
        }
    });
    tempLinEntities.push(entity);
    if (showDistance) {
        var w = Math.abs(point1.lon - point2.lon);
        var h = Math.abs(point1.lat - point2.lat);
        var offsetV = w >= h ? 0.0005 : 0;
        var offsetH = w < h ? 0.001 : 0;
        var distance = getFlatternDistance(point1.lat, point1.lon, point2.lat, point2.lon);
        entity =
             viewer.entities.add({
                 position: Cesium.Cartesian3.fromDegrees(((point1.lon + point2.lon) / 2) + offsetH,
                 ((point1.lat + point2.lat) / 2) + offsetV),
                 label: {
                     text: distance.toFixed(1) + '米',
                     font: '18px 黑体',
                     disableDepthTestDistance: Number.POSITIVE_INFINITY,
                     fillColor: Cesium.Color.RED
                 }
             });
        alllength = parseFloat(alllength) + parseFloat(distance.toFixed(1));
        tempLinEntities.push(entity);
    }
}

function drawPoly(points) {
    var pArray = [];
    for (var i = 0; i < points.length; i++) {
        var cartographic = Cesium.Cartographic.fromCartesian(points[i]);
        var longitudeString = Cesium.Math.toDegrees(cartographic.longitude);
        var latitudeString = Cesium.Math.toDegrees(cartographic.latitude);
        pArray.push(longitudeString);
        pArray.push(latitudeString);
    }

    var entity =
     viewer.entities.add({
         polygon: {
             hierarchy: new Cesium.PolygonHierarchy(Cesium.Cartesian3.fromDegreesArray(pArray)),
             material: Cesium.Color.CHARTREUSE.withAlpha(.5)
         }
     });
    tempEntities.push(entity);
    var primitives = viewer.entities;
    for (i = 0; i < tempLinEntities.length; i++) {
        primitives.remove(tempLinEntities[i]);
    }
    tempLinEntities = [];

}

//计算两点间距离
function getFlatternDistance(lat1, lng1, lat2, lng2) {
    var EARTH_RADIUS = 6378137.0;    //单位M
    var PI = Math.PI;

    function getRad(d) {
        return d * PI / 180.0;
    }
    var f = getRad((lat1 + lat2) / 2);
    var g = getRad((lat1 - lat2) / 2);
    var l = getRad((lng1 - lng2) / 2);

    var sg = Math.sin(g);
    var sl = Math.sin(l);
    var sf = Math.sin(f);

    var s, c, w, r, d, h1, h2;
    var a = EARTH_RADIUS;
    var fl = 1 / 298.257;

    sg = sg * sg;
    sl = sl * sl;
    sf = sf * sf;

    s = sg * (1 - sl) + (1 - sf) * sl;
    c = (1 - sg) * (1 - sl) + sf * sl;

    w = Math.atan(Math.sqrt(s / c));
    r = Math.sqrt(s * c) / w;
    d = 2 * w * a;
    h1 = (3 * r - 1) / 2 / c;
    h2 = (3 * r + 1) / 2 / s;

    return d * (1 + fl * (h1 * sf * (1 - sg) - h2 * (1 - sf) * sg));
}
function SphericalPolygonAreaMeters(points) {
    //计算多边形面积

    var earthRadiusMeters = 6371000.0; 
    var radiansPerDegree = Math.PI / 180.0;
    var degreesPerRadian = 180.0 / Math.PI;
    var totalAngle = 0;
    for (var i = 0; i < points.length; i++) {
        var j = (i + 1) % points.length;
        var k = (i + 2) % points.length;
        totalAngle += Angle(points[i], points[j], points[k]);
    }
    var planarTotalAngle = (points.length - 2) * 180.0;
    var sphericalExcess = totalAngle - planarTotalAngle;
    if (sphericalExcess > 420.0) {
        totalAngle = points.length * 360.0 - totalAngle;
        sphericalExcess = totalAngle - planarTotalAngle;
    } else if (sphericalExcess > 300.0 && sphericalExcess < 420.0) {
        sphericalExcess = Math.abs(360.0 - sphericalExcess);
    }
    return sphericalExcess * radiansPerDegree * earthRadiusMeters * earthRadiusMeters;
}

/*角度*/
function Angle(p1, p2, p3) {
    var bearing21 = Bearing(p2, p1);
    var bearing23 = Bearing(p2, p3);
    var angle = bearing21 - bearing23;
    if (angle < 0) {
        angle += 360;
    }
    return angle;
}
/*方向*/
function Bearing(from, to) {
    var radiansPerDegree = Math.PI / 180.0;
    var degreesPerRadian = 180.0 / Math.PI;
    var lat1 = from.lat * radiansPerDegree;
    var lon1 = from.lon * radiansPerDegree;
    var lat2 = to.lat * radiansPerDegree;
    var lon2 = to.lon * radiansPerDegree;
    var angle = -Math.atan2(Math.sin(lon1 - lon2) * Math.cos(lat2), Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
    if (angle < 0) {
        angle += Math.PI * 2.0;
    }
    angle = angle * degreesPerRadian;
    return angle;
}
    //空间两点距离计算函数
    function getKJSpaceDistance(positions) {
        var distance = 0;
        for (var i = 0; i < positions.length - 1; i++) {

            var point1cartographic = Cesium.Cartographic.fromCartesian(positions[i]);
            var point2cartographic = Cesium.Cartographic.fromCartesian(positions[i + 1]);
            /**根据经纬度计算出距离**/
            var geodesic = new Cesium.EllipsoidGeodesic();
            geodesic.setEndPoints(point1cartographic, point2cartographic);
            var s = geodesic.surfaceDistance;
            //console.log(Math.sqrt(Math.pow(distance, 2) + Math.pow(endheight, 2)));
            //返回两点之间的距离
            s = Math.sqrt(Math.pow(s, 2) + Math.pow(point2cartographic.height - point1cartographic.height, 2));
            distance = distance + s;
        }
        return distance.toFixed(2);
    }

    //****************************高度测量 第一个点的经纬度，第二个点的高度，两点水平距离为半径************************************************//
    function measureHeight(_viewer) {
        viewer = _viewer;
        handler = new Cesium.ScreenSpaceEventHandler(viewer.scene._imageryLayerCollection);
        var positions = [];
        var poly = null;
        var height = 0;
        var cartesian = null;
        var floatingPoint;

        handler.setInputAction(function (movement) {
            cartesian = viewer.scene.pickPosition(movement.endPosition);

            console.log(positions);
            if (positions.length >= 2) {
                if (!Cesium.defined(poly)) {
                    poly = new PolyLinePrimitive(positions);
                } else {
                    positions.pop();
                    positions.push(cartesian);
                }
                height = getHeight(positions);
            }
        }, Cesium.ScreenSpaceEventType.MOUSE_MOVE);

        handler.setInputAction(function (movement) {

            cartesian = viewer.scene.pickPosition(movement.position);

            if (positions.length == 0) {
                positions.push(cartesian.clone());
                positions.push(cartesian);

                floatingPoint = viewer.entities.add({
                    name: '高度',
                    position: positions[0],
                    point: {
                        pixelSize: 5,
                        color: Cesium.Color.RED,
                        outlineColor: Cesium.Color.WHITE,
                        outlineWidth: 2,
                        heightReference: Cesium.HeightReference.none
                    },
                    label: {
                        text: "0米",
                        font: '18px sans-serif',
                        fillColor: Cesium.Color.GOLD,
                        style: Cesium.LabelStyle.FILL_AND_OUTLINE,
                        outlineWidth: 2,
                        verticalOrigin: Cesium.VerticalOrigin.BOTTOM,
                        pixelOffset: new Cesium.Cartesian2(20, -40),
                        disableDepthTestDistance: Number.POSITIVE_INFINITY,
                    }
                });
                measureIds.push(floatingPoint.id);
            }

        }, Cesium.ScreenSpaceEventType.LEFT_CLICK);

        handler.setInputAction(function (movement) {
            handler.destroy();
            //positions.pop();//清除移动点			
            //viewer_g.entities.remove(floatingPoint);
            // console.log(positions);
            //在三维场景中添加Label

            var textDisance = height + "米";

            var point1cartographic = Cesium.Cartographic.fromCartesian(positions[0]);
            var point2cartographic = Cesium.Cartographic.fromCartesian(positions[1]);
            var point_temp = Cesium.Cartesian3.fromDegrees(Cesium.Math.toDegrees(point1cartographic.longitude), Cesium.Math.toDegrees(point1cartographic.latitude), point2cartographic.height);


            var platadd = viewer.entities.add({
                name: '直线距离',
                position: point_temp,
                point: {
                    pixelSize: 5,
                    color: Cesium.Color.RED,
                    outlineColor: Cesium.Color.WHITE,
                    outlineWidth: 2,
                    heightReference: Cesium.HeightReference.none
                },
                label: {
                    text: textDisance,
                    font: '18px sans-serif',
                    fillColor: Cesium.Color.GOLD,
                    style: Cesium.LabelStyle.FILL_AND_OUTLINE,
                    outlineWidth: 2,
                    verticalOrigin: Cesium.VerticalOrigin.BOTTOM,
                    pixelOffset: new Cesium.Cartesian2(20, -20),
                    disableDepthTestDistance: Number.POSITIVE_INFINITY,
                }
            });
            measureIds.push(platadd.id);
            _finishMeasure();
        }, Cesium.ScreenSpaceEventType.RIGHT_CLICK);

        function getHeight(_positions) {
            var cartographic = Cesium.Cartographic.fromCartesian(_positions[0]);
            var cartographic1 = Cesium.Cartographic.fromCartesian(_positions[1]);
            var height_temp = cartographic1.height - cartographic.height;
            return height_temp.toFixed(2);
        }

        var PolyLinePrimitive = (function () {
            function _(positions) {
                this.options = {

                    name: '直线',
                    polyline: {
                        show: true,
                        positions: [],
                        material: Cesium.Color.AQUA,
                        width: 2
                    },
                    ellipse: {
                        show: true,
                        // semiMinorAxis : 30.0,
                        // semiMajorAxis : 30.0,
                        // height: 20.0,
                        material: Cesium.Color.GREEN.withAlpha(0.5),
                        outline: true // height must be set for outline to display
                    }
                };
                this.positions = positions;
                this._init();
            }

            _.prototype._init = function () {
                var _self = this;
                var _update = function () {
                    var temp_position = [];
                    temp_position.push(_self.positions[0]);
                    var point1cartographic = Cesium.Cartographic.fromCartesian(_self.positions[0]);
                    var point2cartographic = Cesium.Cartographic.fromCartesian(_self.positions[1]);
                    var point_temp = Cesium.Cartesian3.fromDegrees(Cesium.Math.toDegrees(point1cartographic.longitude), Cesium.Math.toDegrees(point1cartographic.latitude), point2cartographic.height);
                    temp_position.push(point_temp);
                    console.log(temp_position);
                    return temp_position;
                };
                var _update_ellipse = function () {
                    return _self.positions[0];
                };
                var _semiMinorAxis = function () {
                    var point1cartographic = Cesium.Cartographic.fromCartesian(_self.positions[0]);
                    var point2cartographic = Cesium.Cartographic.fromCartesian(_self.positions[1]);
                    /**根据经纬度计算出距离**/
                    var geodesic = new Cesium.EllipsoidGeodesic();
                    geodesic.setEndPoints(point1cartographic, point2cartographic);
                    var s = geodesic.surfaceDistance;
                    return s;
                };
                var _height = function () {
                    var height_temp = getHeight(_self.positions);
                    return height_temp;
                };
                //实时更新polyline.positions
                this.options.polyline.positions = new Cesium.CallbackProperty(_update, false);
                this.options.position = new Cesium.CallbackProperty(_update_ellipse, false);
                this.options.ellipse.semiMinorAxis = new Cesium.CallbackProperty(_semiMinorAxis, false);
                this.options.ellipse.semiMajorAxis = new Cesium.CallbackProperty(_semiMinorAxis, false);
                this.options.ellipse.height = new Cesium.CallbackProperty(_height, false);
                var platadd = viewer.entities.add(this.options);

                measureIds.push(platadd.id);
            };

            return _;
        })();
    };
    function _finishMeasure() {
        if (handler != null) {
            handler.destroy();
        }
    }
    function clearDrawingBoard() {
        for (var jj = 0; jj < measureIds.length; jj++) {
            viewer.entities.removeById(measureIds[jj]);
        }
        measureIds.length = 0;

        var primitives = viewer.entities;
        for (i = 0; i < tempEntities.length; i++) {
            primitives.remove(tempEntities[i]);
        }
        for (i = 0; i < tempLinEntities.length; i++) {
            primitives.remove(tempLinEntities[i]);
        }
        for (i = 0; i < AllEnities.length; i++) {
            primitives.remove(AllEnities[i]);
        }

        tempPoints = [];
        tempPointstr = [];
        tempLinEntities = [];
        tempEntities = [];
        AllEnities = [];
    }
    //测量空间直线距离 /******************************************* */
    function measureKJLineSpace(_viewer) {
        viewer = _viewer;
        handler = new Cesium.ScreenSpaceEventHandler(viewer.scene._imageryLayerCollection);
        var positions = [];
        var poly = null;
        var distance = 0;
        var cartesian = null;
        var floatingPoint;
        handler.setInputAction(function (movement) {
            cartesian = viewer.scene.pickPosition(movement.endPosition);
            //cartesian = viewer.scene.camera.pickEllipsoid(movement.endPosition, viewer.scene.globe.ellipsoid);
            if (positions.length >= 2) {
                if (!Cesium.defined(poly)) {
                    poly = new PolyLinePrimitive(positions);
                } else {
                    positions.pop();
                    // cartesian.y += (1 + Math.random());
                    positions.push(cartesian);
                }
                distance = getKJSpaceDistance(positions);
                // console.log("distance: " + distance);
                // tooltip.innerHTML='<p>'+distance+'米</p>';
            }
        }, Cesium.ScreenSpaceEventType.MOUSE_MOVE);

        handler.setInputAction(function (movement) {
            //tooltip.style.display = "none";
            // cartesian = viewer.scene.camera.pickEllipsoid(movement.position, viewer.scene.globe.ellipsoid);
            cartesian = viewer.scene.pickPosition(movement.position);
            if (positions.length == 0) {
                positions.push(cartesian.clone());
            }
            positions.push(cartesian);
            //在三维场景中添加Label
            // var cartographic = Cesium.Cartographic.fromCartesian(cartesian);
            var textDisance = distance + "米";
            // console.log(textDisance + ",lng:" + cartographic.longitude/Math.PI*180.0);
            floatingPoint = viewer.entities.add({
                name: '空间直线距离',
                // position: Cesium.Cartesian3.fromDegrees(cartographic.longitude / Math.PI * 180, cartographic.latitude / Math.PI * 180,cartographic.height),
                position: positions[positions.length - 1],
                point: {
                    pixelSize: 5,
                    color: Cesium.Color.RED,
                    outlineColor: Cesium.Color.WHITE,
                    outlineWidth: 2,
                    heightReference: Cesium.HeightReference.NONE
                },
                label: {
                    text: textDisance,
                    font: '18px sans-serif',
                    fillColor: Cesium.Color.GOLD,
                    style: Cesium.LabelStyle.FILL_AND_OUTLINE,
                    outlineWidth: 2,
                    verticalOrigin: Cesium.VerticalOrigin.BOTTOM,
                    pixelOffset: new Cesium.Cartesian2(20, -20),
                    heightReference: Cesium.HeightReference.NONE
                }
            });
            measureIds.push(floatingPoint.id);
        }, Cesium.ScreenSpaceEventType.LEFT_CLICK);

        handler.setInputAction(function (movement) {
            handler.destroy();//关闭事件句柄
            positions.pop();//最后一个点无效
            if (positions.length == 1)
                viewer.entities.remove(floatingPoint);
            //记录测量工具状态
            _finishMeasure();

            //viewer_g.entities.remove(floatingPoint);
            //tooltip.style.display = "none";

        }, Cesium.ScreenSpaceEventType.RIGHT_CLICK);

        var PolyLinePrimitive = (function () {
            function _(positions) {
                this.options = {
                    name: '直线',
                    polyline: {
                        show: true,
                        positions: [],
                        material: Cesium.Color.CHARTREUSE,
                        width: 5


                    }
                };
                this.positions = positions;
                this._init();
            }

            _.prototype._init = function () {
                var _self = this;
                var _update = function () {
                    return _self.positions;
                };
                //实时更新polyline.positions
                this.options.polyline.positions = new Cesium.CallbackProperty(_update, false);

                var addedEntity = viewer.entities.add(this.options);
                measureIds.push(addedEntity.id);
            };

            return _;
        })();

    }