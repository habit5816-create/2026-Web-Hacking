var gBookmarkTree;
var gOK;
var gUrls;

function Startup() {
	initServices();
	initBMService();
	gOK = document.documentElement.getButton('accept');
	gBookmarkTree = document.getElementById('bookmarks-view');
	gBookmarkTree.treeBoxObject.view.selection.select(0);
	gBookmarkTree.focus();
}

function onDblClick() {
	if (!gOK.disabled)
		document.documentElement.acceptDialog();
}

function updateOK() {
	var selection = gBookmarkTree._selection;
	var ds = gBookmarkTree.tree.database;
	var url;
	
	gUrls = [];
	
	for (var i = 0; i<selection.length; ++i) {
		var type = selection.type[i];
		
		if (type == 'Bookmark' || type == '') {
			url = BookmarksUtils.getProperty(selection.item[i], gNC_NS + 'URL', ds)
			
			if (url)
				gUrls.push(url);
    	}/* else if (type == 'Folder' || type == 'PersonalToolbarFolder') {
			RDFC.Init(ds, selection.item[i]);
			
			var children = RDFC.GetElements();
			
			while (children.hasMoreElements()) {
				var child = children.getNext().QueryInterface(kRDFRSCIID);
				type = BookmarksUtils.getProperty(child, gRDF_NS + 'type', ds);
				
				if (type == gNC_NS + 'Bookmark') {
					url = BookmarksUtils.getProperty(child, gNC_NS + 'URL', ds);
					
					if (url)
						gUrls.push(url);
				}
			}
		} */
	}
	
	gOK.disabled = gUrls.length == 0;
}

function onOK(aEvent) {
	window.arguments[0].url = gUrls.join("|");
}