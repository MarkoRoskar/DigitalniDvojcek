var express = require('express');
var router = express.Router();
var MBajkController = require('../controllers/MBajkController.js');

/*
 * GET
 */
router.get('/', MBajkController.list);

/*
 * GET
 */
router.get('/:id', MBajkController.show);

/*
 * POST
 */
router.post('/', MBajkController.create);

/*
 * PUT
 */
router.put('/:id', MBajkController.update);

/*
 * DELETE
 */
router.delete('/:id', MBajkController.remove);

module.exports = router;
