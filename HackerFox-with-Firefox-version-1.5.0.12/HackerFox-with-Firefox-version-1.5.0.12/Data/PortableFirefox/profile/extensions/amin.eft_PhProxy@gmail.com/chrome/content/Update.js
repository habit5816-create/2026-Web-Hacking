var listener = {
    finished : function(data){
		var sURL=new Array();
		sURL=CreateList(data);
		for(y=0;y<10;y++){
			if(sURL[y]=='Error loading page'){
				alert('Some Error Happends,\nPlease test your connection and try again.');
				y=10;
			}
			else if(y==0){
				if(String(sURL[0]).substr(0,38)!='InBasic>ServerInUse>PhProxy>Code124587'){
					y=10;
					alert('Unknown Updating Server !');
					document.getElementById('Ph.Option.Tab2.Cmd').label='Update from Server';
				}
			}
			else if(y==1){
				if(parseFloat(ReadStr('UpdateVer'))>=parseFloat(sURL[1])){
					y=10;
					alert('Proxy List is up to date');
					document.getElementById('Ph.Option.Tab2.Cmd').label='Update from Server';
				}
			}
			else{  	
				WriteStr('URL0'+(y-1),sURL[y]);
				document.getElementById('menu0'+(y-1)).label=sURL[y];
				document.getElementById('Ph.Option.Tab3.lst'+(y-1)).label=sURL[y];
				if(y==9){
					alert('Proxy list Updated successfully');
					document.getElementById('Ph.Option.Tab2.Cmd').label='Update from Server';
					WriteStr('UpdateVer',sURL[1]);
				}
			}
		}
    }
}


function CreateList(sData){
	var y=0;
	var n=0;
	var sURL=new Array();

	for (var x = 0; x <= sData.length; x++){
		if(asc(sData.substr(x,1))==10 || x==sData.length){
			sURL[n]=sData.substr(y,x-y);
			n++;
			y=x+1;
		}
	}

	return sURL
}

function ListCount(lArray){
	for(x=0;lArray[x]!=undefined;x++);
	return x;
}

function asc(c) {
	c = c.charAt(0);

	var i;
	for (i = 0; i < 256; ++ i) {
		var h = i.toString (16);
		if (h . length == 1) h = "0" + h;
		h = "%" + h;
		h = unescape (h);
		if (h == c) break;
	}

	return i;
} 


function GetURL(sURL,Listener){
	var req = new XMLHttpRequest();
	req.open('GET', sURL, true);
	req.onreadystatechange = function (aEvt) {
	  if (req.readyState == 4) {
	     if(req.status == 200)
	      Listener.finished(req.responseText);
	     else
	      Listener.finished("Error loading page");
	  }
	};
	req.send(null); 
}



function ReadStr(id){
	var prefs = Components.classes["@mozilla.org/preferences-service;1"].getService(Components.interfaces.nsIPrefService).getBranch("InBasic.PhProxy.");
        var str = prefs.getCharPref(id);
	return str;
}

function WriteStr(id,sData){
	var prefs = Components.classes["@mozilla.org/preferences-service;1"].getService(Components.interfaces.nsIPrefService).getBranch("InBasic.PhProxy.");
        prefs.setCharPref(id, sData);
}


function ReadBool(id){
	var prefs = Components.classes["@mozilla.org/preferences-service;1"].getService(Components.interfaces.nsIPrefService).getBranch("InBasic.PhProxy.");
        var str = prefs.getBoolPref(id);
	return str;
}

function WriteBool(id,sData){
	var prefs = Components.classes["@mozilla.org/preferences-service;1"].getService(Components.interfaces.nsIPrefService).getBranch("InBasic.PhProxy.");
        prefs.setBoolPref(id, sData);
}