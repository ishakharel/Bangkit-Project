require("dotenv").config();

const checkApiKey = (req, res, next) => {
    const apiKey = req.headers.apikey;

    if(apiKey === process.env.API_KEY) {
        next();
    } else {
        res.status(401).json({
            status: 'error',
            message: 'Invalid API Key'
        });
    }
}

module.exports = checkApiKey;