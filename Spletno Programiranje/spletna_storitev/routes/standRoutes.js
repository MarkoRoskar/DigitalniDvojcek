var express = require('express');
var router = express.Router();
var standController = require('../controllers/standController.js');

/*
 * GET
 */
router.get('/', standController.list);

/*
 * GET
 */
router.get('/:id', standController.show);

/*
 * POST
 */
router.post('/', standController.create);

/*
 * PUT
 */
router.put('/:id', standController.update);

/*
 * DELETE
 */
router.delete('/:id', standController.remove);

module.exports = router;
