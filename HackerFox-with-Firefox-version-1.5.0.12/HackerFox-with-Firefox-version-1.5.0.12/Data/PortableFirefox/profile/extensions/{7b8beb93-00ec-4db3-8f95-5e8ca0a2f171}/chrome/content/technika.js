// get url protocol
function TH_getURLScheme(url) {
	return Components.classes['@mozilla.org/network/io-service;1']
		.getService(Components.interfaces.nsIIOService)
		.extractScheme(url);
}

// get main window
function TH_getMainWindow() {
	return window.QueryInterface(Components.interfaces.nsIInterfaceRequestor)
		.getInterface(Components.interfaces.nsIWebNavigation)
		.QueryInterface(Components.interfaces.nsIDocShellTreeItem)
		.rootTreeItem
		.QueryInterface(Components.interfaces.nsIInterfaceRequestor)
		.getInterface(Components.interfaces.nsIDOMWindow);
}

// get selected browser window
function TH_getSelectedBrowserWindow() {
	var win = TH_getMainWindow();
	return win.gBrowser.selectedBrowser.contentWindow;
}

// execute event if scheme allowed
function TH_isSchemeAllowed(func) {
	var win = TH_getSelectedBrowserWindow();
	var scheme = TH_getURLScheme(win.location);

	if (scheme == 'http' || scheme == 'https' || scheme == 'ftp' || scheme == 'file' || scheme == 'data')
		func(scheme, win);
	else
		throw 'scheme not supported';
}

// get string bundle
function TH_getStringBundle(url) {
	var localeService = Components.classes['@mozilla.org/intl/nslocaleservice;1'].getService(Components.interfaces.nsILocaleService);
	var stringBundleService = Components.classes['@mozilla.org/intl/stringbundle;1'].getService(Components.interfaces.nsIStringBundleService);
	
	var appLocale = localeService.getApplicationLocale();
	return stringBundleService.createBundle(url, appLocale);
}

// get all bookmarks thanks to Steve Yegge; http://steve-yegge.blogspot.com
function TH_getBookmarkList(doSort) {
	var rdf = Components.classes["@mozilla.org/rdf/rdf-service;1"].
		getService(Components.interfaces.nsIRDFService);
		
	var bmks = rdf.GetDataSource("rdf:bookmarks");
	var NC_NS = "http://home.netscape.com/NC-rdf#";
	var kwArc = rdf.GetResource(NC_NS + "ShortcutURL");
	var urlArc = rdf.GetResource(NC_NS + "URL");
	var nameArc = rdf.GetResource(NC_NS + "Name");
	var rdfLiteral = Components.interfaces.nsIRDFLiteral;
	var e = bmks.GetAllResources();
	var items = [];
	
	while(e.hasMoreElements()) {
		var r =  e.getNext().QueryInterface(Components.interfaces.nsIRDFResource);
		var urlR = bmks.GetTarget(r, urlArc, true);
		var kwR = bmks.GetTarget(r, kwArc, true);
		var nameR = bmks.GetTarget(r, nameArc, true);
		
		if (!(nameR && urlR)) {
			continue;
		}
		
		var item = {};
		item.name = nameR.QueryInterface(rdfLiteral).Value;
		item.url = urlR.QueryInterface(rdfLiteral).Value;
		item.kw = '';
		
		if (kwR) {
			item.kw = kwR.QueryInterface(rdfLiteral).Value;
		}
		
		items.push(item);
	}
	
	if (doSort) {
		items.sort(function(a, b) {
			return (a.name.upcase() < b.name.upcase()) ? -1 : 1;
		});
	}
	
	return items;
}

// check if URL is bookmarklet
function TH_isURLBookmarklet(url) {
	if (url.substring(0, 11) == 'javascript:')
		return true;
		
	return false;
}