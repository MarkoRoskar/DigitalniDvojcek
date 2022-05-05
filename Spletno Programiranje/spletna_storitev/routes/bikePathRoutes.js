var express = require('express');
var router = express.Router();
var bikePathController = require('../controllers/bikePathController.js');

router.get('/', bikePathController.list);

router.get('/:id', bikePathController.show);


router.post('/', bikePathController.create);


router.delete('/', bikePathController.removeAll);

module.exports = router;
