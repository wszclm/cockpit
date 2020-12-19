define(["./when-54c2dc71","./Check-6c0211bc","./Math-fc8cecf5","./Cartesian2-bddc1162","./Transforms-43808565","./RuntimeError-2109023a","./WebGLConstants-76bb35d1","./ComponentDatatype-6d99a1ee","./GeometryAttribute-49698167","./GeometryAttributes-4fcfcf40","./IndexDatatype-53503fee","./IntersectionTests-60a97ecf","./Plane-c946480f","./arrayRemoveDuplicates-ebc732b0","./EllipsoidRhumbLine-c704bf4c","./EllipsoidGeodesic-30fae80b","./PolylinePipeline-0cb75e10","./WallGeometryLibrary-1b5fb77d"],function(G,e,L,x,P,i,t,T,D,V,I,a,n,r,o,s,l,R){"use strict";var S=new x.Cartesian3,M=new x.Cartesian3;function d(e){var i=(e=G.defaultValue(e,G.defaultValue.EMPTY_OBJECT)).positions,t=e.maximumHeights,a=e.minimumHeights,n=G.defaultValue(e.granularity,L.CesiumMath.RADIANS_PER_DEGREE),r=G.defaultValue(e.ellipsoid,x.Ellipsoid.WGS84);this._positions=i,this._minimumHeights=a,this._maximumHeights=t,this._granularity=n,this._ellipsoid=x.Ellipsoid.clone(r),this._workerName="createWallOutlineGeometry";var o=1+i.length*x.Cartesian3.packedLength+2;G.defined(a)&&(o+=a.length),G.defined(t)&&(o+=t.length),this.packedLength=o+x.Ellipsoid.packedLength+1}d.pack=function(e,i,t){var a;t=G.defaultValue(t,0);var n=e._positions,r=n.length;for(i[t++]=r,a=0;a<r;++a,t+=x.Cartesian3.packedLength)x.Cartesian3.pack(n[a],i,t);var o=e._minimumHeights,r=G.defined(o)?o.length:0;if(i[t++]=r,G.defined(o))for(a=0;a<r;++a)i[t++]=o[a];var s=e._maximumHeights;if(r=G.defined(s)?s.length:0,i[t++]=r,G.defined(s))for(a=0;a<r;++a)i[t++]=s[a];return x.Ellipsoid.pack(e._ellipsoid,i,t),i[t+=x.Ellipsoid.packedLength]=e._granularity,i};var u=x.Ellipsoid.clone(x.Ellipsoid.UNIT_SPHERE),p={positions:void 0,minimumHeights:void 0,maximumHeights:void 0,ellipsoid:u,granularity:void 0};return d.unpack=function(e,i,t){i=G.defaultValue(i,0);for(var a,n,r=e[i++],o=new Array(r),s=0;s<r;++s,i+=x.Cartesian3.packedLength)o[s]=x.Cartesian3.unpack(e,i);if(0<(r=e[i++]))for(a=new Array(r),s=0;s<r;++s)a[s]=e[i++];if(0<(r=e[i++]))for(n=new Array(r),s=0;s<r;++s)n[s]=e[i++];var l=x.Ellipsoid.unpack(e,i,u),m=e[i+=x.Ellipsoid.packedLength];return G.defined(t)?(t._positions=o,t._minimumHeights=a,t._maximumHeights=n,t._ellipsoid=x.Ellipsoid.clone(l,t._ellipsoid),t._granularity=m,t):(p.positions=o,p.minimumHeights=a,p.maximumHeights=n,p.granularity=m,new d(p))},d.fromConstantHeights=function(e){var i=(e=G.defaultValue(e,G.defaultValue.EMPTY_OBJECT)).positions,t=e.minimumHeight,a=e.maximumHeight,n=G.defined(t),r=G.defined(a);if(n||r)for(var o=i.length,s=n?new Array(o):void 0,l=r?new Array(o):void 0,m=0;m<o;++m)n&&(s[m]=t),r&&(l[m]=a);return new d({positions:i,maximumHeights:l,minimumHeights:s,ellipsoid:e.ellipsoid})},d.createGeometry=function(e){var i=e._positions,t=e._minimumHeights,a=e._maximumHeights,n=e._granularity,r=e._ellipsoid,o=R.WallGeometryLibrary.computePositions(r,i,a,t,n,!1);if(G.defined(o)){var s=o.bottomPositions,l=o.topPositions,m=l.length,d=2*m,u=new Float64Array(d),p=0;for(m/=3,v=0;v<m;++v){var f=3*v,c=x.Cartesian3.fromArray(l,f,S),h=x.Cartesian3.fromArray(s,f,M);u[p++]=h.x,u[p++]=h.y,u[p++]=h.z,u[p++]=c.x,u[p++]=c.y,u[p++]=c.z}for(var g=new V.GeometryAttributes({position:new D.GeometryAttribute({componentDatatype:T.ComponentDatatype.DOUBLE,componentsPerAttribute:3,values:u})}),y=d/3,d=2*y-4+y,_=I.IndexDatatype.createTypedArray(y,d),E=0,v=0;v<y-2;v+=2){var b,C,H=v,A=v+2,k=x.Cartesian3.fromArray(u,3*H,S),w=x.Cartesian3.fromArray(u,3*A,M);x.Cartesian3.equalsEpsilon(k,w,L.CesiumMath.EPSILON10)||(b=v+1,C=v+3,_[E++]=b,_[E++]=H,_[E++]=b,_[E++]=C,_[E++]=H,_[E++]=A)}return _[E++]=y-2,_[E++]=y-1,new D.Geometry({attributes:g,indices:_,primitiveType:D.PrimitiveType.LINES,boundingSphere:new P.BoundingSphere.fromVertices(u)})}},function(e,i){return G.defined(i)&&(e=d.unpack(e,i)),e._ellipsoid=x.Ellipsoid.clone(e._ellipsoid),d.createGeometry(e)}});
