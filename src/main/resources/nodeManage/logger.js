const winston = require('winston');
const DailyRotateFile = require('winston-daily-rotate-file');
const moment = require('moment-timezone');
const { combine, timestamp, printf, colorize } = winston.format;

// 自定义时间格式（东八区）
const appendTimestamp = winston.format((info) => {
    info.timestamp = moment().tz('Asia/Shanghai').format('YYYY-MM-DD HH:mm:ss');
    return info;
});

// 自定义日志格式
const logFormat = printf(({ level, message, timestamp }) => {
    return `${timestamp} [${level.toUpperCase()}]: ${message}`;
});

// 创建 Logger 实例
const logger = winston.createLogger({
    level: 'info', // 默认日志级别
    format: combine(
        appendTimestamp(), // 使用自定义时间格式
        logFormat // 自定义日志格式
    ),
    transports: [
        // 输出到控制台（带颜色）
        new winston.transports.Console({
            format: combine(colorize(), logFormat)
        }),
        // 按天轮转日志文件
        new DailyRotateFile({
            filename: 'logs/application-%DATE%.log',
            datePattern: 'YYYY-MM-DD',
            zippedArchive: true, // 压缩旧日志
            maxSize: '20m',      // 单个文件最大 20MB
            maxFiles: '14d'      // 保留 14 天的日志
        }),
        // 错误日志单独输出到文件
        new winston.transports.File({
            filename: 'logs/error.log',
            level: 'error'
        })
    ]
});

module.exports = logger;
