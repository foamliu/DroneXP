var https = require('https');
var fs = require('fs');

var options = {
  key: fs.readFileSync('key.pem'),
  cert: fs.readFileSync('cert.pem')
};

var static = require('node-static');
var fileServer = new static.Server('..');

var a = https.createServer(options, function (req, res) {
  req.addListener('end', function () {
      fileServer.serve(req, res);
  }).resume();
}).listen(8443);
