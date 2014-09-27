/** fake XMLHttpRequest **/
function myFakeXMLHttpRequest() {
  this.method = null;
  this.path = null;
  this.async = null;
  this.open = function(method, path, async) {
	    this.method = method;
	    this.path = path;
	    this.async = async;
	  };
  this.send = function(envelope) {};
};



/** Jasmine spec file for testing remoting javascript **/

describe('Seam Remoting javascript suite', function () {

  it('Confirm that URL encoding/decoding function we are using works', function () {
    var val = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890!@#$%^&*()_-;',./:\"<>?~`\|[]{}+= ";
    expect(decodeURIComponent(encodeURIComponent(val))).toEqual(val);
  });
  
  it('Seam.Remoting.Map tests GET', function () {
	  var map = new Seam.Remoting.Map();
	  var key = new Object();
	  var val = new Object();
	  map.put(key, val);
	  map.put(new Object(), new Object());
      expect(map.get(key)).toEqual(val);
  });

  it('Seam.Remoting.Map tests PUT', function() {
	  var map = new Seam.Remoting.Map(); 
	  var key = new Object();
	  var val = new Object();
	  map.put(key, val);
	  expect(map.keySet()[0]).toEqual(key);
	  expect(map.values()[0]).toEqual(val);
  });
  
  it('Seam.Remoting.Map tests SIZE', function() {
	  var map = new Seam.Remoting.Map();
	  map.put("a", new Object());
	  expect(map.size()).toEqual(1);
  });
  

  it('Seam.Remoting.Map tests IsEmpty', function() {
	  var map = new Seam.Remoting.Map();
	  expect(map.isEmpty()).toBeTruthy();
	  map.put("a", new Object());
	  expect(map.isEmpty()).toBeFalsy();
  });

  it('Seam.Remoting.Map tests Set KeySet', function() {
	  var map = new Seam.Remoting.Map();
	  expect(map.keySet().length).toEqual(0);
	  var key = new Object();
	  map.put(key, null);
	  expect(map.keySet()[0]).toEqual(key);
	  expect(map.keySet().length).toEqual(1);
	  map.put(new Object(), null);
	  expect(map.keySet().length).toEqual(2);
  });
  
  it('Seam.Remoting.Map tests Values', function() {
	  var map = new Seam.Remoting.Map();
	  expect(map.keySet().length).toEqual(0);
	  var val = new Object();
	  map.put(new Object(), val);
	  expect(map.values()[0]).toEqual(val);
  });
  
  it('Seam.Remoting.Map tests MapRemove', function() {
	  var map = new Seam.Remoting.Map();
	  var key = new Object();
	  map.put(key, null);
	  expect(map.keySet().length).toEqual(1);
	  map.remove(key);
	  expect(map.keySet().length).toEqual(0);	  
  });
  
  it('Seam.Remoting.Map tests MapContains', function() {
	  var map = new Seam.Remoting.Map();
	  var key = new Object();
	  expect(map.contains(key)).toBeFalsy();
	  map.put(key, null);	  
	  expect(map.contains(key)).toBeTruthy();
  });

/** Seam.Remoting.Serialize tests **/
  
  it('Seam.Remoting.Serialize testSerializeBool', function() {
	  expect(Seam.Remoting.serializeValue(true, "bool")).toEqual("<bool>true</bool>");
	  expect(Seam.Remoting.serializeValue(false, "bool")).toEqual("<bool>false</bool>");
	  expect(Seam.Remoting.serializeValue(true)).toEqual("<bool>true</bool>");
	  expect(Seam.Remoting.serializeValue(false)).toEqual("<bool>false</bool>");
  });

  it('Seam.Remoting.Serialize testSerializeNumber', function() {
	  expect(Seam.Remoting.serializeValue(123, "number")).toEqual("<number>123</number>");
	  expect(Seam.Remoting.serializeValue(123)).toEqual("<number>123</number>");
	  expect(Seam.Remoting.serializeValue(123.45)).toEqual("<number>123.45</number>");
  });
  
  it('Seam.Remoting.Serialize testSerializeDate', function() {
	  var dte = new Date(2005, 0, 1);
	  expect(Seam.Remoting.serializeValue(dte, "date")).toEqual("<date>20050101000000000</date>");
	  dte = new Date(2005, 10, 15, 12, 30, 9, 150);
	  expect(Seam.Remoting.serializeValue(dte, "date")).toEqual("<date>20051115123009150</date>");
	  expect(Seam.Remoting.serializeValue(dte)).toEqual("<date>20051115123009150</date>");
  });

  it('Seam.Remoting.Serialize testSerializeString', function() {
	  var val = "abc";
	  expect(Seam.Remoting.serializeValue(val, "str")).toEqual("<str>abc</str>");
  	  expect(Seam.Remoting.serializeValue(val)).toEqual("<str>abc</str>"); 
  });

  it('Seam.Remoting.Serialize testSerializeBag', function() {
	  var bag = new Array();
	  bag.push(1);
	  expect(Seam.Remoting.serializeBag(bag, "bag")).toEqual("<bag><element><number>1</number></element></bag>");
	  expect(Seam.Remoting.serializeBag(bag)).toEqual("<bag><element><number>1</number></element></bag>");
	  bag.push("zzz");
	  expect(Seam.Remoting.serializeBag(bag, "bag")).toEqual("<bag><element><number>1</number></element><element><str>zzz</str></element></bag>");
	  expect(Seam.Remoting.serializeBag(bag)).toEqual("<bag><element><number>1</number></element><element><str>zzz</str></element></bag>");   
  });
  
  it('Seam.Remoting.Serialize testSerializeMap', function() {
	  var map = new Seam.Remoting.Map();
	  map.put("1", "zzzz");
	  expect(Seam.Remoting.serializeMap(map, "map")).toEqual("<map><element><k><str>1</str></k><v><str>zzzz</str></v></element></map>");
	  expect(Seam.Remoting.serializeMap(map)).toEqual("<map><element><k><str>1</str></k><v><str>zzzz</str></v></element></map>");
  });

  it('Seam.Remoting.Serialize testSerializeNull', function() {
	  expect(Seam.Remoting.serializeValue(null)).toEqual("<null/>"); 
  });
  
  it('Seam.Remoting.Serialize testComponent', function() {
	  var comp = function() { };
	  comp.__name = "testComponent";
	  
	  expect(Seam.Component.isRegistered("testComponent")).toBeFalsy();
	  Seam.Component.register(comp);
	  expect(Seam.Component.isRegistered("testComponent")).toBeTruthy();
	  
	  var instance = Seam.Component.getInstance("testComponent");
	  expect(Seam.Component.getInstance("testComponent")).toEqual(instance);
	  expect(instance).not.toEqual(Seam.Component.newInstance("testComponent1"));
	  
	  expect(Seam.Component.getComponentName(instance)).toEqual("testComponent");
	  expect(Seam.Component.getComponentType(instance)).toEqual(comp);
  });
  
  it('Seam.Remoting.Serialize testTypes', function() {
	  var t = function() { };
	  t.__name = "testType";
	  
	  Seam.Remoting.registerType(t);
	  
	  var instance = Seam.Remoting.createType("testType");
	  expect(Seam.Remoting.getTypeName(instance)).toEqual("testType");
	  expect(Seam.Remoting.getType(instance)).toEqual(t);
  });
 
  it('Seam.Remoting.Serialize testExtractEncodedSessionId', function() {
	  expect('abcdefg').toEqual(Seam.Remoting.extractEncodedSessionId('http://localhost:8080/contextPath/page.seam;jsessionid=abcdefg?foo=bar'));
	  expect('abcdefg').toEqual(Seam.Remoting.extractEncodedSessionId('http://localhost:8080/contextPath/page.seam;jsessionid=abcdefg'));
	  expect(null).toEqual(Seam.Remoting.extractEncodedSessionId('http://localhost:8080/contextPath/page.seam'));
  });
  
  
});

describe('Seam Remoting javascript suite AJAX tests', function () {

	beforeEach(function() {
		  spyOn(window, 'XMLHttpRequest').andReturn(new myFakeXMLHttpRequest());
	});
			
	it('Seam.Remoting.Serialize faking XMLHttpRequest', function() {		
		var myRequest = new XMLHttpRequest();		
		expect(myRequest).toBeDefined();
		myRequest.open('POST', 'test/path', false);
		
		expect(myRequest.path).toBeDefined();		
		expect(myRequest.path).toEqual('test/path');
		expect(myRequest.method).toEqual('POST');
		
	});

	it('Seam.Remoting.Serialize testEncodeAjaxRequest', function() {		
		Seam.Remoting.resourcePath = "/resourcePath";
		Seam.Remoting.encodedSessionId = 'abcdefg';
		
		var req = Seam.Remoting.sendAjaxRequest(null, "/execute", null, true);

		// actual tests		
		expect(req).toBeDefined();
		expect(req.method).toEqual("POST");
		expect(req.path).toEqual("/resourcePath/execute;jsessionid=abcdefg");
		expect(req.async).toBeTruthy();			
	});

	it('Seam.Remoting.Serialize testNoEncodeAjaxRequest', function() {
		Seam.Remoting.resourcePath = "/resourcePath";
		Seam.Remoting.encodedSessionId = null;
		
		var req = Seam.Remoting.sendAjaxRequest(null, "/execute", null, true);
		
		expect(req.method).toEqual("POST");
		expect(req.path).toEqual("/resourcePath/execute");
		expect(req.async).toBeTruthy();
	});  	
	
});
