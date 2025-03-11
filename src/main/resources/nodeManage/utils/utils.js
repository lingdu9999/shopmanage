const crypto = require('crypto');
const jwt = require('jsonwebtoken');

const JWT_SECRET = process.env.JWT_SECRET || 'yiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiyayiya';

class Result {
    static success(data, message = '操作成功') {
        return {
            code: 200,
            message: message,
            data: data
        };
    }

    static error(code = 500, message = '操作失败') {
        return {
            code: code,
            message: message,
            data: null
        };
    }
}

const getMD5 = (password) => {
    return crypto.createHash('md5').update(password).digest('hex');
};

const verifyToken = (token) => {
    const TokenBearer = "Bearer ";
    try {
        const decoded = jwt.verify(token.replace(TokenBearer, ''), JWT_SECRET, { algorithms: ['HS256'] });
        return decoded;
    } catch (err) {
        console.error("JWT verification failed:", err.message);
        return null;
    }
};

module.exports = { Result, getMD5, verifyToken };
