var author = "morphis";
var displayname = "Super DragAndGo";
var version = "0.2.4";
var packagename = "SuperDragAndGo";
var packagefile = "superdrag.jar";

const APP_NAME = "superdrag";
const APP_PREFS_FILE     = "defaults/preferences/" + APP_NAME + "_prefs.js"

initInstall(displayname, "/" + author + "/" + displayname, version);

//Get installdir
var cf;
var msg = "Install to your profile?\nPress Cancel to install for all users.";
var installdir = (cf = confirm(msg)) ? getFolder("Profile", "chrome") : getFolder("Chrome");

// Add file and register chrome
setPackageFolder(installdir);
addFile('chrome/' + packagefile);



if (cf)
   cf = PROFILE_CHROME;
else
   cf = DELAYED_CHROME;

registerChrome(CONTENT | cf, getFolder(installdir, packagefile), "content/");
registerChrome(LOCALE | cf, getFolder(installdir, packagefile), "locale/en-US/");


//Install
if(getLastError() == SUCCESS)
{
   performInstall();
   if(!(getLastError() == SUCCESS || getLastError() == 999)) {
      alert("An error occured during installation !\nErrorcode: " + getLastError());
   }
}
else
{
   alert("An error occurred, installation will be canceled.\nErrorcode: " + getLastError());
   cancelInstall(getLastError());
}