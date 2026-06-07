window.addEventListener("load", initOverlay, false);

function initOverlay() {
	var menu = document.getElementById("contentAreaContextMenu");
	menu.addEventListener("popupshowing", contextPopupShowing, false);
}

function contextPopupShowing() {
	var menuitem1 = document.getElementById("context-PH1");
	var menuitem2 = document.getElementById("context-PH2");


	if(menuitem1){
		menuitem1.hidden = !gContextMenu.onLink;
		menuitem2.hidden = !gContextMenu.onLink;
	}
}