var express = require('express');
var router = express.Router();
var standController = require('../controllers/standController.js');

router.get('/', standController.list);

router.get('/:id', standController.show);

router.get('/closeTo', standController.near);


router.post('/', standController.create);


router.delete('/', standController.removeAll);

module.exports = router;
