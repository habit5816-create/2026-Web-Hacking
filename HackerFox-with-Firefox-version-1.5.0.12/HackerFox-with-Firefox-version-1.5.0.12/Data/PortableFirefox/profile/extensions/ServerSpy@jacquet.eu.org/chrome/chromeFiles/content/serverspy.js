/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Server Spy code.
 *
 * The Initial Developer of the Server Spy is
 * Christophe Jacquet.
 * Portions created by the Initial Developer are Copyright (C) 2006
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */
 
var serverspy_svlabel = null;
var bundle;
var gBrowser = null;

serverspy_start();

function serverspy_start() {
	var oHeaderInfo;
	oHeaderInfo = new HeaderInfo();
	addToListener(oHeaderInfo);
 
	// listen for tab switches
	getBrowser().addEventListener("load", onSelectTab, true);
	getBrowser().addEventListener("select", onSelectTab, false);
	
	// initialize serverspy's variables
	window.serverCache = new Array();
	window.serverLast = "";
	
	// get the string bundle
	var src = "chrome://serverspy/locale/ui.properties";
	bundle = fetchStringBundle(src);
}

function fetchStringBundle(src) {
	var localeService = 
		Components.classes["@mozilla.org/intl/nslocaleservice;1"]
		.getService(Components.interfaces.nsILocaleService);
	var appLocale = localeService.getApplicationLocale();
	var stringBundleService = 
		Components.classes["@mozilla.org/intl/stringbundle;1"]
		.getService(Components.interfaces.nsIStringBundleService);
	return stringBundleService.createBundle(src, appLocale);
}

function addToListener(obj)
{
	var observerService = Components.classes["@mozilla.org/observer-service;1"].getService(Components.interfaces.nsIObserverService);
    observerService.addObserver(obj, "http-on-examine-response", false);
}


// HeaderInfo implements nsIObserver
function HeaderInfo() {
	this.observe = HIObserve;
	this.QueryInterface = HIQueryInterface;
}


function HIObserve(aSubject, aTopic, aData) {
    if (aTopic == 'http-on-examine-response') {
		try {
			aSubject.QueryInterface(Components.interfaces.nsIHttpChannel);
			var url = aSubject.URI.asciiSpec;
			var sv = aSubject.getResponseHeader("Server");
			window.serverCache[url] = sv;
			updateLabel();
		} catch(e) {
		}
    }
}
    
function HIQueryInterface(iid) {
    if (!iid.equals(Components.interfaces.nsISupports) &&
        !iid.equals(Components.interfaces.nsIHttpNotify) &&
        !iid.equals(Components.interfaces.nsIHttpNotify) &&
        !iid.equals(Components.interfaces.nsIObserver)) {
          throw Components.results.NS_ERROR_NO_INTERFACE;
	}
	return this;
}

function updateLabel() {
	docurl = _content.document.URL;
	
	sv = window.serverCache[docurl];
	if(!sv) sv = "(" + bundle.GetStringFromName("unknown") + ")";
	
	if(serverspy_svlabel == null) {
		serverspy_svlabel = document.getElementById("serverspy_svlabel");
	}
	
	serverspy_svlabel.label = sv.split(" ", 1)[0];
    serverspy_svlabel.setAttribute("tooltiptext", sv);
	
	window.serverLast = sv;
}

function onSelectTab(event) {
	updateLabel();
}

function getBrowser()
{
	if (!gBrowser)
		gBrowser = document.getElementById("content");
	return gBrowser;
} // getBrowser

function serverspy_about() {
	window.openDialog("chrome://serverspy/content/about.xul",
		bundle.GetStringFromName("appname"),
		"chrome,modal=no,dialog,resizable=no,titlebar,centerscreen", window);
}