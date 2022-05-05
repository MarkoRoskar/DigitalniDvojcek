var express = require('express');
var router = express.Router();
var coridorController = require('../controllers/coridorController.js');

router.get('/', coridorController.list);

router.get('/:id', coridorController.show);


router.post('/', coridorController.create);


router.delete('/:id', coridorController.removeAll);

module.exports = router;
