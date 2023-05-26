const bcrypt = require('bcryptjs');
const db = require('../config/db-config');

const checkPoints = (req, res) => {
    const { userId } = req.body;
    db.query('SELECT * FROM users WHERE id = ?', [userId], (error, results) => {
        if(error){
            res.status(500).send({
                message: 'Internal server error, Try agail later',
                error: error
            });
            return;
        }

        const user = results[0];

        res.status(200).send({
            status: 'success',
            points: user.total_points
        })
    });
}

const exchangePoints = (req, res) => {
    const { userId, merchPoints } = req.body;
    db.query('SELECT * FROM users WHERE id = ?', [userId], (error, results) => {
        if(error){
            res.status(500).send({
                message: 'Internal server error, Try again later',
                error: error
            });
            return;
        }
        
        const user = results[0];

        if(merchPoints > user.total_points) {
            res.status(400).send({
                message: 'Points is not enough to exchange this merch'
            });
            return;
        }

        const total_points = user.total_points - merchPoints;

        db.query('UPDATE users SET total_points = ? WHERE id = ?', [total_points, userId], (error, results2) => {
            if(error){
                res.status(500).send({
                    message: 'Internal server error, Try again later'
                });
                return;
            }

            res.status(200).send({
                status: 'success',
                message: 'Points is successfully exchange'
            })
        });
    });
}

module.exports = { checkPoints, exchangePoints };