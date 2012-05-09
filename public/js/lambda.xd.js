/*
	Copyright (c) 2004-2011, The Dojo Foundation All Rights Reserved.
	Available via Academic Free License >= 2.1 OR the modified BSD license.
	see: http://dojotoolkit.org/license for details
*/


window[esri._dojoScopeName||"dojo"]._xdResourceLoaded(function(_1,_2,_3){return {depends:[["provide","dojox.lang.functional.lambda"]],defineResource:function(_4,_5,_6){if(!_4._hasResource["dojox.lang.functional.lambda"]){_4._hasResource["dojox.lang.functional.lambda"]=true;_4.provide("dojox.lang.functional.lambda");(function(){var df=_6.lang.functional,_7={};var _8="ab".split(/a*/).length>1?String.prototype.split:function(_9){var r=this.split.call(this,_9),m=_9.exec(this);if(m&&m.index==0){r.unshift("");}return r;};var _a=function(s){var _b=[],_c=_8.call(s,/\s*->\s*/m);if(_c.length>1){while(_c.length){s=_c.pop();_b=_c.pop().split(/\s*,\s*|\s+/m);if(_c.length){_c.push("(function("+_b+"){return ("+s+")})");}}}else{if(s.match(/\b_\b/)){_b=["_"];}else{var l=s.match(/^\s*(?:[+*\/%&|\^\.=<>]|!=)/m),r=s.match(/[+\-*\/%&|\^\.=<>!]\s*$/m);if(l||r){if(l){_b.push("$1");s="$1"+s;}if(r){_b.push("$2");s=s+"$2";}}else{var _d=s.replace(/(?:\b[A-Z]|\.[a-zA-Z_$])[a-zA-Z_$\d]*|[a-zA-Z_$][a-zA-Z_$\d]*:|this|true|false|null|undefined|typeof|instanceof|in|delete|new|void|arguments|decodeURI|decodeURIComponent|encodeURI|encodeURIComponent|escape|eval|isFinite|isNaN|parseFloat|parseInt|unescape|dojo|dijit|dojox|window|document|'(?:[^'\\]|\\.)*'|"(?:[^"\\]|\\.)*"/g,"").match(/([a-z_$][a-z_$\d]*)/gi)||[],t={};_4.forEach(_d,function(v){if(!(v in t)){_b.push(v);t[v]=1;}});}}}return {args:_b,body:s};};var _e=function(a){return a.length?function(){var i=a.length-1,x=df.lambda(a[i]).apply(this,arguments);for(--i;i>=0;--i){x=df.lambda(a[i]).call(this,x);}return x;}:function(x){return x;};};_4.mixin(df,{rawLambda:function(s){return _a(s);},buildLambda:function(s){s=_a(s);return "function("+s.args.join(",")+"){return ("+s.body+");}";},lambda:function(s){if(typeof s=="function"){return s;}if(s instanceof Array){return _e(s);}if(s in _7){return _7[s];}s=_a(s);return _7[s]=new Function(s.args,"return ("+s.body+");");},clearLambdaCache:function(){_7={};}});})();}}};});