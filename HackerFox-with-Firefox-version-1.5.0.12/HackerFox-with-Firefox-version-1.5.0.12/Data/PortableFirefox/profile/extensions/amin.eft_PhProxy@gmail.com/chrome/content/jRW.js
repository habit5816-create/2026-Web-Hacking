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
