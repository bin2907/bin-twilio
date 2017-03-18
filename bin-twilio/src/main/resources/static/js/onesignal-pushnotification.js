/*
 * Register push notification
 * 
 * @author: truc.nguyen
 * @create date: 10/04/2016
 */

var OneSignal = window.OneSignal || [];

function oneSignalInit(userId, url) {
	
	var playerId = localStorage.getItem("playerId");
	
	// Get device type via user agent
	var deviceType = function() {
	    var isFirefox = typeof InstallTrigger !== 'undefined';
	    var isSafari = Object.prototype.toString.call(window.HTMLElement).indexOf('Constructor') > 0 || (function (p) { return p.toString() === "[object SafariRemoteNotification]"; })(!window['safari'] || safari.pushNotification);
	    var isChrome = !!window.chrome && !!window.chrome.webstore;

	    return isFirefox ? '8' :
	        isSafari ? '7' :
	        isChrome ? '5' :
	        '-1';
	};
	
	if (deviceType == '-1') {
		console.log('Browser not support push!');
		return;
	}
	
	// Get or generate browser UUID
	var uuid = localStorage.getItem("uuid");
	if (uuid == null || uuid == '' || typeof uuid == 'undefined') {
		uuid = guid();
		localStorage.setItem('uuid', uuid);
	}
	
	if (playerId == null || playerId == '' || typeof playerId == 'undefined') {
		OneSignal.push(["init", {
			appId: "23775d08-7fed-4ced-a44d-152b5d61b532",
			autoRegister: true, /* Set to true to automatically prompt visitors */
			subdomainName: 'https://onemr.onesignal.com',
			persistNotification: false,
			notifyButton: {
				enable: false /* Set to false to hide */
			}
		}]);
		
		OneSignal.push(function() {
			OneSignal.getUserId(function(playerId) {
				console.log(playerId);
				localStorage.setItem("playerId", playerId);
				
		    	// call register API
		    	var data = {
		    			"userId": userId,
		    			"appId": "23775d08-7fed-4ced-a44d-152b5d61b532",
		    			"playerId": playerId,
		    			"deviceId": uuid,
		    			"deviceType": deviceType
		    	}
		    	
		    	$.ajax({
		    		type: "POST",
		    		url: url,
		    		data: data,
		    		success: function(resp) {
		    			console.log(resp);
		    		}
		    	})
			})
		});
	} else {
		var data = {
			"userId": userId,
			"appId": "23775d08-7fed-4ced-a44d-152b5d61b532",
			"playerId": playerId,
			"deviceId": uuid,
			"deviceType": deviceType
    	}
		$.ajax({
    		type: "POST",
    		url: url,
    		data: data,
    		success: function(resp) {
    			console.log(resp);
    		}
    	})
	}
	
	OneSignal.push(["addListenerForNotificationOpened", function(data) {
		console.log("Received NotificationOpened:", data);
	}]);
}

function guid() {
	function s4() {
		return Math.floor((1 + Math.random()) * 0x10000)
		.toString(16)
		.substring(1);
	}
	return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
		s4() + '-' + s4() + s4() + s4();
}
