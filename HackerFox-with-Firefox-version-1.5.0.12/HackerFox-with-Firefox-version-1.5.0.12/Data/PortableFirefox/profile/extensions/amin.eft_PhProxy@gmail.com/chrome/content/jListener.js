var Ph_urlBarListener = {
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
    PhListener.processNewURL(aURI);
  },

  onStateChange: function() {},
  onProgressChange: function() {},
  onStatusChange: function() {},
  onSecurityChange: function() {
	var href=content.document.location.href;
	
	if(String(href).substr(0,String(ReadStr('def_URL_F')).length)==ReadStr('def_URL_F') ){
		document.getElementById('Ph.Status.Icon').src=src='chrome://inbasicph/skin/mainFilterMouse.ico';
		
		//correct Addressbar Text
		setTimeout(function() {
			var strURL = href;
			
			strURL = decodeBase64(strURL);	
			gURLBar.value="PH::  " + strURL ;
		}, 100);
	}
	else{
		document.getElementById('Ph.Status.Icon').src=src='chrome://inbasicph/skin/mainNoMouse.ico';
	} 
  },
  onLinkIconAvailable: function() {}
};

var PhListener = {
  oldURL: null,
  
  init: function() {
    // Listen for webpage loads
    gBrowser.addProgressListener(Ph_urlBarListener,
        Components.interfaces.nsIWebProgress.NOTIFY_STATE_DOCUMENT);
  },
  
  uninit: function() {
    gBrowser.removeProgressListener(Ph_urlBarListener);
  },

  processNewURL: function(aURI) {
    if (aURI.spec == this.oldURL)
      return;
    this.oldURL = aURI.spec;
  }
};

window.addEventListener("load", function() {PhListener.init()}, false);
window.addEventListener("unload", function() {PhListener.uninit()}, false);