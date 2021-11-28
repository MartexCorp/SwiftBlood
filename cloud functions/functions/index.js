// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);


const http = require('http');

exports.writeRequest = functions.database.ref('/RequestBoard/{reqId}').onCreate(
    (snapshot, context) => {
        var reqId = context.params.reqId;
        console.log("Request ID = " + reqId);
        var location = snapshot.child('town').val();

        var instanceId = "20"; // TODO: Replace it with your gateway instance ID here
        var clientId = "lenegatifmartex@gmail.com"; // TODO: Replace it with your Forever Green client ID here
        var clientSecret = "936a92d072ef471ab5c6de34f826d0dc"; // TODO: Replace it with your Forever Green client secret here

        var jsonPayload = JSON.stringify({
            group_admin: "237242992752", // TODO: Specify the WhatsApp number of the group creator, including the country code
            group_name: "Whatsapp", // TODO:  Specify the name of the group
            message: "New Blood Request in " + location + ". Follow the link below and help save a life. https://app.swiftblood.org/" + reqId + ". Brought to you by MTN" // TODO: Specify the content of your message
        });

        var options = {
            hostname: "api.whatsmate.net",
            path: "/v3/whatsapp/group/text/message/" + instanceId,
            port: 80,
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "X-WM-CLIENT-ID": clientId,
                "X-WM-CLIENT-SECRET": clientSecret,
                "Content-Length": Buffer.byteLength(jsonPayload)
            }
        };

        var request = new http.ClientRequest(options);
        request.end(jsonPayload);

        request.on('response', (response) => {
            console.log('Heard back from the WhatsMate WA Gateway:\n');
            console.log('Status code: ' + response.statusCode);
            response.setEncoding('utf8');
            response.on('data', (chunk) => {
                console.log(chunk);
            });
        });

        return console.log("Message Sent");
    }
);
// const CUT_OFF_TIME = 3 * 24 * 60 * 60 * 1000;
const CUT_OFF_TIME = 10 * 60 * 1000;


exports.deleteOldRequests = functions.https.onRequest((req, res) => {
    const timeNow = Date.now();
    const messagesRef = admin.database().ref('/RequestBoard');
    messagesRef.once('value', (snapshot) => {
        snapshot.forEach((child) => {
            if ((Number(child.val()['time']) + CUT_OFF_TIME) >= timeNow) {
                //if ((child.val()['town']) === "Douala") {
                child.ref.set(null);
            }
        })
    });
    return res.set('Access-Control-Allow-Origin', '*');
});


// Return the promise from counterRef.set() so our function
// waits for this async event to complete before it exits.
//const messagesData = await collectionRef.once('value');
//return await counterRef.set(messagesData.numChildren());



// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
