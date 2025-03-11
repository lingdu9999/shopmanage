require('dotenv').config();
const express = require('express');
const cors = require('cors');
const morgan = require('morgan');
const logger = require('./logger');
const authRoutes = require('./routes/authRoutes');
const authFilter = require('./middlewares/authFilter');
const proxyMiddleware = require('./middlewares/proxyMiddleware');
const errorHandler = require('./middlewares/errorHandler');

const app = express();

// 中间件配置
app.use(express.json()); // 解析 JSON 请求体
app.use(cors({
    origin: ['http://localhost:5173', 'http://localhost:5174'],
    credentials: true
}));
app.use(morgan('combined', {
    stream: {
        write: (message) => logger.info(message.trim())
    }
}));

// 路由
app.use(authFilter); // 认证过滤器
app.use(proxyMiddleware); // 请求转发中间件
app.use('/', authRoutes); // 认证相关路由

// 全局错误处理
app.use(errorHandler);

// 启动服务器
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    logger.info(`服务器已启动，正在监听端口 ${PORT}`);
});
