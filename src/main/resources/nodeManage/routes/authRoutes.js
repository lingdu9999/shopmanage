const express = require('express');
const router = express.Router();
const { Result, getMD5 } = require('../utils/utils');
const backendApi = require('../services/backendApi');
const logger = require('../logger');

// 注册接口
router.post('/register', async (req, res) => {
    const ob = req.body;
    ob.password = getMD5(ob.password + '');
    logger.info(`注册请求: ${ob.username}`);

    try {
        const r = await backendApi.post('/users/register', ob);
        if (r.data.code !== 200) {
            logger.warn(`注册失败: ${r.data.message}`);
            return res.send(Result.error(r.data.code, r.data.message || '注册信息有误'));
        }
        logger.info(`注册成功`);
        res.send(r.data);
    } catch (error) {
        logger.error(`注册失败: ${error.message}`);
        if (error.response && error.response.data) {
            return res.send(Result.error(
                error.response.data.code || 500,
                error.response.data.message || '注册信息有误'
            ));
        }
        res.send(Result.error(500, '注册信息失败'));
    }
});

// 登录接口
router.post('/login', async (req, res) => {
    const ob = req.body;
    ob.password = getMD5(ob.password + '');
    logger.info(`登录请求: ${ob.username}`);
    try {
        const r = await backendApi.post('/users/login', ob);
        if (r.data.code !== 200) {
            logger.warn(`登录失败: ${r.data.message}`);
            return res.send(Result.error(r.data.code, r.data.message || '账号或密码错误'));
        }
        if (r.data.data == null) {
            logger.warn('账号或密码错误');
            return res.send(Result.error(500, '账号或密码错误'));
        }

        const userInfo = r.data.data;
        logger.info(`登录成功: ${JSON.stringify({
            userId: userInfo.userId,
            username: userInfo.username
        })}`);
        res.send(Result.success(userInfo, '登录成功'));
    } catch (error) {
        logger.error(`登录失败: ${error.message}`);
        if (error.response && error.response.data) {
            return res.send(Result.error(
                error.response.data.code || 500,
                error.response.data.message || '账号或密码错误'
            ));
        }
        res.send(Result.error(500, '登录失败，请稍后重试'));
    }
});

module.exports = router;
