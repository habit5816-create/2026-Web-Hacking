var WindowObjectReference = null; // global variable

function openFFPromotionPopup()
{
 
  if(WindowObjectReference == null || WindowObjectReference.closed)
  {
    window.onload = window.open("chrome://fireencrypter/content/popup.xul", "Fire Encrypter", "chrome,centerscreen,width=711,height=350,resizable,scrollbars=yes,status=1");
  }
  else
  {
    WindowObjectReference.focus();
  };
}