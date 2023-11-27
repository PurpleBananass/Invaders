"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const app_module_path_1 = __importDefault(require("app-module-path"));
require("reflect-metadata");
app_module_path_1.default.addPath(__dirname);
const app_1 = require("./app");
(async () => {
    const app = new app_1.App(3200);
    await app.initServer();
})();
//# sourceMappingURL=index.js.map