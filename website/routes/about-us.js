var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('about-us', { title: '联系我们' });
});

module.exports = router;