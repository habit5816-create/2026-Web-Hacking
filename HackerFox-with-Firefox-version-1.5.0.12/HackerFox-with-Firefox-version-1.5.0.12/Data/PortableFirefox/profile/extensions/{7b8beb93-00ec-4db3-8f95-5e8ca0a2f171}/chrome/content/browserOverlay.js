// on content load inject active, enabled scripts
function eventContentLoad(e) {
	try {
		TH_isSchemeAllowed(function () {
			var bookmarks = TH_getBookmarkList();
			var levels = [[],[],[],[],[],[],[],[],[],[]];
			
			for (var i = 0; i < bookmarks.length; i++)
				if (TH_isURLBookmarklet(bookmarks[i].url)) {
					var tokens = bookmarks[i].kw.split(',');
					var autorun = false;
					var level = '9';
					
					for (var z = 0; z < tokens.length; z++) {
						var keyword = tokens[z].replace(/^\s+|\s+$/, '');
						
						if (keyword == 'autorun')
							autorun = true;
							
						var matched = keyword.match(/^level([0-9])$/);
						level = matched?matched[1]:level;
						
						if (autorun != false && level != '9')
							break;
					}
					
					if (autorun)
						levels[level].push(bookmarks[i]);
				}
				
			for (var l = 0; l < levels.length; l++)
				for (var i = 0; i < levels[l].length; i++)
					TH_getSelectedBrowserWindow().location = levels[l][i].url;
		});
	} catch (e) {};
}

// onunload remove mem leaks
function eventUnload() {
	window.removeEventListener('DOMContentLoaded', eventContentLoad, false);
	window.removeEventListener('unload', eventUnload, false);
}

// set events for onload and DOMContentLoad
window.addEventListener('DOMContentLoaded', eventContentLoad, false);
window.addEventListener('unload', eventUnload, false);
