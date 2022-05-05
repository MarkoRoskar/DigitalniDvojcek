var express = require('express');
var router = express.Router();
var bikeShedController = require('../controllers/bikeShedController.js');

router.get('/', bikeShedController.list);

router.get('/:id', bikeShedController.show);

router.get('/closeTo', bikeShedController.near);


router.post('/', bikeShedController.create);


router.delete('/', bikeShedController.removeAll);

module.exports = router;
