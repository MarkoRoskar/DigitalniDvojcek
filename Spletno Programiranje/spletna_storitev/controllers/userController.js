var UserModel = require('../models/userModel.js');

require("dotenv").config();
var jwt = require("jsonwebtoken");
const { use } = require('../app.js');

/**
 * userController.js
 *
 * @description :: Server-side logic for managing users.
 */
module.exports = {

    /**
     * userController.list()
     */
    listByToken: function (req, res) {
        UserModel.find(function (err, users) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting user.',
                    error: err
                });
            }

            return res.json(users.filter(user => user.username === req.user.username));
        });
    },

    list: function(req, res) {
        UserModel.find(function (err, users) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting user.',
                    error: err
                });
            }

            return res.json(users);
        });
    },

    /**
     * userController.show()
     */
    show: function (req, res) {
        var token = req.params.token;

        UserModel.findOne({token: token}, function (err, user) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting user.',
                    error: err
                });
            }

            if (!user) {
                return res.status(404).json({
                    message: 'No such user'
                });
            }

            return res.json(user);
        });
    },

    /**
     * userController.showLogin()
     */
    showLogin: function(req, res) {
        res.render("user/login");   // displays user/login.hbs view
    },

    /**
     * userController.showRegister()
     */
    showRegister: function(req, res) {
        res.render("user/register");    // displays user/register.hbs view
    },

    /**
     * userController.login()
     */
    login: function(req, res, next) {
        UserModel.authenticate(req.body.username, req.body.password, function(err, user) {
            if (err || !user) {
                var error = new Error("Wrong username or password");
                error.status = 401;
                return next(error);
            }
            else {
                req.session.userId = user._id;

                return res.redirect('/');
            }
        });
    },

    /**
     * destroys session if it exists
     * if no error occur during session destruction, we are redirected to home page
     */
    /*logout: function(req, res, next) {
        if (req.session) {
            req.session.destroy(function(err) {
                if (err) {
                    return next(err);
                }
                else {
                    return res.redirect('/');
                }
            });
        }
    },*/

    /**
     * userController.create()
     */
    create: function (req, res) {
        var user = new UserModel({
			username : req.body.username,
			password : req.body.password,
			email : req.body.email,
			admin : true
        });

        const accessToken = jwt.sign({ username: req.body.username, password: req.body.password, email: req.body.email }, process.env.ACCESS_TOKEN_SECRET);
        res.json({
            username: req.body.username,
            email: req.body.email,
            accessToken: accessToken
        });

        user.token = accessToken;

        user.save(function (err, user) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when creating user',
                    error: err
                });
            }

            //return res.status(201).json(user);
            //return res.redirect("/");
        });
    },

    /**
     * userController.update()
     */
    update: function (req, res) {
        var id = req.params.id;

        UserModel.findOne({_id: id}, function (err, user) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting user',
                    error: err
                });
            }

            if (!user) {
                return res.status(404).json({
                    message: 'No such user'
                });
            }

            user.username = req.body.username ? req.body.username : user.username;
			user.password = req.body.password ? req.body.password : user.password;
			user.email = req.body.email ? req.body.email : user.email;
			user.admin = req.body.admin ? req.body.admin : user.admin;
			
            user.save(function (err, user) {
                if (err) {
                    return res.status(500).json({
                        message: 'Error when updating user.',
                        error: err
                    });
                }

                return res.json(user);
            });
        });
    },

    /**
     * userController.remove()
     */
    remove: function (req, res) {
        var id = req.params.id;

        UserModel.findByIdAndRemove(id, function (err, user) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when deleting the user.',
                    error: err
                });
            }

            return res.status(204).json();
        });
    }
};
