/*
	Copyright (c) 2004-2011, The Dojo Foundation All Rights Reserved.
	Available via Academic Free License >= 2.1 OR the modified BSD license.
	see: http://dojotoolkit.org/license for details
*/


window[esri._dojoScopeName||"dojo"]._xdResourceLoaded(function(_1,_2,_3){return {depends:[["provide","dijit.layout._LayoutWidget"],["require","dijit._Widget"],["require","dijit._Container"],["require","dijit._Contained"]],defineResource:function(_4,_5,_6){if(!_4._hasResource["dijit.layout._LayoutWidget"]){_4._hasResource["dijit.layout._LayoutWidget"]=true;_4.provide("dijit.layout._LayoutWidget");_4.require("dijit._Widget");_4.require("dijit._Container");_4.require("dijit._Contained");_4.declare("dijit.layout._LayoutWidget",[_5._Widget,_5._Container,_5._Contained],{baseClass:"dijitLayoutContainer",isLayoutContainer:true,buildRendering:function(){this.inherited(arguments);_4.addClass(this.domNode,"dijitContainer");},startup:function(){if(this._started){return;}this.inherited(arguments);var _7=this.getParent&&this.getParent();if(!(_7&&_7.isLayoutContainer)){this.resize();this.connect(_4.isIE?this.domNode:_4.global,"onresize",function(){this.resize();});}},resize:function(_8,_9){var _a=this.domNode;if(_8){_4.marginBox(_a,_8);if(_8.t){_a.style.top=_8.t+"px";}if(_8.l){_a.style.left=_8.l+"px";}}var mb=_9||{};_4.mixin(mb,_8||{});if(!("h" in mb)||!("w" in mb)){mb=_4.mixin(_4.marginBox(_a),mb);}var cs=_4.getComputedStyle(_a);var me=_4._getMarginExtents(_a,cs);var be=_4._getBorderExtents(_a,cs);var bb=(this._borderBox={w:mb.w-(me.w+be.w),h:mb.h-(me.h+be.h)});var pe=_4._getPadExtents(_a,cs);this._contentBox={l:_4._toPixelValue(_a,cs.paddingLeft),t:_4._toPixelValue(_a,cs.paddingTop),w:bb.w-pe.w,h:bb.h-pe.h};this.layout();},layout:function(){},_setupChild:function(_b){var _c=this.baseClass+"-child "+(_b.baseClass?this.baseClass+"-"+_b.baseClass:"");_4.addClass(_b.domNode,_c);},addChild:function(_d,_e){this.inherited(arguments);if(this._started){this._setupChild(_d);}},removeChild:function(_f){var cls=this.baseClass+"-child"+(_f.baseClass?" "+this.baseClass+"-"+_f.baseClass:"");_4.removeClass(_f.domNode,cls);this.inherited(arguments);}});_5.layout.marginBox2contentBox=function(_10,mb){var cs=_4.getComputedStyle(_10);var me=_4._getMarginExtents(_10,cs);var pb=_4._getPadBorderExtents(_10,cs);return {l:_4._toPixelValue(_10,cs.paddingLeft),t:_4._toPixelValue(_10,cs.paddingTop),w:mb.w-(me.w+pb.w),h:mb.h-(me.h+pb.h)};};(function(){var _11=function(_12){return _12.substring(0,1).toUpperCase()+_12.substring(1);};var _13=function(_14,dim){var _15=_14.resize?_14.resize(dim):_4.marginBox(_14.domNode,dim);if(_15){_4.mixin(_14,_15);}else{_4.mixin(_14,_4.marginBox(_14.domNode));_4.mixin(_14,dim);}};_5.layout.layoutChildren=function(_16,dim,_17,_18,_19){dim=_4.mixin({},dim);_4.addClass(_16,"dijitLayoutContainer");_17=_4.filter(_17,function(_1a){return _1a.region!="center"&&_1a.layoutAlign!="client";}).concat(_4.filter(_17,function(_1b){return _1b.region=="center"||_1b.layoutAlign=="client";}));_4.forEach(_17,function(_1c){var elm=_1c.domNode,pos=(_1c.region||_1c.layoutAlign);var _1d=elm.style;_1d.left=dim.l+"px";_1d.top=dim.t+"px";_1d.position="absolute";_4.addClass(elm,"dijitAlign"+_11(pos));var _1e={};if(_18&&_18==_1c.id){_1e[_1c.region=="top"||_1c.region=="bottom"?"h":"w"]=_19;}if(pos=="top"||pos=="bottom"){_1e.w=dim.w;_13(_1c,_1e);dim.h-=_1c.h;if(pos=="top"){dim.t+=_1c.h;}else{_1d.top=dim.t+dim.h+"px";}}else{if(pos=="left"||pos=="right"){_1e.h=dim.h;_13(_1c,_1e);dim.w-=_1c.w;if(pos=="left"){dim.l+=_1c.w;}else{_1d.left=dim.l+dim.w+"px";}}else{if(pos=="client"||pos=="center"){_13(_1c,dim);}}}});};})();}}};});