window.addEventListener("load", function() {Ph_vListener.init()}, false);
window.addEventListener("unload", function() {Ph_vListener.uninit()}, false);

var PH_urlBarListener = {
  QueryInterface: function(aIID)
  {
   if (aIID.equals(Components.interfaces.nsIWebProgressListener) ||
       aIID.equals(Components.interfaces.nsISupportsWeakReference) ||
       aIID.equals(Components.interfaces.nsISupports))
     return this;
   throw Components.results.NS_NOINTERFACE;
  },

  onLocationChange: function(aProgress, aRequest, aURI)
  {
    Ph_vListener.processNewURL(aURI);
  },

  onStateChange: function() {},
  onProgressChange: function() {},
  onStatusChange: function() {},
  onSecurityChange: function() {},
  onLinkIconAvailable: function() {}
};

var Ph_vListener = {
  oldURL: null,
  
  init: function() {
    // Listen for webpage loads
    gBrowser.addProgressListener(PH_urlBarListener,
        Components.interfaces.nsIWebProgress.NOTIFY_STATE_DOCUMENT);
  },
  
  uninit: function() {
    gBrowser.removeProgressListener(PH_urlBarListener);
  },

  processNewURL: function(aURI) 
  {
    if (aURI.spec == this.oldURL)
      return;
    // now we know the url is new...
	  //*****************************************************Change Ph Icon if current tab url is proxy******************************************************
	if(String(aURI.spec).substr(0,String(ReadStr('def_URL_F')).length)==ReadStr('def_URL_F') ){
		document.getElementById('Ph.Status.Icon').src=src='chrome://inbasicph/skin/mainFilterMouse.ico';
		
		//correct Addressbar Text
		setTimeout(function() {		
			var strURL = String(aURI.spec).substr(String(ReadStr('def_URL_F')).length+12,String(aURI.spec).length-String(ReadStr('def_URL_F')).length)  ;
			
			//Remove end part firt
			for(x=0;x<strURL.length;x++){
		   		if(strURL.substr(x,5)=="--&hl"){
					strURL=strURL.substr(0,x);
					x=strURL.length;
				}
			}			
			
		    strURL = decodeBase64(strURL);	
			gURLBar.value="PH::  " + strURL ;
		}, 100);
	}
	else{
		document.getElementById('Ph.Status.Icon').src=src='chrome://inbasicph/skin/mainNoMouse.ico';
	}
    this.oldURL = aURI.spec;
  }
};