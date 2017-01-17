var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('test-location', { title: '地图试验' });
});

module.exports = router;