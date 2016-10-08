var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('test-attitude', { title: '姿态试验' });
});

module.exports = router;