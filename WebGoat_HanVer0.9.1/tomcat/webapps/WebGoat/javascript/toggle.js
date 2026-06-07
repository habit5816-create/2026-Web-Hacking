    function toggle(id) {
		
		element = document.getElementById(id);
        if (!element) return;
    
        if (element.style.visibility=='visible' || element.style.visibility=='') {
            element.style.visibility = 'hidden';
            element.style.overflow = 'hidden';
            element.style.height=1;
        }
        else {
            element.style.visibility = 'visible';
            element.style.overflow = 'visible';
            element.style.height='';
        }
     }