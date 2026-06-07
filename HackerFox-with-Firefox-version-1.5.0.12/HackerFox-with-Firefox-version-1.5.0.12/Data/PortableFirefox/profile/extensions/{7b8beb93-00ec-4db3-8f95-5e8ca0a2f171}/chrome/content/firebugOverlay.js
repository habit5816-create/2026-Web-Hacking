FBL.ns(function() {with (FBL) { 

function getCommandLine(context) {
	return Firebug.largeCommandLine ? context.chrome.$('fbLargeCommandLine') : context.chrome.$('fbCommandLine');
}

Firebug.Console.buildBookmarklet = function (context) {
	var commandLine = getCommandLine(context).value.replace(/(^\s+|\s+$)/, '');
	
	if (!commandLine) {
		var stringBundle = TH_getStringBundle('chrome://technika/locale/technika.properties');
		alert(stringBundle.GetStringFromName('buildBookmarkletError'));
		
		return;
	}
	
	var bookmarklet = 'javascript:' + escape(commandLine) + ';void(0);';
	window.openDialog('chrome://browser/content/bookmarks/addBookmark2.xul', '', 'modal,centerscreen,chrome,dialog,resizable,dependent', {name: null, url: bookmarklet, charset: 'UTF-8'});
};

Firebug.Console.loadBookmarklet = function (context) {
	var rv = {url:null};
	window.openDialog('chrome://technika/content/selectBookmarkletDialog.xul', '', 'modal,centerscreen,chrome,dialog,resizable,dependent', rv);
	
	if (rv.url) {
		if (!TH_isURLBookmarklet(rv.url)) {
			var stringBundle = TH_getStringBundle('chrome://technika/locale/technika.properties');
			alert(stringBundle.GetStringFromName('selectBookmarkletError'));
			
			return;
		}
		
		var code = rv.url.substring(11);
		
		if (code.substring(code.length - 9, code.length) == ';void(0);')
			code = code.substring(0, code.length - 9);
			
		var commandLine = getCommandLine(context);
		commandLine.value = unescape(code);
	}
};

}});
