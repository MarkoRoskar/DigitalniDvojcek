var express = require('express');
var router = express.Router();
var MBajkController = require('../controllers/MBajkController.js');

router.get('/', MBajkController.list);

router.get('/closeTo', MBajkController.near);

router.get('/:id', MBajkController.show);


router.post('/', MBajkController.insert);

module.exports = router;