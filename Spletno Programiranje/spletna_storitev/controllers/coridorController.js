var CoridorModel = require('../models/coridorModel.js');

/**
 * coridorController.js
 *
 * @description :: Server-side logic for managing coridors.
 */
module.exports = {

    /**
     * coridorController.list()
     */
    list: function (req, res) {
        CoridorModel.find(function (err, coridors) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting coridor.',
                    error: err
                });
            }

            return res.json(coridors);
        });
    },

    /**
     * coridorController.show()
     */
    show: function (req, res) {
        var id = req.params.id;

        CoridorModel.findOne({_id: id}, function (err, coridor) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting coridor.',
                    error: err
                });
            }

            if (!coridor) {
                return res.status(404).json({
                    message: 'No such coridor'
                });
            }

            return res.json(coridor);
        });
    },

    /**
     * coridorController.create()
     */
    create: function (req, res) {
        var coridor = new CoridorModel({

        });

        coridor.save(function (err, coridor) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when creating coridor',
                    error: err
                });
            }

            return res.status(201).json(coridor);
        });
    },

    /**
     * coridorController.update()
     */
    update: function (req, res) {
        var id = req.params.id;

        CoridorModel.findOne({_id: id}, function (err, coridor) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting coridor',
                    error: err
                });
            }

            if (!coridor) {
                return res.status(404).json({
                    message: 'No such coridor'
                });
            }

            
            coridor.save(function (err, coridor) {
                if (err) {
                    return res.status(500).json({
                        message: 'Error when updating coridor.',
                        error: err
                    });
                }

                return res.json(coridor);
            });
        });
    },

    /**
     * coridorController.remove()
     */
    remove: function (req, res) {
        var id = req.params.id;

        CoridorModel.findByIdAndRemove(id, function (err, coridor) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when deleting the coridor.',
                    error: err
                });
            }

            return res.status(204).json();
        });
    }
};
