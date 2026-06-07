window.addEventListener("mouseover", StatusCorrect, false);
window.addEventListener("mouseout", StatusCorrectDistroy, false);
window.addEventListener("keypress",OriginalLink,true);

var PHoriginlLink;

function OriginalLink(e){

	if(e.charCode==99 && e.ctrlKey && String(PHoriginlLink).substr(0,String(ReadStr('def_URL_F')).length)==ReadStr('def_URL_F') ){
		//var copytext=String(PHoriginlLink).substr(String(ReadStr('def_URL_F')).length+12,String(PHoriginlLink).length-String(ReadStr('def_URL_F')).length)  ;
		var copytext=PHoriginlLink;
		copytext = decodeBase64(copytext);

		var str   = Components.classes["@mozilla.org/supports-string;1"].
                       createInstance(Components.interfaces.nsISupportsString);
		if (!str) return false;
		str.data  = copytext;

		var trans = Components.classes["@mozilla.org/widget/transferable;1"].
                       createInstance(Components.interfaces.nsITransferable);
		if (!trans) return false;

		trans.addDataFlavor("text/unicode");
		trans.setTransferData("text/unicode", str, copytext.length * 2);

		var clipid = Components.interfaces.nsIClipboard;
		var clip   = Components.classes["@mozilla.org/widget/clipboard;1"].getService(clipid);
		if (!clip) return false;
	
		clip.setData(trans, null, clipid.kGlobalClipboard);
		alert('Original Link is: ' + copytext + '\nOriginal Link Copied successfully');
	}
}

function StatusCorrect(e){

   	var sBarDisp = document.getElementById("Ph.Status.ExternalTip");
   	var sBar = document.getElementById("Ph.Status.Icon");
    
	//To find href corectlly
	
    var target = e.target;

	while (target && !("href" in target))
		target = target.parentNode;
			
	var sStatus = target ? target.href : "";

	
	PHoriginlLink=sStatus;
	
	if(String(sStatus).substr(0,String(ReadStr('def_URL_F')).length)==ReadStr('def_URL_F')) 
	{  
	    //strBefore = String(sStatus).substr(String(ReadStr('def_URL_F')).length+12,String(sStatus).length-String(ReadStr('def_URL_F')).length)  ;
	    strBefore = sStatus;
		sBarDisp.label = decodeBase64(strBefore);		   
		   
		if(ReadStr('infoShow')=='1')
		{
			if(String(sBarDisp.label).length>30){
				sBarDisp.label=String(sBarDisp.label).substr(0,25)+'...'+String(sBarDisp.label).substr(String(sBarDisp.label).length-22,22);
			}
			sBarDisp.showPopup( sBar, -1,  -1, 'popup', 'bottomleft', 'bottomright');
		}
		else if(ReadStr('infoShow')=='0')
		{
			sBarDisp.label='PH::  ' + sBarDisp.label;
			document.getElementById("statusbar-display").label = sBarDisp.label ; 
			e.preventDefault(); 
		}
	}				
}

function StatusCorrectDistroy(e){
	
	if(String(PHoriginlLink).substr(0,String(ReadStr('def_URL_F')).length)==ReadStr('def_URL_F')) 
	{  
	    if(ReadStr('infoShow')=='1')
		{
			var sBarDisp = document.getElementById("Ph.Status.ExternalTip");
			sBarDisp.hidePopup();
		}
		else if(ReadStr('infoShow')=='0')
			document.getElementById("statusbar-display").label = 'Done' ; 			
	}
	
	PHoriginlLink=null;				
}

function fndPhProxy(){

	var sURL=ReadStr('def_URL_F');
	if(String(content.document.location.href).substr(0,String(sURL).length)==sURL){
		alert('Already opend by Proxy !');
	}
	else{
		gBrowser.loadURI(sURL+'index.php?q='+encodeBase64(content.document.location.href)+ '--&hl='+ GetViewMode() );
	}
}

function OpenSecure(data){
	var sURL=ReadStr('def_URL_F');
	if(String(data).substr(0,String(sURL).length)==sURL){	
		gBrowser.loadURI(data);
	}
	else{
		gBrowser.loadURI(sURL+'index.php?q='+encodeBase64(data)+ '--&hl='+GetViewMode());
	}
}

function OpenSecureNewTab(data){
	var sURL=ReadStr('def_URL_F');
	if(String(data).substr(0,String(sURL).length)==sURL){	
		gBrowser.addTab(data);
	}
	else{	
		gBrowser.addTab(sURL+'index.php?q='+encodeBase64(data)+ '--&hl='+GetViewMode());
	}
}