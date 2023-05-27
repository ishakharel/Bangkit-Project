const db = require("../config/db-config");
const jwt = require("jsonwebtoken");
const bcrypt = require("bcryptjs");
const { nanoid } = require("nanoid");
const OTP = require('otp');
const nodemailer = require('nodemailer');

require("dotenv").config();

const login = (req, res) => {
  const { email, password } = req.body;
  if (!email || !password) {
    res.status(400).json({
      status: 'success',
      message: 'Invalid request. Please provide email and password'
    });
    return;
  }

  db.query("SELECT * FROM users WHERE email = ?", [email], (error, results) => {
    if (error) {
      res.status(500).json({
        status: "error",
        message: "Internal server error, cannot find user email, please try again later",
      });
      console.log(error)
      return;
    }

    if (results.length === 0) {
      res.status(404).send({
        status: "error",
        message: "Email not registered",
      });
      return;
    }

    const user = results[0];
    
    const isPassValid = bcrypt.compareSync(password, user.password);

    if (!isPassValid) {
      res.status(401).send({
        status: "error",
        message: "Invalid password",
      });
      return;
    }

    const token = jwt.sign({ id: user.id }, process.env.JWT_SECRET, {
      expiresIn: process.env.JWT_EXPIRES_IN,
    });

    res.status(200).send({
      status: "success",
      data: {
        id: user.id,
        name: user.name,
        email: user.email,
        points: user.total_points,
        token: token,
      },
    });
  });
};

const register = (req, res) => {
  const { email, password, name } = req.body;
  db.query("SELECT * FROM users WHERE email = ?", [email], (err, results) => {
    if (err) {
      res.status(500).send({
        status: "error",
        message: "Internal server error, please try again later",
      });
      return;
    }

    if (results.length > 0) {
      res.status(409).send({
        status: "error",
        message: "Email alredy exist",
      });
      return;
    }

    const id = nanoid(16);
    const hashedPassword = bcrypt.hashSync(password, 8);

    db.query(
      'INSERT INTO users VALUES (?, ?, ?, ?, ?)', [id, name, email, hashedPassword, 0],
      (err, results) => {
        if (err) {
          res.status(500).send({
            status: "error",
            message: "Internal server error, please try again later",
          });
          return;
        }
        res.status(201).send({
          status: "success",
          message: "User registered succesfully",
        });
      }
    );
  });
};

const generateOTP = () => {
  const otp = new OTP();
  return otp.generate(6, { specialChars: false} );
}

const sendOTP = (email, otp) => {
  const transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
      user: process.env.EMAIL_USERNAME,
      pass: process.env.EMAIL_PASSWORD
    }
  });

  const mailOptions = {
    from: 'your_email',
    to: email,
    subject: 'Password reset OTP',
    text: `Your OTP for password reset is: ${otp}`
  }

  try {
    transporter.sendMail(mailOptions);
    console.log('OTP sent to email');
  } catch (err) {
    console.error('Error sending OTP:', err);
  }
}

const forgotPassword = (req, res) => {
  const { email } = req.body;
  db.query('SELECT * FROM users WHERE email = ?', [email], (error, results) => {
    if(error){
      res.status(500).send({
        message: 'Internal server error, try again later',
      });
      return;
    }

    if(results === 0){
      res.status(404).send({
        message: 'User not found'
      });
      return;
    }

    const otp = generateOTP();

    db.query('INSERT INTO users_otp (email, otp) VALUES (?, ?)', [email, otp], (error, results) => {
      if(error){
        res.status(500).send({
          message: 'Internal server error, try again later',
        });
        return;
      }

      sendOTP(email, otp)
        .then(()=> res.status(200).send({ message: 'OTP sent successfully'}))
        .catch(() => res.status(500).send({ message: 'Internal server error, try again later'}));
    });
  });
}

const resetPassword = (req, res) => {
  const { email, newPassword, otp } = req.body;
  
  db.query('SELECT * FROM users_otp WHERE email = ? AND otp = ? AND TIMESTAMPDIFF(MINUTE, created_at, NOW())', [email, otp], (error, results) => {
    if(error) {
      console.error('Error verifying OTP:', error);
      res.status(500).send({
        message: 'Internal server error, try again later'
      });
      return;
    }
    
    if(results.length === 0) {
      res.status(400).send({
        message: 'Invalid OTP'
      });
      return;
    }
    
    const hashedPassword = bcrypt.hashSync(newPassword, 8);
    db.query('UPDATE users SET password = ? WHERE email = ?', [hashedPassword, email], (error, results) => {
      if(error){
        console.log('Error updating password: ', error);
        res.status(500).send({
          message: 'Internal server error, try again later'
        });
        return;
      }
  
      db.query('DELETE FROM users_otp WHERE email = ?', [email], (error, results) => {
        if(error) {
          console.log('Error deleteing OTP: ', error);
          res.status(500).send({
            message: 'Internal server error, try again later'
          });
          return;
        }
  
        res.status(200).send({
          message: 'Password reset successfully'
        });
      });
    });
  });
}

const changePassword = (req, res) => {
  const { email, oldPassword, newPassword } = req.body;
  
  db.query('SELECT * FROM users WHERE email = ?', [email], (error, results) => {
      if(error) {
          res.status(500).send({
              message: 'Internal server error, Try again later'
          });
          return;
      }
      
      if (results.length === 0) {
          res.status(401).send({
              status: "error",
              message: "Email not registered",
          });
          return;
      }
      
      const user = results[0];
      
      if (!user.password) {
          res.status(500).send({
              error: "Failed to retrieve user password",
          });
          return;
      }

      const isPassValid = bcrypt.compareSync(oldPassword, user.password);
      
      if(isPassValid) {
        const hashedPassword = bcrypt.hashSync(newPassword, 8);
          db.query('UPDATE users SET password = ? WHERE email = ?', [hashedPassword, email], (error, results) => {
              if(error){
                  res.status(500).send({
                      message: 'Internal server error, Try agail later'
                  });
                  return;
              }

              res.status(200).send({
                status: 'success',
                message: 'Password is changed!'
              })
          });
      } else {
          res.status(401).send({
              message: 'Invalid Old Password'
          });
          return;
      }
  });
}

const checkAuth = (req, res) => {
  const { token } = req.body;
  if (!token) {
    res.status(400).send({
      status: 'error',
      message: 'Invalid request, Please provide token'
    });
  }

  const decodedToken = jwt.verify(token, process.env.JWT_SECRET);
  if(!decodedToken){
    res.status(401).send({
      status: 'error',
      message: 'Unautherized user'
    });
  }

  res.status(200).send({
    status: 'Success',
    message: 'User Autherized',
    data: {
      userId: decodedToken.id
    }
  });
}

module.exports = { 
  register, 
  login, 
  forgotPassword,
  resetPassword,
  changePassword,
  checkAuth
};