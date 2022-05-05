var express = require('express');
const { JsonWebTokenError } = require('jsonwebtoken');
var router = express.Router();
var userController = require('../controllers/userController.js');
var jwt = require("jsonwebtoken");


function authenticateToken(req, res, next) {
    // get authentication header from response
    const authHeader = req.headers['authorization'];
    console.log("authHeader: " + authHeader)
    var token;
    if (authHeader) {
        // get the token portion from the header (after 1st space)
        token = authHeader.split(" ")[1];
    }
    else {
        token = null;
    }

    // no token received
    if (token == null) {
        return res.sendStatus(401);
    }
    // token received
    else {
        jwt.verify(token, process.env.ACCESS_TOKEN_SECRET, (err, user) => {
            if (err) {
                // no access to the token
                return res.sendStatus(403);
            }
            // valid and accessible token
            req.user = user;
            next();
        });
    }
}


/*
 * GET
 */
router.get('/getByToken', authenticateToken, userController.listByToken);
router.get('/getAll', userController.list);
router.get('/login', userController.showLogin);
router.get('/register', userController.showRegister);
//router.get('/logout', userController.logout);

/*
 * GET
 */
router.get('/:token', userController.show);

/*
 * POST
 */
router.post('/', userController.create);
router.post('/login', userController.login);

/*
 * PUT
 */
router.put('/:id', userController.update);

/*
 * DELETE
 */
router.delete('/:id', userController.remove);


module.exports = router;
