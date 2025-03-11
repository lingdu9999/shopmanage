const backendApi = require('../services/backendApi');
const logger = require('../logger');
const { Result, getMD5 } = require('../utils/utils');

const whiteList = ['/login', '/register'];
const encryptPassword = ['/users/updatePassword'];

const proxyMiddleware = async (req, res, next) => {
    if (whiteList.includes(req.path)) {
        return next();
    }

    if (encryptPassword.includes(req.path)) {
        req.body.oldPassword = getMD5(req.body.oldPassword + '');
        req.body.newPassword = getMD5(req.body.newPassword + '');
    }

    try {
        const config = {
            method: req.method,
            url: req.path,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': req.headers['authorization'],
                'user': req.user
            }
        };

        if (['POST', 'PUT', 'PATCH'].includes(req.method)) {
            config.data = req.body;
        }

        if (Object.keys(req.query).length > 0) {
            config.params = req.query;
        }

        logger.info(`转发请求: ${req.method} ${req.path}`);
        const response = await backendApi(config);
        res.send(response.data);
    } catch (error) {
        logger.error(`请求转发错误: ${error.message}`);
        res.send(Result.error(error.response?.status || 500, error.message));
    }
};

module.exports = proxyMiddleware;
