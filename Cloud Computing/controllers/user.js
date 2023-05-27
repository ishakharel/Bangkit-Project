const db = require('../config/db-config');
const nanoid = require('nanoid');

const getUserById = (req, res) => {
    const userId = req.params.id;

    db.query('SELECT * FROM users WHERE id = ?', [userId], (error, results) => {
        if(error){
            res.status(500).send({
                message: 'Internal server error, Try agail later',
                error: error
            });
            return;
        }

        if(results.length === 0){
            res.status(404).json({
                status: 'error',
                message: 'User Not Found'
            });
            return;
        }

        res.status(200).json({
            status: 'success',
            data: results
        });
    });
}

const changeUsername = (req, res) => {
    const userId = req.params.id;
    const { newUsername } = req.body;

    db.query('UPDATE users SET name = ?, WHERE id = ?', [newUsername, userId], (error, results) => {
        if(error){
            res.status(500).send({
                message: 'Internal server error, Try agail later',
                error: error
            });
            return;
        }

        res.status(200).send({
            status: 'success',
            message: 'Username change successfully'
        })
    });
}

const checkPoints = (req, res) => {
    const userId = req.params.id;
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
    const { merchId, merchPoints } = req.body;
    const userId = req.params.id;
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

            db.query('UPDATE merch SET stok = stok - 1 WHERE id = ?', [merchId], (error, results3) => {
                if(error){
                    res.status(500).send({
                        message: 'Internal server error, Try again later'
                    });
                    return;
                }

                const id = nanoid(20)

                db.query('INSERT INTO users_merch_redeem VALUES (?, ?, ?)', [id, userId, merchId], (error, results4) => {
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

        });
    });
}

const addNotification = (req, res) => {
    const { userId, title, message, date } = req.body;

    const id = nanoid(20);

    db.query('INSERT INTO users_notifications VALUES (?, ?, ?, ?, ?)', 
    [id, userId, title, message, date], (error, results) => {
        if(error) {
            res.status(500).send({
                message: 'Internal server error, try again later'
            });
            return;
        }
        
        res.status(201).send({
            status: 'success',
            message: 'Notification is created successfully'
        })
    });
}

const getNotificationByUserId = (req, res) => {
    const userId = req.params.id;

    db.query('SELECT * FROM users_notifications WHERE user_id = ?',  [userId], (error, results) => {
        if(error){
            res.status(500).send({
                message: 'Internal server error, try again later'
            });
            return;
        }

        res.status(200).send({
            status: 'success',
            data: results
        })
    });
}

const deleteNotificationById = (req, res) => {
    const notifId = req.params.id;
    db.query('DELETE FROM user_notifications WHERE id = ?', [notifId], (error, results) => {
        if(error) {
            res.status(500).send({
                message: 'Internal server error, try again later'
            });
            return;
        }

        res.status(200).sensd({
            status: 'success',
            message: 'notifications is successfully delete'
        })
    });
}

const getAllMerch = (req, res) => {
    db.query('SELECT * FROM merch', (error, results) => {
        if(error) {
            res.status(500).send({
                message: 'Internal server error, try again later'
            });
            return;
        }

        res.status(200).send({
            status: 'success',
            data: results
        });
    });
}

module.exports = {
    getUserById,
    changeUsername, 
    checkPoints, 
    exchangePoints,
    addNotification,
    getNotificationByUserId,
    deleteNotificationById,
    getAllMerch
};