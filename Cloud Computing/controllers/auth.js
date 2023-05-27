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
      status: 'error',
      message: 'Invalid request. Please provide email and password'
    });
    return;
  }

  db.query("SELECT * FROM users WHERE email = ?", [email], (error, results) => {
    if (error) {
      res.status(500).json({
        status: "error",
        message: "Internal server error. Cannot find email, please try again later",
      });
      console.log(error);
      return;
    }

    if (results.length === 0) {
      res.status(404).json({
        status: "error",
        message: "Email is not registered",
      });
      return;
    }

    const user = results[0];
    const isPassValid = bcrypt.compareSync(password, user.password);

    if (!isPassValid) {
      res.status(401).json({
        status: "error",
        message: "Invalid password",
      });
      return;
    }

    const token = jwt.sign({ id: user.id }, process.env.JWT_SECRET, {
      expiresIn: process.env.JWT_EXPIRES_IN,
    });

    res.status(200).json({
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
  
  if(!email || !password || !name) {
    res.status(400).json({
      status: "error",
      message: "Invalid request, please provide email, password, and name"
    });
    return;
  }

  db.query("SELECT * FROM users WHERE email = ?", [email], (error, results) => {
    if (error) {
      res.status(500).json({
        status: "error",
        message: "Internal server error. Cannot find email, please try again later",
      });
      console.log(error);
      return;
    }

    if (results.length > 0) {
      res.status(409).json({
        status: "error",
        message: "Email alredy exist",
      });
      return;
    }

    const id = nanoid(16);
    const hashedPassword = bcrypt.hashSync(password, 8);

    db.query(
      'INSERT INTO users VALUES (?, ?, ?, ?, ?)', [id, name, email, hashedPassword, 0],
      (error, results) => {
        if (error) {
          res.status(500).json({
            status: "error",
            message: "Internal server error. Cannot insert user, please try again later",
          });
          console.log(error);
          return;
        }
        res.status(201).json({
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
    from: process.env.EMAIL_USERNAME,
    to: email,
    subject: 'Password reset OTP',
    text: `Your OTP for password reset is: ${otp}`
  }

  try {
    transporter.sendMail(mailOptions);
    console.log('OTP sent to email');
  } catch (error) {
    console.log('Error sending OTP:', error);
  }
}

const forgotPassword = (req, res) => {
  const { email } = req.body;
  if(!email) {
    res.status(400).json({
      status: 'error',
      message: 'Invalid request. Please provide email'
    });
    return;
  }
  db.query('SELECT * FROM users WHERE email = ?', [email], (error, results) => {
    if(error){
      res.status(500).json({
        status: 'error',
        message: 'Internal server error. Cannot find email, please try again later',
      });
      console.log(error);
      return;
    }

    if(results === 0){
      res.status(404).json({
        status: 'error',
        message: 'Email not found'
      });
      return;
    }

    const otp = generateOTP();

    db.query('INSERT INTO users_otp (email, otp) VALUES (?, ?)', [email, otp], (error, results) => {
      if(error){
        res.status(500).json({
          status: 'error',
          message: 'Internal server error. Cannot insert otp, please try again later',
        });
        console.log(error);
        return;
      }

      sendOTP(email, otp)
        .then(()=> res.status(200).json({ status: 'success', message: 'OTP sent successfully'}))
        .catch(() => res.status(500).json({ status: 'error', message: 'Internal server error. Cannot send to email, please try again later'}));
    });
  });
}

const resetPassword = (req, res) => {
  const { email, newPassword, otp } = req.body;
  if(!email || !newPassword || !otp) {
    res.status(400).json({
      status: 'error',
      message: 'Invalid request. Please provide email, password and otp'
    });
    return;
  }
  
  db.query('SELECT * FROM users_otp WHERE email = ? AND otp = ? AND TIMESTAMPDIFF(MINUTE, created_at, NOW())', [email, otp], (error, results) => {
    if(error) {
      res.status(500).json({
        status: 'error',
        message: 'Internal server error. Cannot verifying OTP, please try again later'
      });
      console.log(error);
      return;
    }
    
    if(results.length === 0) {
      res.status(400).json({
        status: 'error',
        message: 'Invalid OTP'
      });
      return;
    }
    
    const hashedPassword = bcrypt.hashSync(newPassword, 8);
    db.query('UPDATE users SET password = ? WHERE email = ?', [hashedPassword, email], (error, results) => {
      if(error){
        res.status(500).json({
          status: 'error',
          message: 'Internal server error. Cannot update password, please try again later'
        });
        console.log(error);
        return;
      }
  
      db.query('DELETE FROM users_otp WHERE email = ?', [email], (error, results) => {
        if(error) {
          res.status(500).json({
            status: 'error',
            message: 'Internal server error. Cannot delete OTP code, please try again later'
          });
          console.log(error);
          return;
        }
  
        res.status(200).json({
          status: 'success',
          message: 'Password reset successfully'
        });
      });
    });
  });
}

const changePassword = (req, res) => {
  const { email, oldPassword, newPassword } = req.body;
  if(!email || !oldPassword || !newPassword) {
    res.status(400).json({
      status: 'error',
      message: 'Invalid request. Please provide email, old password, and new password'
    });
    return;
  }
  
  db.query('SELECT * FROM users WHERE email = ?', [email], (error, results) => {
      if(error) {
          res.status(500).json({
            status: 'error',
            message: 'Internal server error, Cannot find email, try again later'
          });
          console.log(error);
          return;
      }
      
      if (results.length === 0) {
          res.status(401).json({
            status: "error",
            message: "Email not registered",
          });
          return;
      }
      
      const user = results[0];
      const isPassValid = bcrypt.compareSync(oldPassword, user.password);
      
      if(isPassValid) {
        const hashedPassword = bcrypt.hashSync(newPassword, 8);
          db.query('UPDATE users SET password = ? WHERE email = ?', [hashedPassword, email], (error, results) => {
              if(error){
                  res.status(500).json({
                    status: "error",
                    message: 'Internal server error. Cannot update user Try again later'
                  });
                  console.log(error);
                  return;
              }

              res.status(200).json({
                status: 'success',
                message: 'Password is changed!'
              })
          });
      } else {
          res.status(401).json({
            status: "error",
            message: 'Invalid Old Password'
          });
          return;
      }
  });
}

const checkAuth = (req, res) => {
  const { token } = req.body;
  if (!token) {
    res.status(400).json({
      status: 'error',
      message: 'Invalid request, Please provide token'
    });
    return;
  }

  const decodedToken = jwt.verify(token, process.env.JWT_SECRET);
  if(!decodedToken){
    res.status(401).json({
      status: 'error',
      message: 'Unautherized user'
    });
    return;
  }

  res.status(200).json({
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