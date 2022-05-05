var express = require('express');
var router = express.Router();
var tourPathController = require('../controllers/tourPathController.js');


router.get('/', tourPathController.list);

router.get('/:id', tourPathController.show);

router.post('/', tourPathController.create);

router.delete('/', tourPathController.removeAll);


module.exports = router;
