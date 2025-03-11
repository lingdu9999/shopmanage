const logger = require('../logger');
const { Result } = require('../utils/utils');

const errorHandler = (err, req, res, next) => {
    logger.error(`全局错误: ${err.message}`);
    res.status(500).send(Result.error(500, '服务器内部错误'));
};

module.exports = errorHandler;
