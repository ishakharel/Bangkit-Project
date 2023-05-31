const db = require("../config/db-config");
const jwt = require("jsonwebtoken");
const bcrypt = require("bcryptjs");
const { nanoid } = require("nanoid");
const nodemailer = require("nodemailer");
const otpGenerator = require("otp-generator");
require("dotenv").config();

const login = (req, res) => {
  const { email, password } = req.body;
  if (!email || !password) {
    res.status(400).json({
      status: "error",
      message: "Invalid request. Please provide email and password",
    });
    return;
  }

  db.query("SELECT * FROM users WHERE email = ?", [email], (error, results) => {
    if (error) {
      res.status(500).json({
        status: "error",
        message:
          "Internal server error. Cannot find email, please try again later",
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

    db.query("UPDATE users SET token = ? WHERE id = ?", [token, user.id], (err, results) => {
        res.status(200).json({
          status: "success",
          data: {
            id: user.id,
            name: user.name,
            email: user.email,
            image: user.image,
            points: user.total_points,
            token: token,
          },
        });
      }
    );
  });
};

const register = (req, res) => {
  const { email, password, name } = req.body;

  if (!email || !password || !name) {
    res.status(400).json({
      status: "error",
      message: "Invalid request, please provide email, password, and name",
    });
    return;
  }

  db.query("SELECT * FROM users WHERE email = ?", [email], (error, results) => {
    if (error) {
      res.status(500).json({
        status: "error",
        message:
          "Internal server error. Cannot find email, please try again later",
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

    db.query("INSERT INTO users (id, name, email, password, total_points) VALUES (?, ?, ?, ?, ?)", [id, name, email, hashedPassword, 0], (error, results) => {
        if (error) {
          res.status(500).json({
            status: "error",
            message:
              "Internal server error. Cannot insert user, please try again later",
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

const forgotPassword = (req, res) => {
  const { email } = req.body;
  if (!email) {
    res.status(400).json({
      status: "error",
      message: "Invalid request. Please provide email",
    });
    return;
  }
  db.query("SELECT * FROM users WHERE email = ?", [email], (error, results) => {
    if (error) {
      res.status(500).json({
        status: "error",
        message:
          "Internal server error. Cannot find email, please try again later",
      });
      console.log(error);
      return;
    }

    if (results.length === 0) {
      res.status(404).json({
        status: "error",
        message: "Email not found",
      });
      return;
    }

    const generatedOTP = otpGenerator.generate(6, {
      upperCaseAlphabets: false,
      specialChars: false,
    });

    const id = nanoid(16);
    db.query(
      "INSERT INTO users_otp VALUES (?, ?, ?, NOW())",
      [id, email, generatedOTP],
      (error, results) => {
        if (error) {
          res.status(500).json({
            status: "error",
            message:
              "Internal server error. Cannot insert otp, please try again later",
          });
          console.log(error);
          return;
        }

        const transporter = nodemailer.createTransport({
          service: "gmail",
          host: "smtp.gmail.com",
          port: 465,
          secure: true,
          auth: {
            user: process.env.EMAIL_USERNAME,
            pass: process.env.EMAIL_PASSWORD,
          },
        });

        const mailOptions = {
          from: process.env.EMAIL_USERNAME,
          to: email,
          subject: "Password reset OTP",
          text: `Your OTP for password reset is: ${generatedOTP}`,
        };

        transporter.sendMail(mailOptions, (error) => {
          if (error) {
            res.status(500).json({
              status: "error",
              message: "Failed to send OTP email.",
            });
            console.log(error);
          } else {
            res.status(200).json({
              status: "success",
              message: "OTP email sent successfully",
            });
          }
        });
      }
    );
  });
};

const resetPassword = (req, res) => {
  const { email, newPassword, otp } = req.body;
  if (!email || !newPassword || !otp) {
    res.status(400).json({
      status: "error",
      message: "Invalid request. Please provide email, password and otp",
    });
    return;
  }

  db.query(
    "SELECT * FROM users_otp WHERE email = ? AND otp = ? AND (TIMESTAMPDIFF(MINUTE, created_at, NOW())<5)",
    [email, otp],
    (error, results) => {
      if (error) {
        res.status(500).json({
          status: "error",
          message:
            "Internal server error. Cannot verifying OTP, please try again later",
        });
        console.log(error);
        return;
      }

      if (results.length === 0) {
        res.status(400).json({
          status: "error",
          message: "Invalid OTP and Email",
        });
        return;
      }

      const hashedPassword = bcrypt.hashSync(newPassword, 8);
      db.query(
        "UPDATE users SET password = ? WHERE email = ?",
        [hashedPassword, email],
        (error, results) => {
          if (error) {
            res.status(500).json({
              status: "error",
              message:
                "Internal server error. Cannot update password, please try again later",
            });
            console.log(error);
            return;
          }

          db.query(
            "DELETE FROM users_otp WHERE email = ?",
            [email],
            (error, results) => {
              if (error) {
                res.status(500).json({
                  status: "error",
                  message:
                    "Internal server error. Cannot delete OTP code, please try again later",
                });
                console.log(error);
                return;
              }

              res.status(200).json({
                status: "success",
                message: "Password reset successfully",
              });
            }
          );
        }
      );
    }
  );
};

const changePassword = (req, res) => {
  const { oldPassword, newPassword } = req.body;
  const userId = req.user.id;
  if (!oldPassword || !newPassword) {
    res.status(400).json({
      status: "error",
      message:
        "Invalid request. Please provide email, old password, and new password",
    });
    return;
  }

  db.query("SELECT * FROM users WHERE id = ?", [userId], (error, results) => {
    if (error) {
      res.status(500).json({
        status: "error",
        message: "Internal server error, Cannot find email, try again later",
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

    if (isPassValid) {
      const hashedPassword = bcrypt.hashSync(newPassword, 8);
      db.query(
        "UPDATE users SET password = ? WHERE id = ?",
        [hashedPassword, userId],
        (error, results) => {
          if (error) {
            res.status(500).json({
              status: "error",
              message:
                "Internal server error. Cannot update user Try again later",
            });
            console.log(error);
            return;
          }

          res.status(200).json({
            status: "success",
            message: "Password is changed!",
          });
        }
      );
    } else {
      res.status(401).json({
        status: "error",
        message: "Invalid Old Password",
      });
      return;
    }
  });
};

const logout = (req, res) => {
  const userId = req.user.id;
  db.query("UPDATE users SET token = '' WHERE id = ?", [userId], (error, results) => {
    if(error){
      res.status(500).json({
        status: "error",
        message:
          "Internal server error. Cannot update user Try again later",
      });
      console.log(error);
      return;
    }

    res.status(200).json({
      status: 'success',
      message: 'User Logout Successfully'
    });
  })
}

module.exports = {
  register,
  login,
  forgotPassword,
  resetPassword,
  changePassword,
  logout
};
