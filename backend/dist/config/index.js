"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.IS_DEV = exports.NODE_ENV = void 0;
const dotenv_1 = require("dotenv");
(0, dotenv_1.config)({ path: `.env.${process.env.NODE_ENV || 'development'}.local` });
exports.NODE_ENV = process.env.NODE_ENV;
exports.IS_DEV = process.env.NODE_ENV === 'development';
//# sourceMappingURL=index.js.map