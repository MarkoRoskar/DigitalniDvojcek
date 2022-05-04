var express = require('express');
var router = express.Router();
var bikeShedController = require('../controllers/bikeShedController.js');

/*
 * GET
 */
router.get('/', bikeShedController.list);

/*
 * GET
 */
router.get('/:id', bikeShedController.show);

/*
 * POST
 */
router.post('/', bikeShedController.create);

/*
 * PUT
 */
router.put('/:id', bikeShedController.update);

/*
 * DELETE
 */
router.delete('/:id', bikeShedController.remove);

module.exports = router;
