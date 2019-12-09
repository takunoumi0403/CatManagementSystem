const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);


exports.writeUrl = functions.storage.object().onFinalize((object) => {
  const bucketName = 'catmanagementsystem.appspot.com'; // ご自身の
  const filePath = object.name;
  const db = admin.database().ref("picture").push();

  var now = new Date();
	var year = now.getFullYear();
	var mon = now.getMonth()+1; //１を足すこと
	var day = now.getDate();
	var hour = now.getHours()+9;
	var min = now.getMinutes();
	var sec = now.getSeconds();
  var date = mon + "/" + day + " " + hour + ":" + min; 
  
  db.set({
    filePath,
    downloadUrl: `https://firebasestorage.googleapis.com/v0/b/${bucketName}/o/${encodeURIComponent(filePath)}?alt=media`,
    date: date,
  }).then(() => console.log('Done')); 
});


// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
