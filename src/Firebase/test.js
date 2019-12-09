const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);

exports.writeUrl = functions.storage.object().onFinalize((object) => {
  const bucketName = 'hoge.appspot.com'; // ご自身の
  const filePath = object.name;
  const db = admin.firestore();

  db.collection('images').add({
    filePath,
    downloadUrl: `https://firebasestorage.googleapis.com/v0/b/${bucketName}/o/${encodeURIComponent(filePath)}?alt=media`,
  }).then(() => console.log('Done')); // eslint-disable-line no-console
});
