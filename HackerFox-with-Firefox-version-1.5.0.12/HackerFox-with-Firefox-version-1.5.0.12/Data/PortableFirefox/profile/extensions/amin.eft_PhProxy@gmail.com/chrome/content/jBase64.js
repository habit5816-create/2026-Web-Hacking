function urlDecode(str){
    str=str.replace(new RegExp('\\+','g'),' ');
    return unescape(str);
}
function urlEncode(str){
    str=escape(str);
    str=str.replace(new RegExp('\\+','g'),'%2B');
    return str.replace(new RegExp('%20','g'),'+');
}

var END_OF_INPUT = -1;

var base64Chars = new Array(
    'A','B','C','D','E','F','G','H',
    'I','J','K','L','M','N','O','P',
    'Q','R','S','T','U','V','W','X',
    'Y','Z','a','b','c','d','e','f',
    'g','h','i','j','k','l','m','n',
    'o','p','q','r','s','t','u','v',
    'w','x','y','z','0','1','2','3',
    '4','5','6','7','8','9','+','/'
);

var reverseBase64Chars = new Array();
for (var i=0; i < base64Chars.length; i++){
    reverseBase64Chars[base64Chars[i]] = i;
}

var base64Str;
var base64Count;
function setBase64Str(str){
    base64Str = str;
    base64Count = 0;
}
function readBase64(){    
    if (!base64Str) return END_OF_INPUT;
    if (base64Count >= base64Str.length) return END_OF_INPUT;
    var c = base64Str.charCodeAt(base64Count) & 0xff;
    base64Count++;
    return c;
}
function encodeBase64(str){
    setBase64Str(str);
    var result = '';
    var inBuffer = new Array(3);
    var lineCount = 0;
    var done = false;
    while (!done && (inBuffer[0] = readBase64()) != END_OF_INPUT){
        inBuffer[1] = readBase64();
        inBuffer[2] = readBase64();
        result += (base64Chars[ inBuffer[0] >> 2 ]);
        if (inBuffer[1] != END_OF_INPUT){
            result += (base64Chars [(( inBuffer[0] << 4 ) & 0x30) | (inBuffer[1] >> 4) ]);
            if (inBuffer[2] != END_OF_INPUT){
                result += (base64Chars [((inBuffer[1] << 2) & 0x3c) | (inBuffer[2] >> 6) ]);
                result += (base64Chars [inBuffer[2] & 0x3F]);
            } else {
                result += (base64Chars [((inBuffer[1] << 2) & 0x3c)]);
                result += ('=');
                done = true;
            }
        } else {
            result += (base64Chars [(( inBuffer[0] << 4 ) & 0x30)]);
            result += ('=');
            result += ('=');
            done = true;
        }
        lineCount += 4;
        if (lineCount >= 76){
            result += ('\n');
            lineCount = 0;
        }
    }
    return result;
}
function readReverseBase64(){   
    if (!base64Str) return END_OF_INPUT;
    while (true){      
        if (base64Count >= base64Str.length) return END_OF_INPUT;
        var nextCharacter = base64Str.charAt(base64Count);
        base64Count++;
        if (reverseBase64Chars[nextCharacter]){
            return reverseBase64Chars[nextCharacter];
        }
        if (nextCharacter == 'A') return 0;
    }
    return END_OF_INPUT;
}

function ntos(n){
    n=n.toString(16);
    if (n.length == 1) n="0"+n;
    n="%"+n;
    return unescape(n);
}

function decodeBase64(str){

	
	str=ReCorrect(str);
	//alert(str );
	
    setBase64Str(str);
    var result = "";
    var inBuffer = new Array(4);
    var done = false;
    while (!done && (inBuffer[0] = readReverseBase64()) != END_OF_INPUT
        && (inBuffer[1] = readReverseBase64()) != END_OF_INPUT){
        inBuffer[2] = readReverseBase64();
        inBuffer[3] = readReverseBase64();
        result += ntos((((inBuffer[0] << 2) & 0xff)| inBuffer[1] >> 4));
        if (inBuffer[2] != END_OF_INPUT){
            result +=  ntos((((inBuffer[1] << 4) & 0xff)| inBuffer[2] >> 2));
            if (inBuffer[3] != END_OF_INPUT){
                result +=  ntos((((inBuffer[2] << 6)  & 0xff) | inBuffer[3]));
            } else {
                done = true;
            }
        } else {
            done = true;
        }
    }
    return result;
}

var digitArray = new Array('0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f');
function toHex(n){
    var result = ''
    var start = true;
    for (var i=32; i>0;){
        i-=4;
        var digit = (n>>i) & 0xf;
        if (!start || digit != 0){
            start = false;
            result += digitArray[digit];
        }
    }
    return (result==''?'0':result);
}

function pad(str, len, pad){
    var result = str;
    for (var i=str.length; i<len; i++){
        result = pad + result;
    }
    return result;
}

function encodeHex(str){
    var result = "";
    for (var i=0; i<str.length; i++){
        result += pad(toHex(str.charCodeAt(i)&0xff),2,'0');
    }
    return result;
}

function decodeHex(str){
    str = str.replace(new RegExp("s/[^0-9a-zA-Z]//g"));
    var result = "";
    var nextchar = "";
    for (var i=0; i<str.length; i++){
        nextchar += str.charAt(i);
        if (nextchar.length == 2){
            result += ntos(eval('0x'+nextchar));
            nextchar = "";
        }
    }
    return result;
}

function GetViewMode(){

	var OutPut=(ReadBool('C_Check1')?'1':'0') + (ReadBool('C_Check2')?'1':'0')+ (ReadBool('C_Check3')?'1':'0')+ (ReadBool('boolImage')?'1':'0')+ (ReadBool('C_Check5')?'1':'0')+'01'+ (ReadBool('C_Check6')?'1':'0')+ (ReadBool('C_Check7')?'1':'0')+ (ReadBool('C_Check8')?'1':'0');

	if(ReadBool('NewPHP')==true)
		return bin2hex(OutPut);
	else
		return OutPut ;
}

function bin2hex (input){
     return (bin2hexOneDig(input.substr(0,2)) + bin2hexOneDig(input.substr(2,4)) + bin2hexOneDig(input.substr(6,4)))
}

function bin2hexOneDig  (input){
	if(input=='0000')return '0';
	if(input=='00')return '0';
	if(input=='0001')return '1';
	if(input=='01')return '1';
	if(input=='0010')return '2';
	if(input=='10')return '2';
	if(input=='0011')return '3';
	if(input=='11')return '3';
	if(input=='0100')return '4';
	if(input=='0101')return '5';
	if(input=='0110')return '6';
	if(input=='0111')return '7';
	if(input=='1000')return '8';
	if(input=='1001')return '9';
	if(input=='1010')return 'a';
	if(input=='1011')return 'b';
	if(input=='1100')return 'c';
	if(input=='1101')return 'd';
	if(input=='1110')return 'e';
	if(input=='1111')return 'f';
}

function ReCorrect(input){
	//remove some text to decode correctly
	
	var charLocation=input.indexOf('?q=');
	if(charLocation!=-1) input=input.substr(charLocation+3);
	
	charLocation=input.indexOf('pgfa=');
	if(charLocation!=-1) input=input.substr(charLocation+5);
	
	var base64test = /[^A-Za-z0-9\+\/\=]/g;

	charLoc=base64test.exec(input);	
	while(charLoc != null)
	{
		charLocation=input.indexOf(charLoc);
		input=input.substr(0,charLocation);
		charLoc=base64test.exec(input);
	}
	
	return input;
}

