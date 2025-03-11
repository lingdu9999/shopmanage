const { verifyToken } = require('../utils/utils');
const logger = require('../logger');
const { Result } = require('../utils/utils');

const whiteList = ['/login', '/register'];

const authFilter = async (req, res, next) => {
    if (whiteList.includes(req.path)) {
        return next();
    }
    const token = req.headers['authorization'];
    if (!token) {
        logger.warn('未提供 Token');
        return res.send(Result.error(404, '请先登录'));
    }

    const decoded = verifyToken(token);
    if (decoded) {
        req.user = decoded['data'];
        next();
    } else {
        logger.error('Token 无效或已过期');
        return res.send(Result.error(401, 'token无效或已过期'));
    }
};

module.exports = authFilter;
